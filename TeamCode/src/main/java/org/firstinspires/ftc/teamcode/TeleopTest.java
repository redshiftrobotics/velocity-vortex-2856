package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * A test Teleop program to control the robot with the DriveController.
 * @author Duncan McKee
 */

@TeleOp(name="Teleop Test")
public class TeleopTest extends OpMode {

    private DriveController driveController; //Drive controller to control the robot's movements.
    BNO055IMU imu; //A BNO055IMU used to determine the angle of the robot to use global movements.

    private boolean globalDeBounce = false; //A boolean used to update the robot from global to local movements.
    private boolean globalToggle = false; //A boolean used to store which mode the robot's movements are in.
    private float savedAngle = 0; //The current saved angle for local movements.
    private boolean initialized = false; //A boolean to stop the OpMode if not all of the devices were initialized.

    @Override
    public void init() {
        telemetry.addData("IMU Initializing", 0);
        telemetry.update();

        //Initialize the BNO055IMU to the specifications we use.
        I2cDeviceSynch imuInit;
        BNO055IMU.Parameters imuParameters;
        imuInit = hardwareMap.i2cDeviceSynch.get("imu");
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(imuInit);
        imu.initialize(imuParameters);

        telemetry.addData("IMU Initialized", 1);
        telemetry.update();

        driveController = new DriveController(DriveController.DriveType.Holonomic, hardwareMap); //Initialize the drive train in Holonomic mode.

        initialized = true; //Set the status of the initialization process to true.

        telemetry.addData("Everything is Ready to Go", 2);
        telemetry.update();
    }

    @Override
    public void loop() {
        if(!initialized){ //If the init function was not fully run stop the OpMode.
            requestOpModeStop();
        }
        Move(gamepad1);
        telemetry.addData("IMU", imu.getAngularOrientation().firstAngle * -1);
        telemetry.update();
    }

    void Move(Gamepad pad){
        if(pad.a&&!globalDeBounce){
            globalToggle = !globalToggle;
            globalDeBounce=true;
        }else if(!pad.a){
            globalDeBounce=false;
        }
        if(pad.b){
            savedAngle = imu.getAngularOrientation().firstAngle * -1;
        }
        if(globalToggle) {
            driveController.Drive(pad.right_stick_x, pad.right_stick_y, pad.left_stick_x, imu.getAngularOrientation().firstAngle * -1);
        }else{
            driveController.Drive(pad.right_stick_x, pad.right_stick_y, pad.left_stick_x, savedAngle);
        }
    }
}
