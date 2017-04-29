package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Duncan on 4/28/2017.
 */
public class PIDController {
    private DriveController driveController;
    private Timer timer;
    private BNO055IMU imu;

    private float error, lastError;
    private float target, heading;

    private float P, I, D;
    private float pTuning, iTuning, dTuning;
    private float correction;

    public PIDController(HardwareMap hardwareMap){
        driveController = new DriveController(DriveController.DriveType.Holonomic, hardwareMap);
        I2cDeviceSynch imuInit;
        BNO055IMU.Parameters imuParameters;
        imuInit = hardwareMap.i2cDeviceSynch.get("imu");
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(imuInit);
        imu.initialize(imuParameters);
        heading = imu.getAngularOrientation().firstAngle;
        target = heading;
        timer = new Timer();
    }

    public void SetTuning(float p, float i, float d){
        pTuning = p;
        iTuning = i;
        dTuning = d;
    }

    /**
     * Move the robot in a straight line with a specified movement vector and speed.
     * @param magnitude The distance the robot will travel.
     * @param direction The global angle at which the robot will travel.
     * @param angle The global angle at which the robot will end facing.
     * @param speed The speed at which the robot will move. A float from 0 to 1.
     */
    public void Move(float magnitude, float direction, float angle, float speed, int timeout){
        speed = Range.clip(speed, 0, 1);
        float x = (float)Math.sin(direction) * speed;
        float y = (float)Math.cos(direction) * speed;
        P = 0;
        I = 0;
        D = 0;
        target = angle; //Set the target angle to the angle we are heading to.
        error = 0; //Clear out error.
        CalculateError(); //Calculate the current error and clear out the last error.
        timer.StartTimer(); //Start the timer to calculate loop time and time passed.
        while(!timer.TimePassed(timeout)/*||distance traveled is the magnitude*/){
            timer.SetLoopTime();
            heading = imu.getAngularOrientation().firstAngle;
            CalculateError();
            CalculatePID(timer.loopTime/1000);
            driveController.Drive(x, y, correction, 0);
        }
        driveController.Stop();
    }

    private void CalculatePID(float loopTime){
        P = error;
        I += error * loopTime;
        D = (error - lastError) / loopTime;
        correction = (P * pTuning) + (I * iTuning) + (D * dTuning);
    }

    private void CalculateError(){
        lastError = error;
        if (heading - target + 360 <= 180) {
            error = (heading -  target + 360) * -1;
        } else if (target + 360 - heading <= 180) {
            error = (target - heading + 360);
        } else if (heading - target <= 180 && heading - target >= 0) {
            error = (heading - target) * -1;
        }else{
            error = (target - heading);
        }
    }
}
