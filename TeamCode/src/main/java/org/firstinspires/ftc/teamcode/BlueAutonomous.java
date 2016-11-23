package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by matt on 11/10/16.
 */
@Autonomous(name = "2856 Blue Autonomous")
public class BlueAutonomous extends LinearOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;

    DcMotor shooter;
    Servo hopper;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);

        hopper = hardwareMap.servo.get("hopper");

        robot = new Robot(imu, m0, m1, m2, m3, telemetry);

        robot.Data.PID.PTuning = 100f;
        robot.Data.PID.ITuning = 30f;
        robot.Data.PID.DTuning = 0f;
        hopper.setPosition(0.48);

        waitForStart();
        robot.Straight(.5f, new Float[]{1f,0f}, 10, telemetry);
        Thread.sleep(1000);
        hopper.setPosition(0);
        shooter.setPower(1);
        Thread.sleep(3000);
        hopper.setPosition(0.48);
        shooter.setPower(0);
        m1.setPower(1);
        m2.setPower(1);
        Thread.sleep(300);
        m1.setPower(0);
        m2.setPower(0);
        Thread.sleep(1000);
        robot = new Robot(imu, m0, m1, m2, m3, telemetry);
        robot.Straight(3.5f, new Float[]{1f,0f}, 10, telemetry);
    }
}
