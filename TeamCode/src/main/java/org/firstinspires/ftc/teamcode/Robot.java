package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

import java.util.ArrayList;

/**
 * Created by Noah Rose Ledesma on 10/1/16.
 * Re-implementation of the 2856 PID Driving System without the spaghetti.
 * Class name is not finalized. Method names are eh right now, looking to revise them.
 */
public class Robot {

    public RobotData Data;

    public Robot(I2cDevice imu, DcMotor LeftDrive, DcMotor RightDrive) {
        // Initialize the IMU & its parameters. We will always be using Degrees for angle
        // measurement and Meters per sec per sec for acceleration.
        Data.imuParameters = new IBNO055IMU.Parameters();
        Data.imuParameters.angleUnit = IBNO055IMU.ANGLEUNIT.DEGREES;
        Data.imuParameters.accelUnit = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
        Data.imu = ClassFactory.createAdaFruitBNO055IMU(imu, Data.imuParameters);

        // Store the Robot Hardware
        Data.Drive.LeftDrive = LeftDrive;
        Data.Drive.RightDrive = RightDrive;
        Data.Drive.EncoderCount = 1400;

        // Start the program clock
        Data.Time = new RobotTime();

        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        Data.PID.Headings[1] = (float) Data.imu.getAngularOrientation().heading;
    }

    // Public Interface Methods:

    // Method that moves the robot forward variable number of Rotations. Orientation is verified and
    // corrected by PID control.
    public void Straight(float Rotations, int Timeout){
        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();
        float StartPosition = Data.Drive.LeftDrive.getCurrentPosition();

        // Reset our Integral and Derivative data.
        Data.PID.IntegralData.clear();
        Data.PID.DerivativeData.clear();

        // We need to keep track of how much time passes between a loop.
        float LoopTime = Data.Time.CurrentTime();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(Math.abs(StartPosition - Data.Drive.LeftDrive.getCurrentPosition()) < Math.abs(Rotations) * Data.Drive.EncoderCount){
            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout > Data.Time.CurrentTime()){
                // ... stop our loop if we have.
                break;
            }

            // Record the time since the previous loop.
            LoopTime = Data.Time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            Rotations = CalculateAngles(Rotations);
            // Calculate our PID
            CalculatePID(LoopTime);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = (Data.PID.I * Data.PID.ITuning) + (Data.PID.P * Data.PID.PTuning) + (Data.PID.D * Data.PID.DTuning);
            // Constrain our direction from being too intense.
            if(Direction > 50){ Direction = 50; }
            if(Direction < -50){ Direction = -50; }

            // Define our motor power multiplier


            // Before we set the power of our motors, we need to adjust for forwards or backwards
            // movement. We can use the sign of Rotations to determine this
            if(Rotations > 0) {
                // We are moving forwards.
                Data.Drive.LeftDrive.setPower(Drive.POWER_CONSTANT + (Direction / 200));
                Data.Drive.RightDrive.setPower(Drive.POWER_CONSTANT - (Direction / 200));
            } else {
                // We are moving backwards
                Data.Drive.LeftDrive.setPower(-Drive.POWER_CONSTANT + (Direction / 200));
                Data.Drive.RightDrive.setPower(-Drive.POWER_CONSTANT - (Direction / 200));
            }
        }
        // Our drive loop has completed! Stop the motors.
        Data.Drive.LeftDrive.setPower(0);
        Data.Drive.RightDrive.setPower(0);
    }

    // Private Methods

    // Method that grabs the IMU data and calculates a new ComputedTarget.
    private float CalculateAngles(float Rotations){
        // First we will move the current angle heading into the previous angle heading slot.
        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = (float) Data.imu.getAngularOrientation().heading;
        // Finally we calculate a ComputedTarget from the current angle heading.
        Data.PID.ComputedTarget = Data.PID.Headings[1] + (Rotations * 360);

        // Now we determine if we need to re-calculate the angles.
        if(Data.PID.Headings[0] > 300 && Data.PID.Headings[1] < 60) {
            Rotations++;
            CalculateAngles(Rotations);
        } else if(Data.PID.Headings[0] < 300 && Data.PID.Headings[1] > 60) {
            Rotations--;
            CalculateAngles(Rotations);
        }
    return Rotations;
    }

    // Method that calculates P, I, and D. Requires the time
    private void CalculatePID(float LoopTime){
        // Append to our data sets.
        Data.PID.IntegralData.add(Data.PID.ComputedTarget - Data.PID.Target);
        Data.PID.DerivativeData.add(Data.PID.ComputedTarget);

        // Keep IntegralData and DerivativeData from having an exceeding number of entries.
        if (Data.PID.IntegralData.size() > 500){
            Data.PID.IntegralData.remove(0);
        }

        if(Data.PID.DerivativeData.size() > 5){
            Data.PID.DerivativeData.remove(0);
        }

        // Set our P, I, and D values.
        // `P` will be the ComputedTarget - Target
        Data.PID.P = Data.PID.ComputedTarget - Data.PID.Target;

        // `I` will be the average of the IntegralData (Cries softly at the lack of Java8 streams)

        float IntegralAverage = 0;
        for(float value : Data.PID.IntegralData){
            IntegralAverage += value;
        }
        Data.PID.I /= Data.PID.IntegralData.size();

        // `D` will be the difference of the ComputedTarget and the Derivative average divided by
        // the time since the last loop in seconds multiplied by one plus half of the size of
        // the Derivative data set size.

        float DerivativeAverage = 0;
        for(float value : Data.PID.DerivativeData){
            DerivativeAverage += value;
        }
        DerivativeAverage /= Data.PID.DerivativeData.size();

        Data.PID.D = (Data.PID.ComputedTarget - DerivativeAverage) / (LoopTime * (1 + (Data.PID.DerivativeData.size() / 2)));
    }
}


// Data container classes
// RobotData acts as the main container for Data.
// The PID, RobotTime, and Drive Classes act as child data containers for neater organization.

class RobotData {
    IBNO055IMU imu;
    IBNO055IMU.Parameters imuParameters;
    PID PID;
    RobotTime Time;
    Drive Drive;
}
// PID data
class PID {
    float ComputedTarget;
    float Target;
    float P, I, D;
    float PTuning, ITuning, DTuning;
    float[] Headings = new float[2];
    ArrayList<Float> DerivativeData;
    ArrayList<Float> IntegralData;
}
// Time data
class RobotTime {
    private ElapsedTime ProgramTime;

    public RobotTime(){
        ProgramTime = new ElapsedTime();
    }

    public float CurrentTime(){
        return (float) ProgramTime.time() * 1000;
    }

    public float TimeFrom(float PreviousTime){
        return (float) (ProgramTime.time() * 1000) - PreviousTime;
    }
}
// Robot hardware data.
class Drive {
    DcMotor LeftDrive;
    DcMotor RightDrive;
    int EncoderCount;
    final static float POWER_CONSTANT = (3/8f); // I believe this value does not change. 0.5*(3/4)
}