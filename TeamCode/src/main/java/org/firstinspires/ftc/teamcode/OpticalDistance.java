package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Duncan on 2/11/2017.
 */
@Autonomous(name = "Optical Distance")
public class OpticalDistance extends LinearOpMode{

    OpticalDistanceSensor ODS;
    ColorSensor bs;

    @Override
    public void runOpMode() throws InterruptedException {
        ODS = hardwareMap.opticalDistanceSensor.get("csb");
        bs = hardwareMap.colorSensor.get("rbs");

        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("Light", ODS.getLightDetected() * 1024);
            telemetry.addData("Beacon sensor", bs.red() + "|" + bs.green()+ "|" + bs.blue());
            telemetry.update();
        }
    }
}
