package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by adam on 12/16/16.
 * A further refactor of original, for more streamlined design,
 * and better long term usability.
 */


public class PIDController {
    float computedAngle; //current computed robot angle, constrained by constrainAngleRange
    float target; //current target, the value we are trying to correct to.
    float P, I, D; /*current p, i, and d variables,
    holding current proportional, integral, and derivative proportional calculations. */
    float PTuning, ITuning, DTuning; //Tuning constants for P, I, and D
    float[] headings = new float[2]; /* Headings - holds two points of angle data.
    Why two? So we can see the difference between the current angle and the last angle, which
    is important to tell if we've gone over certain thresholds*/
    int totalRotations; /* total rotations is the number of times the robot has done a
    360 degree turn. multiplying this by 360 and adding it to the current imu angle yields the
     robot's actual orientation relative to the starting position.*/

    public float currentError = 0f, lastError = 0f;

    public BNO055IMU imu;


    private void incrementTotalRotations() {
        totalRotations++;
    }

    private void decrementTotalRotations() {
        totalRotations--;
    }

    private int getTotalRotations() {
        return totalRotations;
    }

    /**
     *
     * @return computedAngle - the current range constrained angle being used
     */

    public float getComputedAngle() {
        return computedAngle;
    }

    //Historical data for calculating rates over time for integral and derivative variables.

    //utility logfile
    public LogFile logFile = new LogFile("sdcard/log.file");
    // Constructor


    /** Constructor
     *
     * @param i2cSync an I2cDeviceSynch referencing an imu.
     */

    public PIDController(I2cDeviceSynch i2cSync) {
        //initialize the imu
        imuInit(i2cSync);
    }

    private void imuInit(I2cDeviceSynch i2cSync) {
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(i2cSync);
        imu.initialize(imuParameters);
    }

    /**
     * Clears P, I, and D constants. Should be called at the beginning
     * of every movement function. The only value that is absolutely necessary to zero out
     * it I, since it is a running total, but P and D are cleared for consistency
     *
     *
     */

    public void clearHistoricData() {
        P = 0;
        I = 0;
        D = 0;
    }

    /** Set the tuning constants
     *
     * @param pTune P tuning constant
     * @param iTune I tuning constant
     * @param dTune D tuning constant
     */

    public void setTuning(float pTune, float iTune, float dTune) {
        PTuning = pTune;
        ITuning = iTune;
        DTuning = dTune;
    }

    /**
     *
     * @param angle the angle to constrain
     * @return a negated, constrained version of the angle.
     */
    public float constrainAngleRange(float angle) {
        return ((angle * -1) + 180) % 360;
    }

    /**
     * adds to the current target. Must be called in
     * turning functions to update the PID target by the number of degrees the robot
     *  has to turn.
     *
     * @param angle the vale to add to the current target
     */

    public void addTarget(float angle) {
        target += angle;
        target = target % 360;
    }

    /** Method to manually set target value. Not used, but potentially necessary for some
     * implementations
     *
     * @param target the new target value.
     */
    public void setTarget(float target) {
        this.target = target;
    }

    /**
     * Default initialization method, sets the current target to the computed angle.
     */
    /*public void initializeTarget() {
        target = calculateAngles();
    }*/

    public void initializeTarget() {
        target = getCurrentImuAngle();
    }


    /** Returns the current target.
     *
     * @return the current target
     */
    public float getTarget() {
        return target;
    }

    /** Sets the current computed target value
     *
     * @param angle the angle to set the computed angle to
     */
    public void setComputedAngle(float angle) {
        computedAngle = angle;
    }

    /** Calculates the proportional, integral, and derivative terms
     *
     *
     * @param deltaT Time since the last iteration of the loop, i.e., currentTime - previousTime
     */

    public void calculateVars(float deltaT) {
        calculateError();
        P = getCurrentError();
        I += getCurrentError() * deltaT / 1000; //Riemann sum
        D = (getCurrentError() - getLastError()) / (deltaT / 1000);

        Log.d("Pid values ", "P: " + Float.toString(P) +" D: " + Float.toString(D) + " I: " + Float.toString(I));
    }

    /** Calculates computed angle value, updating the number of full rotations accordingly, and accounting
     * for this in calculations.
     * @return the newest angular orientation data point
     */
    /*public float calculateAngles() {
        //assign angle headings
        updateHeadings();
        logFile.append("Raw IMU: " + Math.abs(imu.getAngularOrientation().firstAngle) + " " + imu.getAngularOrientation().secondAngle + " " + imu.getAngularOrientation().thirdAngle);

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
    }*/

    public float getCurrentImuAngle() {
        return imu.getAngularOrientation().firstAngle * -1;
    }

    /** Updates currentError field within the corresponding
     * object. Must be called in order for the value
     * returned by {@link #getCurrentError()}
     *
     */
    public void calculateError() {
        lastError = currentError;
        Log.d("Current Error: ", Float.toString(currentError));
        float currentAngle = getCurrentImuAngle(); //calculateAngles();
        if (currentAngle + 360 - target < 180) {
            currentError = (currentAngle - target + 360) * -1;
        } else if(target + 360 - currentAngle < 180) {
            currentError = (target - currentAngle + 360);
        } else if(currentAngle - target < 180) {
            currentError = (currentAngle - target) * -1;
        } else if(target - currentAngle < 180) {
            currentError = (target - currentAngle);
        }
    }

    public float getLastError() {
        return lastError;
    }

    public float getCurrentError() {
        return currentError;
    }

    /** Wrapper method which updates internal data, and does the actual PID calculation.
     *
     * PID formula:
     * u(t) = Kp * e(t) + Ki * (integral(0, t) e(x) dx) + Kd * (de(t) / dt)
     * where:
     *  Kp = p constant
     *  Ki = i constant
     *  Kd = d constant
     *  u(t) = current adjustment value
     *  e(t) = desired value - current value
     * integral(0, t) e(x) dx = integral of function f(x) with respect to x on interval [0, t]
     * d(e)/dt = the derivative of function e(t) with respect to t.
     *
     *
     * @param deltaT the time between the last loop iteration, and the current iteration.
     *                 Passed as an argument to calculateVars.
     * @return The corrected value based on the PID formula
     */

    /*public float getCorrectedValue(float timeDiff) {
        calculateAngles(); //fetch new angle data, updating our target
        calculateVars(timeDiff); //calculate P, I, and D constants

        /*
        Calculate the actual corrected value, scaled down by
         *//*
        //return ((I * ITuning) / 2000) + ((P * PTuning) / 2000) + ((D * DTuning) / 2000);
        return ((I * ITuning) / 100) + ((P * PTuning) / 100) + ((D * DTuning) / 100);
    }*/

    /**
     * Performs the PID formula, including updating all variables.
     * @param deltaT the time since the last iteration
     * @return corrected value obtained through the formula
     */
    public float getCorrectedValue(float deltaT) {
        calculateVars(deltaT);
        float corrected = ((I * ITuning) / 2000) + ((P * PTuning) / 2000) + ((D * DTuning) / 2000);
        Log.d("PID CORRECTED ", Float.toString(corrected));
        return corrected;
    }

}
