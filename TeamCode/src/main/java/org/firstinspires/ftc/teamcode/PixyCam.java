package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceReader;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import java.util.ArrayList;

/**
 * Created by Michael on 6/6/2017.
 */
@Autonomous(name = "Pixy", group = "Sensor")
public class PixyCam extends LinearOpMode {
    I2cDevice pixy;
    I2cDeviceSynch pixyReader;
    I2cAddr pixyAddress = I2cAddr.create7bit(0x54);
    byte[] readCache;

    pixyObject PixyObject = new pixyObject();

    int zeroCheck = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        pixy = hardwareMap.i2cDevice.get("pixy");
        pixyReader = new I2cDeviceSynchImpl(pixy, pixyAddress, false);
        pixyReader.engage();

        waitForStart();

        while (opModeIsActive()) {
            readCache = pixyReader.read(0x54, 26);

            zeroCheck = 0;
            for(int i = 0; i< 16; i++){
                zeroCheck+=readCache[i];
            }

            if((readCache[1]==-86&&readCache[2]==85)){
                PixyObject.UpdateObject(readCache);
                telemetry.addData("Tracking", "True");
                telemetry.addData("Sig", PixyObject.signature + ", x: " + PixyObject.xCenter + ", y: " + PixyObject.yCenter + ", w: " + PixyObject.width + ", h: " + PixyObject.height);
            }else if(zeroCheck==0){
                telemetry.addData("Sig", PixyObject.signature + ", x: " + PixyObject.xCenter + ", y: " + PixyObject.yCenter + ", w: " + PixyObject.width + ", h: " + PixyObject.height);
                telemetry.addData("Tracking", "False");
            }



//            telemetry.addData("0", ((readCache[0] < 0) ? readCache[0] + 256 : readCache[0]));
//            telemetry.addData("1", ((readCache[1] < 0) ? readCache[1] + 256 : readCache[1]));
//            telemetry.addData("2", ((readCache[2] < 0) ? readCache[2] + 256 : readCache[2]));
//            telemetry.addData("3", ((readCache[3] < 0) ? readCache[3] + 256 : readCache[3]));
//            telemetry.addData("4", ((readCache[4] < 0) ? readCache[4] + 256 : readCache[4]));
//            telemetry.addData("5", ((readCache[5] < 0) ? readCache[5] + 256 : readCache[5]));
//            telemetry.addData("6", ((readCache[6] < 0) ? readCache[6] + 256 : readCache[6]));
//            telemetry.addData("7", ((readCache[7] < 0) ? readCache[7] + 256 : readCache[7]));
//            telemetry.addData("8", ((readCache[8] < 0) ? readCache[8] + 256 : readCache[8]));
//            telemetry.addData("9", ((readCache[9] < 0) ? readCache[9] + 256 : readCache[9]));
//            telemetry.addData("10", ((readCache[10] < 0) ? readCache[10] + 256 : readCache[10]));
//            telemetry.addData("11", ((readCache[11] < 0) ? readCache[11] + 256 : readCache[11]));
//            telemetry.addData("12", ((readCache[12] < 0) ? readCache[12] + 256 : readCache[12]));
//            telemetry.addData("13", ((readCache[13] < 0) ? readCache[13] + 256 : readCache[13]));
//            telemetry.addData("14", ((readCache[14] < 0) ? readCache[14] + 256 : readCache[14]));
//            telemetry.addData("15", ((readCache[15] < 0) ? readCache[15] + 256 : readCache[15]));
//            telemetry.addData("15", ((readCache[15] < 0) ? readCache[15] + 256 : readCache[15]));

            telemetry.update();
        }
    }

}

class pixyObject{
    int signature = 0;
    int xCenter = 0; //xCenter overall is 160
    int yCenter = 0; //yCenter overall is 100
    int width = 0; //width max is 320
    int height = 0; //height max is 200

    void UpdateObject(byte[] cache){
        signature = cache[7] * 256 + ((cache[6] < 0) ? cache[6] + 256 : cache[6]);
        xCenter = cache[9] * 256 + ((cache[8] < 0) ? cache[8] + 256 : cache[8]);
        yCenter = cache[11] * 256 + ((cache[10] < 0) ? cache[10] + 256 : cache[10]);
        width = cache[13] * 256 + ((cache[12] < 0) ? cache[12] + 256 : cache[12]);
        height = cache[15] * 256 + ((cache[14] < 0) ? cache[14] + 256 : cache[14]);
    }
}