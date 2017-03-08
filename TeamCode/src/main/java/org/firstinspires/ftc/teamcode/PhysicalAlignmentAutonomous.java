package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

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
 * Created by Duncan on 2/11/2017.
 */
@Disabled
@Autonomous(name = "Physical Alignment Autonomous")
public class PhysicalAlignmentAutonomous extends LinearVisionOpMode{

    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor csf;
    ColorSensor csb;
    UltrasonicSensor us;
    ModernRoboticsI2cColorSensor bs;

    private String sideText;

    private String sideColor;
    private String color;

    private boolean beacon1 = false;
    private boolean beacon2 = false;

    private int side; //1 for blue, -1 for red because everything will be flipped this is now determined by a file... see the beginning of runOpMode()

    DcMotor shooter;

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
            sideColor = "Red";
        } else if (sideText.equals("blue")) {
            //the string for which the color you want to press is on the right... so for a blue auto it would be "red, blue" and for red it would be "blue, red"
            colorTargetIsRight = "red, blue";
            side = 1;
            sideColor = "Blue";
        }

        waitForVisionStart();
        initVision();
        initDevices();

        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};

        waitForStart();

        //Phase 1:
        //1: Move away from wall
        straightConst();
        robot.Straight(.1f, forward, 1, telemetry);

        //2: Turn to follow corner vortex
        turnConst();
        robot.AngleTurn(45f * side, 1, telemetry);

        //3: Move along corner vortex
        straightConst();
        robot.Straight(0.5f, forward, 2, telemetry);

        //4: Turn to be parallel to wall
        turnConst();
        robot.AngleTurn(-45f * side, 1, telemetry);

        //5A: Track to line
        straightConst();
        //robot.MoveToLine(forward, csf, 0.4f, 2, telemetry);

        //5B: Track closer to line
        //robot.MoveToLine(backward, csf, 0.25f, 2, telemetry);

        //6: Turn to beacon
        turnConst();
        robot.AngleTurn(90f * side, 2, telemetry);

        //7A: Press beacon
        straightConst();
        robot.Straight(0.3f, forward, 1, telemetry);

        //7B: Detect beacon color
        if(bs.red()>bs.blue()){
            telemetry.addData("Red", bs.red());
            color = "Red";
        }else if(bs.blue()>bs.red()){
            telemetry.addData("Blue", bs.blue());
            color = "Blue";
        }else{
            color = "N/A";
        }
        if(sideColor==color){
            beacon1 = true;
        }

        //8: Back off from beacon
        robot.Straight(0.3f, backward, 1, telemetry);

        //9: Turn to be parallel with wall
        turnConst();
        robot.AngleTurn(-90f * side, 2, telemetry);

        //10: Follow wall to closer to the line
        straightConst();
        robot.Straight(1.3f, forward, 1, telemetry);

        //11A: Track to line
        //robot.MoveToLine(forward, csf, 0.4f, 2, telemetry);

        //11B: Track closer to line
        //robot.MoveToLine(backward, csf, 0.25f, 2, telemetry);

        //12: Turn to beacon
        turnConst();
        robot.AngleTurn(90f * side, 2, telemetry);

        //13A: Press beacon
        straightConst();
        robot.Straight(0.3f, forward, 1, telemetry);

        //13B: Detect beacon color
        if(bs.red()>bs.blue()){
            telemetry.addData("Red", bs.red());
            color = "Red";
        }else if(bs.blue()>bs.red()){
            telemetry.addData("Blue", bs.blue());
            color = "Blue";
        }else{
            color = "N/A";
        }
        if(sideColor==color){
            beacon2 = true;
        }

        //14: Back off from beacon
        robot.Straight(0.3f, backward, 1, telemetry);

        //Phase 2:
        //Start logic to determine where to go
        if(!beacon1){
            //Phase 2A:
            //1: Move back to first beacon
            straightConst();
            robot.Straight(0.5f, backward, 2, telemetry);

            //2A: Track to line
            //robot.MoveToLine(backward, csf, 0.4f, 1, telemetry);

            //2B: Track closer to line
            //robot.MoveToLine(forward, csf, 0.25f, 1, telemetry);

            //3: Turn to beacon
            turnConst();
            robot.AngleTurn(90f * side, 2, telemetry);

            //4: Press beacon
            straightConst();
            robot.Straight(0.3f, forward, 1, telemetry);

            //5: Back off beacon
            robot.Straight(0.3f, backward, 1, telemetry);

            //6: Turn to be parallel to wall
            turnConst();
            robot.AngleTurn(-90f * side, 2, telemetry);

            //7: Move back to second beacon
            straightConst();
            robot.Straight(0.5f, forward, 2, telemetry);

            //8A: Track to line
            //robot.MoveToLine(forward, csf, 0.4f, 1, telemetry);

            //8B: Track closer to line
            //robot.MoveToLine(backward, csf, 0.25f, 1, telemetry);
        }else{
            //Phase 2B:
            //1: Turn towards center vortex
            turnConst();
            robot.AngleTurn(135f * side, 2, telemetry);

            //2: Move towards center vortex
            straightConst();
            robot.Straight(0.5f, forward, 1, telemetry);

            //3: Shoot balls

            //4: Move back
            robot.Straight(0.5f, backward, 1, telemetry);

            //5: Turn towards beacon
            turnConst();
            robot.AngleTurn(-135f * side, 2, telemetry);
        }

        //Phase 3:
        if(!beacon2){
            //Phase 3A:
            //1: Press beacon
            straightConst();
            robot.Straight(0.3f, forward, 1, telemetry);

            //2: Move off beacon
            robot.Straight(0.3f, backward, 1, telemetry);
        }else if(!beacon1){
            //Phase 3B:
            //1: Turn towards center vortex
            turnConst();
            robot.AngleTurn(135f * side, 2, telemetry);

            //2: Move towards center vortex
            straightConst();
            robot.Straight(0.5f, forward, 1, telemetry);

            //3: Shoot balls

            //4: Move back
            robot.Straight(0.5f, backward, 1, telemetry);

            //5: Turn towards beacon
            turnConst();
            robot.AngleTurn(-135f * side, 2, telemetry);
        }

        //Phase 4:
        //1: Turn to be parallel with wall
        turnConst();
        robot.AngleTurn(90 * side, 2, telemetry);

        //2: Move backward to corner vortex
        straightConst();
        robot.Straight(1.5f, backward, 2, telemetry);
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
        csf = hardwareMap.colorSensor.get("csf");
        csf = hardwareMap.colorSensor.get("csb");
        csb = hardwareMap.colorSensor.get("csFront");
        bs = (ModernRoboticsI2cColorSensor) hardwareMap.colorSensor.get("bs");
        us = hardwareMap.ultrasonicSensor.get("us");
        shooter.setDirection(DcMotor.Direction.REVERSE);
        //hopper = hardwareMap.servo.get("hopper");
        //robot = new Robot(imu, m0, m1, m2, m3, us, telemetry);
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
