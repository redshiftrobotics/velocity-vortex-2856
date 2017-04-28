package org.firstinspires.ftc.teamcode;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

import static com.qualcomm.robotcore.hardware.I2cDeviceSynch.ReadMode.ONLY_ONCE;

/**
 * Created by adam on 3/13/17.
 */

class DistanceTimer {

    private long start = 0;

    public void start() {
       this.start = System.currentTimeMillis();
    }

    public long end() {
        if (start == 0) return 0;
        else
            return System.currentTimeMillis() - this.start;
    }
}

public class DistanceSensor {

    private DistanceTimer distanceTimer;
    private I2cDeviceSynchImpl sensor;
    private int lastReading = 0;

    private int currentAverage = 0;

    private ArrayList<Integer> previousReadings;

    public static int SENSOR_REGISTER = 0x0;
    public static int DATA_LENGTH = 2;
    public static int WAIT_TIME = 100; //time to wait between measurements in milliseconds
    public static int DEFAULT_ADDRESS = 0x70;
    public static int RANGE_CMD = 0x51;
    public static int CHANGE_ADDR_FIRST_CMD = 170;
    public static int CHANGE_ADDR_SECOND_CMD = 160;

    public static int MAX_DATA_SIZE = 10;






    private static I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(0x0, DATA_LENGTH,  ONLY_ONCE);
    private static I2cAddr distanceSensorAddress = new I2cAddr(DEFAULT_ADDRESS);

    public DistanceSensor(@NonNull I2cDevice device) {
        sensor = new I2cDeviceSynchImpl(device, distanceSensorAddress, false);
        sensor.setReadWindow(readWindow);
        sensor.engage();
        distanceTimer = new DistanceTimer();
        previousReadings = new ArrayList<>();
    }

    public void startReading() {
        if (distanceTimer.end() == 0 || distanceTimer.end() >= WAIT_TIME) {
            sensor.write8(RANGE_CMD, 0x0);
            distanceTimer.start();
        }
    }



    public int getNextReading() {
        if (distanceTimer.end() != 0 && distanceTimer.end() < WAIT_TIME) {
            //return currentAverage;
            return currentAverage;
        }

        sensor.setReadWindow(readWindow);
        byte[] readings = sensor.read(SENSOR_REGISTER, DATA_LENGTH);

        if (previousReadings.size() > MAX_DATA_SIZE) {
            previousReadings.remove(0);
        }

        int distance = (readings[0] << 8) | readings[1];
        lastReading = distance;

        previousReadings.add(distance);

       sanitizeData();

        return currentAverage;
    }



    public void stopSensor() {
        this.sensor.close();
    }

   public int getLastRawReading() {
        return this.lastReading;
    }

    public int getUnsanitizedReading(Telemetry t) {
        if (distanceTimer.end() != 0 && distanceTimer.end() < WAIT_TIME) {
            return this.lastReading;
        }

        t.addData("End: ", distanceTimer.end());
        t.update();
        sensor.setReadWindow(readWindow);
        byte[] readings = sensor.read(SENSOR_REGISTER, DATA_LENGTH);
        int distance = (readings[0] << 8) | readings[1];
        lastReading = distance;

        distanceTimer.start();

        return distance;
    }


    private void sanitizeData() {
        int adjusted = 0;
        int max = previousReadings.get(0);
        int min = previousReadings.get(0);
        boolean ignore70 = false;
        int num_ignored = 0;

        for (int reading : previousReadings) {
            if (reading > max) {
                max = reading;
            } else if (reading < min) {
                min = reading;
            }
        }

        if(max > 84 || min < 69){
            ignore70 = true;
        }


        for (int reading  : previousReadings) {
            if ((ignore70 && reading < 84 && reading > 69) || reading <= 0) {
                num_ignored++;
            } else {
                adjusted += reading;
            }
        }
        if(num_ignored>=MAX_DATA_SIZE){
            adjusted = 0;
        }else {
            adjusted /= (MAX_DATA_SIZE - num_ignored);
        }

        currentAverage = adjusted;
    }
}
