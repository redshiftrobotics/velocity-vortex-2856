package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Duncan on 8/15/2017.
 */

@TeleOp(name="Demo Op Servo",group="Demo")
public class demoOpServo extends OpMode{

    Servo servo;

    @Override
    public void init() {
        servo = hardwareMap.servo.get("servo");
    }

    @Override
    public void loop() {
        servo.setPosition((gamepad1.right_stick_y+1f)/2f);
    }
}
