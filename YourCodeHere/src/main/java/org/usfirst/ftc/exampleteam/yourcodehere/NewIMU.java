package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

import java.util.ArrayList;
import java.util.Date;


public class NewIMU
{
	// Our sensors, motors, and other devices go here, along with other long term state
	IBNO055IMU imu;
	IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();

	//heading data
	float Heading;
	float PreviousHeading;
	int Rotations = 0;
	boolean FirstUpdate = true;
	ElapsedTime ProgramTime;

	//time data
	float PreviousTime = 0;
	float CurrentTime = 0;
	float UpdateTime = 0;

	//PID data
	float ComputedRotation;
	float PreviousComputedRotation;
	float Target = 20;
	float TargetRateOfChange = 45;
	ArrayList HistoricData = new ArrayList();
	ArrayList DerivativeData = new ArrayList();
	float D;
	float I;
	float P;
	float DConstant;
	float IConstant;
	float PConstant;

	//causes the robot to stop at a white light
	boolean StopAtLight = false;
	boolean StopAtUltrasonic = false;

	//can be "Straight" or "Turn"
	String Motion = "Turn";
	//Forward or Backward are the options
	String MovingDirection = "Forward";
	//can be 'Right' or 'Left'
	String StationaryWheel = "Right";

	//declare the logger
	org.usfirst.ftc.exampleteam.yourcodehere.Logging Logging;

	//from 0 to 1
	//.4 for test chassis
	float Power = .5f;
	float TurningPower = .8f;

	DcMotor LeftMotor;
	DcMotor RightMotor;
	ColorSensor LightSensor;
	HardwareMap hardwareMap;
	TelemetryDashboardAndLog telemetry;
	NewAutonomous MainOpMode;
	UltrasonicSensor Ultrasonic;

	public NewIMU(DcMotor LeftMotor, DcMotor RightMotor, ColorSensor lightSensor, UltrasonicSensor Ultrasonic, HardwareMap map, TelemetryDashboardAndLog tel, NewAutonomous Auto)
	{
		this.Ultrasonic = Ultrasonic;
		this.LeftMotor = LeftMotor;
		this.RightMotor = RightMotor;
		this.LightSensor = lightSensor;
		this.hardwareMap = map;
		this.telemetry = tel;
		this.MainOpMode = Auto;

		// setup the IMU
		parameters.angleUnit = IBNO055IMU.ANGLEUNIT.DEGREES;
		parameters.accelUnit = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
		parameters.loggingEnabled = true;
		parameters.loggingTag = "BNO055";

		// the I2C device is names IMU
		imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);

        //setup telemetry
        telemetry.setUpdateIntervalMs(200);

        //setup the program timer
        ProgramTime = new ElapsedTime();

		//sets the current time
        CurrentTime = (float) ProgramTime.time() * 1000;

		//setup the file logger
		Logging = new Logging("TurnData.txt");
    }

	public void ResetValues()
	{
		HistoricData.clear();
		DerivativeData.clear();
	}

	public void SlowStop(int Direction) throws InterruptedException
	{
		LeftMotor.setPower(.01 * Math.abs(Direction) / Direction);
		RightMotor.setPower(.01 * Math.abs(Direction) / Direction);

		Thread.sleep(500);
	}

	public void Stop() throws InterruptedException
	{
		LeftMotor.setPower(0);
		RightMotor.setPower(0);

		Thread.sleep(200);
	}

	public void Straight(float Rotations) throws InterruptedException
	{
		Straight(Rotations, 100);
	}

    //this is the update loop
    public int Straight(float Rotations, int Timeout) throws InterruptedException
    {
		Date c = new Date();
		long StartTime = c.getTime();

		//remove the historic data values
		ResetValues();

        Motion = "Straight";

		if(Rotations > 0)
		{
			MovingDirection = "Forward";
		}
		else
		{
			MovingDirection = "Backward";
		}

        //start position
        long StartPosition = RightMotor.getCurrentPosition();

        //update the angles
        UpdateAngles();

        //set the target to the current position
        Target = ComputedRotation;

		while(Math.abs(StartPosition - RightMotor.getCurrentPosition()) < Math.abs(Rotations) * 1400)
		{
			telemetry.addData("7", Ultrasonic.getUltrasonicLevel());

			//see if it has passed the timeout
			Date a = new Date();
			long Time = a.getTime();

			if(Math.abs(StartTime - Time) > Timeout * 1000)
			{
				telemetry.log.add("Timed Out");
				break;
			}

			// if the light sensor value is above a certain threshold, stop the movement
			int Threshold = 60;
			if (this.LightSensor.blue() > Threshold && this.LightSensor.red() > Threshold && this.LightSensor.green() > Threshold && this.StopAtLight)
			{
				telemetry.log.add("stopped becuase of light.");
				return 1;
			}

			if (StopAtUltrasonic == true && this.Ultrasonic.getUltrasonicLevel() < 15 && this.Ultrasonic.getUltrasonicLevel() != 0)
			{
				telemetry.log.add("stopped becuase of ultrasonic.");
				return 1;
			}

			//this is the update loop
			UpdateTime();
			UpdateAngles();
			PreformCalculations();

			//update telemetry
			telemetry.update();

			//idle using the reference to the instance of the main opmode
			MainOpMode.idle();
		}

		return 1;
    }

	private float ValueStandardDeviation()
	{
		//compute the mean
		float Mean = 0;
		for (int i = 0; i < DerivativeData.size(); i++)
		{
			Mean += (float) DerivativeData.get(i);
		}
		Mean /= DerivativeData.size();

		float Summation = 0;
		for (int i = 0; i < DerivativeData.size(); i++)
		{
			Summation += Math.pow((float) DerivativeData.get(i) - Mean, 2);
		}

		Summation /= DerivativeData.size();

		Summation = (float) Math.sqrt((double)Summation);

		return Summation;
	}

	public float Rotation()
	{
		//make sure that we have the latest data
		UpdateAngles();

		return ComputedRotation;
	}

	public void Turn(final float Degrees) throws InterruptedException
	{
		TurnToAngle(ComputedRotation + Degrees, "Right", 10);
	}

	public void Turn(final float Degrees, final String StationaryWheel, int Timeout) throws InterruptedException
	{
		TurnToAngle(ComputedRotation + Degrees, StationaryWheel, Timeout);
	}

	//this is the update loop
	public void TurnToAngle(final float Degrees, String StationaryWheel, int Timeout) throws InterruptedException
	{
		Date c = new Date();
		long StartTime = c.getTime();
		long CurrentTime = StartTime;

		this.StationaryWheel = StationaryWheel;

		//remove the historic data values
		ResetValues();

		Motion = "Turn";

		//update the angles, if this is uncommented it gets the values from the UI
		UpdateAngles();

		//set the target to the current position
		telemetry.log.add("rotate " + String.valueOf(Degrees - ComputedRotation) + " degrees");

		Target = Degrees;

		//degrees that something has to be off
		float Error = 3;

		//while the distance from the target is greater than the error
		while ((ValueStandardDeviation() > .1f || Math.abs(ComputedRotation - Target) > Error))
		{
			//get the time for the timeout
			Date r = new Date();
			CurrentTime = r.getTime();

			if(Math.abs(CurrentTime - StartTime) > Timeout * 1000)
			{
				telemetry.log.add("turn timed out");
				break;
			}

			// if the light sensor value is above a certain threshold, stop the movement
			int Threshold = 60;
			if (this.LightSensor.blue() > Threshold && this.LightSensor.red() > Threshold && this.LightSensor.green() > Threshold && this.StopAtLight)
			{
				telemetry.log.add("stopped becuase of light.");
				break;
			}

			if(ValueStandardDeviation() < .001 && Math.abs(ComputedRotation - Target) < 5)
			{
				break;
			}

			//get the standard deviation
			ValueStandardDeviation();

			//this is the update loop
			UpdateTime();
			UpdateAngles();
			PreformCalculations();
			//update telemetry
			telemetry.update();

			//idle using the reference to the instance of the main opmode
			this.MainOpMode.idle();
		}
	}

    void PreformCalculations()
    {
        //add the rotation to the historic data
        HistoricData.add(ComputedRotation - Target);

        DerivativeData.add(ComputedRotation);

        //make sure the list never gets longer than a certain length
        if(HistoricData.size() > 500)
        {
            HistoricData.remove(0);
        }

        //make sure the list never gets longer than a certain length
        if(DerivativeData.size() > 5)
        {
            DerivativeData.remove(0);
        }

        //find the average of the historic data list for the I value
        float IntegralAverage = 0;
        for(int i = 0; i < HistoricData.size(); i++)
        {
            IntegralAverage += (float) HistoricData.get(i);
        }
        IntegralAverage /= HistoricData.size();


        //gets the derivative
        float DerivativeAverage = 0;
        for (int i = 0; i < DerivativeData.size(); i++) {
            DerivativeAverage += (float) DerivativeData.get(i);
        }

        DerivativeAverage /= DerivativeData.size();

        // compute all of the values
        I = (IntegralAverage);
        P = ComputedRotation - Target;

        //constants
        if(Motion == "Straight")
        {
            D = (ComputedRotation - DerivativeAverage) / ((UpdateTime / 1000) * (1 + (DerivativeData.size() / 2)));

			//constants for the real chassis
			PConstant = 4f;
			IConstant = 0f;
			DConstant = .7f;
        }
        else if (Motion == "Turn")
        {
			if(Target < ComputedRotation) {
				D = (ComputedRotation - DerivativeAverage) / ((UpdateTime / 1000) * (1 + (DerivativeData.size() / 2))) - TargetRateOfChange;
			}
			else
			{
				D = (ComputedRotation - DerivativeAverage) / ((UpdateTime / 1000) * (1 + (DerivativeData.size() / 2))) + TargetRateOfChange;

			}

			//functional
			PConstant = 4;
			DConstant = -.1f;
			IConstant = .35f;
        }


		float Direction = I * IConstant + P * PConstant + D * DConstant;

		if (Motion == "Straight" && MovingDirection == "Backward")
		{
			// flip the direction because we're going backwards
			Direction *= -1;
		}

        //constrain the direction so that abs(Direction) < 50
        if(Direction > 50)
        {
            Direction = 50;
        }
        else if (Direction < -50)
        {
            Direction = -50;
        }

        if(Motion == "Straight")
        {
			float Multiplier;

			//make sure that the robot is moving the correct direction
			if(MovingDirection == "Forward") {
				Multiplier = Power * 3 / 4.0f;
				LeftMotor.setPower(Multiplier + (Direction / 200));
				RightMotor.setPower(Multiplier - (Direction / 200));
			}
			else
			{
				Multiplier = Power * -3 / 4.0f;
				LeftMotor.setPower(Multiplier - (Direction / 200));
				RightMotor.setPower(Multiplier + (Direction / 200));
			}
        }
        else if (Motion == "Turn")
        {
            float Multiplier = TurningPower * 4;

			if(StationaryWheel == "Right")
			{
				RightMotor.setPower(0);
				LeftMotor.setPower((Direction / 200) * Multiplier);
			}
			else if(StationaryWheel == "Left") {
				LeftMotor.setPower(0);
				RightMotor.setPower(-(Direction / 200) * Multiplier);
			}
			else if(StationaryWheel == "Neither")
			{
				LeftMotor.setPower((Direction / 200) * Multiplier);
				RightMotor.setPower(-(Direction / 200) * Multiplier);
			}
        }
    }

    void UpdateTime()
    {
        PreviousTime = CurrentTime;
        CurrentTime = (float) ProgramTime.time() * 1000;

        //set the update time
        UpdateTime = CurrentTime - PreviousTime;
    }

    void UpdateAngles() {
		telemetry.addData("00", "Rotation: " + ComputedRotation);

        //sets the previous heading
        PreviousHeading = Heading;

        //sets the current heading
        EulerAngles Angle = imu.getAngularOrientation();
        Heading = (float) Angle.heading;

        //set the previous to to the current
        PreviousComputedRotation = ComputedRotation;

        //set the current rotation
        ComputedRotation = Heading + (Rotations * 360);

		//do rotation computation here
		if(!FirstUpdate) {
			//if it switches from large to small
			if (PreviousHeading > 300 && Heading < 60) {
				//increase the rotations by one
				Rotations++;

				//update again because otherwise it would be out of date
				UpdateAngles();
			}

			if (PreviousHeading < 60 && Heading > 300) {
				Rotations--;

				//update again because otherwise it would be out of date
				UpdateAngles();
			}
		}

		Logging.write(ComputedRotation + ", " + Heading);

        if(FirstUpdate)
        {
            FirstUpdate = false;

			//if it was the first time do it again so that we can get the actual data
			UpdateAngles();
        }
    }
}
