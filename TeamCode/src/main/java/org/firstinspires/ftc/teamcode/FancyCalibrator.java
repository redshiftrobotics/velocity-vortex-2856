package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbI2cController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;

/**
 * Created by adam on 1/24/17.
 */
@Disabled
@Autonomous(name = "Calibrate Color Sensor")
public class FancyCalibrator extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        ModernRoboticsI2cColorSensor cs = (ModernRoboticsI2cColorSensor) hardwareMap.colorSensor.get("bs");
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("cs: ", "blue: " +  cs.blue());
            telemetry.addData("red: ", cs.red());
            cs.enableLed(false);

            telemetry.update();
        }
    }
}
