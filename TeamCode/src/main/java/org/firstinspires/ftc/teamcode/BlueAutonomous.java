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

/**
 * Created by matt on 11/10/16.
 */
@Autonomous(name = "2856 Blue Autonomous")
public class BlueAutonomous extends LinearVisionOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor cs;

    DcMotor shooter;
    Servo hopper;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForVisionStart();
        initVision();
        initDevices();

        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};

        robot.Data.PID.PTuning = 100f;
        robot.Data.PID.ITuning = 30f;
        robot.Data.PID.DTuning = 0f;
        //hopper.setPosition(0.48);
        waitForStart();
        robot.Straight(1.25f, forward, 10, telemetry);
        //Thread.sleep(1000);
        //hopper.setPosition(0);
        //shooter.setPower(1);
        //Thread.sleep(3000);
        //hopper.setPosition(0.48);
        //shooter.setPower(0);
        robot.Straight(1f, forward, 10, telemetry);
        robot.AngleTurn(45f, 10, telemetry);
        robot.Straight(3f, forward, 10, telemetry);
        robot.AngleTurn(-45f, 10, telemetry);
        //robot.Straight(1f, new Float[]{1f,0f}, 10, telemetry);

        robot.MoveToLine(backward, .65f, 10, telemetry);
        Thread.sleep(1000);
        robot.MoveToLine(forward, .2f, 10, telemetry);

        //in front of first beacon: decide color, shift accordingly, and move in
        if(beacon.getAnalysis().getColorString().equals("red, blue")) { //blue is right
            robot.Straight(0.3f, backward, 10, telemetry);
        } else { //blue is left
            robot.Straight(0.3f, forward, 10, telemetry);
        }
        robot.Straight(1f, new Float[]{0f, -1f}, 10, telemetry);
        robot.Straight(1f, new Float[]{0f, 1f}, 10, telemetry);

        //straight to clear existing line
        robot.Straight(1f, forward, 10, telemetry);
        robot.MoveToLine(forward, .4f, 10, telemetry);
        Thread.sleep(1000);
        robot.MoveToLine(backward, .2f, 10, telemetry);

        //in front of second beacon: decide color, shift accordingly, and move in
        if(beacon.getAnalysis().getColorString().equals("red, blue")) { //blue is right
            robot.Straight(0.3f, backward, 10, telemetry);
        } else { //blue is left
            robot.Straight(0.3f, forward, 10, telemetry);
        }
        robot.Straight(1f, new Float[]{0f, -1f}, 10, telemetry);
        robot.Straight(1f, new Float[]{0f, 1f}, 10, telemetry);

        //go backwards because who cares
        robot.Straight(3f, backward, 10, telemetry);

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
}
