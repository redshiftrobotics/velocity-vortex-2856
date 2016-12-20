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
public class BlueAutonomous /*extends LinearVisionOpMode*/ {
   /* I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor cs;

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

        // Provide in a more user friendly form.
        sideText = text.toString();
        if(sideText.equals("red")) {
            side = -1;
        } else if (sideText.equals("blue")) {
            side = 1;
        }

        waitForVisionStart();
        initVision();
        initDevices();

        Float forward = 0f;
        Float backward = 180f;

        //the string for which the color you want to press is on the right... so for a blue auto it would be "red, blue" and for red it would be "blue, red"
        String colorTargetIsRight = "red, blue";

        robot.setTuning(100, 30, 0);
        //hopper.setPosition(0.48);
        waitForStart();
        robot.Straight(1.25f, forward, 10);

        //Thread.sleep(1000);
        //hopper.setPosition(0);
        //shooter.setPower(1);
        //Thread.sleep(3000);
        //hopper.setPosition(0.48);
        //shooter.setPower(0);
        robot.Straight(1f, forward, 10);
        robot.AngleTurn(55f*side, 10);
        robot.Straight(3.2f, forward, 10);
        robot.AngleTurn(-55f*side, 10);
        //robot.Straight(1f, new Float[]{1f,0f}, 10, telemetry);

        robot.moveToLine(forward, .65f, 10);
        Thread.sleep(1000);
        robot.moveToLine(backward, .2f, 10);

        //in front of first beacon: decide color, shift accordingly, and move in
        if(beacon.getAnalysis().getColorString().equals("red, blue")) { //blue is right
            robot.Straight(0.3f, backward, 10);
        } else { //blue is left
            robot.Straight(0.3f, forward, 10);
        }
        robot.Straight(.7f, 270, 10);
        robot.Straight(.7f, 90, 10);

        //straight to clear existing line
        robot.Straight(1f, backward, 10);
        robot.moveToLine(backward, .4f, 10);
        Thread.sleep(1000);
        robot.moveToLine(forward, .2f, 10);

        //in front of second beacon: decide color, shift accordingly, and move in
        if(beacon.getAnalysis().getColorString().equals(colorTargetIsRight)) { //blue is right
            robot.Straight(0.3f, backward, 10);
        } else { //blue is left
            robot.Straight(0.3f, forward, 10);
        }
        robot.Straight(.7f, 270, 10);
        robot.Straight(.7f, 90, 10);

        //go backwards because who cares
        robot.Straight(5f, backward, 10);

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

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        //shooter = hardwareMap.dcMotor.get("shooter");
        cs = hardwareMap.colorSensor.get("cs");
        //shooter.setDirection(DcMotor.Direction.REVERSE);
        //hopper = hardwareMap.servo.get("hopper");
        robot = new Robot(imu, m0, m1, m2, m3, cs, telemetry);
    }
    */
}
