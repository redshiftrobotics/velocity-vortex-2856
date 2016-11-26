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
    Robot robot;
    ColorSensor cs;


    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        cs = hardwareMap.colorSensor.get("cs");

        robot = new Robot(imu, m0, m1, m2, m3, cs, telemetry);


        //working PIDs
        //P: 100
        //I: 30
        //D: 0

        //loop
        robot.Data.PID.PTuning = 100f;
        robot.Data.PID.ITuning = 30f;
        robot.Data.PID.DTuning = 0f;
        waitForStart();
        //robot.Straight(5f, new Float[]{0f,1f}, 7, telemetry);
        robot.AngleTurn(165f, 15, telemetry);

    }

    enum TuneState {
        P, I, D
    }
}
