package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adam on 3/25/17.
 */

public class ConcurrentAimer {

    public DistanceSensor sensor;
    public final AtomicInteger sharedDistance;

    Runnable routine = new Runnable() {
        @Override
        public void run() {
            for (;;) {
                sensor.startReading();
                int distance = sensor.getNextReading();
                synchronized (sharedDistance) {
                    sharedDistance.set(distance);
                }
            }
        }
    };

    public Thread thread = new Thread(routine);


    public ConcurrentAimer(I2cDevice d, AtomicInteger i) {
        this.sensor = new DistanceSensor(d);
        this.sharedDistance = i;
    }

    public void start() {
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }
}
