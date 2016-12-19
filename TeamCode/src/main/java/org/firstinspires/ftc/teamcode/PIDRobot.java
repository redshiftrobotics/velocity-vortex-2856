package org.firstinspires.ftc.teamcode;

/**
 * Created by adam on 12/13/16.
 */


import org.firstinspires.ftc.robotcore.external.Func;

/**
 * Base interface which all robots that wish to use our PID must implement.
 */
public abstract class PIDRobot {

    PID pid;
    PIDHardware hardware;
    public TimeData time;

    public static float angleTurnThreshold = 0.03f;


    public PIDRobot(PIDHardware hardware) {
       this.hardware = hardware;
    }
    /**
     *
     * @param direction passed a float value representing the
     *                  PID corrected direction for the motor.
     *                  How the value is used can be decided
     *                  by the subclass implementation.
     */

    //TODO: get rid of abstract methods
    public abstract void applyLinearMove(float direction);

    /**
     * Overloaded version of ApplyLinearMove
     * @param direction see above for explanation.
     * @param vector movement parameter for a mechanum chassis or other chassis
     *               capable of movement on arbitrary movement vectors.
     *
     */
    public abstract void applyLinearMove(float direction, float[] vector);

    /**
     *
     * @param direction passed a PID corrected direction float value,
     *                  but is meant to be applied to turn the robot,
     *                  rather than move linearly.
     */
    public abstract void applyTurn(float direction);

    public void linearMove(float rotations, float[] movement, int timeout) {
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
       pid.updateData();

        // Get the current program time and starting encoder position before we start our drive loop
        float startTime = time.now();
        float startPosition = hardware.getMeasuredEncoderValue(); /*
        use implementation supplied encoder value */

        // Reset our Integral and Derivative data.

        //Calculate PIDS again because Isaac Zinda only knows


        // We need to keep track of how much time passes between a loop.
        float loopTime = time.now();
        float endTime = startTime + timeout;
        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(Math.abs(startPosition - hardware.getMeasuredEncoderValue()) < Math.abs(rotations) * Hardware.EncoderCount
                && time.now() < endTime) {
            // First we check if we have exceeded our timeout and...
            // Record the time since the previous loop.
            loopTime = time.since(loopTime);
            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            float correctedValue = pid.getCorrectedValue(loopTime);
            applyLinearMove(correctedValue, movement);
        }
        // Our drive loop has completed! Stop the motors.
        hardware.stop();
    }

    public void linearMove(float rotations,  int timeout) {
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
        pid.updateData();
        // Get the current program time and starting encoder position before we start our drive loop
        //float startTime = time.now();
        //pid.setCurrentTimeDataPoint(time.now());
        float startPosition = hardware.getMeasuredEncoderValue(); /*
        use implementation supplied encoder value */
        //Calculate PIDS again because Isaac Zinda only knows


        // We need to keep track of how much time passes between a loop.
        float endTime = time.now() + timeout;
        float prevTimePoint = time.now();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        while(Math.abs(startPosition - hardware.getMeasuredEncoderValue()) < Math.abs(rotations) * Hardware.EncoderCount
                && time.now() < endTime) {
            // First we check if we have exceeded our timeout and...

            // Record the time since the previous loop.
             prevTimePoint = time.since(prevTimePoint);

            // Calculate our angles. This method may modify the input Rotations.
            //IMURotations =
            float correctedValue = pid.getCorrectedValue(prevTimePoint);
            applyLinearMove(correctedValue);
        }
        // Our drive loop has completed! Stop the motors.
        hardware.stop();
    }

    public void turnToAngle(float angle, int timeout) {
        // We need two points of data from the IMU to do our calculation. So lets take the first one
        // and put it into our "current" headings slot.
       // pid.calculateAngles(hardware);

        // Get the current program time and starting encoder position before we start our drive loop
        float startTime = time.now();
        // Reset our Integral and Derivative data.
       // pid.clearData();


        //Calculate PIDS again because Isaac Zinda only knows
        //pid.updateHeadings();
        pid.updateData();

        // Manually calculate our first target
        //pid.setTarget(pid.getTarget() + angle);
        pid.addTarget(angle);
        // We need to keep track of how much time passes between a loop.
        float loopTime = time.now();

        // This is the main loop of our straight drive.
        // We use encoders to form a loop that corrects rotation until we reach our target.
        float endTime = startTime + timeout;
        loopTime = time.since(loopTime);
        float correctedValue = pid.getCorrectedValue(loopTime);
        while(time.now() < endTime && Math.abs(correctedValue) <= angleTurnThreshold){
            // Record the time since the previous loop.
            loopTime = time.since(loopTime);
            // Calculate the Direction to travel to correct any rotational errors.
           correctedValue = pid.getCorrectedValue(loopTime);
            applyTurn(correctedValue);
        }
        // Our drive loop has completed! Stop the motors.
        hardware.stop();
    }
}

