package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * PIDController is the class for all PID
 * related movements and calculations.
 * A PIDController object encapsulates the information
 * required to run 2856's autonomous.   This information includes:
 * <ul>
 *     <li>A {@link HardwareData} Object</li>
 *     <li>A {@link PIDData} Object</li>
 *     <li>A {@link Time} Object</li>
 *     <li>A Direction Object</li>
 *     <li>The OpModes Telemetry</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kelsey
 * @author Adam Perlin
 * @author Noah Rose Ledesma
 * @version 2.0, 12/18/2016
 * @deprecated Replaced with {@link PIDController} on 12/19/2016.
 */
public class PIDControllerOld {
    //region Private Data
    private PIDDataOld pidData;
    private HardwareData hardwareData;
    private Time time;
    private DirectionObject directionObject;
    private Telemetry telemetry;
    //endregion

    //region Initialization

    /**
     * Constructor for the PIDController class using
     * an array of DcMotors.
     * @param imuInput The imu I2c Device to get current angle.
     * @param motorsInput The array of DcMotors following the structure of
     *                    the {@link HardwareData} Object.
     * @param colorSensorInput The ColorSensor Device to use {@link #DetectLine()}.
     * @param telemetryInput The Telemetry of the phone to output data.
     */
    public PIDControllerOld(I2cDeviceSynch imuInput, DcMotor[] motorsInput, ColorSensor colorSensorInput, Telemetry telemetryInput){
        //Set up the private variables
        telemetry = telemetryInput;
        hardwareData = new HardwareData(imuInput, motorsInput, colorSensorInput);
        time = new Time();
        directionObject = new DirectionObject(0, 0, 0);

        //Set the target angle to the current angle
        pidData.targetAngle = hardwareData.imu.getAngularOrientation().firstAngle*-1;
    }

    /**
     * Contructor for the PIDController class using
     * four seperate DcMotors
     * @param imuInput The imu I2c Device to get current angle.
     * @param m0Input DcMotor 0 following the structure
     *                of the {@link HardwareData} Object.
     * @param m1Input DcMotor 1 following the structure
     *                of the {@link HardwareData} Object.
     * @param m2Input DcMotor 2 following the structure
     *                of the {@link HardwareData} Object.
     * @param m3Input DcMotor 3 following the structure
     *                of the {@link HardwareData} Object.
     * @param colorSensorInput The ColorSensor Device to use {@link #DetectLine()}.
     * @param telemetryInput The Telemetry of the phone to output data.
     */
    public PIDControllerOld(I2cDeviceSynch imuInput, DcMotor m0Input, DcMotor m1Input, DcMotor m2Input, DcMotor m3Input, ColorSensor colorSensorInput, Telemetry telemetryInput){
        //Set up the private variables
        telemetry = telemetryInput;
        hardwareData = new HardwareData(imuInput, m0Input, m1Input, m2Input, m3Input, colorSensorInput);
        time = new Time();
        directionObject = new DirectionObject(0, 0, 0);

        //Set the target angle to the current angle
        pidData.targetAngle = hardwareData.imu.getAngularOrientation().firstAngle*-1;
    }

    /**
     * Set the PID constants in the {@link PIDData} to the given input values.
     * Only call once, at the beginning of Autonomous.
     * @param pInput The constant multiplier for the P value.
     * @param iInput The constant multiplier for the I value.
     * @param dInput The constant multiplier for the D value.
     */
    public void SetPIDValues(float pInput, float iInput, float dInput){
        pidData.SetValues(pInput, iInput, dInput);
    }

    /**
     * Set the constant movement multipliers and tolerance values in the {@link HardwareData}.
     * Only call once, at the beginning of Autonomous.
     * @param forwardInput The constant multiplier for directional movement.
     * @param rotateInput The constant multiplier for rotational movement.
     * @param rotationToleranceInput The constant threshold for rotational movement.
     * @param timeoutInput The default timeout if none is specified.
     * @param colorToleranceInput The minimum average value for the ColorSensor to return true.
     */
    public void SetConstants(float forwardInput, float rotateInput, float rotationToleranceInput, int timeoutInput, float colorToleranceInput){
        hardwareData.forwardPower = forwardInput;
        hardwareData.rotationPower = rotateInput;
        hardwareData.rotationTolerance = rotationToleranceInput;
        hardwareData.defaultTimeout = timeoutInput;
        hardwareData.colorTolerance = colorToleranceInput;
    }
    //endregion

    //region LinearMove

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction for the given rotations or
     * until the given timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param rotationsInput The amount of rotations to move in
     *                       the given direction.
     * @param timeoutInput The amount of time move the robot for.
     * @see DirectionObject
     */
    public void LinearMove(float[] directionInput, float rotationsInput, int timeoutInput){
        //Set up the start time and position to the current time and position
        float startTime = time.CurrentTime();
        float startPosition = hardwareData.motors[0].getCurrentPosition();

        //runs a loop until the motor rotations hits the rotationsInput
        while(Math.abs(startPosition - hardwareData.motors[0].getCurrentPosition()) < Math.abs(rotationsInput) * hardwareData.encoderCount){
            //If we hit the timeout stop the loop
            if(startTime + timeoutInput < time.CurrentTime()){
                break;
            }

            //Calculate the pid and the direction to turn to
            float direction = CalculatePID();

            //set the motor power to the direction input and the direction to turn to
            directionObject.SetMotors(directionInput[0] * hardwareData.forwardPower, directionInput[1] * hardwareData.forwardPower, direction * hardwareData.rotationPower, hardwareData.motors);
        }

        //reset all pid values and motor values to zero
        directionObject.SetMotors(0, 0, 0, hardwareData.motors);
        pidData.p = 0;
        pidData.i = 0;
        pidData.d = 0;
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle for the given rotations or
     * until the given timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param rotationsInput The amount of rotations to move in
     *                       the given direction.
     * @param timeoutInput The amount of time move the robot for.
     * @see DirectionObject
     */
    public void LinearMove(float angleInput, float rotationsInput, int timeoutInput){
        //Convert the angle to a direction vector
        float[] direction = {(float) Math.cos(angleInput),(float) Math.sin(angleInput)};
        LinearMove(direction, rotationsInput, timeoutInput);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction for the given rotations or
     * until the default timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param rotationsInput The amount of rotations to move in
     *                       the given direction.
     * @see DirectionObject
     */
    public void LinearMove(float[] directionInput, float rotationsInput){
        LinearMove(directionInput, rotationsInput, hardwareData.defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle for the given rotations or
     * until the default timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param rotationsInput The amount of rotations to move in
     *                       the given direction.
     * @see DirectionObject
     */
    public void LinearMove(float angleInput, float rotationsInput){
        //Convert the angle to a direction vector
        float[] direction = {(float) Math.cos(angleInput),(float) Math.sin(angleInput)};
        LinearMove(direction, rotationsInput);
    }
    //endregion

    //region MoveToLine

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at a given speed until the ColorSensor
     * or the given timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param speedInput The speed at which the robot moves.
     * @param timeoutInput The amount of time move the robot for.
     * @see DirectionObject
     */
    public void MoveToLine(float[] directionInput, float speedInput, int timeoutInput){
        //Set up the start time and position to the current time and position
        float startTime = time.CurrentTime();
        float startPosition = hardwareData.motors[0].getCurrentPosition();

        //while there is no line below the robot
        while(!DetectLine()){
            //If we hit the timeout stop the loop
            if(startTime + timeoutInput < time.CurrentTime()){
                break;
            }

            //Calculate the pid and the direction to turn to
            float direction = CalculatePID();

            //set the motor power to the direction input and the direction to turn to
            directionObject.SetMotors(directionInput[0] * hardwareData.forwardPower, directionInput[1] * hardwareData.forwardPower, direction * hardwareData.rotationPower, hardwareData.motors);
        }

        //reset all pid values and motor values to zero
        directionObject.SetMotors(0, 0, 0, hardwareData.motors);
        pidData.p = 0;
        pidData.i = 0;
        pidData.d = 0;
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a given speed until the ColorSensor
     * or the given timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param speedInput The speed at which the robot moves.
     * @param timeoutInput The amount of time move the robot for.
     * @see DirectionObject
     */
    public void MoveToLine(float angleInput, float speedInput, int timeoutInput){
        //Convert the angle to a direction vector
        float[] movement = {(float) Math.cos(angleInput),(float) Math.sin(angleInput)};
        MoveToLine(movement, speedInput, timeoutInput);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at a given speed until the ColorSensor
     * or the default timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param speedInput The speed at which the robot moves.
     * @see DirectionObject
     */
    public void MoveToLine(float[] directionInput, float speedInput){
        MoveToLine(directionInput, speedInput, hardwareData.defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a given speed until the ColorSensor
     * or the default timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param speedInput The speed at which the robot moves.
     * @see DirectionObject
     */
    public void MoveToLine(float angleInput, float speedInput){
        MoveToLine(angleInput, speedInput, hardwareData.defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at the default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param timeoutInput The amount of time move the robot for.
     * @see DirectionObject
     */
    public void MoveToLine(float[] directionInput, int timeoutInput){
        MoveToLine(directionInput, hardwareData.forwardPower, timeoutInput);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param timeoutInput The amount of time move the robot for.
     * @see DirectionObject
     */
    public void MoveToLine(float angleInput, int timeoutInput){
        MoveToLine(angleInput, hardwareData.forwardPower, timeoutInput);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at the default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @see DirectionObject
     */
    public void MoveToLine(float[] directionInput){
        MoveToLine(directionInput, hardwareData.forwardPower, hardwareData.defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a default speed until the ColorSensor
     * or the default timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @see DirectionObject
     */
    public void MoveToLine(float angleInput){
        MoveToLine(angleInput, hardwareData.forwardPower, hardwareData.defaultTimeout);
    }
    //endregion

    //region AngularTurn

    /**
     * Turn the robot to a given angle, using PID to stop on the correct value.
     * Rotates to a given angle or until a given timeout is triggered.
     * @param angleInput An angle to turn the robot to.
     * @param timeoutInput The amount of time turn the robot for.
     * @see DirectionObject
     */
    public void AngularTurn(float angleInput, int timeoutInput){
        //Set up the start time to the current time
        float startTime = time.CurrentTime();
        //Set the targetAngle to the angle input
        pidData.targetAngle = angleInput;

        //Run until the timeout happens
        while(startTime + timeoutInput > time.CurrentTime()){

            //Calculate the pid and the direction to turn to
            float direction = CalculatePID();

            //If we are within our rotational tolerance stop
            if(Math.abs(direction) <= hardwareData.rotationTolerance) {
                break;
            }

            //set the motor power to the direction to turn to
            directionObject.SetMotors(0, 0, direction * hardwareData.rotationPower, hardwareData.motors);
        }

        //reset all pid values and motor values to zero
        directionObject.SetMotors(0, 0, 0, hardwareData.motors);
        pidData.p = 0;
        pidData.i = 0;
        pidData.d = 0;
    }

    /**
     * Turn the robot to a given angle, using PID to stop on the correct value.
     * Rotates to a given angle or until a default timeout is triggered.
     * @param angleInput An angle to turn the robot to.
     * @see DirectionObject
     */
    public void AngularTurn(float angleInput){
        AngularTurn(angleInput, hardwareData.defaultTimeout);
    }
    //endregion

    //region Calculations

    /**
     * Calculates what the PID values will be, based off of the
     * current error and past error in the {@link PIDData}.
     */
    private float CalculatePID(){
        //Set the current angle to the imu's angle
        pidData.currentAngle = hardwareData.imu.getAngularOrientation().firstAngle*-1;
        //calculate how far off we are
        pidData.CalculateError();
        //calculate the difference in time from the last time this was run
        time.CurrentDelta();

        //Set the p to the error
        pidData.p = pidData.error;
        //Add the change in error times dT to i
        pidData.i += pidData.error * time.deltaTime / 1000;
        //Set d to teh change in error over dT
        pidData.d = (pidData.error - pidData.lastError)/(time.deltaTime/1000);

        return ((pidData.i * pidData.iTuning) / 2000) + ((pidData.p * pidData.iTuning) / 2000) + ((pidData.d * pidData.dTuning) / 2000);
    }

    /**
     * Determines if the robot is over a line, if the ColorSensor's
     * average is higher than the color tolerance.
     * @return <code>true</code> if the ColorSensor detects a line; <code>false</code> otherwise.
     */
    private boolean DetectLine(){
        //Calculate the average of blue, green, and red
        float average = (hardwareData.colorSensor.blue() + hardwareData.colorSensor.green() + hardwareData.colorSensor.red())/3;
        //if average is greater than tolerance return true otherwise return false
        if(average>hardwareData.colorTolerance)
            return true;
        return false;
    }
    //endregion
}

/**
 * PIDData is the base class in which all PID
 * related data and values are stored.
 * A PIDData object is used in a {@link PIDControllerOld} to calculate
 * how to move the motors. A PIDData object contains the
 * information required to run PID related movement
 * functions.   This information includes:
 * <ul>
 *     <li>The current and target angle</li>
 *     <li>The current and last error</li>
 *     <li>The current P, I, and D values</li>
 *     <li>The P, I, and D constants</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kelsey
 * @author Adam Perlin
 * @author Noah Rose Ledesma
 * @version 2.0, 12/18/2016
 * @deprecated Replaced with {@link PIDData} on 12/19/2016.
 */
class PIDDataOld{
    public float currentAngle;
    public float targetAngle;
    public float error, lastError;
    public float p, i, d;
    public float pTuning, iTuning, dTuning;

    /**
     * Constructor for the PIDData class setting the
     * P, I, D constant values.
     * @param pTuningInput The constant multiplier for the P value.
     * @param iTuningInput The constant multiplier for the I value.
     * @param dTuningInput The constant multiplier for the D value.
     */
    PIDDataOld(float pTuningInput, float iTuningInput, float dTuningInput){
        //Set the tuning when initializing
        pTuning = pTuningInput;
        iTuning = iTuningInput;
        dTuning = dTuningInput;
    }

    /**
     * Set the PID constants to the given input values.
     * Only call once, at the beginning of Autonomous.
     * @param pTuningInput The constant multiplier for the P value.
     * @param iTuningInput The constant multiplier for the I value.
     * @param dTuningInput The constant multiplier for the D value.
     */
    public void SetValues(float pTuningInput, float iTuningInput, float dTuningInput){
        pTuning = pTuningInput;
        iTuning = iTuningInput;
        dTuning = dTuningInput;
    }

    /**
     * Calculates the closes rotational error, based
     * off of the current and the target angle.
     * Finds the shortest distance to turn to
     * achieve the target angle.
     */
    public void CalculateError(){
        //Set the last error to the current error
        lastError = error;
        //Calculate the closes way to turn to get to the target angle form the current angle
        if(currentAngle + 360 - targetAngle < 180){
            error = (currentAngle - targetAngle + 360) * -1;
        }else if(targetAngle + 360 - currentAngle < 180){
            error = (targetAngle - currentAngle + 360);
        }else if(currentAngle - targetAngle < 180){
            error = (currentAngle - targetAngle) * -1;
        }else if(targetAngle - currentAngle < 180){
            error = (targetAngle - currentAngle);
        }
    }
}

/**
 * Time is the class in which all time
 * related values are stored.
 * A Time object is used in a {@link PIDControllerOld} to calculate
 * how long to run a function for. A Time object contains the
 * information required to calculate PID.   This information includes:
 * <ul>
 *     <li>The total elapsed time of the program</li>
 *     <li>The current and the past time, in milliseconds</li>
 *     <li>The change in time, in milliseconds</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kelsey
 * @author Adam Perlin
 * @author Noah Rose Ledesma
 * @version 2.0, 12/18/2016
 * @deprecated Replaced with {@link PIDData} on 12/19/2016.
 */
class Time {
    private ElapsedTime programTime;
    private float lastTime;
    private float curTime;

    public float deltaTime;

    /**
     * Constructor for the Time class, and setting up the elapsed time.
     */
    public Time(){
        programTime = new ElapsedTime();
    }

    /**
     * Return the current program length.
     * @return The current elapsed time in seconds.
     */
    public float CurrentTime(){
        return (float) programTime.seconds();
    }

    /**
     * Calculates the difference in time from the
     * current time and the input time.
     * @param previousTime A given time to calculate elapsed time sense then
     *                     in seconds.
     * @return The elapsed time from the given time in seconds.
     * @deprecated As of version 2.0, replaced by
     *             {@link #CurrentDelta()}
     */
    public float TimeFrom(float previousTime){
        return (float) (programTime.seconds() - previousTime);
    }

    /**
     * Calculates and stores the current change in time
     * from the last time this function was called.
     */
    public void CurrentDelta(){
        curTime = System.currentTimeMillis();
        deltaTime = curTime - lastTime;
        lastTime = curTime;
    }
}

/**
 * HardwareData is the class in which all hardware
 * related values and devices are stored.
 * A HardwareData object is used in a {@link PIDControllerOld} to control
 * all hardware devices. A Hardware object contains the
 * information required to control a PID robot.   This information includes:
 * <ul>
 *     <li>The imu and the imu's parameters</li>
 *     <li>The array of DcMotors following a specific structure</li>
 *     <li>The ColorSensor</li>
 *     <li>Several constants for tolerances and multipliers</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kelsey
 * @author Adam Perlin
 * @author Noah Rose Ledesma
 * @version 2.0, 12/18/2016
 * @deprecated Replaced with {@link HardwareController} on 12/19/2016.
 */
class HardwareData{
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

    public ColorSensor colorSensor;

    public int encoderCount;

    public float forwardPower;
    public float rotationPower;
    public float rotationTolerance;
    public int defaultTimeout;
    public float colorTolerance;

    /**
     * Constructor for the HardwareData class setting up
     * the imu, DcMotor array, and ColorSensor.
     * @param imuInput The imu I2c Device to get current angle.
     * @param motorsInput The array of DcMotors.
     * @param colorSensorInput The ColorSensor Device to get the Line Data.
     */
    HardwareData(I2cDeviceSynch imuInput, DcMotor[] motorsInput, ColorSensor colorSensorInput){
        //Set up the imu Parameters to use correct units
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        //Initialize the imu based on the Parameters
        imu = new AdafruitBNO055IMU(imuInput);
        imu.initialize(imuParameters);

        //Initialize the motors
        motors = motorsInput;

        //Reverse specific motors based on gears and chains, comment out which ones should not be flipped
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);

        //Set up encoder count
        encoderCount = 1400;

        //Set up colorSensor
        colorSensor = colorSensorInput;
    }

    /**
     * Constructor for the HardwareData class setting up
     * the imu, four DcMotors, and ColorSensor.
     * @param imuInput The imu I2c Device to get current angle.
     * @param m0Input DcMotor 0.
     * @param m1Input DcMotor 1.
     * @param m2Input DcMotor 2.
     * @param m3Input DcMotor 3.
     * @param colorSensorInput The ColorSensor Device to get the Line Data.
     */
    HardwareData(I2cDeviceSynch imuInput, DcMotor m0Input, DcMotor m1Input, DcMotor m2Input, DcMotor m3Input, ColorSensor colorSensorInput){
        //Set up the imu Parameters to use correct units
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        //Initialize the imu based on the Parameters
        imu = new AdafruitBNO055IMU(imuInput);
        imu.initialize(imuParameters);

        //Initialize the motors
        motors[0] = m0Input;
        motors[1] = m1Input;
        motors[2] = m2Input;
        motors[3] = m3Input;

        //Reverse specific motors based on gears and chains, comment out which ones should not be flipped
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        //motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);

        //Set up encoder count
        encoderCount = 1400;

        //Set up colorSensor
        colorSensor = colorSensorInput;
    }
}