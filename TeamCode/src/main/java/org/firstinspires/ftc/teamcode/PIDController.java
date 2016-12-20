package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import java.util.ArrayList;

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
    ArrayList<Float> derivativeData;
    ArrayList<Float> integralData;

    //utility logfile
    public LogFile logFile = new LogFile("sdcard/log.file");
    // Constructor


    /** Constructor
     *
     * @param i2cSync an I2cDeviceSynch referencing an imu.
     */

    public PIDController(I2cDeviceSynch i2cSync) {
        // Init non-primitives
        derivativeData = new ArrayList<>();
        integralData = new ArrayList<>();
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
     * Clears historical integral and derivative data. Must be called at the beginning of
     * every movement function to get accurate correction data.
     *
     */
    public void clearHistoricData() {
        derivativeData.clear();
        integralData.clear();
    }

    /**
     * Shifts the latest heading into the previous, and sets the latest heading to a new
     * angular orientation data point.
     */

    public void updateHeadings() {
        //TODO: See if headings actually has to be updated twice...
        headings[0] = headings[1];
        // Then, we assign the new angle heading.
        headings[1] = constrainAngleRange(imu.getAngularOrientation().firstAngle);

        headings[0] = headings[1];
        // Then, we assign the new angle heading.
        headings[1] = constrainAngleRange(imu.getAngularOrientation().secondAngle);
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
    public void initializeTarget() {
        target = calculateAngles();
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
     * @param timeDiff Time since the last iteration of the loop, i.e., currentTime - previousTime
     */

    public void calculateVars(float timeDiff) {
        // Append to our data sets.
        integralData.add(getComputedAngle() - getTarget());
        derivativeData.add(getComputedAngle());

        // Keep integralData and derivativeData from having an exceeding number of entries.
        if (integralData.size() > 500){
            integralData.remove(0);
        }

        if (derivativeData.size() > 5) {
            derivativeData.remove(0);
        }

        // Set our P, I, and D values.
        // `P` will be the ComputedAngle - target
        P = getComputedAngle() - getTarget();

        // `I` will be the average of the integralData (Cries softly at the lack of Java8 streams)

        float IntegralAverage = 0;
        for(float value : integralData) {
            IntegralAverage += value;
        }

        I = IntegralAverage / integralData.size();

        // `D` will be the difference of the ComputedAngle and the Derivative average divided by
        // the time since the last loop in seconds multiplied by one plus half of the size of
        // the Derivative data set size.


        float DerivativeAverage = 0;
        for(float value : derivativeData) {
            DerivativeAverage += value;
        }
        DerivativeAverage /= derivativeData.size();

        D = (getComputedAngle() - DerivativeAverage) / ((timeDiff / 1000) * (1 + (derivativeData.size() / 2)));
    }

    /** Calculates computed angle value, updating the number of full rotations accordingly, and accounting
     * for this in calculations.
     * @return the newest angular orientation data point
     */
    public float calculateAngles() {
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
     * @param timeDiff the time between the last loop iteration, and the current iteration.
     *                 Passed as an argument to calculateVars.
     * @return The corrected value based on the PID formula
     */

    public float getCorrectedValue(float timeDiff) {
        calculateAngles(); //fetch new angle data, updating our target
        calculateVars(timeDiff); //calculate P, I, and D constants

        /*
        Calculate the actual corrected value, scaled down by
         */
        return ((I * ITuning) / 2000) + ((P * PTuning) / 2000) + ((D * DTuning) / 2000);
    }


}
