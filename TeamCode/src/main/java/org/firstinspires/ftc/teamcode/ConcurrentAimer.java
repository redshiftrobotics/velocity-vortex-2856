package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adam on 3/25/17.
 */

/**
 * This class is used to read from an i2c distance
 * sensor in a concurrent manner. We found that
 * there was simply too much latency in getting readings
 * within the OpMode eventloop, so we outsourced
 * polling the sensor to a thread.
 */

public class ConcurrentAimer {

    /** Distance Sensor instance for
     * fetching ultrasonic sensor data.
     *
     * @see DistanceSensor
     */
    public DistanceSensor sensor;

    /**
     * Pointer to shared AtomicInteger which
     * will be updated with the current distance.
     */
    public final AtomicInteger sharedDistance;

    /**
     * The runnable to use for getting the current distance
     */
    Runnable routine = new Runnable() {
        @Override
        public void run() {
            for (;;) { //infinite loop - relies on interruption to stop.

                sensor.startReading(); //start a new reading
                int distance = sensor.getNextReading(); //get the actual data
                sharedDistance.set(distance); //set the AtomicInteger to the new value
            }
        }
    };

    public Thread thread = new Thread(routine);


    /** Constructor for ConcurrentAimer.
     * <p> Takes a distance sensor instance, and an AtomicInteger
     * to store data in. </p>
     * @param sensor the distance sensor to read from
     * @param muxVar thread safe value to update with new data
     */
    public ConcurrentAimer(I2cDevice sensor, AtomicInteger muxVar) {
        this.sensor = new DistanceSensor(sensor);
        this.sharedDistance = muxVar;
    }

    /**
     * Starts the internal thread.
     */

    public void start() {
        this.thread.start();
    }

    /**
     * Interrupts the internal thread
     */

    public void stop() {
        this.thread.interrupt();
    }
}
