package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.ArrayList;

/**
 * Created by adam on 12/16/16.
 */
public class PID {

    float computedAngle; //rename
    float target;
    float P, I, D;
    float PTuning, ITuning, DTuning;
    float[] headings = new float[2];
    int totalRotations;
    float currentTimeDataPoint;

    public BNO055IMU imu;
    private BNO055IMU.Parameters imuParameters;

    private void incrementTotalRotations() {
        totalRotations++;
    }

    private void decrementTotalRotations() {
        totalRotations--;
    }

    public int getTotalRotations() {
        return totalRotations;
    }

    public float getComputedAngle() {
        return computedAngle;
    }

    public void setCurrentTimeDataPoint(float time) {
        currentTimeDataPoint = time;
    }



    ArrayList<Float> DerivativeData;
    ArrayList<Float> IntegralData;

    public LogFile logFile = new LogFile("sdcard/log.file");
    // Constructor
    public PID(I2cDeviceSynch i2cSync) {
        // Init non-primitives
        DerivativeData = new ArrayList<>();
        IntegralData = new ArrayList<>();
        imuInit(i2cSync);
    }

    private void imuInit(I2cDeviceSynch i2cSync) {
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(i2cSync);
        imu.initialize(imuParameters);
    }

    public void clearData() {
        DerivativeData.clear();
        IntegralData.clear();
    }

    public void updateHeadings() {
        Orientation current = imu.getAngularOrientation();
        float firstAngle = current.firstAngle;
        float secondAngle = current.secondAngle;
        headings[0] = headings[1];
        // Then, we assign the new angle heading.
        headings[1] = ((firstAngle * -1) + 180) % 360;

        headings[0] = headings[1];
        // Then, we assign the new angle heading.
        headings[1] = ((secondAngle * -1) + 180) % 360;
    }

    public void updateData() {
        updateHeadings();
        calculateAngles();
        clearData();
    }

    public void setTarget(float val) {
        this.target = val;
    }

    public void addTarget(float angle) {
        target += angle;
    }

    public float getTarget() {
        return target;
    }

    public void setComputedAngle(float angle) {
        computedAngle = angle;
    }

    public void calculateVars(float loopTime) {
        // Append to our data sets.
        IntegralData.add(computedAngle - target);
        DerivativeData.add(computedAngle);

        // Keep IntegralData and DerivativeData from having an exceeding number of entries.
        if (IntegralData.size() > 500){
            IntegralData.remove(0);
        }

        if(DerivativeData.size() > 5) {
            DerivativeData.remove(0);
        }

        // Set our P, I, and D values.
        // `P` will be the ComputedAngle - target
        P = getComputedAngle() - getTarget();

        // `I` will be the average of the IntegralData (Cries softly at the lack of Java8 streams)

        float IntegralAverage = 0;
        for(float value : IntegralData){
            IntegralAverage += value;
        }
        I = IntegralAverage / IntegralData.size();

        // `D` will be the difference of the ComputedAngle and the Derivative average divided by
        // the time since the last loop in seconds multiplied by one plus half of the size of
        // the Derivative data set size.

        float DerivativeAverage = 0;
        for(float value : DerivativeData) {
            DerivativeAverage += value;
        }
        DerivativeAverage /= DerivativeData.size();

        D = (getComputedAngle() - DerivativeAverage) / ((loopTime/1000) * (1 + (DerivativeData.size() / 2)));
    }

    public float calculateAngles() {
        // First we will move the current angle heading into the previous angle heading slot.
        headings[0] = headings[1];
        // Then, we assign the new angle heading.

        //constrain orientation angle values so that they never go from 359 to 0; this kind of
        //change will screw with the implementation currently.
        headings[1] = ((imu.getAngularOrientation().firstAngle * -1) + 180) % 360;


        //logFile.append("Raw IMU: " + Math.abs(hardware.imu.getAngularOrientation().firstAngle) + " " + hardware.imu.getAngularOrientation().secondAngle + " " + hardware.imu.getAngularOrientation().thirdAngle);


        // Finally we calculate a ComputedAngle from the current angle heading.
        setComputedAngle(headings[1] + (getTotalRotations() * 360));

        // Log.e("#####################", "About to increment IMURotations");
        // now we determine if we need to re-calculate the angles.
        //Log.e("############### current", Float.toString(headings[1]));
        // Log.e("############### past", Float.toString(headings[0]));
        if(headings[0] > 300 && headings[1] < 60) {
            Log.e("################# ####", "Adding to IMURotations");
            incrementTotalRotations(); //rotations of 360 degrees
            calculateAngles();
            //} else if(headings[0] < 300 && headings[1] > 60) {
        } else if(headings[0] < 60 && headings[1] > 300) {
            Log.e("#####################", "Subtracting from IMURotations");
            decrementTotalRotations();
            calculateAngles();
        }
        return headings[1];
    }

    public float getCorrectedValue(float currentTime) {
        calculateAngles();
        calculateVars(currentTime);
        return ((I * ITuning) / 2000) + ((P * PTuning) / 2000) + ((D *DTuning) / 2000);
    }

}