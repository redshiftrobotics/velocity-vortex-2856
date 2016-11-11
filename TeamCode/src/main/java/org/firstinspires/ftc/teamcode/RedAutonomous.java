package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * Created by matt on 11/10/16.
 */
@Autonomous(name = "RedAutonomous")
public class RedAutonomous extends LinearOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;


    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");

        robot = new Robot(imu, m0, m1, m2, m3, telemetry);

        waitForStart();

        robot.Straight(1f, new Float[]{1f,0f}, 1, telemetry);
        robot.AngleTurn(45, 2, telemetry);
        robot.Straight(3f, new Float[]{1f,0f}, 4, telemetry);
        robot.AngleTurn(-45, 1, telemetry);
    }
}
