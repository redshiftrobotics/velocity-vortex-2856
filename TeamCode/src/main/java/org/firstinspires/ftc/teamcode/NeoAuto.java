package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.lasarobotics.vision.opmode.LinearVisionOpMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by matt on 10/15/16.
 */
@Autonomous(name = "New 2856 Autonomous")
public class NeoAuto extends LinearOpMode {
    I2cDeviceSynch imu;
    I2cDevice rs;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    DcMotor shooter;
    Robot robot;
    ColorSensor csf;
    ColorSensor csb;
    UltrasonicSensor us;
    Servo actuator;
    Servo bAlign;
    Servo fAlign;
    ColorSensor bs; // beacon sensor
    int side;

    Float[] forward = new Float[]{1f,0f};
    Float[] backward = new Float[]{-1f,0f};

    @Override
    public void runOpMode() throws InterruptedException {
        side = getSide();

        bAlign = hardwareMap.servo.get("balign");
        fAlign = hardwareMap.servo.get("falign");
        bAlign.setPosition(0);
        fAlign.setPosition(0);

        bs = hardwareMap.colorSensor.get("rbs");
        actuator = hardwareMap.servo.get("ra");
        rs = hardwareMap.i2cDevice.get("rrs");
        actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);

        initDevices();

        waitForStart();

        bAlign.setPosition(1);
        fAlign.setPosition(1);

        straightConst();
        robot.Straight(1.4f * (35f/45f), forward, 10, telemetry);
        turnConst();
        if(side == -1) {
            //turn a little to the right
            robot.AngleTurn(-25*side, 10, telemetry);
            shooter.setPower(1);
            Thread.sleep(1000);
            shooter.setPower(0);
            turnConst();

            robot.AngleTurn((180)*side, 10, telemetry);
            robot.AngleTurn((75+25)*side, 10, telemetry);
            forward = new Float[]{-1f,0f};
            backward = new Float[]{1f,0f};
        } else {
            robot.AngleTurn(0, 10, telemetry); // reset
            shooter.setPower(1);
            Thread.sleep(1000);
            shooter.setPower(0);
            turnConst();
            robot.AngleTurn(75*side, 10, telemetry);
        }

        straightConst();
//        robot.Straight(0.2f, forward, 4, telemetry);
//        lineConst();
//        robot.MoveToLine(forward, csf, .2f, 10, telemetry);

        //begin alignment
        robot.Straight(2.3f * (35f/45f), forward, 2, telemetry); // 2.45 to 2.37



        turnConst();

        //robot.AngleTurn(-60*side, 10, telemetry);


        robot.AngleTurn(-65*side, 10, telemetry);

        robot.Data.PID.PTuning = 16;
        robot.Data.PID.ITuning = 0;
        robot.Straight(0.9f, forward, 3, telemetry);

        Thread.sleep(1000);

        lineConst();
        robot.Data.PID.ITuning = 0;

        robot.MoveToLine(forward, csb, 0.2f * (35f/45f), 10, telemetry);
        Thread.sleep(100);
        robot.MoveToLine(backward, csb, 0.15f * (35f/45f), 3, telemetry);
        push(0);



        turnConst();
        //robot.AngleTurn(-20*side, 3, telemetry);
        robot.UpdateTarget(-20*side);


        lineConst();
        robot.Data.PID.ITuning = 0;

        robot.Straight(0.4f, backward, 10, telemetry);
        robot.MoveToLine(backward, csb, 0.2f * (35f/45f), 10, telemetry);
        Thread.sleep(100);
        robot.MoveToLine(forward, csb, 0.15f * (35f/45f), 3, telemetry);

        push(0);

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

    private void lineConst() {
        robot.Data.PID.PTuning = 4f; // working at 4
        robot.Data.PID.ITuning = 5f; // working at 5
        robot.Data.PID.DTuning = 0f;
    }

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);
        csf = hardwareMap.colorSensor.get("csf");
        csb = hardwareMap.colorSensor.get("csb");
        robot = new Robot(this, imu, m0, m1, m2, m3, rs, telemetry);
    }

    public void push(int degrees) {
        forward = new Float[]{1f,0f};
        backward = new Float[]{-1f,0f};
        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.2f;

        if (side == -1) { // on red side
            if(bs.blue() > bs.red()) {
                telemetry.addData("color", "BLUE > RED");
                robot.Straight(0.35f * (35f/45f), forward, 10, telemetry);
            } else {
                telemetry.addData("color", "RED > BLUE");
            }
        } else { // on blue side
            if(bs.blue() > bs.red()) {
                telemetry.addData("color", "BLUE > RED");
            } else {
                telemetry.addData("color", "RED > BLUE");
                robot.Straight(0.35f * (35f/45f), forward, 10, telemetry);
            }
        }
        telemetry.update();




        forward = new Float[]{-1f,0f};
        backward = new Float[]{1f,0f};






        // out first then turn into it... worked well with turning first too
        actuator.setPosition(1.0);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //robot.AngleTurn(-degrees*side, 3, telemetry);
        actuator.setPosition(0);
        //robot.AngleTurn(degrees*side, 5, telemetry);
        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.7f;
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        actuator.setPosition(1.0);
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        actuator.setPosition(0);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
