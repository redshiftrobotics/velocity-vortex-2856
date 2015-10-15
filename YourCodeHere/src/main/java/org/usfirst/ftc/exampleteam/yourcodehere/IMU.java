package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class IMU
{
    // Our sensors, motors, and other devices go here, along with other long term state
    static IBNO055IMU imu;
    static IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();

    //heading data
    static float Heading;
    static float PreviousHeading;
    static int Rotations = 0;
    static boolean FirstUpdate = true;
    static ElapsedTime ProgramTime;

    //time data
    static float PreviousTime = 0;
    static float CurrentTime = 0;
    static float UpdateTime = 0;

    //PID data
    static float ComputedRotation;
    static float PreviousComputedRotation;
    static float Target = 20;
    static float TargetRateOfChange = 10;
    static ArrayList HistoricData = new ArrayList();
    static ArrayList DerivativeData = new ArrayList();
    static float D;
    static float I;
    static float P;
    static float DConstant;
    static float IConstant;
    static float PConstant;
    //can be "Straight" or "Turn"
    static String Motion = "Turn";
    //from 0 to 1
    static float Power = .5f;

    //motor setup
    static DcMotorController DriveController;
    static DcMotor LeftMotor;
    static DcMotor RightMotor;
    static HardwareMap hardwareMap;
    static TelemetryDashboardAndLog telemetry;


    public static void Initialize(HardwareMap map, TelemetryDashboardAndLog tel)
    {
        //set the hardware map
        hardwareMap = map;
        telemetry = tel;

        //setup the motors
        DriveController = hardwareMap.dcMotorController.get("drive_controller");
        LeftMotor = hardwareMap.dcMotor.get ("left_drive");
        RightMotor = hardwareMap.dcMotor.get ("right_drive");

        //gets the current encoder position

        LeftMotor.setDirection(DcMotor.Direction.REVERSE); //left motor is reversed

        // setup the IMU
        parameters.angleunit = IBNO055IMU.ANGLEUNIT.DEGREES;
        parameters.accelunit = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "BNO055";

        // the I2C device is names IMU
        imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);

        // Enable reporting of position. Note: this is still buggy
        imu.startAccelerationIntegration(new Position(), new Velocity());

        //setup telemetry
        telemetry.setUpdateIntervalMs(200);

        //setup the program timer
        ProgramTime = new ElapsedTime();

        //sets the current time
        CurrentTime = (float) ProgramTime.time() * 1000;
    }

    //this is the update loop
    public static void Forward(float Rotations)
    {
        Motion = "Straight";

        //start position
        long StartPosition = RightMotor.getCurrentPosition();

        //this is the first update
        FirstUpdate = true;

        //update the angles
        UpdateAngles();

        //set the target to the current position
        Target = ComputedRotation;

        telemetry.addData("7", Math.abs(StartPosition - RightMotor.getCurrentPosition()));

        while(Math.abs(StartPosition - RightMotor.getCurrentPosition()) < Rotations * 1400) {
            //this is the update loop
            UpdateTime();
            UpdateAngles();
            PreformCalculations();

            telemetry.update();
            //idle!!!
        }
    }

    static void PreformCalculations()
    {
        //add the rotation to the historic data
        HistoricData.add(ComputedRotation - Target);

        DerivativeData.add(ComputedRotation);

        //make sure the list never gets longer than a certain length
        if(HistoricData.size() > 20)
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
        I += (IntegralAverage) * (UpdateTime / 1000f);
        P = ComputedRotation - Target;

        //constants
        if(Motion == "Straight")
        {
            D = (ComputedRotation - DerivativeAverage) / ((UpdateTime / 1000) * (1 + (DerivativeData.size() / 2)));

            IConstant = 2.5f;
            PConstant = 3.2f;
            DConstant = 0;
        }
        else if (Motion == "Turn")
        {
            //compute the d with the rate of change
            D = (ComputedRotation - DerivativeAverage) / ((UpdateTime / 1000) * (1 + (DerivativeData.size() / 2))) - TargetRateOfChange;

            DConstant = 3f;
            IConstant = .1f;
            PConstant = 4f;
        }

        float Direction = I * IConstant + P * PConstant + D * DConstant;

        //logs data
        telemetry.addData("00", "P: " + P);
        telemetry.addData("01", "I: " + I);
        telemetry.addData("8", "D: " + D);
        telemetry.addData("6", "Derivative Average: " + DerivativeAverage);
        telemetry.addData("03", "Weight: " + Direction);

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
            float Multiplier = Power / 2;
            LeftMotor.setPower(Multiplier + (Direction / 200));
            RightMotor.setPower(Multiplier - (Direction / 200));
        }
        else if (Motion == "Turn")
        {
            float Multiplier = Power * 2;

            LeftMotor.setPower((Direction / 200) * Multiplier);
            RightMotor.setPower(-(Direction / 200) * Multiplier);
        }
    }

    static void UpdateTime()
    {
        PreviousTime = CurrentTime;
        CurrentTime = (float) ProgramTime.time() * 1000;

        //set the update time
        UpdateTime = CurrentTime - PreviousTime;
    }

    static void UpdateAngles() {
        //sets the previous heading
        PreviousHeading = Heading;

        //sets the current heading
        EulerAngles Angle = imu.getAngularOrientation();
        Heading = (float) Angle.heading;

        //do rotation computation here
        if(!FirstUpdate) {
            //if it switches from large to small
            if (PreviousHeading > 300 && Heading < 60) {
                //increase the rotations by one
                Rotations++;
            }

            if (PreviousHeading < 60 && Heading > 300) {
                Rotations--;
            }
        }

        //set the previous to to the current
        PreviousComputedRotation = ComputedRotation;

        //set the current rotation
        ComputedRotation = Heading + (Rotations * 360);

        if(FirstUpdate)
        {
            FirstUpdate = false;
        }
    }
}
