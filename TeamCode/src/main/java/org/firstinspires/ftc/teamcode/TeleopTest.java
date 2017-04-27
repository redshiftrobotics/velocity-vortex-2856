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

    DcMotor motors[] = new DcMotor[4];
    double[] drivePower;
    DriveController driveController = new DriveController(DriveController.DriveType.Holonomic);
    I2cDeviceSynch imuInit;
    BNO055IMU imu;
    BNO055IMU.Parameters imuParameters;

    boolean aDebounce = false;
    boolean globalToggle = false;
    float savedAngle = 0;

    @Override
    public void init() {
        imuInit = hardwareMap.i2cDeviceSynch.get("imu");
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        imu = new AdafruitBNO055IMU(imuInit);
        imu.initialize(imuParameters);
        motors[0] = hardwareMap.dcMotor.get("m0");
        motors[1] = hardwareMap.dcMotor.get("m1");
        motors[2] = hardwareMap.dcMotor.get("m2");
        motors[3] = hardwareMap.dcMotor.get("m3");
        drivePower = new double[4];
    }

    @Override
    public void loop() {
        Move(gamepad1);
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
            drivePower = driveController.Drive(pad.right_stick_x, pad.right_stick_y, pad.left_stick_x, imu.getAngularOrientation().firstAngle * -1);
        }else{
            drivePower = driveController.Drive(pad.right_stick_x, pad.right_stick_y, pad.left_stick_x, savedAngle);
        }
        motors[0].setPower(drivePower[0]);
        motors[1].setPower(drivePower[1]);
        motors[2].setPower(drivePower[2]);
        motors[3].setPower(drivePower[3]);
    }
}
