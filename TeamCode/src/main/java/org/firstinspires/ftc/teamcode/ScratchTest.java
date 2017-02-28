package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
    ColorSensor bs;
    I2cDevice lrs;
    I2cDevice rrs;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        csf = hardwareMap.colorSensor.get("csf");
        csb = hardwareMap.colorSensor.get("csb");
        bs = hardwareMap.colorSensor.get("lbs");
        lrs = hardwareMap.i2cDevice.get("lrs");
        rrs = hardwareMap.i2cDevice.get("rrs");
        bs.enableLed(false);
        la = hardwareMap.servo.get("la");
        la.setPosition(0);
        robot = new Robot(imu, m0, m1, m2, m3, lrs, rrs, telemetry);
        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};

        robot.Data.PID.PTuning = 5f;
        robot.Data.PID.ITuning = 0f;
        robot.Data.PID.DTuning = 0f;

        waitForStart();
//        robot.MoveToLine(forward, csb, 0.2f, 10, telemetry);
//        robot.MoveToLine(backward, csb, 0.1f, 10, telemetry);
//        if(bs.blue() > bs.red()) {
//            telemetry.addData("color", "BLUE > RED");
//            robot.Straight(0.1f, forward, 10, telemetry);
//            push();
//        } else {
//            telemetry.addData("color", "RED > BLUE");
//            push();
//        }
//        telemetry.update();


        //robot.WallFollow(forward, "left", csb, 10, telemetry);

        robot.Data.PID.PTuning = 6f;
        robot.Data.PID.ITuning = 0f;
        robot.Data.PID.DTuning = 0f;
        robot.WallFollow(10, forward, "left", csb, 20, telemetry);

        /*
        robot.AngleTurn(-10f, 10, telemetry);
        robot.AlignWithWall(forward, 10, telemetry);
        robot.AngleTurn(10f, 10, telemetry);
        */


//        push();
//        Thread.sleep(1000);
//        robot.Data.PID.PTuning = 4f;
//        robot.Data.PID.ITuning = 5f;
//        robot.Data.PID.DTuning = 0f;
//        robot.MoveToLine(forward, csb, 0.2f, 10, telemetry);
//        robot.MoveToLine(backward, csb, 0.17f, 10, telemetry);
//        push();
    }

    private void push() {
        la.setPosition(1.0);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        la.setPosition(0.8);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        la.setPosition(1.0);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        la.setPosition(0);
    }
}
