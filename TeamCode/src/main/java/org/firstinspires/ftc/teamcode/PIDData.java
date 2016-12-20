package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * PIDData is the base class in which all PID
 * related data and values are stored.
 * A PIDData object is used in a {@link PIDController} to calculate
 * how to move the motors. A PIDData object contains the
 * information required to run PID related movement
 * functions.   This information includes:
 * <ul>
 *     <li>The current and target angle</li>
 *     <li>The current and last error</li>
 *     <li>The current P, I, and D values</li>
 *     <li>The P, I, and D constants</li>
 *     <li>All values relating to time</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kelsey
 * @author Adam Perlin
 * @author Noah Rose Ledesma
 * @version 3.0, 12/19/2016
 */
public class PIDData {
    //region Error Data
    public float currentAngle;
    public float targetAngle;
    public float error, lastError;
    public float rotationTolerance;
    public float p, i, d;
    public float pTuning, iTuning, dTuning;
    //endregion
    //region Time Data
    public float deltaTime;

    private ElapsedTime _programTime;
    private float _lastTime;
    //endregion

    //region Initialization

    /**
     * Constructor for the PIDData class, and setting up the elapsed time.
     */
    PIDData(){
        _programTime = new ElapsedTime();
        _lastTime = System.currentTimeMillis();
    }

    //endregion

    //region Functions

    /**
     * Set the PID constants to the given input values.
     * Only call once, at the beginning of Autonomous.
     * @param pTuningInput The constant multiplier for the P value.
     * @param iTuningInput The constant multiplier for the I value.
     * @param dTuningInput The constant multiplier for the D value.
     */
    public void SetValues(float pTuningInput, float iTuningInput, float dTuningInput){
        pTuning = pTuningInput;
        iTuning = iTuningInput;
        dTuning = dTuningInput;
    }

    /**
     * Calculates the closes rotational error, based
     * off of the current and the target angle.
     * Finds the shortest distance to turn to
     * achieve the target angle.
     */
    public void CalculateError(){
        //Set the last error to the current error
        lastError = error;
        //Calculate the closes way to turn to get to the target angle form the current angle
        if(currentAngle + 360 - targetAngle < 180){
            error = (currentAngle - targetAngle + 360) * -1;
        }else if(targetAngle + 360 - currentAngle < 180){
            error = (targetAngle - currentAngle + 360);
        }else if(currentAngle - targetAngle < 180){
            error = (currentAngle - targetAngle) * -1;
        }else if(targetAngle - currentAngle < 180){
            error = (targetAngle - currentAngle);
        }
    }


    /**
     * Return the current program length.
     * @return The current elapsed time in seconds.
     */
    public float CurrentTime(){
        return (float) _programTime.seconds();
    }

    /**
     * Calculates and stores the current change in time
     * from the last time this function was called.
     */
    public void CurrentDelta(){
        deltaTime = System.currentTimeMillis() - _lastTime;
        _lastTime = System.currentTimeMillis();
    }


    /**
     * Calculates what the PID values will be, based off of the
     * current error and past error in the {@link PIDData}.
     * @param hardwareController A {@link HardwareController} object to get the imu's angle
     */
    public float CalculatePID(HardwareController hardwareController){
        //Set the current angle to the imu's angle
        currentAngle = hardwareController.imu.getAngularOrientation().firstAngle*-1;
        //calculate how far off we are
        CalculateError();
        //calculate the difference in time from the last time this was run
        CurrentDelta();

        //Set the p to the error
        p = error;
        //Add the change in error times dT to i
        i += error * deltaTime / 1000;
        //Set d to teh change in error over dT
        d = (error - lastError)/(deltaTime/1000);

        return ((i * iTuning) / 2000) + ((p * iTuning) / 2000) + ((d * dTuning) / 2000);
    }

    //endregion
}
