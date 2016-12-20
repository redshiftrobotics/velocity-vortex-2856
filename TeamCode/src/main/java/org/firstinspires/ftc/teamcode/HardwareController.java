package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;

/**
 * HardwareController is the class in which all hardware
 * related values and devices are stored.
 * A HardwareController object is used in {@link PIDController} and
 * {@link Teleop2856} to control
 * all hardware devices. A HardwareController object contains the
 * information required to control a PID robot.   This information includes:
 * <ul>
 *     <li>The imu and the imu's parameters</li>
 *     <li>The array of DcMotors following a specific structure</li>
 *     <li>The ColorSensor</li>
 *     <li>Functions to calculate what values to set each motor to</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kelsey
 * @author Adam Perlin
 * @author Noah Rose Ledesma
 * @version 3.0, 12/19/2016
 */
public class HardwareController {
    //region Hardware Data
    public BNO055IMU imu;
    private BNO055IMU.Parameters _imuParameters;
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
    public DcMotor[] motors;
    public int encoderCount;

    private ColorSensor _colorSensor1;
    private ColorSensor _colorSensor2;
    public float colorTolerance;
    //endregion
    //region Speed Data
    private float _xSpeed;
    private float _ySpeed;
    private float _zAngle;
    //endregion

    //region Initialization

    /**
     * Constructor for the HardwareController class setting up
     * the DcMotor array,.
     * @param $motors The array of DcMotors.
     */
    HardwareController(DcMotor[] $motors){
        //Initialize the motors
        motors = $motors;

        //Set up encoder count
        encoderCount = 1400;
    }

    /**
     * Constructor for the HardwareController class setting up
     * the four DcMotors.
     * @param $m0 DcMotor 0.
     * @param $m1 DcMotor 1.
     * @param $m2 DcMotor 2.
     * @param $m3 DcMotor 3.
     */
    HardwareController(DcMotor $m0, DcMotor $m1, DcMotor $m2, DcMotor $m3){
        //Initialize the motors
        motors = ((DcMotor[]) Utility.MakeArray($m0, $m1, $m2, $m3));

        //Set up encoder count
        encoderCount = 1400;
    }

    /**
     * Constructor for the HardwareController class setting up
     * the imu, DcMotor array, and ColorSensor.
     * @param $imu The imu I2c Device to get current angle.
     * @param $motors The array of DcMotors.
     * @param $colorSensor1 The ColorSensor Device to get the Line Data.
     * @param $colorSensor2 The ColorSensor Device to get the Line Data.
     */
    HardwareController(I2cDeviceSynch $imu, DcMotor[] $motors, ColorSensor $colorSensor1, ColorSensor $colorSensor2){
        //Set up the imu Parameters to use correct units
        _imuParameters = new BNO055IMU.Parameters();
        _imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        _imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        //Initialize the imu based on the Parameters
        imu = new AdafruitBNO055IMU($imu);
        imu.initialize(_imuParameters);

        //Initialize the motors
        motors = $motors;

        //Set up encoder count
        encoderCount = 1400;

        //Set up colorSensors
        _colorSensor1 = $colorSensor1;
        _colorSensor2 = $colorSensor2;
    }

    /**
     * Constructor for the HardwareController class setting up
     * the imu, four DcMotors, and ColorSensor.
     * @param $imu The imu I2c Device to get current angle.
     * @param $m0 DcMotor 0.
     * @param $m1 DcMotor 1.
     * @param $m2 DcMotor 2.
     * @param $m3 DcMotor 3.
     * @param $colorSensor1 The ColorSensor Device to get the Line Data.
     * @param $colorSensor2 The ColorSensor Device to get the Line Data.
     */
    HardwareController(I2cDeviceSynch $imu, DcMotor $m0, DcMotor $m1, DcMotor $m2, DcMotor $m3, ColorSensor $colorSensor1, ColorSensor $colorSensor2){
        //Set up the imu Parameters to use correct units
        _imuParameters = new BNO055IMU.Parameters();
        _imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        _imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        //Initialize the imu based on the Parameters
        imu = new AdafruitBNO055IMU($imu);
        imu.initialize(_imuParameters);

        //Initialize the motors
        motors = ((DcMotor[]) Utility.MakeArray($m0, $m1, $m2, $m3));

        //Set up encoder count
        encoderCount = 1400;

        //Set up colorSensor
        _colorSensor1 = $colorSensor1;
        _colorSensor2 = $colorSensor2;
    }

    //endregion

    //region Functions

    /**
     * Determines if the robot is over a line, if the ColorSensor's
     * average is higher than the color tolerance.
     * @return <code>true</code> if the ColorSensor detects a line; <code>false</code> otherwise.
     */
    public boolean DetectLine(){
        //Calculate the average of blue, green, and red
        float average1 = (_colorSensor1.blue() + _colorSensor1.green() + _colorSensor1.red())/3;
        float average2 = (_colorSensor2.blue() + _colorSensor2.green() + _colorSensor2.red())/3;
        //if either average is greater than the tolerance return true otherwise return false
        return (average1>colorTolerance || average2 >colorTolerance);
    }

    /**
     * Allows for changing of the DirectionObject's values.
     * @param $xSpeed The value to set the _xSpeed.
     * @param $ySpeed The value to set the _ySpeed.
     * @param $zAngle The value to set the _zAngle.
     */
    public void SetDirection(float $xSpeed, float $ySpeed, float $zAngle){
        _xSpeed = $xSpeed;
        _ySpeed = $ySpeed;
        _zAngle = $zAngle;
    }

    //region Run Motors

    /**
     * Stops all drive motors.
     */
    public void StopMotors(){
        RunMotors(0, 0, 0);
    }

    /**
     * Base function for setting power to the motors
     * in the motors array. This simply sets the power of each
     * to the respective power without changing anything.
     */
    public void RunMotors(){
        motors[0].setPower(_FrontLeftSpeed());
        motors[1].setPower(_FrontRightSpeed());
        motors[2].setPower(_BackRightSpeed());
        motors[3].setPower(_BackLeftSpeed());
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param $xSpeed The value to set the _xSpeed.
     * @param $ySpeed The value to set the YSpeed.
     * @param $zAngle The value to set the _zAngle.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(float $xSpeed, float $ySpeed, float $zAngle){
        SetDirection($xSpeed, $ySpeed, $zAngle);
        RunMotors();
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param $motors An array of 4 DcMotors following the structure of {@link HardwareController}.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(DcMotor[] $motors){
        motors = $motors;
        RunMotors();
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param $m0 DcMotor 0 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m1 DcMotor 1 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m2 DcMotor 2 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m3 DcMotor 3 following the structure
     *                of the {@link HardwareController} Object.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(DcMotor $m0, DcMotor $m1, DcMotor $m2, DcMotor $m3){
        motors = ((DcMotor[]) Utility.MakeArray($m0, $m1, $m2, $m3));
        RunMotors();
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param $xSpeed The value to set the _xSpeed.
     * @param $ySpeed The value to set the YSpeed.
     * @param $zAngle The value to set the _zAngle.
     * @param $motors An array of 4 DcMotors following the structure of {@link HardwareController}.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(float $xSpeed, float $ySpeed, float $zAngle, DcMotor[] $motors){
        SetDirection($xSpeed, $ySpeed, $zAngle);
        motors = $motors;
        RunMotors();
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param $xSpeed The value to set the _xSpeed.
     * @param $ySpeed The value to set the YSpeed.
     * @param $zAngle The value to set the _zAngle.
     * @param $m0 DcMotor 0 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m1 DcMotor 1 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m2 DcMotor 2 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m3 DcMotor 3 following the structure
     *                of the {@link HardwareController} Object.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(float $xSpeed, float $ySpeed, float $zAngle, DcMotor $m0, DcMotor $m1, DcMotor $m2, DcMotor $m3){
        SetDirection($xSpeed, $ySpeed, $zAngle);
        motors = ((DcMotor[]) Utility.MakeArray($m0, $m1, $m2, $m3));
        RunMotors();
    }

    //endregion

    //region Calculate Speed Values

    /**
     * Returns the rotation speed of the front left DcMotor.
     * @return The rotation speed of the front left DcMotor.
     */
    private float _FrontLeftSpeed(){
        float speed = 0;

        speed -= _ySpeed;
        speed += _zAngle;
        speed += _xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the front right DcMotor.
     * @return The rotation speed of the front right DcMotor.
     */
    private float _FrontRightSpeed(){
        float speed = 0;

        speed += _ySpeed;
        speed += _zAngle;
        speed += _xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the back right DcMotor.
     * @return The rotation speed of the back right DcMotor.
     */
    private float _BackRightSpeed(){
        float speed = 0;

        speed += _ySpeed;
        speed += _zAngle;
        speed -= _xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the back left DcMotor.
     * @return The rotation speed of the back left DcMotor.
     */
    private float _BackLeftSpeed(){
        float speed = 0;

        speed -= _ySpeed;
        speed += _zAngle;
        speed -= _xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    //endregion

    //endregion
}
