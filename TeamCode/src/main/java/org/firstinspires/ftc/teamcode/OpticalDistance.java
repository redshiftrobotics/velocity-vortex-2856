package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Duncan on 2/11/2017.
 */
@Autonomous(name = "Optical Distance")
public class OpticalDistance extends LinearOpMode{

    OpticalDistanceSensor ODS;

    @Override
    public void runOpMode() throws InterruptedException {
        ODS = hardwareMap.opticalDistanceSensor.get("csb");

        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("Light", ODS.getLightDetected() * 1024);
            telemetry.update();
        }
    }
}
