package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * Class to control the robot for teleop control.
 * @author Duncan
 * @version 1.0
 */
public class TeleopController extends DriveController{
    //region Private Variables
    private AdafruitBNO055IMU imu; //A BNO055IMU used to determine the angle of the robot to use global movements.
    private float savedOrientation; //The last orientation saved by the imu.
    private boolean globalDeBounce = false; //A boolean used to update the robot from global to local movements.
    private boolean globalToggle = false; //A boolean used to store which mode the robot's movements are in.
    //endregion

    /**
     * Constructor for the TeleopController,
     * @param hardwareMap The hardware map that sets up the motors and imu.
     */
    public TeleopController(HardwareMap hardwareMap) {
        super(DriveType.Holonomic, hardwareMap);
        I2cDeviceSynch imuInit; //Set up the IMU to the parameters we use.
        AdafruitBNO055IMU.Parameters imuParameters;
        imuInit = hardwareMap.i2cDeviceSynch.get("imu");
        imuParameters = new AdafruitBNO055IMU.Parameters();
        imuParameters.angleUnit = AdafruitBNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = AdafruitBNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(imuInit);
        imu.initialize(imuParameters);
        savedOrientation = 0; //Set the current orientation to 0.
    }

    //region Public Methods
    /**
     * Runs the drive train under the control of the gamepad.
     * @param gamepad The gamepad to use to control the drive train.
     */
    public void Drive(Gamepad gamepad){
        if(gamepad.a&&!globalDeBounce){ //On button press of a toggle between global and local orientation.
            globalToggle = !globalToggle;
            globalDeBounce=true;
        }else if(!gamepad.a){
            globalDeBounce=false;
        }
        if(gamepad.b){ //Set the orientation to the current orientation.
            savedOrientation = imu.getAngularOrientation().firstAngle;
        }
        if(globalToggle) { //Run the robot in global orientation mode.
            Drive(gamepad.right_stick_x, gamepad.right_stick_y, gamepad.left_stick_x, imu.getAngularOrientation().firstAngle * -1);
        }else{ //Run the robot in local orientation mode.
            Drive(gamepad.right_stick_x, gamepad.right_stick_y, gamepad.left_stick_x, savedOrientation);
        }
    }
    //endregion
}