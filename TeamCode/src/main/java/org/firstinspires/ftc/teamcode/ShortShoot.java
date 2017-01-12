package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * Created by matt on 1/11/17.
 */

@Autonomous(name = "Short Shoot")
public class ShortShoot extends LinearOpMode{
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor cs;
    ColorSensor cs1;


    DcMotor shooter;

    @Override
    public void runOpMode() throws InterruptedException {
        initDevices();

        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};

        waitForStart();

        straightConst();

        //hopper.setPosition(0.48);
        waitForStart();
        robot.Straight(.6f, forward, 10, telemetry); //.625

        shooter.setPower(1);
        Thread.sleep(2000);
        shooter.setPower(0);

        robot.Straight(.5f, backward, 10, telemetry);
    }

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        shooter = hardwareMap.dcMotor.get("shooter");
        cs = hardwareMap.colorSensor.get("cs");
        cs1 = hardwareMap.colorSensor.get("cs1");
        shooter.setDirection(DcMotor.Direction.REVERSE);
        //hopper = hardwareMap.servo.get("hopper");
        robot = new Robot(imu, m0, m1, m2, m3, cs, cs1, telemetry);
    }

    private void turnConst() {
        robot.Data.PID.PTuning = 50f;
        robot.Data.PID.ITuning = 0f;
        robot.Data.PID.DTuning = 0f;
    }

    private void straightConst() {
        robot.Data.PID.PTuning = 63f;
        robot.Data.PID.ITuning = 10f;
        robot.Data.PID.DTuning = 0f;
    }
}
