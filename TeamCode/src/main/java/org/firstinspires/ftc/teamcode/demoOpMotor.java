package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Duncan on 8/15/2017.
 */

@TeleOp(name="Demo Op Motor",group="Demo")
public class demoOpMotor extends OpMode{

    DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("motor");
    }

    @Override
    public void loop() {
        motor.setPower(gamepad1.right_stick_y);
    }
}
