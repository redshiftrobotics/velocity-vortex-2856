package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * PIDController is the class for all PID
 * related movements and calculations.
 * A PIDController object encapsulates the information
 * required to run 2856's autonomous.   This information includes:
 * <ul>
 *     <li>A {@link HardwareController} Object</li>
 *     <li>A {@link PIDData} Object</li>
 *     <li>The OpModes Telemetry</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kelsey
 * @author Adam Perlin
 * @author Noah Rose Ledesma
 * @version 3.0, 12/19/2016
 */
public class PIDController {
    //region Private Data
    /**
     * A {@link HardwareController} to store all motors and motor speeds in.
     */
    private HardwareController _hardwareController;
    /**
     * A {@link PIDData} to store all PID values and error calculations.
     */
    private PIDData _pidData;
    /**
     * The telemetry to ouput debug to.
     */
    private Telemetry _telemetry;

    /**
     * The start time for any movement methods.
     * @see #LinearMove
     * @see #MoveToLine
     * @see #AngularTurn
     */
    private float _startTime;
    /**
     * The start position for any movement methods.
     * @see #LinearMove
     * @see #MoveToLine
     * @see #AngularTurn
     */
    private float _startPosition;
    //endregion
    //region Public Data
    /**
     * The default timeout for any movement methods which are not passed one.
     * @see #LinearMove
     * @see #MoveToLine
     * @see #AngularTurn
     */
    public int defaultTimeout;
    /**
     * The constant applied to linear motion for the motors.
     * @see #LinearMove
     * @see #MoveToLine
     * @see #AngularTurn
     */
    public float forwardConstant;
    /**
     * The constant applied to rotational motion for the motors.
     * @see #LinearMove
     * @see #MoveToLine
     */
    public float rotationConstant;
    /**
     * The tolerance for the {@link #AngularTurn} method.
     * @see #AngularTurn
     */
    public float rotationTolerance;
    //endregion

    //region Initialization
    /**
     * Constructor for the PIDController class using
     * an array of DcMotors.
     * @param $imu The imu I2c Device to get current angle.
     * @param $motors The array of DcMotors following the structure of
     *                    the {@link HardwareController} Object.
     * @param $colorSensor1 The ColorSensor Device to use {@link HardwareController#DetectLine()}.
     * @param $colorSensor2 The ColorSensor Device to use {@link HardwareController#DetectLine()}.
     * @param $telemetry The Telemetry of the phone to output data.
     */
    PIDController(I2cDeviceSynch $imu, DcMotor[] $motors, ColorSensor $colorSensor1, ColorSensor $colorSensor2, Telemetry $telemetry){
        //Set up the private variables
        _telemetry = $telemetry;
        _hardwareController = new HardwareController($imu, $motors, $colorSensor1, $colorSensor2);
        _pidData = new PIDData();

        //Set the target angle to the current angle
        _pidData.targetAngle = _hardwareController.imu.getAngularOrientation().firstAngle*-1;
    }

    /**
     * Contructor for the PIDController class using
     * four seperate DcMotors
     * @param $imu The imu I2c Device to get current angle.
     * @param $m0 DcMotor 0 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m1 DcMotor 1 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m2 DcMotor 2 following the structure
     *                of the {@link HardwareController} Object.
     * @param $m3 DcMotor 3 following the structure
     *                of the {@link HardwareController} Object.
     * @param $colorSensor1 The ColorSensor Device to use {@link HardwareController#DetectLine()}.
     * @param $colorSensor2 The ColorSensor Device to use {@link HardwareController#DetectLine()}.
     * @param $telemetry The Telemetry of the phone to output data.
     * @see #PIDController(I2cDeviceSynch, DcMotor[], ColorSensor, ColorSensor, Telemetry)
     */
    PIDController(I2cDeviceSynch $imu, DcMotor $m0, DcMotor $m1, DcMotor $m2, DcMotor $m3, ColorSensor $colorSensor1, ColorSensor $colorSensor2, Telemetry $telemetry){
        //Set up the private variables
        _telemetry = $telemetry;
        _hardwareController = new HardwareController($imu, ((DcMotor[]) Utility.MakeArray($m0, $m1, $m2, $m3)), $colorSensor1, $colorSensor2);
        _pidData = new PIDData();

        //Set the target angle to the current angle
        _pidData.targetAngle = _hardwareController.imu.getAngularOrientation().firstAngle*-1;
    }

    /**
     * Set the PID constants in the {@link PIDData} to the given input values.
     * Along with a tolerance for the color sensor.
     * Only call once, at the beginning of Autonomous.
     * @param $p The constant multiplier for the P value.
     * @param $i The constant multiplier for the I value.
     * @param $d The constant multiplier for the D value.
     * @param $colorTolerance The minimum average value for the ColorSensor to return true.
     */
    public void SetPIDConstatns(float $p, float $i, float $d, float $colorTolerance){
        _pidData.SetValues($p, $i, $d);
        _pidData.rotationTolerance = rotationTolerance;
        _hardwareController.colorTolerance = $colorTolerance;
    }

    /**
     * Set the PID constants in the {@link PIDData} to the given input values.
     * Only call once, at the beginning of Autonomous.
     * @param $p The constant multiplier for the P value.
     * @param $i The constant multiplier for the I value.
     * @param $d The constant multiplier for the D value.
     * @see #SetPIDConstatns(float, float, float, float)
     */
    public void SetPIDConstatns(float $p, float $i, float $d){
        _pidData.SetValues($p, $i, $d);
        _pidData.rotationTolerance = rotationTolerance;
    }

    /**
     * Sets the default multipliers and tolerances
     */
    public void SetDefaultMultipliers(){
        defaultTimeout = 5;
        forwardConstant = 0.6f;
        rotationConstant = 1.0f;
        rotationTolerance = 0.03f;
        _pidData.rotationTolerance = rotationTolerance;
    }

    //endregion

    //region Functions

    /**
     * Clears the PID data to allow for a new movement function to run.
     * @see #LinearMove
     * @see #MoveToLine
     * @see #AngularTurn
     */
    private void _ClearPID(){
        //reset all pid values and motor values to zero
        _hardwareController.StopMotors();
        _pidData.p = 0;
        _pidData.i = 0;
        _pidData.d = 0;
    }

    //region Linear Move
    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction for the given rotations or
     * until the given timeout is triggered.
     * @param $direction A length two float array with x motion
     *                       and y motion.
     * @param $rotations The amount of rotations to move in
     *                       the given direction.
     * @param $timeout The amount of time move the robot for.
     * @see HardwareController
     */
    public void LinearMove(float[] $direction, float $rotations, int $timeout){
        //Set up the start time and position to the current time and position
        _startTime = _pidData.CurrentTime();
        _startPosition = _hardwareController.motors[0].getCurrentPosition();

        //runs a loop until the motor rotations hits the $rotations
        while(Math.abs(_startPosition - _hardwareController.motors[0].getCurrentPosition()) < Math.abs($rotations) * _hardwareController.encoderCount){
            //If we hit the timeout stop the loop
            if(_startTime + $timeout < _pidData.CurrentTime()){
                break;
            }

            //Calculate the pid and the direction to turn to
            //set the motor power to the direction input and the direction to turn to
            _hardwareController.RunMotors($direction[0] * forwardConstant, $direction[1] * forwardConstant, _pidData.CalculatePID(_hardwareController) * rotationConstant);
        }
        _ClearPID();
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle for the given rotations or
     * until the given timeout is triggered.
     * @param $angle An angle to move the robot to.
     * @param $rotations The amount of rotations to move in
     *                       the given direction.
     * @param $timeout The amount of time move the robot for.
     * @see HardwareController
     * @see #LinearMove
     */
    public void LinearMove(float $angle, float $rotations, int $timeout){
        //Convert the angle to a direction vector
        LinearMove(new float[]{(float) Math.cos($angle),(float) Math.sin($angle)}, $rotations, $timeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction for the given rotations or
     * until the default timeout is triggered.
     * @param $direction A length two float array with x motion
     *                       and y motion.
     * @param $rotations The amount of rotations to move in
     *                       the given direction.
     * @see HardwareController
     * @see #LinearMove
     */
    public void LinearMove(float[] $direction, float $rotations){
        LinearMove($direction, $rotations, defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle for the given rotations or
     * until the default timeout is triggered.
     * @param $angle An angle to move the robot to.
     * @param $rotations The amount of rotations to move in
     *                       the given direction.
     * @see HardwareController
     * @see #LinearMove
     */
    public void LinearMove(float $angle, float $rotations){
        //Convert the angle to a direction vector
        LinearMove(new float[]{(float) Math.cos($angle),(float) Math.sin($angle)}, $rotations, defaultTimeout);
    }
    //endregion

    //region Move To Line
    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at a given speed until the ColorSensor
     * or the given timeout is triggered.
     * @param $direction A length two float array with x motion
     *                       and y motion.
     * @param $speed The speed at which the robot moves.
     * @param $timeout The amount of time move the robot for.
     * @see HardwareController
     */
    public void MoveToLine(float $speed, float[] $direction, int $timeout){
        //Set up the start time and position to the current time and position
        _startTime = _pidData.CurrentTime();
        _startPosition = _hardwareController.motors[0].getCurrentPosition();

        //while there is no line below the robot
        while(!_hardwareController.DetectLine()){
            //If we hit the timeout stop the loop
            if(_startTime + $timeout < _pidData.CurrentTime()){
                break;
            }

            //Calculate the pid and the direction to turn to
            //set the motor power to the direction input and the direction to turn to
            _hardwareController.RunMotors($direction[0] * forwardConstant * $speed, $direction[1] * forwardConstant * $speed, _pidData.CalculatePID(_hardwareController) * rotationConstant);
        }
        _ClearPID();
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a given speed until the ColorSensor
     * or the given timeout is triggered.
     * @param $angle An angle to move the robot to.
     * @param $speed The speed at which the robot moves.
     * @param $timeout The amount of time move the robot for.
     * @see HardwareController
     * @see #MoveToLine
     */
    public void MoveToLine(float $speed, float $angle, int $timeout){
        //Convert the angle to a direction vector
        MoveToLine($speed, new float[]{(float) Math.cos($angle),(float) Math.sin($angle)}, $timeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at a given speed until the ColorSensor
     * or the default timeout is triggered.
     * @param $direction A length two float array with x motion
     *                       and y motion.
     * @param $speed The speed at which the robot moves.
     * @see HardwareController
     * @see #MoveToLine
     */
    public void MoveToLine(float $speed, float[] $direction){
        MoveToLine($speed, $direction, defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a given speed until the ColorSensor
     * or the default timeout is triggered.
     * @param $angle An angle to move the robot to.
     * @param $speed The speed at which the robot moves.
     * @see HardwareController
     * @see #MoveToLine
     */
    public void MoveToLine(float $speed, float $angle){
        MoveToLine($speed, $angle, defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at the default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param $direction A length two float array with x motion
     *                       and y motion.
     * @param $timeout The amount of time move the robot for.
     * @see HardwareController
     * @see #MoveToLine
     */
    public void MoveToLine(float[] $direction, int $timeout){
        MoveToLine(forwardConstant, $direction, $timeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param $angle An angle to move the robot to.
     * @param $timeout The amount of time move the robot for.
     * @see HardwareController
     * @see #MoveToLine
     */
    public void MoveToLine(float $angle, int $timeout){
        MoveToLine(forwardConstant, $angle, $timeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at the default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param $direction A length two float array with x motion
     *                       and y motion.
     * @see HardwareController
     * @see #MoveToLine
     */
    public void MoveToLine(float[] $direction){
        MoveToLine(forwardConstant, $direction, defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a default speed until the ColorSensor
     * or the default timeout is triggered.
     * @param $angle An angle to move the robot to.
     * @see HardwareController
     * @see #MoveToLine
     */
    public void MoveToLine(float $angle){
        MoveToLine(forwardConstant, $angle, defaultTimeout);
    }
    //endregion

    //region Angular Turn
    /**
     * Turn the robot to a given angle, using PID to stop on the correct value.
     * Rotates to a given angle or until a given timeout is triggered.
     * @param $angle An angle to turn the robot to.
     * @param $timeout The amount of time turn the robot for.
     * @see HardwareController
     */
    public void AngularTurn(float $angle, int $timeout){
        //Set up the start time to the current time
        float startTime = _pidData.CurrentTime();
        //Set the targetAngle to the angle input
        _pidData.targetAngle = $angle;

        //Run until the timeout happens
        while(startTime + $timeout > _pidData.CurrentTime()){

            //Calculate the pid and the direction to turn to
            float direction = _pidData.CalculatePID(_hardwareController);

            //If we are within our rotational tolerance stop
            if(Math.abs(direction) <= rotationConstant) {
                break;
            }

            //set the motor power to the direction to turn to
            _hardwareController.RunMotors(0, 0, direction * rotationConstant);
        }
        _ClearPID();
    }

    /**
     * Turn the robot to a given angle, using PID to stop on the correct value.
     * Rotates to a given angle or until a default timeout is triggered.
     * @param $angle An angle to turn the robot to.
     * @see HardwareController
     * @see #AngularTurn
     */
    public void AngularTurn(float $angle){
        AngularTurn($angle, defaultTimeout);
    }
    //endregion

    //endregion
}
