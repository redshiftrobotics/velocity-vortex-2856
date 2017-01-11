package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by matt on 1/11/17.
 */

@Autonomous(name = "Calibrator")
public class Calibrator extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ColorSensor cs = hardwareMap.colorSensor.get("cs");
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("cs", (cs.red() + cs.blue() + cs.green())/3);
            telemetry.update();
        }
    }
}
