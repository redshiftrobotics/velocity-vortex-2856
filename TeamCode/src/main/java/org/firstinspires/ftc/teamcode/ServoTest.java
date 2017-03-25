package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by matt on 3/22/17.
 */

@TeleOp(name="Servo TASTEY FUCKKKKK")
public class ServoTest extends OpMode {
    Servo servo;
    public Float servoPos;
    boolean pressedA = false;
    boolean pressedY = false;
    @Override
    public void init() {
        servo = hardwareMap.servo.get("s");
        servoPos = 0.5f;
    }

    @Override
    public void loop() {

        if (gamepad1.a && !pressedA) {
            servoPos += 0.01f;
            pressedA = true;
        }

        if (gamepad1.y && !pressedY) {
            servoPos -= 0.01f;
            pressedY = true;
        }


        if (!gamepad1.a) {
            pressedA = false;
        }

        if (!gamepad1.y) {
            pressedY = false;
        }

        telemetry.addData("Position: ", servoPos);
        telemetry.update();
        servo.setPosition(servoPos);
    }


}
