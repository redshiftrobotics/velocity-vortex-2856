package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
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
    ColorSensor cs;

    DcMotor shooter;
    Servo hopper;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        //shooter = hardwareMap.dcMotor.get("shooter");
        cs = hardwareMap.colorSensor.get("cs");
        //shooter.setDirection(DcMotor.Direction.REVERSE);
//
//        hopper = hardwareMap.servo.get("hopper");
//
        robot = new Robot(imu, m0, m1, m2, m3, cs, telemetry);
//
        robot.Data.PID.PTuning = 100f;
        robot.Data.PID.ITuning = 30f;
        robot.Data.PID.DTuning = 0f;
//        hopper.setPosition(0.48);
//
        waitForStart();
        robot.Straight(1.25f, new Float[]{1f,0f}, 10, telemetry);
//        Thread.sleep(1000);
//        hopper.setPosition(0);
        //shooter.setPower(1);
        //Thread.sleep(3000);
//        hopper.setPosition(0.48);
        //shooter.setPower(0);
        robot.Straight(1f, new Float[]{1f,0f}, 10, telemetry);
        robot.AngleTurn(45f, 10, telemetry);
        robot.Straight(3f, new Float[]{1f,0f}, 10, telemetry);
        robot.AngleTurn(-45f, 10, telemetry);
        //robot.Straight(1f, new Float[]{1f,0f}, 10, telemetry);
        robot.MoveToLine(new Float[]{-1f,0f}, .65f, 10, telemetry);
        Thread.sleep(1000);
        robot.MoveToLine(new Float[]{1f,0f}, .2f, 10, telemetry);
        robot.Straight(1f, new Float[]{0f,-1f}, 10, telemetry);
        robot.Straight(1f, new Float[]{0f,1f}, 10, telemetry);
        //straight to clear existing line
        robot.Straight(1f, new Float[]{1f,0f}, 10, telemetry);
        robot.MoveToLine(new Float[]{1f,0f}, .4f, 10, telemetry);
        Thread.sleep(1000);
        robot.MoveToLine(new Float[]{-1f,0f}, .2f, 10, telemetry);
        robot.Straight(1f, new Float[]{0f,-1f}, 10, telemetry);
        robot.Straight(1f, new Float[]{0f,1f}, 10, telemetry);
        robot.Straight(3f, new Float[]{-1f,0f}, 10, telemetry);

    }
}
