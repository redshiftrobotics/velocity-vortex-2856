package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Noah Rose Ledesma on 10/1/16.
 * Re-implementation of the 2856 PID Driving System without the spaghetti.
 * Class name is not finalized. Method names are eh right now, looking to revise them.
 */
public class Robot {

    public float startDegrees;

    public RobotData Data = new RobotData();

    public float IMURotations = 0;

    //changed from I2cDevice
    public Robot(I2cDeviceSynch imu, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3, Telemetry tm) {

        tm.addData("IMU ", "Innitializing");
        tm.update();
        // Initialize the IMU & its parameters. We will always be using Degrees for angle
        // measurement and Meters per sec per sec for acceleration.
        Data.imuParameters = new BNO055IMU.Parameters();
        Data.imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        Data.imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        Data.imu = new AdafruitBNO055IMU(imu);
        Data.imu.initialize(Data.imuParameters);

        startDegrees = Data.imu.getAngularOrientation().firstAngle * -1;

        // Store the Robot Hardware
        Data.Drive.m0 = m0;
        Data.Drive.m1 = m1;
        Data.Drive.m2 = m2;
        Data.Drive.m3 = m3;
        Data.Drive.m1.setDirection(DcMotorSimple.Direction.REVERSE);
        Data.Drive.m3.setDirection(DcMotorSimple.Direction.REVERSE);

        Data.Drive.EncoderCount = 1400;

        // Start the program clock
        Data.Time = new RobotTime();
    }

    // Public Interface Methods:

    // Method that moves the robot forward variable number of Rotations. Orientation is verified and
    // corrected by PID control.
    public void Straight(float Rotations, Float[] movement, int Timeout, Telemetry tm){
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = Data.imu.getAngularOrientation().firstAngle*-1;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = Data.imu.getAngularOrientation().firstAngle*-1;

        CalculateAngles(tm);

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();
        float StartPosition = Data.Drive.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        Data.PID.IntegralData.clear();
        Data.PID.DerivativeData.clear();


        //Calculate PIDS again because Isaac Zinda only knows


        // Manually calculate our first target
        Data.PID.Target = CalculateAngles(tm); // returns Data.PID.Headings[1]

        // We need to keep track of how much time passes between a loop.
        float LoopTime = Data.Time.CurrentTime();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(Math.abs(StartPosition - Data.Drive.m0.getCurrentPosition()) < Math.abs(Rotations) * Data.Drive.EncoderCount){
            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout < Data.Time.CurrentTime()){
                // ... stop our loop if we have.
                break;
            }

            // Record the time since the previous loop.
            LoopTime = Data.Time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            CalculateAngles(tm);
            // Calculate our PID
            CalculatePID(LoopTime, tm);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((Data.PID.I * Data.PID.ITuning) / 2000) + ((Data.PID.P * Data.PID.PTuning) / 2000) + ((Data.PID.D * Data.PID.DTuning) / 2000);
            // Constrain our direction from being too intense.

            //if(Direction > 50){ Direction = 50; }
            //if(Direction < -50){ Direction = -50; }

            // Define our motor power multiplier

            //tm.addData("Direction ", Direction);
            //tm.update();

            Data.Drive.m0.setPower(Drive.POWER_CONSTANT - (Direction));
            Data.Drive.m1.setPower(Drive.POWER_CONSTANT + (Direction));
            Data.Drive.m2.setPower(Drive.POWER_CONSTANT + (Direction));
            Data.Drive.m3.setPower(Drive.POWER_CONSTANT - (Direction));
//            tm.addData("P", Data.PID.P);
//            if(Data.PID.P > 180) {
//                Data.Drive.m0.setPower(((movement[0] - movement[1]) * 0.65) + (Direction));
//                Data.Drive.m1.setPower(((movement[0] + movement[1]) * 0.65) - (Direction));
//                Data.Drive.m2.setPower(((movement[0] - movement[1]) * 0.65) - (Direction));
//                Data.Drive.m3.setPower(((movement[0] + movement[1]) * 0.65) + (Direction));
//                tm.addData("DIRECTION IS", "NEGATIVE");
//            } else {
//                Data.Drive.m0.setPower(((movement[0] - movement[1]) * 0.65) + (Direction));
//                Data.Drive.m1.setPower(((movement[0] + movement[1]) * 0.65) - (Direction));
//                Data.Drive.m2.setPower(((movement[0] - movement[1]) * 0.65) - (Direction));
//                Data.Drive.m3.setPower(((movement[0] + movement[1]) * 0.65) + (Direction));
//                tm.addData("DIRECTION IS", "POSITIVE");
//            }
//
//            tm.update();
        }
        // Our drive loop has completed! Stop the motors.
        Data.Drive.m0.setPower(0);
        Data.Drive.m1.setPower(0);
        Data.Drive.m2.setPower(0);
        Data.Drive.m3.setPower(0);
    }

    public void AngleTurn(float angle, int Timeout, Telemetry tm){
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        CalculateAngles(tm);

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();
        float StartPosition = Data.Drive.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        Data.PID.IntegralData.clear();
        Data.PID.DerivativeData.clear();


        //Calculate PIDS again because Isaac Zinda only knows


        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = Data.imu.getAngularOrientation().firstAngle*-1;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = Data.imu.getAngularOrientation().firstAngle*-1;


        // Manually calculate our first target
        Data.PID.Target = CalculateAngles(tm) + angle;

        // We need to keep track of how much time passes between a loop.
        float LoopTime = Data.Time.CurrentTime();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(StartTime + Timeout > Data.Time.CurrentTime()){

            // Record the time since the previous loop.
            LoopTime = Data.Time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            CalculateAngles(tm);
            // Calculate our PID
            CalculatePID(LoopTime, tm);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((Data.PID.I * Data.PID.ITuning) / 2000) + ((Data.PID.P * Data.PID.PTuning) / 2000) + ((Data.PID.D * Data.PID.DTuning) / 2000);
            // Constrain our direction from being too intense.

            //if(Direction > 50){ Direction = 50; }
            //if(Direction < -50){ Direction = -50; }

            // Define our motor power multiplier

            //tm.addData("Direction ", Direction);
            //tm.update();

            if(Math.abs(Direction) <= 0.03f) {
                break;
            }

            Data.Drive.m0.setPower(Data.Drive.POWER_CONSTANT - (Direction));
            Data.Drive.m1.setPower(Data.Drive.POWER_CONSTANT + (Direction));
            Data.Drive.m2.setPower(Data.Drive.POWER_CONSTANT  + (Direction));
            Data.Drive.m3.setPower(Data.Drive.POWER_CONSTANT  - (Direction));
        }
        // Our drive loop has completed! Stop the motors.
        Data.Drive.m0.setPower(0);
        Data.Drive.m1.setPower(0);
        Data.Drive.m2.setPower(0);
        Data.Drive.m3.setPower(0);
    }

    public void recenter(int Timeout, Telemetry tm){
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        CalculateAngles(tm);

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();
        float StartPosition = Data.Drive.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        Data.PID.IntegralData.clear();
        Data.PID.DerivativeData.clear();


        //Calculate PIDS again because Isaac Zinda only knows


        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = Data.imu.getAngularOrientation().firstAngle*-1;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = Data.imu.getAngularOrientation().firstAngle*-1;


        // Manually calculate our first target
        Data.PID.Target = startDegrees;

        // We need to keep track of how much time passes between a loop.
        float LoopTime = Data.Time.CurrentTime();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(StartTime + Timeout > Data.Time.CurrentTime()){

            // Record the time since the previous loop.
            LoopTime = Data.Time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            CalculateAngles(tm);
            // Calculate our PID
            CalculatePID(LoopTime, tm);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((Data.PID.I * Data.PID.ITuning) / 2000) + ((Data.PID.P * Data.PID.PTuning) / 2000) + ((Data.PID.D * Data.PID.DTuning) / 2000);
            // Constrain our direction from being too intense.

            //if(Direction > 50){ Direction = 50; }
            //if(Direction < -50){ Direction = -50; }

            // Define our motor power multiplier

            //tm.addData("Direction ", Direction);
            //tm.update();

            if(Math.abs(Direction) <= 0.03f) {
                break;
            }

            Data.Drive.m0.setPower(Data.Drive.POWER_CONSTANT - (Direction));
            Data.Drive.m1.setPower(Data.Drive.POWER_CONSTANT + (Direction));
            Data.Drive.m2.setPower(Data.Drive.POWER_CONSTANT  + (Direction));
            Data.Drive.m3.setPower(Data.Drive.POWER_CONSTANT  - (Direction));
        }
        // Our drive loop has completed! Stop the motors.
        Data.Drive.m0.setPower(0);
        Data.Drive.m1.setPower(0);
        Data.Drive.m2.setPower(0);
        Data.Drive.m3.setPower(0);
    }

    // Private Methods

    // Method that grabs the IMU data and calculates a new ComputedTarget.
    private float CalculateAngles(Telemetry tm){
        // First we will move the current angle heading into the previous angle heading slot.
        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.

        Data.PID.Headings[1] = Data.imu.getAngularOrientation().firstAngle*-1;


        appendLog("Raw IMU: " + Math.abs(Data.imu.getAngularOrientation().firstAngle) + " " + Data.imu.getAngularOrientation().secondAngle + " " + Data.imu.getAngularOrientation().thirdAngle);


        // Finally we calculate a ComputedTarget from the current angle heading.
        Data.PID.ComputedTarget = Data.PID.Headings[1] + (IMURotations * 360);

        Log.e("#####################", "About to increment IMURotations");
        // Now we determine if we need to re-calculate the angles.
        Log.e("############### current", Float.toString(Data.PID.Headings[1]));
        Log.e("############### past", Float.toString(Data.PID.Headings[0]));
        if(Data.PID.Headings[0] > 300 && Data.PID.Headings[1] < 60) {
            Log.e("################# ####", "Adding to IMURotations");
            IMURotations++; //rotations of 360 degrees
            CalculateAngles(tm);
        //} else if(Data.PID.Headings[0] < 300 && Data.PID.Headings[1] > 60) {
        } else if(Data.PID.Headings[0] < 60 && Data.PID.Headings[1] > 300) {
            Log.e("#####################", "Subtracting from IMURotations");
            IMURotations--;
            CalculateAngles(tm);
        }
        return Data.PID.Headings[1];
    }

    public void appendLog(String text)
    {
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // Method that calculates P, I, and D. Requires the time
    private void CalculatePID(float LoopTime, Telemetry tm){


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
        Data.PID.I = IntegralAverage / Data.PID.IntegralData.size();

        // `D` will be the difference of the ComputedTarget and the Derivative average divided by
        // the time since the last loop in seconds multiplied by one plus half of the size of
        // the Derivative data set size.

        float DerivativeAverage = 0;
        for(float value : Data.PID.DerivativeData){
            DerivativeAverage += value;
        }
        DerivativeAverage /= Data.PID.DerivativeData.size();

        Data.PID.D = (Data.PID.ComputedTarget - DerivativeAverage) / ((LoopTime/1000) * (1 + (Data.PID.DerivativeData.size() / 2)));
    }
}


// Data container classes
// RobotData acts as the main container for Data.
// The PID, RobotTime, and Drive Classes act as child data containers for neater organization.

class RobotData {
    BNO055IMU imu;
    BNO055IMU.Parameters imuParameters;
    PID PID;
    RobotTime Time;
    Drive Drive;
    RobotData(){
        PID = new PID();
        Time = new RobotTime();
        Drive = new Drive();
    }
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
    // Constructor
    PID(){
        // Init non-primitives
        DerivativeData = new ArrayList<>();
        IntegralData = new ArrayList<>();
    }
}
// Time data
class RobotTime {
    private ElapsedTime ProgramTime;

    public RobotTime(){
        ProgramTime = new ElapsedTime();
    }

    public float CurrentTime(){
        return (float) ProgramTime.seconds();
    }

    public float TimeFrom(float PreviousTime){
        return (float) (ProgramTime.seconds() - PreviousTime);
    }
}
// Robot hardware data.
class Drive {
    //motors indexing around the robot like the quadrants in a graph or like the motors on a drone
    // for example
    /*
    Front
    ________
    |0    1|
    |      |
    |3    2|
    --------
    Rear
    */
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;

    int EncoderCount;
    final static float POWER_CONSTANT = (3/8f); // I believe this value does not change. 0.5*(3/4)
}
