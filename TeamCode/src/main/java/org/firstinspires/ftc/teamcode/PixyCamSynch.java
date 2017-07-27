package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceReader;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

@Autonomous(name = "PixySynch", group = "Sensor")
public class PixyCamSynch extends LinearOpMode {
    I2cDeviceSynch pixy;
    byte[] readCache;
    pixyObject PixyObject = new pixyObject();

    int zeroCheck = 0;

    DcMotor trackingMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        pixy = hardwareMap.i2cDeviceSynch.get("pixy");
        pixy.engage();
        trackingMotor = hardwareMap.dcMotor.get("tracking");

        waitForStart();

        while (opModeIsActive()) {
            readCache = pixy.read(0x54, 26);

            zeroCheck = 0;
            for(int i = 0; i< 16; i++){
                zeroCheck+=readCache[i];
            }

            if((readCache[1]==-86&&readCache[2]==85)){
                PixyObject.UpdateObject(readCache);
                telemetry.addData("Tracking", "True");
                telemetry.addData("Sig", PixyObject.signature + ", x: " + PixyObject.xCenter + ", y: " + PixyObject.yCenter + ", w: " + PixyObject.width + ", h: " + PixyObject.height);

                trackingMotor.setPower(((float)PixyObject.xCenter)/1000f);
            }else if(zeroCheck==0){
                telemetry.addData("Sig", PixyObject.signature + ", x: " + PixyObject.xCenter + ", y: " + PixyObject.yCenter + ", w: " + PixyObject.width + ", h: " + PixyObject.height);
                telemetry.addData("Tracking", "False");
                trackingMotor.setPower(0);
            }

            telemetry.update();
        }
    }
}