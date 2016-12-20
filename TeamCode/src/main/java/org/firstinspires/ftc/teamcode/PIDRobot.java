package org.firstinspires.ftc.teamcode;

/**
 * Created by adam on 12/13/16.
 */


import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Base interface which all robots that wish to use our PID must implement.
 */
public class PIDRobot {

    private PIDController pid;
    private TimeData time;
    private Hardware hardware;
    public static float angleTurnThreshold = 0.03f;
    public static float motorSpeed = 0.65f;
    public static float correctedSpeedScalar = 0.5f;
    public Telemetry telemetry;

    public PIDRobot(Hardware hardware, I2cDeviceSynch imu, Telemetry telemetry) {
        this.hardware = hardware;
        this.telemetry = telemetry;
        pid = new PIDController(imu);
        time = new TimeData();
        pid.initializeTarget();
        telemetry.addData("Robot: ", "Initializing");
        telemetry.update();
    }

    /**
     * Wrapper method. See {@link PIDController#setTuning(float, float, float}
     * @param P p value
     * @param I i value
     * @param D d value
     */

    public void setTuning(float P, float I, float D) {
        pid.setTuning(P, I, D);
    }
    private void applyLinearMove(float correctedValue, float[] movement, float speed, float scalar) {
        hardware.getMotor(0).setPower((((movement[0] - movement[1]) * speed) - (correctedValue)) * scalar);
        hardware.getMotor(1).setPower((((movement[0] + movement[1]) * speed) + (correctedValue)) * scalar);
        hardware.getMotor(2).setPower((((movement[0] - movement[1]) * speed) + (correctedValue)) * scalar);
        hardware.getMotor(3).setPower((((movement[0] + movement[1]) * speed) - (correctedValue)) * scalar);
    }
    
    private void applyTurn(float correctedValue) {
        hardware.getMotor(0).setPower(Hardware.POWER_CONSTANT - (correctedValue));
        hardware.getMotor(1).setPower(Hardware.POWER_CONSTANT + (correctedValue));
        hardware.getMotor(2).setPower(Hardware.POWER_CONSTANT  + (correctedValue));
        hardware.getMotor(3).setPower(Hardware.POWER_CONSTANT  - (correctedValue));
    }

    public void linearMove(float rotations, float[] movement, int timeout) {
        float startTime = time.now(); //beginning time
        float endTime = startTime + timeout; //when we need to time out...
         /*
        Note about change from previous implementation
        We don't need to update any headings here or calculate any angles. getCorrectedValue
        takes care of both of these things. All we need to do is clear out historical integral
        and derivative data.
         */
        pid.clearHistoricData();

        //get the current encoder position of the specified motor...

        float startPosition = hardware.getMotor(0).getCurrentPosition();

         /* loopTime represents the time since the last data point was taken. We start by assigning
              the current time to this variable, so we can continually update it in our loop...
         */
        float loopTime = time.now();

        /*
            Corrected value holds the current PID produced correction necessary to move towards
            our target. It gets applied to movement functions.
         */
        float correctedValue;

        /*
            Apply motor corrections
            while the loop hasn't timed out, and the absolute value of the correction is less
            than the specified threshold (our corrections will never be perfect, so we have
            to call it good at a certain point).
         */

        while(Math.abs(startPosition - hardware.getMotor(0).getCurrentPosition()) < Math.abs(rotations) * Hardware.EncoderCount
                && time.now() < endTime) {
            loopTime = time.since(loopTime); // Record the time since the previous loop.
            correctedValue = pid.getCorrectedValue(loopTime); //get the current corrected values
            applyLinearMove(correctedValue, movement, motorSpeed, 1f); //set them to motors in order to make the corrections
        }
        // Our drive loop has completed! stop the motors.
        stop();
    }

    public void turnToAngle(float angle, int timeout) {
        float startTime = time.now(); //beginning time
        float endTime = startTime + timeout; //when we need to time out...

        /*
        Note about change from previous implementation
        We don't need to update any headings here or calculate any angles. getCorrectedValue
        takes care of both of these things. All we need to do is clear out historical integral
        and derivative data.
         */
        pid.clearHistoricData();

       /* because this is a turn function, we have to add to our current PID
        target. This renders our current orientation incorrect, and our PID loop corrects
        accordingly, making the robot turn.
        */
        pid.addTarget(angle);

        /* loopTime represents the time since the last data point was taken. We start by assigning
              the current time to this variable, so we can continually update it in our loop...
         */
        float loopTime = time.now();

        /*
        preload correctedValue with our first pid correction value, in order to begin the loop...
         */
        float correctedValue = pid.getCorrectedValue(loopTime);

        /*
            Apply motor corrections
            while the loop hasn't timed out, and the absolute value of the correction is less
            than the specified threshold (our corrections will never be perfect, so we have
            to call it good at a certain point).
         */
        while(time.now() < endTime && Math.abs(correctedValue) <= angleTurnThreshold){
            loopTime = time.since(loopTime); // Record the time since the previous loop.
            correctedValue = pid.getCorrectedValue(loopTime); //get the current corrected values
            applyTurn(correctedValue); //set them to motors in order to make the robot turn
        }
        // Our drive loop has completed! stop the motors.
        stop();
    }

    public void moveToLine(float[] movement, float speed, int timeout) {
        float startTime = time.now();
        float endTime = startTime + timeout;
         /*
        Note about change from previous implementation
        We don't need to update any headings here or calculate any angles. getCorrectedValue
        takes care of both of these things. All we need to do is clear out historical integral
        and derivative data.
         */
        pid.clearHistoricData();
        /* loopTime represents the time since the last data point was taken. We start by assigning
              the current time to this variable, so we can continually update it in our loop...
        */
        float loopTime = time.now();
         /*
            Corrected value holds the current PID produced correction necessary to move towards
            our target. It gets applied to movement functions.
         */
        float correctValue;

        /*
        While the average of all our color values is less than our threshold of 70...
         */

        while(hardware.getAdjustedColorValues() < 70 && time.now() < endTime) {
            loopTime = time.since(loopTime);
            correctValue = pid.getCorrectedValue(loopTime);
            applyLinearMove(correctValue, movement, motorSpeed, correctedSpeedScalar);
        }
        // Our drive loop has completed! Stop the motors.
        stop();
    }

    private void stop() {
        for (int i = 0; i < hardware.motorCount(); i++) {
            hardware.getMotor(i).setPower(0);
        }
    }
}


