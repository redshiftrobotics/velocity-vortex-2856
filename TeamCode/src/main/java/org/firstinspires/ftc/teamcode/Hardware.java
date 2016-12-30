package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by adam on 12/19/16.
 */
public class Hardware {
        public int motorCount() {
            return motors.length;
        }

        public Hardware(DcMotor dc0, DcMotor dc1, DcMotor dc2, DcMotor dc3, ColorSensor cs) {
            motors = new DcMotor[]{dc0, dc1, dc2, dc3};
            motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
            motors[3].setDirection(DcMotorSimple.Direction.REVERSE);
            colorSensor = cs;
        }

        //our motors
        private DcMotor[] motors;

        public DcMotor getMotor(int n) {
            return motors[n];
        }

        ColorSensor colorSensor; //color sensor used for line detection
        final static int EncoderCount = 1440; //encoder count for a full rotation. Should not change.
        final static float POWER_CONSTANT = (3/8f); // I believe this value does not change. 0.5*(3/4)

        public float getAdjustedColorValues() {
            return (colorSensor.red() + colorSensor.blue() + colorSensor.green()) / 3;
        }
}


