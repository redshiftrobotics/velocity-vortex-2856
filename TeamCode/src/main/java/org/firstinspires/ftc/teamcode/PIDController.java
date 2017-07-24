package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
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
    //region Public Variables
    public double xyTolerance = 0.1; //The tolerance of values that the robot can have in the xy plane.
    public double zTolerance = 0.1; //The tolerance of values that the robot can have around the z axis.
    //endregion

    //region Private Variables
    private Timer timer; //A timer used to calculate timeout and looptime.
    private AdafruitBNO055IMU imu; //The imu we use to determine position and orientation.

    private double xError, xErrorLast; //The current and previous error in the x axis.
    private double yError, yErrorLast; //The current and previous error in the y axis.
    private double xTarget, xPosition; //The target and current position in the x axis.
    private double yTarget, yPosition; //The target and current position in the y axis.
    private float zError, zErrorLast; //The current and previous error around the z axis.
    private float zTarget, zHeading; //The target and current orientation around the z axis.

    private double xP, xI, xD; //The calculated PID values for the x axis.
    private double yP, yI, yD; //The calculated PID values for the y axis.
    private float zP, zI, zD; //The calculated PID values for the z axis.
    private double xyTuningP, xyTuningI, xyTuningD; //The PID tuning constants for the xy plane.
    private float zTuningP, zTuningI, zTuningD; //The PID tuning constants for the z axis.

    private double xPower, yPower; //The power to set the movements in the xy plane.
    private float zPower; //The power to set the rotation around the z axis.
    //endregion

    /**
     * Constructor for the PIDController,
     * @param hardwareMap The hardware map that sets up the motors and imu.
     */
    public PIDController(HardwareMap hardwareMap){
        super(DriveType.Holonomic, hardwareMap);
        I2cDeviceSynch imuInit; //Set up the IMU to the parameters we use.
        AdafruitBNO055IMU.Parameters imuParameters;
        imuInit = hardwareMap.i2cDeviceSynch.get("imu");
        imuParameters = new AdafruitBNO055IMU.Parameters();
        imuParameters.angleUnit = AdafruitBNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = AdafruitBNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(imuInit);
        imu.initialize(imuParameters);
        timer = new Timer();
    }

    //region Public Methods
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
        speed = Range.clip(speed, 0, 1); //Clip the speed so it stays in the desired range.
        xTarget = targetPosition[0]; //Set the target x value to move to.
        yTarget = targetPosition[1]; //Set the target y value to move to.
        ClearPID(); //Clear past data from PID.
        timer.StartTimer(); //Start the timer to calculate loop time and time passed.
        while(!timer.TimePassed(timeout)||((Math.abs(xError)<xyTolerance)&&(Math.abs(yError)<xyTolerance)&&((Math.abs(xErrorLast)<xyTolerance)&&(Math.abs(yErrorLast)<xyTolerance)))){ //Until the timeout is hit or the tolerance is hit keep moving.
            timer.SetLoopTime(); //Calculate the looptime.
            Calculate(timer.loopTime/1000); //Calculate the PID values.
            Drive(xPower * speed, yPower * speed, zPower * speed, 0); //Set the drive motors power to the PID values.
        }
        Stop(); //Stop moving the robot when we arrive.
    }

    /**
     * Move the robot to a specific angle with a specified movement vector and speed.
     * @param angle The global angle at which the robot will end facing.
     * @param speed The speed at which the robot will move. A float from 0 to 1.
     * @param timeout The time at which this function will give up.
     */
    public void Drive(float angle, float speed, int timeout){
        speed = Range.clip(speed, 0, 1); //Clip the speed so it stays in the desired range.
        zTarget = angle; //Set the target angle to the angle we are heading to.
        ClearPID(); //Clear past data from PID.
        timer.StartTimer(); //Start the timer to calculate loop time and time passed.
        while(!timer.TimePassed(timeout)||((Math.abs(zError)<zTolerance)&&(Math.abs(zErrorLast)<zTolerance))){ //Until the timeout is hit or the tolerance is hit keep moving.
            timer.SetLoopTime(); //Calculate the looptime.
            Calculate(timer.loopTime/1000); //Calculate the PID values.
            Drive(xPower * speed, yPower * speed, zPower * speed, 0); //Set the drive motors power to the PID values.
        }
        Stop(); //Stop moving the robot when we arrive.
    }
    //endregion

    //region Private Methods
    /**
     * A function to execute all function necessary to run a PID loop.
     * @param loopTime The amount of time that the last loop run took in seconds.
     */
    private void Calculate(float loopTime){
        xPosition = imu.getPosition().x; //Set the current positions and heading.
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
        xP = xError; //P value = Error.
        xI += xError * loopTime; //I value = Integral(Past error up to now).
        xD = (xError - xErrorLast) / loopTime; //D value = Slope of the error.
        xPower = (xP * xyTuningP) + (xI * xyTuningI) + (xD * xyTuningD); //Power = P * Kp + I * Ki + D * Kd.

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
        xErrorLast = xError; //Set the last error.
        xError = xTarget - xPosition; //Calculate the new error.
        yErrorLast = yError;
        yError = yTarget - yPosition;
        zErrorLast = zError;
        if (zHeading - zTarget + 360 <= 180) { //Calculate the angular error the best way and direction to optimize distance.
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
        zP = 0; //Set every value to 0.
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
    //endregion
}
