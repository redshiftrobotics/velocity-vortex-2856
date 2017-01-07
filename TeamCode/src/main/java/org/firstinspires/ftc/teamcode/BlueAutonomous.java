package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.opmode.extensions.CameraControlExtension;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Size;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by matt on 11/10/16.
 */
@Autonomous(name = "2856 Autonomous")
public class BlueAutonomous extends LinearVisionOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor cs;
    ColorSensor cs1;

    private String sideText;

    private int side; //1 for blue, -1 for red because everything will be flipped this is now determined by a file... see the beginning of runOpMode()

    DcMotor shooter;
    Servo hopper;

    @Override
    public void runOpMode() throws InterruptedException {
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

        waitForVisionStart();
        initVision();
        initDevices();

        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};

        //FLIP THE VALUE BECAUSE THE FRONT CAMERA IS A MIRROR
        if(colorTargetIsRight.equals("red, blue")) {
            colorTargetIsRight = "blue, red";
        } else {
            colorTargetIsRight = "red, blue";
        }

        straightConst();

        //hopper.setPosition(0.48);
        waitForStart();
        robot.Straight(.5f, forward, 10, telemetry); //.625


        turnConst();

        if(side == -1) {
            robot.AngleTurn(-25f * side, 2, telemetry);
        }

        shooter.setPower(1);
        Thread.sleep(2000);
        shooter.setPower(0);

        if(side == -1) {
            robot.AngleTurn(25f * side, 2, telemetry);
        }


        straightConst();

        robot.Straight(.3f, forward, 10, telemetry);

        turnConst();

        //Thread.sleep(1000);
        robot.AngleTurn(60f*side, 4, telemetry);
        //Thread.sleep(1000);

        straightConst();

        robot.Straight(1.1f, forward, 10, telemetry); //1.2 1.6
        //Thread.sleep(1000);


        turnConst();

        robot.AngleTurn(-60f*side, 4, telemetry);
        //robot.Straight(1f, new Float[]{1f,0f}, 10, telemetry);
        //Thread.sleep(1000);

        straightConst();

        //NEW
        robot.MoveToLine(backward, 0.4f, 10, telemetry);
        Thread.sleep(500);
        robot.MoveToLine(forward, 0.4f, 10, telemetry);

        Thread.sleep(1000);

        //END NEW || OLD

        //robot.MoveToLine(forward, 0.4f, 10, telemetry);
        //Thread.sleep(500);
        //robot.MoveToLine(backward, 0.4f, 10, telemetry);
        // END OLD

        telemetry.addData("beacon is: ", beacon.getAnalysis().getColorString());
        telemetry.update();


        // this should only need to be here if the color sensor is offset from the camera
//        if(side == -1) { // if on the red side
//            robot.Straight(0.1f, forward, 10, telemetry);
//        } else {
//            robot.Straight(0.14f, backward, 10, telemetry);
//        }
//        Thread.sleep(1000);

        //in front of first beacon: decide color, shift accordingly, and move in
        if(beacon.getAnalysis().getColorString().equals(colorTargetIsRight)) { //target color is right
            telemetry.addData("beacon ", "right");
            telemetry.update();
            if(side == 1) { // if we are on BLUE SIDE
                robot.Straight(0.12f, backward, 10, telemetry); // target is right so move backward
            } else { // we are on RED SIDE
                robot.Straight(0.12f, forward, 10, telemetry); // target is right so move forward
            }
        } else if (beacon.getAnalysis().getColorString().equals("???, ???")) {
            //do nothing
        } else { //target is left
            telemetry.addData("beacon ", "left");
            telemetry.update();
            if(side == 1) { // for blue side
                robot.Straight(0.12f, forward, 10, telemetry);
            } else { // red side
                robot.Straight(0.12f, backward, 10, telemetry);
            }
        }



        robot.Straight(1f, new Float[]{0f, -1f*side}, 4, telemetry); //this will timeout, intentional
        robot.Straight(.45f, new Float[]{0f, 1f*side}, 10, telemetry);
/*
        //straight to clear existing line
        robot.Straight(1f, backward, 10, telemetry);
        robot.MoveToLine(backward, .4f, 10, telemetry);
        Thread.sleep(500);
        robot.MoveToLine(forward, .4f, 10, telemetry);

        if(side == -1) { // if on the red side
            robot.Straight(0.1f, forward, 10, telemetry);
        } else {
            robot.Straight(0.14f, backward, 10, telemetry);
        }
        Thread.sleep(1000);



        //in front of second beacon: decide color, shift accordingly, and move in
        if(beacon.getAnalysis().getColorString().equals(colorTargetIsRight)) { //target is right
            telemetry.addData("beacon ", "right");
            telemetry.update();
            if(side == 1) { // if we are on blue side we need a little bump forwards to press but not on red side
                robot.Straight(0.1f, forward, 10, telemetry);
            }
        } else if (beacon.getAnalysis().getColorString().equals("???, ???")) {
            //do nothing
        } else { //target is left
            if(side == 1) { // for blue side
                robot.Straight(0.23f, forward, 10, telemetry);
            } else { // red side
                robot.Straight(0.15f, backward, 10, telemetry);
            }
            telemetry.addData("beacon ", "left");
            telemetry.update();
        }
        robot.Straight(1f, new Float[]{0f, -1f*side}, 4, telemetry); //this will timeout, intentional
        robot.Straight(.5f, new Float[]{0f, 1f*side}, 10, telemetry);
        */


        robot.AngleTurn(10f*side, 3, telemetry);
        //go backwards because who cares
        robot.Straight(1.65f, backward, 5, telemetry);

    }

    private void initVision() {
        setCamera(Cameras.SECONDARY);
        setFrameSize(new Size(1440,2560));
        enableExtension(Extensions.BEACON);
        enableExtension(Extensions.ROTATION);
        enableExtension(Extensions.CAMERA_CONTROL);
        beacon.setAnalysisMethod(Beacon.AnalysisMethod.COMPLEX);
        beacon.setColorToleranceRed(0);
        beacon.setColorToleranceBlue(0);
        rotation.setIsUsingSecondaryCamera(false);
        rotation.disableAutoRotate();
        rotation.setActivityOrientationFixed(ScreenOrientation.PORTRAIT);
        cameraControl.setColorTemperature(CameraControlExtension.ColorTemperature.AUTO);
        cameraControl.setAutoExposureCompensation();
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
