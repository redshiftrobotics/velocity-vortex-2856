package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;


@TeleOp(name="IMU")
public class IMU extends SynchronousOpMode {
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
    ArrayList HistoricData = new ArrayList();
    float D;
    float I;
    float P;
    float DConstant;
    float IConstant;
    float PConstant;
    //can be "Straight" or "Turn"
    String Motion = "Straight";
    //from 0 to 1
    float Power = .2f;

    //motor setup
    DcMotorController DriveController;
    DcMotor LeftMotor;
    DcMotor RightMotor;

    @Override
    public void main() throws InterruptedException {
        //setup the motors
        DriveController = hardwareMap.dcMotorController.get("drive_controller");
        LeftMotor = hardwareMap.dcMotor.get ("left_drive");
        RightMotor = hardwareMap.dcMotor.get ("right_drive");
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

        // Wait until the game starts
        waitForStart();

        //sets the current time
        CurrentTime = (float) ProgramTime.time() * 1000;

        // Loop and update the dashboard
        while (opModeIsActive())
        {
            //update telemetry
            telemetry.update();

            UpdateTime();
            UpdateAngles();
            PreformCalculations();

            idle();
        }
    }

    void PreformCalculations()
    {
        //add the rotation to the historic data
        HistoricData.add(ComputedRotation);

        //make sure the list never gets longer than four
        if(HistoricData.size() > 10)
        {
            HistoricData.remove(0);
        }

        //find the average of the list
        float Sum = 0;
        for(int i = 0; i < HistoricData.size(); i++)
        {
            Sum += (float) HistoricData.get(i);
        }

        float Average = Sum / HistoricData.size();

        //compute all of the values
        I += (ComputedRotation - Target) * (UpdateTime / 1000f);
        P = ComputedRotation - Target;
        D = (ComputedRotation - Average) / ((UpdateTime / 1000f) * (1 + HistoricData.size() / 2));


        //DConstant = .1f;
        IConstant = .4f;
        PConstant = 1;

        float Direction = D * DConstant + I * IConstant + P * PConstant;

        //logs data
        telemetry.addData("00", "P: " + P);
        telemetry.addData("01", "I: " + I);
        telemetry.addData("02", "D: " + D);
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

    void UpdateTime()
    {
        PreviousTime = CurrentTime;
        CurrentTime = (float) ProgramTime.time() * 1000;

        //set the update time
        UpdateTime = CurrentTime - PreviousTime;
    }

    void UpdateAngles() {
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
            //on the first update set the target rotation
            Target = ComputedRotation;

            FirstUpdate = false;
        }
    }
}
