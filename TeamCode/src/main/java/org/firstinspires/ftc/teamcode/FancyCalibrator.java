package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbI2cController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;

/**
 * Created by adam on 1/24/17.
 */
@Disabled
@Autonomous(name = "Calibrate Color Sensor")
public class FancyCalibrator extends LinearOpMode {

    public byte[] range1Cache;
    public I2cDevice RANGE1;
    public I2cDeviceSynch RANGE1Reader;
    @Override
    public void runOpMode() throws InterruptedException {
        RANGE1 = hardwareMap.i2cDevice.get("r");

        ModernRoboticsI2cColorSensor cs = (ModernRoboticsI2cColorSensor) hardwareMap.colorSensor.get("bs");
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("cs: ", "blue: " +  cs.blue());
            telemetry.addData("red: ", cs.red());
            cs.enableLed(false);

            telemetry.update();

            range1Cache = RANGE1Reader.read(0x4, 2);

        }
       RANGE1Reader = new I2cDeviceSynchImpl(RANGE1, new I2cAddr(0x14), false);

        RANGE1Reader.engage();
    }
}
