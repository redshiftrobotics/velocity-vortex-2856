package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by matt on 1/11/17.
 */

@Autonomous(name = "2856 - 2 Balls and End")
public class NewShoot extends LinearOpMode{
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor cs;
    ColorSensor cs1;
    ColorSensor csFront;
    UltrasonicSensor us;

    DcMotor shooter;

    @Override
    public void runOpMode() throws InterruptedException {
        initDevices();

        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};
        straightConst();

        //hopper.setPosition(0.48);
        waitForStart();
        robot.Straight(.55f, forward, 10, telemetry); //.625

        shooter.setPower(1);
        Thread.sleep(1000);
        shooter.setPower(0);

        robot.Straight(.4f, backward, 10, telemetry); //.625
    }

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setPower(0);
        shooter.setDirection(DcMotor.Direction.REVERSE);
        Servo capServo1 = hardwareMap.servo.get("cap1");
        Servo capServo2 = hardwareMap.servo.get("cap2");
        Servo capHold = hardwareMap.servo.get("hold");
        capServo1.setPosition(0.95);
        capServo2.setPosition(0.25);
        Servo bAlign = hardwareMap.servo.get("balign");
        Servo fAlign = hardwareMap.servo.get("falign");
        Servo actuator = hardwareMap.servo.get("ra");
        Servo aim = hardwareMap.servo.get("shooterServo");
        aim.setPosition(0.54);
        //actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);
        bAlign.setPosition(0.2);
        fAlign.setPosition(0.1);
        //hopper = hardwareMap.servo.get("hopper");
        robot = new Robot(this, imu, m0, m1, m2, m3, telemetry);
        capHold.setPosition(0.15);
        telemetry.addData("IMU:", robot.Data.imu.getAngularOrientation());
    }

    private void straightConst() {
        robot.Data.PID.PTuning = 10f;
        robot.Data.PID.ITuning = 5f;
        robot.Data.PID.DTuning = 0f;
    }

    private void turnConst() {
        robot.Data.PID.PTuning = 10f; // 7f
        robot.Data.PID.ITuning = 8f; // 5f
        robot.Data.PID.DTuning = 0f;
    }
}
