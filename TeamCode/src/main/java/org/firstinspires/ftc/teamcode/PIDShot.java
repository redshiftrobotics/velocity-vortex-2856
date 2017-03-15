package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
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

@Autonomous(name = "PID Shot")
public class PIDShot extends LinearOpMode{
    I2cDeviceSynch imu;
    int side;
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

       /* Thread.sleep(500);

        while(gamepad2.a){
            telemetry.addData("IMU", imu.getConnectionInfo());
            telemetry.update();
        }*/

        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};

        side = getSide();
        waitForStart();

        straightConst();

        //hopper.setPosition(0.48);
        waitForStart();
        Thread.sleep(10000);
        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.4f;
        robot.Straight(0.3f, forward, 10, telemetry); //.625


        turnConst();
        robot.AngleTurn(42f * side, 10, telemetry);
        robot.AngleTurn(0, 10, telemetry);

        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.7f;
        robot.Straight(1.7f, forward, 10, telemetry); //.625

        if (side == 1) {
            robot.AngleTurn(25f * side, 10, telemetry);
            robot.AngleTurn(0, 10, telemetry);
        }

        shooter.setPower(1);
        Thread.sleep(1500);
        shooter.setPower(0);

        if (side == 1) {
            robot.AngleTurn(72 - 25f * side, 5, telemetry);
            robot.AngleTurn(0, 10, telemetry);
        } else {
            robot.AngleTurn(72f * side, 5, telemetry);
            robot.AngleTurn(0, 10, telemetry);
        }

        robot.Straight(3.5f, forward, 6, telemetry);
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
        robot = new Robot(this, imu, m0, m1, m2, m3, lrs, telemetry);
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

    private int getSide() {
        int s;
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

        // Provide in a more user friendly form.
        String sideText = text.toString();
        if(sideText.equals("red")) {
            s = -1;
        } else if (sideText.equals("blue")) {
            s = 1;
        } else { //this should never happen
            s = 1;
        }
        return s;
    }
}
