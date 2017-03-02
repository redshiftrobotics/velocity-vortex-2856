package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by matt on 1/11/17.
 */

@Autonomous(name = "Cap Shoot")
public class CapShoot extends LinearOpMode {
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

    private String sideText;

    DcMotor shooter;

    private int side;

    @Override
    public void runOpMode() throws InterruptedException {
        initDevices();

        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};


        File file = new File("/sdcard/Pictures", "prefs");
        StringBuilder text = new StringBuilder();
        // Attempt to load line from file into the buffer.
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            // Ensure that the first line is not null.
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            // Close the buffer reader
            br.close();
        }
        // Catch exceptions... Or don't because that would require effort.
        catch (IOException e) {
        }


        // Provide in a more user friendly form.
        sideText = text.toString();
        if(sideText.equals("red")) {
            side = -1;
        } else if (sideText.equals("blue")) {
            side = 1;
        }

        waitForStart();

        straightConst();

        //hopper.setPosition(0.48);
        waitForStart();
        Thread.sleep(7000);
        robot.Straight(1.7f, forward, 10, telemetry); //.625
        shooter.setPower(1);
        Thread.sleep(1000);
        shooter.setPower(0);

        if(side == 1) {
            robot.AngleTurn(-20f, 3, telemetry);
        }
        robot.Straight(1.2f, forward, 10, telemetry);
    }

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);
        //hopper = hardwareMap.servo.get("hopper");
        robot = new Robot(imu, m0, m1, m2, m3, lrs, telemetry);
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
