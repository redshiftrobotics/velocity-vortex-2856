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

/**
 * A simple utility timer class.
 */

class Timer {

    private long start = 0;

    public void start() {
       this.start = System.currentTimeMillis();
    }

    /** Stops the timer and returns the time offset
     *
     * @return the time since start() was called.
     */

    public long end() {
        if (start == 0) return 0;
        else
            return System.currentTimeMillis() - this.start;
    }
}

/**
 * Maxbotics i2c distance sensor implementation. Based on
 * https://github.com/hotwired/hotwired2016/blob/master/TeamCode/src/main/java/com/hotwired/sensors/MaxSonarI2CXLRanger.java
 *
 *
 */
public class DistanceSensor {

    //internal timer for storing time between readings
    private Timer timer;
    private I2cDeviceSynchImpl sensor; //internal reference to the actual device
    private int lastReading = 0; //meant to hold the last sensor reading

    private int currentAverage = 0; //holds the current window average of the past 10 readings

    private ArrayList<Integer> previousReadings; //current read window. Always stays 10 readings long

    public static int SENSOR_REGISTER = 0x0; //the sensor register to read from. In this case,
    //the beginning of sensor memory.
    public static int DATA_LENGTH = 2; //the number of bytes to read from the sensor.
    public static int WAIT_TIME = 100; //time to wait between measurements in milliseconds
    public static int DEFAULT_ADDRESS = 0x70; //the default Maxbotics sensor i2c address.
    public static int RANGE_CMD = 0x51; //The command to write to the sensor to initiate reading


    public static int MAX_DATA_SIZE = 10; //the maximum length of previousReadings





    //sets a read window -- a convenient way to read from adjacent registers.
    private static I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(0x0, DATA_LENGTH,  ONLY_ONCE);
    //instantiate the actual address object.
    private static I2cAddr distanceSensorAddress = new I2cAddr(DEFAULT_ADDRESS);

    /** Constructor which takes a distance sensor as the only argument.
     * Do not instantiate with a different device. It won't work!
     *
     * @param device I2cDevice reference to use as a distance sensor.
     */
    public DistanceSensor(@NonNull I2cDevice device) {

        //instantiate sensor
        sensor = new I2cDeviceSynchImpl(device, distanceSensorAddress, false);
        //set the read window
        sensor.setReadWindow(readWindow);
        //ENGAGE!
        sensor.engage();
        //init other references...
        timer = new Timer();
        previousReadings = new ArrayList<>();
    }

    /**
     * Writes the reading start command to the sensor command register to
     * begin reading
     */
    public void startReading() {
        if (timer.end() == 0 || timer.end() >= WAIT_TIME) {
            sensor.write8(RANGE_CMD, 0x0);
            timer.start();
        }
    }


    /**
     *
     * This method
     * @see DistanceSensor#startReading()
     * must be called in order for reading to work.
     *
     * @return the next distance reading, as an integer
     */



    public int getNextReading() {
        if (timer.end() != 0 && timer.end() < WAIT_TIME) {
            //return currentAverage;
            return currentAverage;
        }

        sensor.setReadWindow(readWindow);
        byte[] readings = sensor.read(SENSOR_REGISTER, DATA_LENGTH);

        if (previousReadings.size() > MAX_DATA_SIZE) {
            previousReadings.remove(0);
        }

        /*
        Bit shifting? IN JAVA? I thought programming in java was supposed to get
        rid of this madness!

        NEVER! EMBRACE THE RAW POWER!!!

        (In case you're wondering, the below bit twiddling OR's
        a big endian 2 byte array together into one integer).
         */

        int distance = (readings[0] << 8) | readings[1];

        //set lastReading accordingly
        lastReading = distance;

        //add to our arraylist in order to compute a  moving average.

        previousReadings.add(distance);

        //sanitize readings and compute an average.
        sanitizeData();


        //return our currentAverage

        return currentAverage;
    }



    public void stopSensor() {
        this.sensor.close();
    }

   public int getLastRawReading() {
        return this.lastReading;
    }

    /**
     *
     * @param t telemetry object for debugging purposes.
     * @return the unsanitized reading (i.e., without the moving average)
     */
    public int getUnsanitizedReading(Telemetry t) {

        //return last reading if it's the first time, or we haven't waited long enough
        if (timer.end() != 0 && timer.end() < WAIT_TIME) {
            return this.lastReading;
        }

        t.addData("End: ", timer.end()); //
        t.update();

        sensor.setReadWindow(readWindow);
        byte[] readings = sensor.read(SENSOR_REGISTER, DATA_LENGTH);

        int distance = (readings[0] << 8) | readings[1];
        lastReading = distance;

        timer.start();

        return distance;
    }


    private void sanitizeData() {
        int adjusted = 0; //our adjusted average
        int max = previousReadings.get(0); //computed max
        int min = previousReadings.get(0); //computed min
        boolean ignore70 = false; //should we ignore data from (69, 84) ?
        int numIgnored = 0;

        for (int reading : previousReadings) { //iterate through previous readings...
            if (reading > max) { //set max to current max
                max = reading;
            } else if (reading < min) { //otherwise set min to current min...
                min = reading;
            }
        }

        if (max > 84 || min < 69) { // if our MAX data is greater than the 70 range, ignore it.
            ignore70 = true;
        }


        for (int reading : previousReadings) { //iterate through previous readings...
            if ((ignore70 && reading < 84 && reading > 69) || reading <= 0) { //if we have a bad reading...
                numIgnored++; //increment counter so we can still get an accurate average
            } else {
                adjusted += reading; //add the reading to our adjusted average
            }
        }
        if (numIgnored >= MAX_DATA_SIZE) { //if we've ignored all our data...
            adjusted = 0;
        }else {
            adjusted /= (MAX_DATA_SIZE - numIgnored); //compute average with the number of values we
            //actually added to our adjusted total.
        }

        currentAverage = adjusted; //set currentAverage to adjusted.
    }
}
