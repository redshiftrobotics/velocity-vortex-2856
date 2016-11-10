package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

/**
 * Created by Duncan on 11/5/2016.
 */

@Autonomous(name = "VuforiaAuto")
public class AutonomousVuforia extends LinearOpMode {

    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;

    @Override
    public void runOpMode() throws InterruptedException {
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");

        Tracking.Setup(Tracking.ImageType.Wheels, VuforiaLocalizer.CameraDirection.BACK);
        waitForStart();
        while(opModeIsActive()) {
            while (!Tracking.listener.isVisible()) {
                Tracking.moveVert(0.3f, m0, m1, m2, m3);
            }
            Tracking.moveVert(0, m0, m1, m2, m3);
            Tracking.Align(m0,m1,m2,m3);
        }
    }
}
