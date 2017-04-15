package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by matt on 1/11/17.
 */

@Autonomous(name = "Long Shoot")
public class LongShoot extends LinearOpMode{
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor cs;
    ColorSensor cs1;
    ColorSensor csFront;
    I2cDevice lrs;
    I2cDevice rrs;
    UltrasonicSensor us;

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
        Thread.sleep(15000);
        robot.Straight(1.82f, forward, 10, telemetry); //.625

        shooter.setPower(1);
        Thread.sleep(1000);
        shooter.setPower(0);

        robot.Straight(1f, backward, 10, telemetry);
    }

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        lrs = hardwareMap.i2cDevice.get("rrs");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);
        Servo capServo = hardwareMap.servo.get("cap");
        Servo bAlign = hardwareMap.servo.get("balign");
        Servo fAlign = hardwareMap.servo.get("falign");
        Servo actuator = hardwareMap.servo.get("ra");
        actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);
        bAlign.setPosition(0.2);
        fAlign.setPosition(0.1);
        capServo.setPosition(0.3);
        //hopper = hardwareMap.servo.get("hopper");
        robot = new Robot(this, imu, m0, m1, m2, m3,  telemetry);
        telemetry.addData("IMU:", robot.Data.imu.getAngularOrientation());
    }

    private void straightConst() {
        robot.Data.PID.PTuning = 10f;
        robot.Data.PID.ITuning = 5f;
        robot.Data.PID.DTuning = 0f;
    }

    private void turnConst() {
        robot.Data.PID.PTuning = 10f; // 7f
        robot.Data.PID.ITuning = 8f; // 5f
        robot.Data.PID.DTuning = 0f;
    }
}
