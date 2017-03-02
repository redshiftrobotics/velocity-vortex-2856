package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
@Autonomous(name = "2856 Button Autonomous")
public class NeoAuto extends LinearVisionOpMode {
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
    ColorSensor bs; // beacon sensor
    int side;

    Float[] forward = new Float[]{1f,0f};
    Float[] backward = new Float[]{-1f,0f};

    @Override
    public void runOpMode() throws InterruptedException {
        side = getSide();

        if(side == -1) { // on red side, thus using left side of robot
            bs = hardwareMap.colorSensor.get("lbs");
            actuator = hardwareMap.servo.get("la");
            rs = hardwareMap.i2cDevice.get("lrs");
        } else {
            bs = hardwareMap.colorSensor.get("rbs");
            actuator = hardwareMap.servo.get("ra");
            rs = hardwareMap.i2cDevice.get("rrs");
            actuator.setDirection(Servo.Direction.REVERSE);
        }

        initDevices();

        waitForStart();

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
            robot.Data.PID.turnPrecision = 2; // we don't need precision on this turn, it just needs to be fast
            //robot.AngleTurn((70+25)*side, 10, telemetry);
            robot.AngleTurn((75+25)*side, 10, telemetry);
            robot.Data.PID.turnPrecision = 1;
        } else {
            robot.AngleTurn(0, 10, telemetry); // reset
            shooter.setPower(1);
            Thread.sleep(1000);
            shooter.setPower(0);
            turnConst();
            robot.Data.PID.turnPrecision = 2; // we don't need precision on this turn, it just needs to be fast
            robot.AngleTurn(70*side, 10, telemetry);
            robot.Data.PID.turnPrecision = 1;
        }

        straightConst();
//        robot.Straight(0.2f, forward, 4, telemetry);
//        lineConst();
//        robot.MoveToLine(forward, csf, .2f, 10, telemetry);

        //begin alignment
        robot.Straight(2.37f * (35f/45f), forward, 2, telemetry); // 2.45 to 2.37



        turnConst();



        //robot.AngleTurn(-60*side, 10, telemetry);
        robot.AngleTurn(-65*side, 10, telemetry);
        robot.AngleTurn(0, 10, telemetry);


        straightConst();

        robot.AlignWithWall(10, forward, 10, telemetry); // 9 worked well, 8 was still to far I think... update, with no lash 7 is too close, 8 is the butter zone for both now (yay continuity!)


        turnConst();



        robot.Data.PID.turnPrecision = 0.5f;
        robot.AngleTurn(-10*side, 10, telemetry);
        robot.AngleTurn(0, 10, telemetry); // reset
        robot.Data.PID.turnPrecision = 1f;


        lineConst();
        robot.MoveToLine(forward, csb, 0.2f * (35f/45f), 10, telemetry);
        Thread.sleep(100);
        robot.MoveToLine(backward, csb, 0.1f * (35f/45f), 10, telemetry);
        telemetry.log();
        telemetry.addData("Beacon 1", robot.getDistance());
        telemetry.update();



        turnConst();


        telemetry.addData("BEACON 1 DIST", robot.getDistance());
        telemetry.update();
        if(robot.getDistance() >= 6 && robot.getDistance() < 10) { // ok
            push(10); // turn in further to have a better chance of pressing
        } else if (robot.getDistance() <= 7){ // good
            push(5);
        } else if (robot.getDistance() >= 10) { // extreme, fix
            push(20);
        }

        //straightConst();
        //robot.Straight(0.2f, forward, 10, telemetry);




        // for some reason at this point we end with a lot of variance... how much we had to press the beacon plays a large roll in it
        // we almost never are two far away to correct but often are too close. but if we move farther intentionally, we _do_ get to far away.
        // therefor, if we are closer than we like for running AlignWithWall (anything closer than a couple centimeters further out than our target),
        // we want to move out much further, otherwise we just bump out a little bit.
        turnConst();


        robot.Data.PID.turnPrecision = 0.5f;
        robot.AngleTurn(15*side, 10, telemetry); // turn out from the wall
        robot.Data.PID.turnPrecision = 1f;
        straightConst();

        // move away from the wall to set up for AlignWithWall...
        if (robot.getDistance() < 8) { // if we are closer than 10 cm we need to get out a little further for AlignWithWall to work
            robot.Straight(1f * (35f/45f), backward, 10, telemetry); //.6 working
        } else { // otherwise just out a little so we dont overshoot the line when Aligning
            robot.Straight(.83f * (35f/45f), backward, 10, telemetry); //.6 working, then *****.75******
        }

        turnConst();
        robot.AngleTurn(-25*side, 10, telemetry); // turn in to start the wall align
        straightConst();
        robot.AlignWithWall(10, backward, 10, telemetry); // if we are lashing we need to get in closer with 7, but if we keep the set screws tight, 8 or 9 has better chances


        telemetry.addData("AlignWithWall 2 distance", robot.getDistance());
        telemetry.update();

        turnConst();
        robot.Data.PID.turnPrecision = 0.5f;
        robot.AngleTurn(10*side, 10, telemetry);
        robot.AngleTurn(0, 10, telemetry); // reset
        robot.Data.PID.turnPrecision = 1f;

        lineConst();
        robot.MoveToLine(backward, csb, 0.2f * (35f/45f), 10, telemetry);
        Thread.sleep(100);
        robot.MoveToLine(forward, csb, 0.1f * (35f/45f), 10, telemetry);
        telemetry.addData("Beacon 2", robot.getDistance());
        telemetry.update();


        turnConst();



        if(robot.getDistance() >= 6 && robot.getDistance() < 10) { // ok
            push(10); // turn in further to have a better chance of pressing
        } else if (robot.getDistance() <= 7){ // good
            push(5);
        } else if (robot.getDistance() >= 10) { // extreme, fix
            push(20);
        }


        robot.AngleTurn(-15f, 2, telemetry);
        robot.Straight(3f * (35f/45f), backward, 3, telemetry);
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
        robot = new Robot(imu, m0, m1, m2, m3, rs, telemetry);
    }

    public void push(int degrees) {
        boolean wasFirst = true;

        if (side == -1) { // on red side
            if(bs.blue() > bs.red()) {
                telemetry.addData("color", "BLUE > RED");
                robot.Straight(0.2f * (35f/45f), forward, 10, telemetry);
                wasFirst = false;
            } else {
                telemetry.addData("color", "RED > BLUE");
            }
        } else { // on blue side
            if(bs.blue() > bs.red()) {
                telemetry.addData("color", "BLUE > RED");
            } else {
                telemetry.addData("color", "RED > BLUE");
                robot.Straight(0.2f * (35f/45f), forward, 10, telemetry);
            }
        }
        telemetry.update();

        if(degrees >= 20 && wasFirst) { // if it was not the first button, there is a large risk of pressing the first button if backing up
            robot.Straight(0.07f * (35f/45f), backward, 10, telemetry);
        }



        // out first then turn into it... worked well with turning first too
        actuator.setPosition(1.0);
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        robot.AngleTurn(-degrees*side, 3, telemetry);




        actuator.setPosition(0);
        robot.AngleTurn(degrees*side, 5, telemetry);
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
