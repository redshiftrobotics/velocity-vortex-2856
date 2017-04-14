package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by matt on 1/11/17.
 */

@Autonomous(name = "2856 - 2 Balls, Corner Vortex, and End")
public class NewShootVortex extends LinearOpMode{
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


        String sideText;
        int side = 1;
        // Retrieve file.
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

        String colorTargetIsRight = "";

        // Provide in a more user friendly form.
        sideText = text.toString();

        if(sideText.equals("red")) {
            //the string for which the color you want to press is on the right... so for a blue auto it would be "red, blue" and for red it would be "blue, red"
            colorTargetIsRight = "blue, red";
            side = -1;
        } else if (sideText.equals("blue")) {
            //the string for which the color you want to press is on the right... so for a blue auto it would be "red, blue" and for red it would be "blue, red"
            colorTargetIsRight = "red, blue";
            side = 1;
        }

        initDevices();

        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};
        straightConst();

        //hopper.setPosition(0.48);
        waitForStart();
        robot.Straight(.57f, forward, 10, telemetry); //.625

        shooter.setPower(1);
        Thread.sleep(1000);
        shooter.setPower(0);

        robot.AngleTurn(120*side, 10, telemetry);

        robot.Straight(3f, forward, 10, telemetry); //.625
    }

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        lrs = hardwareMap.i2cDevice.get("rrs");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setPower(0);
        shooter.setDirection(DcMotor.Direction.REVERSE);
        Servo capServo = hardwareMap.servo.get("cap");
        Servo bAlign = hardwareMap.servo.get("balign");
        Servo fAlign = hardwareMap.servo.get("falign");
        Servo actuator = hardwareMap.servo.get("ra");
        Servo aim = hardwareMap.servo.get("shooterServo");
        aim.setPosition(0.54);
        actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);
        bAlign.setPosition(0.2);
        fAlign.setPosition(0.1);
        capServo.setPosition(0.3);
        //hopper = hardwareMap.servo.get("hopper");
        robot = new Robot(this, imu, m0, m1, m2, m3, lrs, telemetry);
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
