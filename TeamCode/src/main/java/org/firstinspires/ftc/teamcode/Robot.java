package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorMRRangeSensor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by Noah Rose Ledesma on 10/1/16.
 * Re-implementation of the 2856 PID Driving System without the spaghetti.
 * Class name is not finalized. Method names are eh right now, looking to revise them.
 */
public class Robot {

    public RobotData Data = new RobotData();

    public float IMURotations = 0;

    //RangeSensor Setup
    //set up range sensor stuff for WallFollow()
    byte[] rangeCache;
    I2cDeviceSynch RANGE_Reader;

    I2cAddr RANGEADDRESS = new I2cAddr(0x14);
    final int RANGE_REG_START = 0x04;
    final int RANGE_READ_LENGTH = 2;

    LinearOpMode opMode;

    //changed from I2cDevice
    public Robot(LinearOpMode op, I2cDeviceSynch imu, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3, I2cDevice rs, Telemetry tm) {


        opMode = op;

        tm.addData("IMU ", "Initializing...");
        tm.update();
        // Initialize the IMU & its parameters. We will always be using Degrees for angle
        // measurement and Meters per sec per sec for acceleration.
        Data.imuParameters = new BNO055IMU.Parameters();
        Data.imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        Data.imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        Data.imu = new AdafruitBNO055IMU(imu);
        Data.imu.initialize(Data.imuParameters);

        Data.PID.Target = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        tm.addData("IMU ", "Initialized.");
        tm.update();

        // Store the Robot Hardware
        Data.Drive.m0 = m0;
        Data.Drive.m1 = m1;
        Data.Drive.m2 = m2;
        Data.Drive.m3 = m3;
        Data.Drive.m0.setDirection(DcMotorSimple.Direction.REVERSE);
        Data.Drive.EncoderCount = 1400;

        //Tracking.Setup(Tracking.ImageType.Wheels, VuforiaLocalizer.CameraDirection.FRONT);

        // Start the program clock


        // range sensor setup
        RANGE_Reader = new I2cDeviceSynchImpl(rs, RANGEADDRESS, false);
        RANGE_Reader.engage();
    }
    // Public Interface Methods:

    // Method that moves the robot forward variable number of Rotations. Orientation is verified and
    // corrected by PID control.
    public void Straight(float Rotations, Float[] movement, int Timeout, Telemetry tm){
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.

        Data.Time = new RobotTime();

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        updateHeadings();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();
        float StartPosition = Data.Drive.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        Data.PID.IntegralData.clear();
        Data.PID.DerivativeData.clear();


        //Calculate PIDS again because Isaac Zinda only knows


        // We need to keep track of how much time passes between a loop.
        float LoopTime = Data.Time.CurrentTime();

        float SystemTime = System.currentTimeMillis();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(Math.abs(StartPosition - Data.Drive.m0.getCurrentPosition()) < Math.abs(Rotations) * Data.Drive.EncoderCount && opMode.opModeIsActive()){
            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout < Data.Time.CurrentTime()){
                // ... stop our loop if we have.
                break;
            }

            // Record the time since the previous loop.
            SystemTime = System.currentTimeMillis() - SystemTime;
            tm.log().add(Float.toString(SystemTime));
            tm.update();
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            updateHeadings();
            // Calculate our PID
            CalculatePID(LoopTime, tm);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((Data.PID.I * Data.PID.ITuning) / 2000) + ((Data.PID.P * Data.PID.PTuning) / 2000) + ((Data.PID.D * Data.PID.DTuning) / 2000);
                Data.Drive.m0.setPower(((movement[0] * Drive.STRAIGHT_POWER_CONSTANT) + Direction) * (35f/45f));
                Data.Drive.m1.setPower(((movement[0] * Drive.STRAIGHT_POWER_CONSTANT) - Direction) * (35f/45f));
        }
        // Our drive loop has completed! Stop the motors.
        Data.Drive.m0.setPower(0);
        Data.Drive.m1.setPower(0);
    }

    // for gettin just the raw distance
    public int getDistance() {
        rangeCache = RANGE_Reader.read(RANGE_REG_START, RANGE_READ_LENGTH);
        return (rangeCache[0] & 0xFF);
    }

    public void AlignWithWall(int targetDistance, Float[] movement, int Timeout, Telemetry tm){
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        updateHeadings();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();
        float StartPosition = Data.Drive.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        Data.PID.IntegralData.clear();
        Data.PID.DerivativeData.clear();


        //Calculate PIDS again because Isaac Zinda only knows


        // We need to keep track of how much time passes between a loop.
        float LoopTime = Data.Time.CurrentTime();

        float SystemTime = System.currentTimeMillis();



        rangeCache = RANGE_Reader.read(RANGE_REG_START, RANGE_READ_LENGTH);
        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while((rangeCache[0] & 0xFF) > targetDistance && opMode.opModeIsActive()) {
            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout < Data.Time.CurrentTime()) {
                // ... stop our loop if we have.
                break;
            }

            // Record the time since the previous loop.
            SystemTime = System.currentTimeMillis() - SystemTime;
            tm.log().add(Float.toString(SystemTime));
            tm.update();
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            updateHeadings();
            // Calculate our PID
            CalculatePID(LoopTime, tm);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((Data.PID.I * Data.PID.ITuning) / 2000) + ((Data.PID.P * Data.PID.PTuning) / 2000) + ((Data.PID.D * Data.PID.DTuning) / 2000);
            Data.Drive.m0.setPower(((movement[0] * 0.25) + Direction) * (35f/45f));
            Data.Drive.m1.setPower(((movement[0] * 0.25) - Direction) * (35f/45f));
            rangeCache = RANGE_Reader.read(RANGE_REG_START, RANGE_READ_LENGTH);
        }
        // Our drive loop has completed! Stop the motors.
        Data.Drive.m0.setPower(0);
        Data.Drive.m1.setPower(0);
    }


    public void WallFollow(int targetDistance, Float[] movement, String side, ColorSensor cs, int Timeout, Telemetry tm){
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.

        float initAngle = Data.PID.Target;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        updateHeadings();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();

        // Reset our Integral and Derivative data.
        //Data.PID.uDerivativeData.clear();

        // We need to keep track of how much time passes between a loop.
        float LoopTime = Data.Time.CurrentTime();

        float SystemTime = System.currentTimeMillis();
        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while((cs.red() + cs.blue() + cs.green())/3 < 50 && opMode.opModeIsActive()){


            // Record the time since the previous loop.
            SystemTime = System.currentTimeMillis() - SystemTime;

            float targetAngle;
            if (side.equals("left")) {
                targetAngle = (getDistance() - targetDistance) * 3.5f; // as we get farther away, it gets more negative... const is scalar
            } else {
                targetAngle = (targetDistance - getDistance()) * 3.5f; // as we get farther away, it gets more positive... const is scalar
            }

            SetTarget(initAngle + Range.clip(targetAngle, -15f, 15f));

            updateHeadings();
            // Calculate our PID
            CalculatePID(LoopTime, tm);

            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout < Data.Time.CurrentTime()){
                // ... stop our loop if we have.
                break;
            }

            // Calculate our angles. This method may modify the input Rotations.
            // Calculate our PID
            //UltrasonicPID(LoopTime, side, tm);

            //tm.addData("Direction", Direction);
            //tm.update();
            float Direction = ((Data.PID.I * Data.PID.ITuning) / 2000) + ((Data.PID.P * Data.PID.PTuning) / 2000) + ((Data.PID.D * Data.PID.DTuning) / 2000);
            Data.Drive.m0.setPower(((movement[0] * 0.25) - Direction) * (35f/45f));
            Data.Drive.m1.setPower(((movement[0] * 0.25) + Direction) * (35f/45f));
        }
        // Our drive loop has completed! Stop the motors.
        Data.Drive.m0.setPower(0);
        Data.Drive.m1.setPower(0);
    }

    private double filterUS(){
        while(Data.Drive.lus.getUltrasonicLevel()==0||Data.Drive.lus.getUltrasonicLevel()==255);
        return Data.Drive.lus.getUltrasonicLevel();
    }

    public void MoveToLine(Float[] movement, OpticalDistanceSensor cs, float speed, int Timeout, Telemetry tm){
        Data.Time = new RobotTime();
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        updateHeadings();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();

        // Reset our Integral and Derivative data.
        Data.PID.IntegralData.clear();
        Data.PID.DerivativeData.clear();

        // We need to keep track of how much time passes between a loop.
        float LoopTime = Data.Time.CurrentTime();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(cs.getLightDetected() * 1024 < 500 && opMode.opModeIsActive()){ // 40 working
            // First we check if we have exceeded our timeout and...
            if(StartTime + Timeout < Data.Time.CurrentTime()){
                // ... stop our loop if we have.
                break;
            }

            // Record the time since the previous loop.
            LoopTime = Data.Time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            updateHeadings();
            // Calculate our PID
            CalculatePID(LoopTime, tm);

            // Calculate the Direction to travel to correct any rotational errors.
            float Direction = ((Data.PID.I * Data.PID.ITuning) / 2000) + ((Data.PID.P * Data.PID.PTuning) / 2000) + ((Data.PID.D * Data.PID.DTuning) / 2000);

            Data.Drive.m0.setPower((movement[0] * speed) + Direction);
            Data.Drive.m1.setPower((movement[0] * speed) - Direction);
        }
        // Our drive loop has completed! Stop the motors.
        Data.Drive.m0.setPower(0);
        Data.Drive.m1.setPower(0);
    }

    public void UpdateTarget(float angle) {
        Data.PID.Target += angle - Data.PID.TARGET_MODIFIER;
    }

    public void SetTarget(float angle) {
        Data.PID.Target = angle - Data.PID.TARGET_MODIFIER; // hardset, dangerous, doesn't take into account starting pos... keep that in mind
    }

    public void AngleTurn(float angle, int Timeout, Telemetry tm){
        Data.Time = new RobotTime();
        tm.addData("Started", "TURNING");
        tm.update();

        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        updateHeadings();

        // Get the current program time and starting encoder position before we start our drive loop
        float StartTime = Data.Time.CurrentTime();
        float StartPosition = Data.Drive.m0.getCurrentPosition();

        // Reset our Integral and Derivative data.
        Data.PID.IntegralData.clear();
        Data.PID.DerivativeData.clear();


        //Calculate PIDS again because Isaac Zinda only knows


        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.
        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;

        float LoopTime = Data.Time.CurrentTime();
        CalculatePID(LoopTime, tm);


        // Manually calculate our first target
        Data.PID.Target += angle - Data.PID.TARGET_MODIFIER;
        // We need to keep track of how much time passes between a loop.

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(StartTime + Timeout > Data.Time.CurrentTime() && opMode.opModeIsActive()){

            // Record the time since the previous loop.
            LoopTime = Data.Time.TimeFrom(LoopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            updateHeadings();
            // Calculate our PID
            CalculatePID(LoopTime, tm);

            // Calculate the Direction to travel to correct any rotational errors
            float Direction = ((Data.PID.I * Data.PID.ITuning) / 2000) + ((Data.PID.P * Data.PID.PTuning) / 2000) + ((Data.PID.D * Data.PID.DTuning) / 2000);
            // Constrain our direction from being too intense.

            //if(Direction > 50){ Direction = 50; }
            //if(Direction < -50){ Direction = -50; }

            // Define our motor power multiplier

            //tm.addData("Direction ", Direction);
            //tm.update();

            if (Math.abs(Data.PID.P) <= Data.PID.turnPrecision) {
                tm.addData("Finished", Math.abs(Data.PID.P) + " <= " + Data.PID.turnPrecision);
                tm.update();
                break;
            }

            Data.Drive.m0.setPower(((Direction*0.1/Math.abs(Direction)) + Direction) * (35f/45f));
            Data.Drive.m1.setPower((-(Direction*0.1/Math.abs(Direction)) - Direction) * (35f/45f));
//            Data.Drive.m0.setPower(-.5 * (Direction/Math.abs(Direction)));
//            Data.Drive.m1.setPower(.5 * (Direction/Math.abs(Direction)));
//            Data.Drive.m2.setPower(.5 * (Direction/Math.abs(Direction)));
//            Data.Drive.m3.setPower(-.5 * (Direction/Math.abs(Direction)));
        }

        // Our drive loop has completed! Stop the motors.
        Data.PID.P = 0;
        Data.PID.I = 0;
        Data.PID.D = 0;
        Data.Drive.m0.setPower(0);
        Data.Drive.m1.setPower(0);
    }

    // Private Methods
    private void updateHeadings() {
        Data.PID.Headings[0] = Data.PID.Headings[1];
        // Then, we assign the new angle heading.

        Data.PID.Headings[1] = ((Data.imu.getAngularOrientation().firstAngle*-1) + 180 - Data.PID.TARGET_MODIFIER) % 360;
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
        //Data.PID.IntegralData.add(Data.PID.ComputedTarget - Data.PID.Target);
        //Data.PID.DerivativeData.add(Data.PID.ComputedTarget);

        Data.PID.I += (Data.PID.P) * Math.abs(LoopTime/1000);

//        // Keep IntegralData and DerivativeData from having an exceeding number of entries.
//        if (Data.PID.IntegralData.size() > 500){
//            Data.PID.IntegralData.remove(0);
//        }

//        if(Data.PID.DerivativeData.size() > 5){
//            Data.PID.DerivativeData.remove(0);
//        }

        if (Data.PID.Headings[1] + 360 - Data.PID.Target <= 180) {
            Data.PID.P = (Data.PID.Headings[1] -  Data.PID.Target + 360);
        } else if ( Data.PID.Target + 360 - Data.PID.Headings[1] <= 180) {
            Data.PID.P = ( Data.PID.Target - Data.PID.Headings[1] + 360) * -1;
        } else if (Data.PID.Headings[1] -  Data.PID.Target <= 180) {
            Data.PID.P = (Data.PID.Headings[1] -  Data.PID.Target);
        }
        // Set our P, I, and D values.
        // `P` will be the ComputedTarget - Target

        // `I` will be the average of the IntegralData (Cries softly at the lack of Java8 streams)

//        float IntegralAverage = 0;
//        for(float value : Data.PID.IntegralData){
//            IntegralAverage += value;
//        }
//        Data.PID.I = IntegralAverage / Data.PID.IntegralData.size();

        // `D` will be the difference of the ComputedTarget and the Derivative average divided by
        // the time since the last loop in seconds multiplied by one plus half of the size of
        // the Derivative data set size.

        float DerivativeAverage = 0;
        for(float value : Data.PID.DerivativeData){
            DerivativeAverage += value;
        }
        //DerivativeAverage /= Data.PID.DerivativeData.size();
        Data.PID.D = ((Data.PID.P)-Data.PID.LastError)/(LoopTime/1000);
        //Data.PID.D = (Data.PID.ComputedTarget - DerivativeAverage) / ((LoopTime/1000) * (1 + (Data.PID.DerivativeData.size() / 2)));
        Data.PID.LastError = Data.PID.P;
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
    float TARGET_MODIFIER = 0f;
    public float turnPrecision = 1f;
    float ComputedTarget;
    float Target;
    float uComputedTarget;
    float P, I, D;
    float uP, uI, uD;
    float PTuning, ITuning, DTuning;
    float[] Headings = new float[2];
    float[] uHeadings = new float[2];
    float LastError;
    float uLastError;
    ArrayList<Float> DerivativeData;
    ArrayList<Float> uDerivativeData;
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
        ProgramTime = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
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
    UltrasonicSensor lus;
    UltrasonicSensor rus;
    int EncoderCount;
    static float STRAIGHT_POWER_CONSTANT = 1f;
}
