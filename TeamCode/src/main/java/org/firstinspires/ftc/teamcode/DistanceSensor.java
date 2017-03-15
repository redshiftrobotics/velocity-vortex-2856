package org.firstinspires.ftc.teamcode;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by adam on 3/13/17.
 */
public class DistanceSensor {

    public I2cDeviceSynchImpl sensor;
    public static int SENSOR_REGISTER = 0x0;
    public static int address = 0x0;
    public static int BYTES = 2; //number of bytes to read
    public static int DATA_ARRAY_OFFSET = 0;
    public static int UPPER_DATA_BOUND = 1 << 8; //may be 1 << 7

    public I2cAddr distanceSensorAddress = new I2cAddr(address);

    public DistanceSensor(@NonNull I2cDevice device) {
        sensor = new I2cDeviceSynchImpl(device, distanceSensorAddress, false);
        sensor.engage();
    }

    public int getNextReading() {
        byte[] reading = sensor.read(SENSOR_REGISTER, BYTES);
        int distance = reading[DATA_ARRAY_OFFSET] & 0xFF;

        if (!validReading(distance)) {
            return -1;
        }

        return distance;
    }

    public boolean validReading(int val) {
       return val != UPPER_DATA_BOUND;
    }

}
