package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by matt on 4/19/17.
 */
@TeleOp(name = "Servo Calibration")
public class FullChassisServoCalibrator extends OpMode {

    public Servo capServo1;
    public Servo capServo2;

    public boolean leftTriggerPressed = false;
    public boolean leftBumperButtonPressed = false;

    public boolean rightTriggerPressed = false;
    public boolean rightBumperButtonPressed = false;

    public float capServo1Position = 0.5f;
    public float capServo2Position = 0.5f;



    @Override
    public void loop() {
        if (gamepad1.left_trigger > 0.1 && !leftTriggerPressed) {
            capServo1Position += 0.05;
            leftTriggerPressed = true;
        } else if(!(gamepad1.left_trigger>0.1)){
            leftTriggerPressed = false;
        }

        if (gamepad1.left_bumper && !leftBumperButtonPressed) {
            capServo1Position -= 0.05;
            leftBumperButtonPressed = true;
        } else if(!gamepad1.left_bumper){
            leftBumperButtonPressed = false;
        }

        if (gamepad1.right_trigger > 0.1 && !rightTriggerPressed) {
            capServo2Position += 0.05;
            rightTriggerPressed = true;
        } else if(!(gamepad1.right_trigger>0.1)){
            rightTriggerPressed = false;
        }

        if (gamepad1.right_bumper && !rightBumperButtonPressed) {
            capServo2Position -= 0.05;
            rightBumperButtonPressed = true;
        } else if(!gamepad1.right_bumper){
            rightBumperButtonPressed = false;
        }

        capServo1Position = Range.clip(capServo1Position, 0, 1);
        capServo2Position = Range.clip(capServo2Position, 0, 1);

        capServo1.setPosition(capServo1Position);
        capServo2.setPosition(capServo2Position);

        telemetry.addData("Servo 1 ", capServo1Position);
        telemetry.addData("Servo 2 ", capServo2Position);
        telemetry.update();


    }

    public void init() {
        capServo1 = hardwareMap.servo.get("cap1");
        capServo2 = hardwareMap.servo.get("cap2");
    }
}
