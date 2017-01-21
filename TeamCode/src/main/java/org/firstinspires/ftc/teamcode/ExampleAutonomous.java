package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.opmode.VisionOpMode;
import org.lasarobotics.vision.opmode.extensions.CameraControlExtension;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Size;

/**
 * Created by matt on 10/15/16.
 */
@Autonomous(name = "ExampleAutonomous", group = "pid-test")
public class ExampleAutonomous extends LinearVisionOpMode {
    UltrasonicSensor us;

    @Override
    public void runOpMode() throws InterruptedException {
        us = hardwareMap.ultrasonicSensor.get("us");

        //robot = new Robot(imu, m0, m1, m2, m3, cs, cs1, telemetry);

        //working PIDs
        //P: 100
        //I: 30
        //D: 0

        //loop
        //Float[] forward = new Float[]{1f,0f};
        //Float[] backward = new Float[]{-1f,0f};
//        robot.Data.PID.PTuning = 50f;
//        robot.Data.PID.ITuning = 0f;
//        robot.Data.PID.DTuning = 0f;
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("Distance", String.valueOf(us.getUltrasonicLevel()));
            telemetry.update();
        }
        //telemetry.addData("beacon", beacon.getAnalysis().getColorString());
        //robot.Push(5f, new Float[]{0f,-1f}, 7, telemetry);
//        robot.AngleTurn(45f, 4, telemetry);
//        robot.Data.PID.PTuning = 100f;
//        robot.Data.PID.ITuning = 30f;
//        robot.Data.PID.DTuning = 0f;
        //robot.MoveToLine(forward, 0.2f, 10, telemetry);

    }


    private void initVision() {
        setCamera(Cameras.SECONDARY);
        new Size();
        setFrameSize(new Size(1440,2560));
        enableExtension(VisionOpMode.Extensions.BEACON);
        enableExtension(VisionOpMode.Extensions.ROTATION);
        enableExtension(VisionOpMode.Extensions.CAMERA_CONTROL);
        beacon.setAnalysisMethod(Beacon.AnalysisMethod.COMPLEX);
        beacon.setColorToleranceRed(0);
        beacon.setColorToleranceBlue(0);
        rotation.setIsUsingSecondaryCamera(false);
        rotation.disableAutoRotate();
        rotation.setActivityOrientationFixed(ScreenOrientation.PORTRAIT);
        cameraControl.setColorTemperature(CameraControlExtension.ColorTemperature.AUTO);
        cameraControl.setAutoExposureCompensation();
    }
    enum TuneState {
        P, I, D
    }
}
