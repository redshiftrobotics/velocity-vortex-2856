package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * Created by matt on 10/15/16.
 */
@Autonomous(name = "ExampleAutonomous", group = "pid-test")
public class ExampleAutonomous extends LinearOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    PIDRobot robot;
    ColorSensor cs;


    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        cs = hardwareMap.colorSensor.get("cs1");

        /*Hardware hardware = new Hardware(m0, m1, m2, m3, cs);
        PIDRobot robot = new PIDRobot(hardware, imu, telemetry);
        robot.setTuning(63f, 10f, 0f);
        waitForStart();
        robot.linearMove(2f, new float[]{1f, 0f}, 4);
        sleep(10000);
        robot.moveToLine(new float[]{-1f, 0}, 0.5f, 10);
        sleep(10000);
        robot.turnToAngle(45, 5);
        robot.turnToAngle(90, 5);*/
        Hardware hardware = new Hardware(m0, m1, m2, m3, cs);
        PIDRobot robot = new PIDRobot(hardware, imu, telemetry);
        robot.setTuning(63f, 10f, 0f);
        waitForStart();
        robot.linearMove(2f, new float[]{1f, 0f}, 4);
        robot.turnToAngle(-45, 5);
    }

    enum TuneState {
        P, I, D
    }
}
