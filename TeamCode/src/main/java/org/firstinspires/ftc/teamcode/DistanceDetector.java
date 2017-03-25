package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.util.Range;

/**
 * Created by adam on 3/13/17.
 */
public class DistanceDetector {
    public DistanceSensor distanceSensor;

    public static ShooterPosition[] positions = {
            new ShooterPosition(0, 0.0)
    };

    public DistanceDetector(DistanceSensor s) throws Exception {
       distanceSensor = s;
    }

    /*private int getIndex(int reading) {
        int i = Range.clip(reading / DISTANCE_INTERVAL, 0, positions.length);
        Log.d("index: ", Integer.toString(i));
        return i;
    }*/

    private int getIndex(int reading) {


        return 0;
    }

    public double getServoAngle() {
        int distance = distanceSensor.getNextReading();
        int index = getIndex(distance);
        return positions[index].servoAngle;
    }
}
