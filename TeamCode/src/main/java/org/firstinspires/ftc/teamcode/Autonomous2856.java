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
 * Autonomous2856 is the autonomous op mode for 2856
 * in the FTC game Velocity Vortex. Autonomous2856 uses
 * PID to maintain its rotation throughout the robots journey.
 * This op mode extends LinearOpMode, and makes the robot do
 * the following actions:
 * <ul>
 *     <li>Move forward 1 tile</li>
 *     <li>Fire two projectiles into the Center Vortex</li>
 *     <li>Move forward 1 tile</li>
 *     <li>Rotate to face the beacons, while knocking off the cap ball</li>
 *     <li>Move forward 3 tiles</li>
 *     <li>Rotate to be parallel with the beacons</li>
 *     <li>Move forward to the farther line, to press the first beacon</li>
 *     <li>Move backward to the closer line, to press the second beacon</li>
 *     <li>Continue backward onto the Corner Vortex</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kesley
 * @version 1.0, 12/18/2016
 */
@Autonomous(name = "2856 Autonomous")
public class Autonomous2856 extends LinearVisionOpMode {
    I2cDeviceSynch imu;
    DcMotor[] motors;
    PIDController pidController;
    ColorSensor colorSensor1;
    ColorSensor colorSensor2;
    DcMotor shooter;

    private String sideText;
    private int side; //1 for blue, -1 for red because everything will be flipped this is now determined by a file... see the beginning of runOpMode()
    String colorTargetIsRight;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        Utility.InitMotors(hardwareMap, motors);
        colorSensor1 = hardwareMap.colorSensor.get("cs1");
        colorSensor2 = hardwareMap.colorSensor.get("cs2");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);

        pidController = new PIDController(imu, motors, colorSensor1, colorSensor2, telemetry);
        pidController.SetPIDConstatns(63f, 10f, 0f, 50f);
        pidController.SetDefaultMultipliers();

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

        initVision();
        waitForStart();

        pidController.LinearMove(0f, 0.5f);
        pidController.AngularTurn((-25*side)%360, 2);
        shooter.setPower(1);
        Thread.sleep(2000);
        shooter.setPower(0);
        pidController.AngularTurn(0, 2);
        pidController.LinearMove(0f, 0.625f);
        pidController.AngularTurn(55f, 2);
        pidController.LinearMove(0f, 1f);
        pidController.AngularTurn(0f, 2);
        pidController.LinearMove(0f, 0.3f);

        pidController.MoveToLine(1.0f, 0f);
        Thread.sleep(500);
        pidController.MoveToLine(0.5f, 180f);

        telemetry.addData("beacon is: ", beacon.getAnalysis().getColorString());
        telemetry.update();
        if(side == -1) { // if on the red side
            pidController.LinearMove(0f, 0.1f);
        } else {
            pidController.LinearMove(180f, 0.14f);
        }
        Thread.sleep(1000);
        //in front of first beacon: decide color, shift accordingly, and move in
        if(beacon.getAnalysis().getColorString().equals(colorTargetIsRight)) { //target color is right
            telemetry.addData("beacon ", "right");
            telemetry.update();
            if(side == 1) { // if we are on blue side we need a little bump forwards to press but not on red side
                pidController.LinearMove(0f,0.1f);
            }
        } else if (beacon.getAnalysis().getColorString().equals("???, ???")) {
            //do nothing
        } else { //target is left
            telemetry.addData("beacon ", "left");
            telemetry.update();
            if(side == 1) { // for blue side
                pidController.LinearMove(0f, 0.23f);
            } else { // red side
                pidController.LinearMove(180f, 0.15f);
            }
        }
        pidController.LinearMove((90f*side)%360, 1f, 4); //this will timeout, intentional
        pidController.LinearMove((90f*-side)%360, 0.45f, 10);

        //straight to clear existing line
        pidController.LinearMove(180f, 1f, 10);
        pidController.MoveToLine(0.4f, 180f);
        Thread.sleep(500);
        pidController.MoveToLine(0.4f, 0f);

        telemetry.addData("beacon is: ", beacon.getAnalysis().getColorString());
        telemetry.update();
        if(side == -1) { // if on the red side
            pidController.LinearMove(0f, 0.1f);
        } else {
            pidController.LinearMove(180f, 0.14f);
        }
        Thread.sleep(1000);
        //in front of second beacon: decide color, shift accordingly, and move in
        if(beacon.getAnalysis().getColorString().equals(colorTargetIsRight)) { //target color is right
            telemetry.addData("beacon ", "right");
            telemetry.update();
            if(side == 1) { // if we are on blue side we need a little bump forwards to press but not on red side
                pidController.LinearMove(0f,0.1f);
            }
        } else if (beacon.getAnalysis().getColorString().equals("???, ???")) {
            //do nothing
        } else { //target is left
            telemetry.addData("beacon ", "left");
            telemetry.update();
            if(side == 1) { // for blue side
                pidController.LinearMove(0f, 0.23f);
            } else { // red side
                pidController.LinearMove(180f, 0.15f);
            }
        }
        pidController.LinearMove((90f*side)%360, 1f, 4); //this will timeout, intentional
        pidController.LinearMove((90f*-side)%360, 0.45f, 10);

        pidController.LinearMove(180f, 1.3f);
    }

    private void initVision() {
        setCamera(Cameras.PRIMARY);
        setFrameSize(new Size(900,900));
        enableExtension(Extensions.BEACON);
        enableExtension(Extensions.ROTATION);
        enableExtension(Extensions.CAMERA_CONTROL);
        beacon.setAnalysisMethod(Beacon.AnalysisMethod.FAST);
        beacon.setColorToleranceRed(0);
        beacon.setColorToleranceBlue(0);
        rotation.setIsUsingSecondaryCamera(false);
        rotation.disableAutoRotate();
        rotation.setActivityOrientationFixed(ScreenOrientation.PORTRAIT);
        cameraControl.setColorTemperature(CameraControlExtension.ColorTemperature.AUTO);
        cameraControl.setAutoExposureCompensation();
    }
}
