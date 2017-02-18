package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.lasarobotics.vision.opmode.LinearVisionOpMode;

/**
 * Created by matt on 10/15/16.
 */
@Autonomous(name = "ScratchTest", group = "pid-test")
public class ScratchTest extends LinearVisionOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor csf;
    ColorSensor csb;
    UltrasonicSensor us;
    Servo la;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        csf = hardwareMap.colorSensor.get("csf");
        csb = hardwareMap.colorSensor.get("csb");
        robot = new Robot(imu, m0, m1, m2, m3, us, telemetry);
        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};

        robot.Data.PID.PTuning = 10f;
        robot.Data.PID.ITuning = 5f;
        robot.Data.PID.DTuning = 0f;

        waitForStart();

        robot.MoveToLine(forward, csf, 0.1f, 10, telemetry);

    }
}
