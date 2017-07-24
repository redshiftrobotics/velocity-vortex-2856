package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceReader;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by Michael on 6/6/2017.
 */
@Autonomous(name = "Pixy", group = "Sensor")
public class PixyCam extends LinearOpMode {
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
            readCache = pixyReader.read(0x54, 26);

//            telemetry.addData("0", readCache[0]);
//            telemetry.addData("1", readCache[1]);
//            telemetry.addData("2", readCache[2]);
//            telemetry.addData("3", readCache[3]);
//            telemetry.addData("4", readCache[4]);
//            telemetry.addData("5", readCache[5]);
//            telemetry.addData("6", readCache[6]);
//            telemetry.addData("7", readCache[7]);
//            telemetry.addData("isInReadMode", pixy.isI2cPortInReadMode());

            if(readCache[6]==1){
                telemetry.addData("red", 1);
                telemetry.addData("XC", readCache[8]);
                telemetry.addData("YC", readCache[10]);
                telemetry.addData("XW", readCache[12]);
                telemetry.addData("YW", readCache[14]);
            }else if(readCache[6]==2){
                telemetry.addData("blue", 1);
                telemetry.addData("XC", readCache[8]);
                telemetry.addData("YC", readCache[10]);
                telemetry.addData("XW", readCache[12]);
                telemetry.addData("YW", readCache[14]);
            }

            telemetry.update();
        }
    }
}