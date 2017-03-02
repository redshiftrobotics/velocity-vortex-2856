package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;

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
    I2cDevice lrs;
    Servo la;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        lrs = hardwareMap.i2cDevice.get("lrs");
        robot = new Robot(imu, m0, m1, m2, m3, lrs, telemetry);
        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};
        //working PIDs
        //P: 100
        //I: 30
        //D: 0

        //loop
        //Float[] backward = new Float[]{-1f,0f};

        robot.Data.PID.PTuning = 26f;
        robot.Data.PID.ITuning = 0f;
        robot.Data.PID.DTuning = 0f;

        float IMult = 0;
        float DMult = 0;
        float forwardDistance = 2.4f;

        telemetry.addData("Tune val: ", robot.Data.PID.PTuning);
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.a) {
                DMult -= .01;
                robot.Data.PID.DTuning = DMult * robot.Data.PID.PTuning;
                telemetry.update();
                Thread.sleep(100);
            } else if (gamepad1.y) {
                DMult += .01;
                robot.Data.PID.DTuning = DMult * robot.Data.PID.PTuning;
                telemetry.update();
                Thread.sleep(100);
            } else if (gamepad1.x) {
                IMult -= .01;
                robot.Data.PID.ITuning = IMult * robot.Data.PID.PTuning;
                telemetry.update();
                Thread.sleep(100);
            } else if (gamepad1.b) {
                IMult += .01;
                robot.Data.PID.ITuning = IMult * robot.Data.PID.PTuning;
                telemetry.update();
                Thread.sleep(100);
            } else if (gamepad1.dpad_up) {
                robot.Data.PID.PTuning -= 1;
                robot.Data.PID.ITuning = IMult * robot.Data.PID.PTuning;
                robot.Data.PID.DTuning = DMult * robot.Data.PID.PTuning;
                Thread.sleep(100);
                telemetry.update();
            } else if (gamepad1.dpad_down) {
                robot.Data.PID.PTuning += 1;
                robot.Data.PID.ITuning = IMult * robot.Data.PID.PTuning;
                robot.Data.PID.DTuning = DMult * robot.Data.PID.PTuning;
                Thread.sleep(100);
                telemetry.update();
            } else if (gamepad1.left_bumper) {
                robot.Straight(2.1f, forward, 10, telemetry);
            } else if (gamepad1.right_bumper) {
                robot.Straight(2.1f, backward, 10, telemetry);
            }

            telemetry.addData("Tune val P: ", robot.Data.PID.PTuning);
            telemetry.addData("Tune val I: ", robot.Data.PID.ITuning);
            telemetry.addData("Tune val D: ", robot.Data.PID.DTuning);
            telemetry.update();
            Thread.sleep(100);
        }
//        while(opModeIsActive()) {
//            telemetry.addData("Distance", String.valueOf(us.getUltrasonicLevel()));
//            telemetry.update();
//        }
        //robot.ultraSeek(20, 0, 100, telemetry);


    }

    enum TuneState {
        P, I, D
    }
}
