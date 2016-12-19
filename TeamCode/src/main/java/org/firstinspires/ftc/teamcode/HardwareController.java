package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;

/**
 * HardwareController is the class in which all hardware
 * related values and devices are stored.
 * A HardwareController object is used in {@link PIDController} and
 * {@link MecanumTeleop} to control
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
    private BNO055IMU.Parameters imuParameters;
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

    private ColorSensor colorSensor1;
    private ColorSensor colorSensor2;
    public float colorTolerance;
    //endregion
    //region Speed Data
    private float xSpeed;
    private float ySpeed;
    private float zAngle;
    //endregion

    //region Initialization

    /**
     * Constructor for the HardwareController class setting up
     * the DcMotor array,.
     * @param dcmotors The array of DcMotors.
     */
    HardwareController(DcMotor[] dcmotors){
        //Initialize the motors
        motors = dcmotors;

        //Reverse specific motors based on gears and chains, comment out which ones should not be flipped
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);

        //Set up encoder count
        encoderCount = 1400;
    }

    /**
     * Constructor for the HardwareController class setting up
     * the four DcMotors.
     * @param m0 DcMotor 0.
     * @param m1 DcMotor 1.
     * @param m2 DcMotor 2.
     * @param m3 DcMotor 3.
     */
    HardwareController(DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3){
        //Initialize the motors
        motors = ((DcMotor[]) Utility.MakeArray(m0, m1, m2, m3));

        //Reverse specific motors based on gears and chains, comment out which ones should not be flipped
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);

        //Set up encoder count
        encoderCount = 1400;
    }

    /**
     * Constructor for the HardwareController class setting up
     * the imu, DcMotor array, and ColorSensor.
     * @param imuDevice The imu I2c Device to get current angle.
     * @param dcmotors The array of DcMotors.
     * @param colorSensorDevice1 The ColorSensor Device to get the Line Data.
     * @param colorSensorDevice2 The ColorSensor Device to get the Line Data.
     */
    HardwareController(I2cDeviceSynch imuDevice, DcMotor[] dcmotors, ColorSensor colorSensorDevice1, ColorSensor colorSensorDevice2){
        //Set up the imu Parameters to use correct units
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        //Initialize the imu based on the Parameters
        imu = new AdafruitBNO055IMU(imuDevice);
        imu.initialize(imuParameters);

        //Initialize the motors
        motors = dcmotors;

        //Reverse specific motors based on gears and chains, comment out which ones should not be flipped
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);

        //Set up encoder count
        encoderCount = 1400;

        //Set up colorSensors
        colorSensor1 = colorSensorDevice1;
        colorSensor2 = colorSensorDevice2;
    }

    /**
     * Constructor for the HardwareController class setting up
     * the imu, four DcMotors, and ColorSensor.
     * @param imuDevice The imu I2c Device to get current angle.
     * @param m0 DcMotor 0.
     * @param m1 DcMotor 1.
     * @param m2 DcMotor 2.
     * @param m3 DcMotor 3.
     * @param colorSensorDevice1 The ColorSensor Device to get the Line Data.
     * @param colorSensorDevice2 The ColorSensor Device to get the Line Data.
     */
    HardwareController(I2cDeviceSynch imuDevice, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3, ColorSensor colorSensorDevice1, ColorSensor colorSensorDevice2){
        //Set up the imu Parameters to use correct units
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        //Initialize the imu based on the Parameters
        imu = new AdafruitBNO055IMU(imuDevice);
        imu.initialize(imuParameters);

        //Initialize the motors
        motors = ((DcMotor[]) Utility.MakeArray(m0, m1, m2, m3));

        //Reverse specific motors based on gears and chains, comment out which ones should not be flipped
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);

        //Set up encoder count
        encoderCount = 1400;

        //Set up colorSensor
        colorSensor1 = colorSensorDevice1;
        colorSensor2 = colorSensorDevice2;
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
        float average1 = (colorSensor1.blue() + colorSensor1.green() + colorSensor1.red())/3;
        float average2 = (colorSensor2.blue() + colorSensor2.green() + colorSensor2.red())/3;
        //if either average is greater than the tolerance return true otherwise return false
        return (average1>colorTolerance || average2 >colorTolerance);
    }

    /**
     * Allows for changing of the DirectionObject's values.
     * @param x The value to set the xSpeed.
     * @param y The value to set the ySpeed.
     * @param z The value to set the zAngle.
     */
    public void SetDirection(float x, float y, float z){
        xSpeed = x;
        ySpeed = y;
        zAngle = z;
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
        motors[0].setPower(FrontLeftSpeed());
        motors[1].setPower(FrontRightSpeed());
        motors[2].setPower(BackRightSpeed());
        motors[3].setPower(BackLeftSpeed());
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param x The value to set the xSpeed.
     * @param y The value to set the YSpeed.
     * @param z The value to set the zAngle.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(float x, float y, float z){
        SetDirection(x, y, z);
        RunMotors();
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param dcmotors An array of 4 DcMotors following the structure of {@link HardwareController}.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(DcMotor[] dcmotors){
        motors = dcmotors;
        RunMotors();
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param m0 DcMotor 0 following the structure
     *                of the {@link HardwareController} Object.
     * @param m1 DcMotor 1 following the structure
     *                of the {@link HardwareController} Object.
     * @param m2 DcMotor 2 following the structure
     *                of the {@link HardwareController} Object.
     * @param m3 DcMotor 3 following the structure
     *                of the {@link HardwareController} Object.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3){
        motors = ((DcMotor[]) Utility.MakeArray(m0, m1, m2, m3));
        RunMotors();
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param x The value to set the xSpeed.
     * @param y The value to set the YSpeed.
     * @param z The value to set the zAngle.
     * @param dcmotors An array of 4 DcMotors following the structure of {@link HardwareController}.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(float x, float y, float z, DcMotor[] dcmotors){
        SetDirection(x, y, z);
        motors = dcmotors;
        RunMotors();
    }

    /**
     * Function for setting power to the motors
     * in the motors array.
     * @param x The value to set the xSpeed.
     * @param y The value to set the YSpeed.
     * @param z The value to set the zAngle.
     * @param m0 DcMotor 0 following the structure
     *                of the {@link HardwareController} Object.
     * @param m1 DcMotor 1 following the structure
     *                of the {@link HardwareController} Object.
     * @param m2 DcMotor 2 following the structure
     *                of the {@link HardwareController} Object.
     * @param m3 DcMotor 3 following the structure
     *                of the {@link HardwareController} Object.
     * @see #RunMotors() Base function for all RunMotors functions
     */
    public void RunMotors(float x, float y, float z, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3){
        SetDirection(x, y, z);
        motors = ((DcMotor[]) Utility.MakeArray(m0, m1, m2, m3));
        RunMotors();
    }

    //endregion

    //region Calculate Speed Values

    /**
     * Returns the rotation speed of the front left DcMotor.
     * @return The rotation speed of the front left DcMotor.
     */
    public float FrontLeftSpeed(){
        float speed = 0;

        speed -= ySpeed;
        speed += zAngle;
        speed += xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the front right DcMotor.
     * @return The rotation speed of the front right DcMotor.
     */
    public float FrontRightSpeed(){
        float speed = 0;

        speed += ySpeed;
        speed += zAngle;
        speed += xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the back right DcMotor.
     * @return The rotation speed of the back right DcMotor.
     */
    public float BackRightSpeed(){
        float speed = 0;

        speed += ySpeed;
        speed += zAngle;
        speed -= xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the back left DcMotor.
     * @return The rotation speed of the back left DcMotor.
     */
    public float BackLeftSpeed(){
        float speed = 0;

        speed -= ySpeed;
        speed += zAngle;
        speed -= xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    //endregion

    //endregion
}
