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
    private Telemetry _telemetry;
    private HardwareController _hardwareController;
    private PIDData _pidData;

    private float _startTime;
    private float _startPosition;
    //endregion
    //region Public Data
    public int defaultTimeout;
    public float forwardConstant;
    public float rotationConstant;
    public float rotationTolerance;
    //endregion

    //region Initialization
    /**
     * Constructor for the PIDController class using
     * an array of DcMotors.
     * @param imu The imu I2c Device to get current angle.
     * @param motors The array of DcMotors following the structure of
     *                    the {@link HardwareController} Object.
     * @param colorSensor1 The ColorSensor Device to use {@link HardwareController#DetectLine()}.
     * @param colorSensor2 The ColorSensor Device to use {@link HardwareController#DetectLine()}.
     * @param telemetryInput The Telemetry of the phone to output data.
     */
    PIDController(I2cDeviceSynch imu, DcMotor[] motors, ColorSensor colorSensor1, ColorSensor colorSensor2, Telemetry telemetryInput){
        //Set up the private variables
        _telemetry = telemetryInput;
        _hardwareController = new HardwareController(imu, motors, colorSensor1, colorSensor2);
        _pidData = new PIDData();

        //Set the target angle to the current angle
        _pidData.targetAngle = _hardwareController.imu.getAngularOrientation().firstAngle*-1;
    }

    /**
     * Contructor for the PIDController class using
     * four seperate DcMotors
     * @param imu The imu I2c Device to get current angle.
     * @param m0 DcMotor 0 following the structure
     *                of the {@link HardwareController} Object.
     * @param m1 DcMotor 1 following the structure
     *                of the {@link HardwareController} Object.
     * @param m2 DcMotor 2 following the structure
     *                of the {@link HardwareController} Object.
     * @param m3 DcMotor 3 following the structure
     *                of the {@link HardwareController} Object.
     * @param colorSensor1 The ColorSensor Device to use {@link HardwareController#DetectLine()}.
     * @param colorSensor2 The ColorSensor Device to use {@link HardwareController#DetectLine()}.
     * @param telemetryInput The Telemetry of the phone to output data.
     */
    PIDController(I2cDeviceSynch imu, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3, ColorSensor colorSensor1, ColorSensor colorSensor2, Telemetry telemetryInput){
        //Set up the private variables
        _telemetry = telemetryInput;
        _hardwareController = new HardwareController(imu, ((DcMotor[]) Utility.MakeArray(m0, m1, m2, m3)), colorSensor1, colorSensor2);
        _pidData = new PIDData();

        //Set the target angle to the current angle
        _pidData.targetAngle = _hardwareController.imu.getAngularOrientation().firstAngle*-1;
    }

    /**
     * Set the PID constants in the {@link PIDData} to the given input values.
     * Along with a tolerance for the color sensor.
     * Only call once, at the beginning of Autonomous.
     * @param pInput The constant multiplier for the P value.
     * @param iInput The constant multiplier for the I value.
     * @param dInput The constant multiplier for the D value.
     * @param colorTolerance The minimum average value for the ColorSensor to return true.
     */
    public void SetPIDConstatns(float pInput, float iInput, float dInput, float colorTolerance){
        _pidData.SetValues(pInput, iInput, dInput);
        _pidData.rotationTolerance = rotationTolerance;
        _hardwareController.colorTolerance = colorTolerance;
    }

    public void SetDefaultMultipliers(){
        defaultTimeout = 5;
        forwardConstant = 0.6f;
        rotationConstant = 1.0f;
        rotationTolerance = 0.03f;
    }

    //endregion

    //region Functions

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
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param rotationsInput The amount of rotations to move in
     *                       the given direction.
     * @param timeoutInput The amount of time move the robot for.
     * @see HardwareController
     */
    public void LinearMove(float[] directionInput, float rotationsInput, int timeoutInput){
        //Set up the start time and position to the current time and position
        _startTime = _pidData.CurrentTime();
        _startPosition = _hardwareController.motors[0].getCurrentPosition();

        //runs a loop until the motor rotations hits the rotationsInput
        while(Math.abs(_startPosition - _hardwareController.motors[0].getCurrentPosition()) < Math.abs(rotationsInput) * _hardwareController.encoderCount){
            //If we hit the timeout stop the loop
            if(_startTime + timeoutInput < _pidData.CurrentTime()){
                break;
            }

            //Calculate the pid and the direction to turn to
            //set the motor power to the direction input and the direction to turn to
            _hardwareController.RunMotors(directionInput[0] * forwardConstant, directionInput[1] * forwardConstant, _pidData.CalculatePID(_hardwareController) * rotationConstant);
        }
        _ClearPID();
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle for the given rotations or
     * until the given timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param rotationsInput The amount of rotations to move in
     *                       the given direction.
     * @param timeoutInput The amount of time move the robot for.
     * @see HardwareController
     */
    public void LinearMove(float angleInput, float rotationsInput, int timeoutInput){
        //Convert the angle to a direction vector
        LinearMove(new float[]{(float) Math.cos(angleInput),(float) Math.sin(angleInput)}, rotationsInput, timeoutInput);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction for the given rotations or
     * until the default timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param rotationsInput The amount of rotations to move in
     *                       the given direction.
     * @see HardwareController
     */
    public void LinearMove(float[] directionInput, float rotationsInput){
        LinearMove(directionInput, rotationsInput, defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle for the given rotations or
     * until the default timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param rotationsInput The amount of rotations to move in
     *                       the given direction.
     * @see HardwareController
     */
    public void LinearMove(float angleInput, float rotationsInput){
        //Convert the angle to a direction vector
        LinearMove(new float[]{(float) Math.cos(angleInput),(float) Math.sin(angleInput)}, rotationsInput, defaultTimeout);
    }
    //endregion

    //region Move To Line
    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at a given speed until the ColorSensor
     * or the given timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param speedInput The speed at which the robot moves.
     * @param timeoutInput The amount of time move the robot for.
     * @see HardwareController
     */
    public void MoveToLine(float speedInput, float[] directionInput, int timeoutInput){
        //Set up the start time and position to the current time and position
        _startTime = _pidData.CurrentTime();
        _startPosition = _hardwareController.motors[0].getCurrentPosition();

        //while there is no line below the robot
        while(!_hardwareController.DetectLine()){
            //If we hit the timeout stop the loop
            if(_startTime + timeoutInput < _pidData.CurrentTime()){
                break;
            }

            //Calculate the pid and the direction to turn to
            //set the motor power to the direction input and the direction to turn to
            _hardwareController.RunMotors(directionInput[0] * forwardConstant * speedInput, directionInput[1] * forwardConstant * speedInput, _pidData.CalculatePID(_hardwareController) * rotationConstant);
        }
        _ClearPID();
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a given speed until the ColorSensor
     * or the given timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param speedInput The speed at which the robot moves.
     * @param timeoutInput The amount of time move the robot for.
     * @see HardwareController
     */
    public void MoveToLine(float speedInput, float angleInput, int timeoutInput){
        //Convert the angle to a direction vector
        MoveToLine(speedInput, new float[]{(float) Math.cos(angleInput),(float) Math.sin(angleInput)}, timeoutInput);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at a given speed until the ColorSensor
     * or the default timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param speedInput The speed at which the robot moves.
     * @see HardwareController
     */
    public void MoveToLine(float speedInput, float[] directionInput){
        MoveToLine(speedInput, directionInput, defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a given speed until the ColorSensor
     * or the default timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param speedInput The speed at which the robot moves.
     * @see HardwareController
     */
    public void MoveToLine(float speedInput, float angleInput){
        MoveToLine(speedInput, angleInput, defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at the default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @param timeoutInput The amount of time move the robot for.
     * @see HardwareController
     */
    public void MoveToLine(float[] directionInput, int timeoutInput){
        MoveToLine(forwardConstant, directionInput, timeoutInput);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @param timeoutInput The amount of time move the robot for.
     * @see HardwareController
     */
    public void MoveToLine(float angleInput, int timeoutInput){
        MoveToLine(forwardConstant, angleInput, timeoutInput);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves in a given direction at the default speed until the ColorSensor
     * or the given timeout is triggered.
     * @param directionInput A length two float array with x motion
     *                       and y motion.
     * @see HardwareController
     */
    public void MoveToLine(float[] directionInput){
        MoveToLine(forwardConstant, directionInput, defaultTimeout);
    }

    /**
     * Move the robot around in a line, uses PID to keep the angle the same.
     * Moves at a given angle at a default speed until the ColorSensor
     * or the default timeout is triggered.
     * @param angleInput An angle to move the robot to.
     * @see HardwareController
     */
    public void MoveToLine(float angleInput){
        MoveToLine(forwardConstant, angleInput, defaultTimeout);
    }
    //endregion

    //region Angular Turn
    /**
     * Turn the robot to a given angle, using PID to stop on the correct value.
     * Rotates to a given angle or until a given timeout is triggered.
     * @param angleInput An angle to turn the robot to.
     * @param timeoutInput The amount of time turn the robot for.
     * @see HardwareController
     */
    public void AngularTurn(float angleInput, int timeoutInput){
        //Set up the start time to the current time
        float startTime = _pidData.CurrentTime();
        //Set the targetAngle to the angle input
        _pidData.targetAngle = angleInput;

        //Run until the timeout happens
        while(startTime + timeoutInput > _pidData.CurrentTime()){

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
     * @param angleInput An angle to turn the robot to.
     * @see HardwareController
     */
    public void AngularTurn(float angleInput){
        AngularTurn(angleInput, defaultTimeout);
    }
    //endregion

    //endregion
}
