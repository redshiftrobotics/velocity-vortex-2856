package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by adam on 3/17/17.
 */

@TeleOp(name = "sensor_test", group = "test")





public class ultrasonictest extends OpMode {


    public boolean lastPressedB = false;
    public File outFile;
    private FileOutputStream outStream;

    public I2cDevice device;
    public DistanceSensor sensor;

    public long timerOffset = 0;

    public int CUTOFF = 100;


    @Override
    public void loop() {
        if (timerOffset == 0 || System.currentTimeMillis() - timerOffset >= CUTOFF) {
            sensor.startReading();
            int reading = sensor.getNextReading();
            telemetry.addData("US value", Integer.toString(reading));
           // telemetry.addData("Average value", Integer.toString(sensor.getNextReading()));
            telemetry.addData("Unsanitized", Integer.toString(sensor.getUnsanitizedReading(telemetry)));

            if (gamepad1.a) {
                telemetry.addData("Status", "recording");
                writeReading(reading);
            }

            if (gamepad1.b && !lastPressedB) {
                startNewReading();
                lastPressedB = !lastPressedB;
            } else if (!gamepad1.b && lastPressedB) {
                lastPressedB = false;
            }

            timerOffset = System.currentTimeMillis();
        } else {
            telemetry.addData("status", "waiting");
        }
        telemetry.update();
    }

    @Override
    public void init() {
        device = hardwareMap.i2cDevice.get("distance");
        sensor = new DistanceSensor(device);


        outFile = new File("/sdcard/us_output.txt");

        if (!outFile.exists()) {
            try {
                if (!outFile.createNewFile()) {
                    telemetry.addData("failed to create new file!", "!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        sensor.stopSensor();
    }



    public void writeReading(int data) {
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(outFile, true));
            buf.append(Integer.toString(data));
            buf.newLine();
            buf.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startNewReading() {
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(outFile, true));
            buf.append("NEW");
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
