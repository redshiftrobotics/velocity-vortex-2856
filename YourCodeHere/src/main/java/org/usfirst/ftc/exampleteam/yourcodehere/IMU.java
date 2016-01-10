package org.usfirst.ftc.exampleteam.yourcodehere;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.CustomSettingsActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;


public class IMU
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

	//this is the threading function, the code that is run whenever the robot turns
	public String ThreadingFunction = "None";

	//can be "Straight" or "Turn"
	String Motion = "Turn";
	//Forward or Backward are the options
	String MovingDirection = "Forward";
	//can be 'Right' or 'Left'
	String StationaryWheel = "Right";

	//declare the logger
	Logger Logging;

	//from 0 to 1
	//.4 for test chassis
	float Power = .5f;
	float TurningPower = .8f;

	DcMotor LeftMotor;
	DcMotor RightMotor;
	DcMotor BackBrace;
	DcMotorController DriveController;

	HardwareMap hardwareMap;
	TelemetryDashboardAndLog telemetry;
	MainAutonomous MainOpMode;
	// Button btn;

	public IMU(DcMotor LeftMotor, DcMotor RightMotor, DcMotor BackBrace, HardwareMap map, TelemetryDashboardAndLog tel, MainAutonomous OpMode)
	{
		this.BackBrace = BackBrace;
		this.LeftMotor = LeftMotor;
		this.RightMotor = RightMotor;
		//set the hardware map
		hardwareMap = map;
		telemetry = tel;
		MainOpMode = OpMode;

		// setup the IMU
		parameters.angleunit = IBNO055IMU.ANGLEUNIT.DEGREES;
		parameters.accelunit = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
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
		Logging = new Logger("TurnData.txt");
    }

	private void ThreadingFunction(String Function)
	{
		if(Function == "None")
		{

		}
		else if(Function == "BackBraceLower")
		{
			if (Math.abs(MainOpMode.BackBraceInitial - BackBrace.getCurrentPosition()) < 1440 * 4)
			{
				BackBrace.setPower(.5);
			}
			else
			{
				this.BackBrace.setPower(0);
			}
		}
		else if (Function == "BackBraceRaise")
		{
			// if the back brace still needs to be lowered...
			if (Math.abs(MainOpMode.BackBraceInitial - BackBrace.getCurrentPosition()) > 200)
			{
				BackBrace.setPower(-1);
			}
			else
			{
				BackBrace.setPower(0);
			}
		}
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
		Straight(Rotations, 100, "None");
	}

	public void Straight(float Rotations, int Timeout) throws InterruptedException
	{
		Straight(Rotations, Timeout, "None");
	}

    //this is the update loop
    public int Straight(float Rotations, int Timeout, String Function) throws InterruptedException
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

		telemetry.log.add("Function: " + Function);

		while(Math.abs(StartPosition - RightMotor.getCurrentPosition()) < Math.abs(Rotations) * 1400)
		{
			if(Function == "ControlRotation")
			{
				//if the robot is 5 degfrees off, stop its movement
				if(Math.abs(this.Rotation() - MainOpMode.LastStageRotation) > 10)
				{
					telemetry.log.add("turned too much, robot stopped.");
					this.Stop();
					return -1;
				}
			}

			ThreadingFunction(Function);

			//see if it has passed the timeout
			Date a = new Date();
			long Time = a.getTime();

			if(Math.abs(StartTime - Time) > Timeout * 1000)
			{
				telemetry.log.add("Timed Out");
				break;
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
		TurnToAngle(ComputedRotation + Degrees, "Right", "None");
	}

	public void Turn(final float Degrees, final String StationaryWheel, String ThreadingFunction) throws InterruptedException
	{
		TurnToAngle(ComputedRotation + Degrees, StationaryWheel, ThreadingFunction);
	}

	public void Turn(final float Degrees, final String StationaryWheel) throws InterruptedException
	{
		TurnToAngle(ComputedRotation + Degrees, StationaryWheel, "None");
	}

	//this is the update loop
	public void TurnToAngle(final float Degrees, String StationaryWheel, String ThreadingFunction) throws InterruptedException
	{
		this.StationaryWheel = StationaryWheel;

		//remove the historic data values
		ResetValues();

		Motion = "Turn";

		//update the angles, if this is uncommented it gets the values from the UI
		UpdateAngles();

		//set the target to the current position
		telemetry.log.add("current rotation is " + ComputedRotation + ", amount to rotate is " + Degrees);

		Target = Degrees;

		//degrees that something has to be off
		float Error = 3;

		//while the distance from the target is greater than the error
		while (ValueStandardDeviation() > .1f || Math.abs(ComputedRotation - Target) > Error)
		{
			ThreadingFunction(ThreadingFunction);

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
			MainOpMode.idle();
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
			DConstant = -.1f;
			IConstant = .15f;
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
				Multiplier = Power / 2;
				LeftMotor.setPower(Multiplier + (Direction / 200));
				RightMotor.setPower(Multiplier - (Direction / 200));
			}
			else
			{
				Multiplier = Power / -2;
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
