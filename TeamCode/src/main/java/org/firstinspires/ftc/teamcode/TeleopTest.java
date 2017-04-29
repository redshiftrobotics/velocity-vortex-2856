package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * A test teleop program to control the robot with the DriveController.
 * @author Duncan McKee
 */

@TeleOp(name="Teleop Test")
public class TeleopTest extends OpMode {

    private DriveController driveController;
    BNO055IMU imu;

    private boolean aDebounce = false;
    private boolean globalToggle = false;
    private float savedAngle = 0;
    private boolean initialized = false;

    @Override
    public void init() {
        telemetry.addData("IMU Initializing", 0);
        telemetry.update();
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

        driveController = new DriveController(DriveController.DriveType.Holonomic, hardwareMap);
        initialized = true;
        telemetry.addData("Everything is ready to go", 2);
        telemetry.update();
    }

    @Override
    public void loop() {
        if(!initialized){
            requestOpModeStop();
        }
        Move(gamepad1);
        telemetry.addData("IMU", imu.getAngularOrientation().firstAngle * -1);
        telemetry.update();
    }

    void Move(Gamepad pad){
        if(pad.a&&!aDebounce){
            globalToggle = !globalToggle;
            aDebounce=true;
        }else if(!pad.a){
            aDebounce=false;
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
