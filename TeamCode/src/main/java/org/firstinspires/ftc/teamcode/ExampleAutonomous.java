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
    Servo la;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        robot = new Robot(imu, m0, m1, m2, m3, cs, cs1, csFront, us, telemetry);
        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};
        //working PIDs
        //P: 100
        //I: 30
        //D: 0

        //loop
        //Float[] backward = new Float[]{-1f,0f};

        robot.Data.PID.PTuning = 15f;
        robot.Data.PID.ITuning = 0f;
        robot.Data.PID.DTuning = 0f;


        telemetry.addData("Tune val: ", robot.Data.PID.PTuning);

        waitForStart();

        if(gamepad1.a) {
            robot.Data.PID.PTuning -= 1;
            Thread.sleep(10);
        } else if(gamepad1.y) {
            robot.Data.PID.PTuning += 1;
            Thread.sleep(10);
        }

        robot.Straight(0.5f, forward, 10, telemetry);
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
