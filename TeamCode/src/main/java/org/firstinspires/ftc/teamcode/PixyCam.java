package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by Duncan on 7/7/2017.
 */

@Autonomous(name="pixy")
public class PixyCam extends LinearOpMode{
    I2cDevice pixy;
    I2cDeviceSynch pixyReader;
    I2cAddr pixyAddress = I2cAddr.create7bit(0x54);
    byte[] readCache;

    @Override
    public void runOpMode() throws InterruptedException {
        pixy = hardwareMap.i2cDevice.get("pixy");
        pixyReader = new I2cDeviceSynchImpl(pixy, pixyAddress, false);
        pixyReader.engage();

        waitForStart();

        while(opModeIsActive()) {
            readCache = pixyReader.read(0x54, 100);

            telemetry.addData("1", readCache[1]);
            telemetry.addData("2", readCache[2]);
            telemetry.addData("6", readCache[6]);
            telemetry.addData("7", readCache[7]);
            telemetry.addData("8", readCache[8]);
            telemetry.addData("9", readCache[9]);
            telemetry.addData("isInReadMode", pixy.isI2cPortInReadMode());
            telemetry.update();
        }
    }
}