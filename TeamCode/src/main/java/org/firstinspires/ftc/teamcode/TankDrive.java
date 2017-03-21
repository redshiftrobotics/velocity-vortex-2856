package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Duncan on 1/31/2017.
 */
@Disabled
@TeleOp(name="Tank Drive")
public class TankDrive extends OpMode{

    DcMotor leftDriveF;
    DcMotor leftDriveB;
    DcMotor rightDriveF;
    DcMotor rightDriveB;

    @Override
    public void init() {
        leftDriveF = hardwareMap.dcMotor.get("leftDriveF");
        leftDriveB = hardwareMap.dcMotor.get("leftDriveB");
        rightDriveF = hardwareMap.dcMotor.get("rightDriveF");
        rightDriveB = hardwareMap.dcMotor.get("rightDriveB");
    }

    @Override
    public void loop() {
        leftDriveF.setPower(-gamepad1.left_stick_y);
        leftDriveB.setPower(-gamepad1.left_stick_y);
        rightDriveF.setPower(gamepad1.right_stick_y);
        rightDriveB.setPower(gamepad1.right_stick_y);
    }
}
