package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Noah Rose Ledesma on 10/1/16.
 * Re-implementation of the 2856 PID Driving System without the spaghetti.
 * Class name is not finalized. Method names are eh right now, looking to revise them.
 * 
 * Further refactored by Adam Perlin on 12/3/16 for more streamlining, and better design for long
 * term usability.
 */


public class Robot {
    PID pid;
    Time time;
    Hardware hardware;
    public float IMURotations = 0;
    public Telemetry telemetry;
    public Robot(I2cDeviceSynch imu, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3, ColorSensor cs, Telemetry telemetry) {
        this.telemetry = telemetry;
        telemetry.addData("IMU ", "Innitializing");
        telemetry.update();
        this.pid = new PID();
        hardware = new Hardware(m0, m1, m2, m3, imu, cs);
        pid.Target = ((hardware.currentImuOrientation().firstAngle*-1) + 180) % 360;

        // Store the Robot Hardware
         //instantiate color sensor

        Tracking.Setup(Tracking.ImageType.Wheels, VuforiaLocalizer.CameraDirection.FRONT);

        // Start the program clock
        time = new Time();
    }

    public void Push(float Rotations, Float[] movement, int Timeout) {
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        pid.UpdateHeadings(hardware.currentImuOrientation().firstAngle, hardware.currentImuOrientation().firstAngle);
        CalculateAngles();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = time.Now();
        float StartPosition = hardware.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        pid.clearData();
        
        //Calculate PIDS again because Isaac Zinda only knows


        // We need to keep track of how much time passes between a loop.
        float LoopTime = time.Now();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(Math.abs(StartPosition - hardware.m0.getCurrentPosition()) < Math.abs(Rotations) * Hardware.EncoderCount){
            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout < time.Now()){
                // ... stop our loop if we have.
                break;
            }

            // Record the time since the previous loop.
            LoopTime = time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            CalculateAngles();
            // Calculate our PID
            CalculatePID(LoopTime);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((pid.I * pid.ITuning) / 2000) + ((pid.P * pid.PTuning) / 2000) + ((pid.D * pid.DTuning) / 2000);

           // float offset = Tracking.getOffset();

            hardware.m0.setPower(((  (movement[0]) /*+ Range.clip((offset / 200), -.2, .2)*/    - movement[1]) * 0.65) - (Direction));
            hardware.m1.setPower(((  (movement[0]) /*+ Range.clip((offset / 200), -.2, .2)*/  + movement[1]) * 0.65) + (Direction));
            hardware.m2.setPower(((  (movement[0])  /*+ Range.clip((offset / 200), -.2, .2)*/  - movement[1]) * 0.65) + (Direction));
            hardware.m3.setPower(((  (movement[0]) /*+ Range.clip((offset / 200), -.2, .2)*/   + movement[1]) * 0.65) - (Direction));
        }
        // Our drive loop has completed! Stop the motors.
        hardware.m0.setPower(0);
        hardware.m1.setPower(0);
        hardware.m2.setPower(0);
        hardware.m3.setPower(0);
    }

    // Public Interface Methods:

    // Method that moves the robot forward variable number of Rotations. Orientation is verified and
    // corrected by PID control.

    public float[] toMovementVector(float angle) {
        return new float[]{(float) Math.cos(angle), (float) Math.sin(angle)};
    }

    public void Straight(float Rotations, float Angle, int Timeout){
        //Convert angle to movement vector
        float[] movement = toMovementVector(Angle);

        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        pid.UpdateHeadings(hardware.currentImuOrientation().firstAngle, hardware.currentImuOrientation().firstAngle);

        CalculateAngles();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = time.Now();
        float StartPosition = hardware.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        pid.clearData();
        //Calculate PIDS again because Isaac Zinda only knows


        // We need to keep track of how much time passes between a loop.
        float LoopTime = time.Now();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(Math.abs(StartPosition - hardware.m0.getCurrentPosition()) < Math.abs(Rotations) * Hardware.EncoderCount){
            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout < time.Now()){
                // ... stop our loop if we have.
                break;
            }

            // Record the time since the previous loop.
            LoopTime = time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            CalculateAngles();
            // Calculate our PID
            CalculatePID(LoopTime);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((pid.I * pid.ITuning) / 2000) + ((pid.P * pid.PTuning) / 2000) + ((pid.D * pid.DTuning) / 2000);
            hardware.m0.setPower(((movement[0] - movement[1]) * 0.65) - (Direction));
            hardware.m1.setPower(((movement[0] + movement[1]) * 0.65) + (Direction));
            hardware.m2.setPower(((movement[0] - movement[1]) * 0.65) + (Direction));
            hardware.m3.setPower(((movement[0] + movement[1]) * 0.65) - (Direction));
        }
        // Our drive loop has completed! Stop the motors.
        hardware.m0.setPower(0);
        hardware.m1.setPower(0);
        hardware.m2.setPower(0);
        hardware.m3.setPower(0);
    }

    public void MoveToLine(float angle, float speed, int Timeout) {
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        float[] movement = toMovementVector(angle);
        pid.Headings[0] = pid.Headings[1];
        // Then, we assign the new angle heading.
        pid.Headings[1] = ((hardware.imu.getAngularOrientation().firstAngle*-1) + 180) % 360;

        pid.Headings[0] = pid.Headings[1];
        // Then, we assign the new angle heading.
        pid.Headings[1] = ((hardware.imu.getAngularOrientation().firstAngle*-1) + 180) % 360;

        CalculateAngles();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = time.Now();
        float StartPosition = hardware.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        pid.clearData();

        //Calculate PIDS again because Isaac Zinda only knows

        // We need to keep track of how much time passes between a loop.
        float LoopTime = time.Now();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while((hardware.colorSensor.red() + hardware.colorSensor.blue() + hardware.colorSensor.green())/3 < 70){
            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout < time.Now()){ //TODO: add as additional loop condition
                // ... stop our loop if we have.
                break;
            }

            // Record the time since the previous loop.
            LoopTime = time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            CalculateAngles();
            // Calculate our PID
            CalculatePID(LoopTime);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((pid.I * pid.ITuning) / 2000) + ((pid.P * pid.PTuning) / 2000) + ((pid.D * pid.DTuning) / 2000);

            // Define our motor power multiplier

            //telemetry.addData("Direction ", Direction);
            //telemetry.update();

            hardware.m0.setPower(((movement[0] - movement[1]) * speed) - (Direction));
            hardware.m1.setPower(((movement[0] + movement[1]) * speed) + (Direction));
            hardware.m2.setPower(((movement[0] - movement[1]) * speed) + (Direction));
            hardware.m3.setPower(((movement[0] + movement[1]) * speed) - (Direction));
        }
        // Our drive loop has completed! Stop the motors.
        hardware.m0.setPower(0);
        hardware.m1.setPower(0);
        hardware.m2.setPower(0);
        hardware.m3.setPower(0);
    }

    public void AngleTurn(float angle, int Timeout){
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        CalculateAngles();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = time.Now();
        float StartPosition = hardware.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        pid.clearData();


        //Calculate PIDS again because Isaac Zinda only knows
        pid.UpdateHeadings(hardware.currentImuOrientation().firstAngle, hardware.currentImuOrientation().firstAngle);


        // Manually calculate our first target
        pid.updateTarget(pid.getTarget() + angle);

        // We need to keep track of how much time passes between a loop.
        float LoopTime = time.Now();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(StartTime + Timeout > time.Now()){

            // Record the time since the previous loop.
            LoopTime = time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            CalculateAngles();
            // Calculate our PID
            CalculatePID(LoopTime);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((pid.I * pid.ITuning) / 2000) + ((pid.P * pid.PTuning) / 2000) + ((pid.D * pid.DTuning) / 2000);
            // Constrain our direction from being too intense.

            //if(Direction > 50){ Direction = 50; }
            //if(Direction < -50){ Direction = -50; }

            // Define our motor power multiplier

            //telemetry.addData("Direction ", Direction);
            //telemetry.update();

            if(Math.abs(Direction) <= 0.03f) {
                break;
            }

            hardware.m0.setPower(Hardware.POWER_CONSTANT - (Direction));
            hardware.m1.setPower(Hardware.POWER_CONSTANT + (Direction));
            hardware.m2.setPower(Hardware.POWER_CONSTANT  + (Direction));
            hardware.m3.setPower(Hardware.POWER_CONSTANT  - (Direction));
        }
        // Our drive loop has completed! Stop the motors.
        hardware.m0.setPower(0);
        hardware.m1.setPower(0);
        hardware.m2.setPower(0);
        hardware.m3.setPower(0);
    }
    
    // Private Methods

    // Method that grabs the IMU data and calculates a new ComputedAngle.
    private float CalculateAngles() {
        // First we will move the current angle heading into the previous angle heading slot.
        pid.Headings[0] = pid.Headings[1];
        // Then, we assign the new angle heading.

        pid.Headings[1] = ((hardware.imu.getAngularOrientation().firstAngle*-1) + 180) % 360;


        appendLog("Raw IMU: " + Math.abs(hardware.imu.getAngularOrientation().firstAngle) + " " + hardware.imu.getAngularOrientation().secondAngle + " " + hardware.imu.getAngularOrientation().thirdAngle);


        // Finally we calculate a ComputedAngle from the current angle heading.
        pid.ComputedAngle = pid.Headings[1] + (IMURotations * 360);

        Log.e("#####################", "About to increment IMURotations");
        // Now we determine if we need to re-calculate the angles.
        Log.e("############### current", Float.toString(pid.Headings[1]));
        Log.e("############### past", Float.toString(pid.Headings[0]));
        if(pid.Headings[0] > 300 && pid.Headings[1] < 60) {
            Log.e("################# ####", "Adding to IMURotations");
            IMURotations++; //rotations of 360 degrees
            CalculateAngles();
        //} else if(pid.Headings[0] < 300 && pid.Headings[1] > 60) {
        } else if(pid.Headings[0] < 60 && pid.Headings[1] > 300) {
            Log.e("#####################", "Subtracting from IMURotations");
            IMURotations--;
            CalculateAngles();
        }
        return pid.Headings[1];
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

    public void setConstants(float P, float I, float D) {
        pid.P = P;
        pid.I = I;
        pid.D = D;
    }
    
    public void setTuning(float PTune, float ITune, float DTune) {
        pid.PTuning = PTune;
        pid.ITuning = ITune;
        pid.DTuning = DTune;
    }

    // Method that calculates P, I, and D. Requires the time
    private void CalculatePID(float LoopTime) {
        // Append to our data sets.
        pid.IntegralData.add(pid.ComputedAngle - pid.Target);
        pid.DerivativeData.add(pid.ComputedAngle);

        // Keep IntegralData and DerivativeData from having an exceeding number of entries.
        if (pid.IntegralData.size() > 500){
            pid.IntegralData.remove(0);
        }

        if(pid.DerivativeData.size() > 5){
            pid.DerivativeData.remove(0);
        }

        // Set our P, I, and D values.
        // `P` will be the ComputedAngle - Target
        pid.P = pid.ComputedAngle - pid.Target; //TODO: replace with getTarget()

        // `I` will be the average of the IntegralData (Cries softly at the lack of Java8 streams)

        float IntegralAverage = 0;
        for(float value : pid.IntegralData){
            IntegralAverage += value;
        }
        pid.I = IntegralAverage / pid.IntegralData.size();

        // `D` will be the difference of the ComputedAngle and the Derivative average divided by
        // the time since the last loop in seconds multiplied by one plus half of the size of
        // the Derivative data set size.

        float DerivativeAverage = 0;
        for(float value : pid.DerivativeData){
            DerivativeAverage += value;
        }
        DerivativeAverage /= pid.DerivativeData.size();

        pid.D = (pid.ComputedAngle - DerivativeAverage) / ((LoopTime/1000) * (1 + (pid.DerivativeData.size() / 2)));
    }
}


    /**  PID data, including constants, tuning, and current computed target. 
     * Also includes lists of historical data, and helper methods for getting and setting tuning
     */
class PID {
    float ComputedAngle; //rename 
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
    
    public void clearData() {
        DerivativeData.clear();
        IntegralData.clear();
    }
    
    public void UpdateHeadings(float p1, float p2) {
        Headings[0] = Headings[1];
        // Then, we assign the new angle heading.
        Headings[1] = ((p1*-1) + 180) % 360;

        Headings[0] = Headings[1];
        // Then, we assign the new angle heading.
        Headings[1] = ((p2*-1) + 180) % 360;
    }
    
    public void updateTarget(float val) {
        this.Target = val;
    }
    
    public float getTarget() {
        return Target;
    }
}
// Time data
class Time {
    private ElapsedTime ProgramTime;

    public Time(){
        ProgramTime = new ElapsedTime();
    }

    public float Now(){
        return (float) ProgramTime.seconds();
    }

    public float TimeFrom(float PreviousTime){
        return (float) (ProgramTime.seconds() - PreviousTime);
    }
}
// Robot hardware data.
class Hardware {
    //motors indexing around the robot like the quadrants in a graph or like the motors on a drone.
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
    BNO055IMU imu;
    BNO055IMU.Parameters imuParameters;
    public Hardware(DcMotor dc0, DcMotor dc1, DcMotor dc2, DcMotor dc3, I2cDeviceSynch i2cSynch, ColorSensor cs) {
        m0 = dc0;
        m1 = dc1;
        m2 = dc2;
        m3 = dc3;
        m0.setDirection(DcMotorSimple.Direction.REVERSE);
        m3.setDirection(DcMotorSimple.Direction.REVERSE);
        imuInit(i2cSynch);
        colorSensor = cs;
    }
    
    public void imuInit(I2cDeviceSynch i2cSynch) {
        // Initialize the IMU & its parameters. We will always be using Degrees for angle
        // measurement and Meters per sec per sec for acceleration.
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(i2cSynch);
        imu.initialize(imuParameters);
    }
    
    public Orientation currentImuOrientation() {
        return imu.getAngularOrientation();
    }
    
    //our motors
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    ColorSensor colorSensor; //color sensor used for line detection 
    
    final static int EncoderCount = 1440; //encoder count for a full rotation. Should not change.
    final static float POWER_CONSTANT = (3/8f); // I believe this value does not change. 0.5*(3/4)
}
