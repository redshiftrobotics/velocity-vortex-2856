package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.opmode.VisionOpMode;
import org.lasarobotics.vision.opmode.extensions.CameraControlExtension;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Size;

/**
 * Created by matt on 10/15/16.
 */
@Autonomous(name = "ExampleAutonomous", group = "pid-test")
public class ExampleAutonomous extends LinearVisionOpMode {
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

    @Override
    public void runOpMode() throws InterruptedException {
        us = hardwareMap.ultrasonicSensor.get("us");
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        cs = hardwareMap.colorSensor.get("cs");
        cs1 = hardwareMap.colorSensor.get("cs1");
        csFront = hardwareMap.colorSensor.get("csFront");

        robot = new Robot(imu, m0, m1, m2, m3, cs, cs1, csFront, us, telemetry);

        //working PIDs
        //P: 100
        //I: 30
        //D: 0

        //loop
        Float[] forward = new Float[]{1f,0f};
        //Float[] backward = new Float[]{-1f,0f};
//        robot.Data.PID.PTuning = 50f;
//        robot.Data.PID.ITuning = 0f;
//        robot.Data.PID.DTuning = 0f;
        waitForStart();
        robot.Straight(1f, forward, 10, telemetry);
//        while(opModeIsActive()) {
//            telemetry.addData("Distance", String.valueOf(us.getUltrasonicLevel()));
//            telemetry.update();
//        }
        //robot.ultraSeek(20, 0, 100, telemetry);

        //robot.ultraSeek(30, 1, 5, telemetry);
        //telemetry.addData("beacon", beacon.getAnalysis().getColorString());
        //robot.Push(5f, new Float[]{0f,-1f}, 7, telemetry);
//        robot.AngleTurn(45f, 4, telemetry);
//        robot.Data.PID.PTuning = 100f;
//        robot.Data.PID.ITuning = 30f;
//        robot.Data.PID.DTuning = 0f;
        //robot.MoveToLine(forward, 0.2f, 10, telemetry);

    }

    enum TuneState {
        P, I, D
    }
}
