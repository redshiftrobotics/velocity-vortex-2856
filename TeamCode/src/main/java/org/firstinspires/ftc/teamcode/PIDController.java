package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;

/**
 * New and improved PID class to control autonomous programs.
 * Updated to have x, y, and z PID.
 * @author Duncan McKee
 * @version 2.0
 */
public class PIDController extends DriveController {
    private Timer timer;
    private BNO055IMU imu;

    private double xError, xErrorLast;
    private double yError, yErrorLast;
    private double xTarget, xPosition;
    private double yTarget, yPosition;
    private float zError, zErrorLast;
    private float zTarget, zHeading;

    private double xP, xI, xD;
    private double yP, yI, yD;
    private float zP, zI, zD;
    private double xyTuningP, xyTuningI, xyTuningD;
    private float zTuningP, zTuningI, zTuningD;

    private double xPower, yPower;
    private float zPower;

    /**
     * Constructor for the PIDController,
     * @param hardwareMap The hardware map that sets up the motors and imu.
     */
    public PIDController(HardwareMap hardwareMap){
        super(DriveType.Holonomic, hardwareMap);
        I2cDeviceSynch imuInit;
        BNO055IMU.Parameters imuParameters;
        imuInit = hardwareMap.i2cDeviceSynch.get("imu");
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(imuInit);
        imu.initialize(imuParameters);
        zHeading = imu.getAngularOrientation().firstAngle;
        zTarget = zHeading;
        timer = new Timer();
    }

    /**
     * Sets the zTuning constants to new values.
     * @param p The value for the zTuningP constant.
     * @param i The value for the zTuningI constant.
     * @param d The value for the zTuningD constant.
     */
    public void SetTuningZ(float p, float i, float d){
        zTuningP = p;
        zTuningI = i;
        zTuningD = d;
    }

    /**
     * Sets the xyTuning constants to new values.
     * @param p The value for the xyTuningP constant.
     * @param i The value for the xyTuningI constant.
     * @param d The value for the xyTuningD constant.
     */
    public void SetTuningXY(float p, float i, float d){
        xyTuningP = p;
        xyTuningI = i;
        xyTuningD = d;
    }

    /**
     * Move the robot in a straight line with a specified movement vector and speed.
     * @param targetPosition The position the robot will travel to.
     * @param speed The speed at which the robot will move. A float from 0 to 1.
     * @param timeout The time at which this function will give up.
     */
    public void Drive(float[] targetPosition, float speed, int timeout){
        speed = Range.clip(speed, 0, 1);
        xTarget = targetPosition[0];
        yTarget = targetPosition[1];
        ClearPID();
        timer.StartTimer(); //Start the timer to calculate loop time and time passed.
        while(!timer.TimePassed(timeout)/*||distance traveled is the magnitude*/){
            timer.SetLoopTime();
            Calculate(timer.loopTime/1000);
            Drive(xPower * speed, yPower * speed, zPower * speed, 0);
        }
        Stop();
    }

    /**
     * Move the robot to a specific angle with a specified movement vector and speed.
     * @param angle The global angle at which the robot will end facing.
     * @param speed The speed at which the robot will move. A float from 0 to 1.
     * @param timeout The time at which this function will give up.
     */
    public void Drive(float angle, float speed, int timeout){
        speed = Range.clip(speed, 0, 1);
        zTarget = angle; //Set the target angle to the angle we are heading to.
        ClearPID();
        timer.StartTimer(); //Start the timer to calculate loop time and time passed.
        while(!timer.TimePassed(timeout)/*||distance traveled is the magnitude*/){
            timer.SetLoopTime();
            Calculate(timer.loopTime/1000);
            Drive(xPower * speed, yPower * speed, zPower * speed, 0);
        }
        Stop();
    }

    /**
     * A function to execute all function necessary to run a PID loop.
     * @param loopTime The amount of time that the last loop run took in seconds.
     */
    private void Calculate(float loopTime){
        xPosition = imu.getPosition().x;
        yPosition = imu.getPosition().y;
        zHeading = imu.getAngularOrientation().firstAngle;
        CalculateError();
        CalculatePID(loopTime);
    }

    /**
     * A function to calculate the specific values for x, y, and z PID.
     * @param loopTime The amount of time that the last loop run took in seconds.
     */
    private void CalculatePID(float loopTime){
        xP = xError;
        xI += xError * loopTime;
        xD = (xError - xErrorLast) / loopTime;
        xPower = (xP * xyTuningP) + (xI * xyTuningI) + (xD * xyTuningD);

        yP = yError;
        yI += yError * loopTime;
        yD = (yError - yErrorLast) / loopTime;
        yPower = (yP * xyTuningP) + (yI * xyTuningI) + (yD * xyTuningD);

        zP = zError;
        zI += zError * loopTime;
        zD = (zError - zErrorLast) / loopTime;
        zPower = (zP * zTuningP) + (zI * zTuningI) + (zD * zTuningD);
    }

    /**
     * A function that calculates the error for each of the 3 dimensions.
     * As well as storing the past error for calculation use.
     */
    private void CalculateError(){
        xErrorLast = xError;
        xError = xTarget - xPosition;
        yErrorLast = yError;
        yError = yTarget - yPosition;
        zErrorLast = zError;
        if (zHeading - zTarget + 360 <= 180) {
            zError = (zHeading -  zTarget + 360) * -1;
        } else if (zTarget + 360 - zHeading <= 180) {
            zError = (zTarget - zHeading + 360);
        } else if (zHeading - zTarget <= 180 && zHeading - zTarget >= 0) {
            zError = (zHeading - zTarget) * -1;
        }else{
            zError = (zTarget - zHeading);
        }
    }

    /**
     * A function that resets all variables of PID to ensure that there is no bleed over between runs.
     */
    private void ClearPID(){
        zP = 0;
        zI = 0;
        zD = 0;
        xP = 0;
        xI = 0;
        xD = 0;
        yP = 0;
        yI = 0;
        yD = 0;
        zError = 0; //Clear out error.
        xError = 0; //Clear out error.
        yError = 0; //Clear out error.
        CalculateError(); //Calculate the current error and clear out the last error.
    }
}
