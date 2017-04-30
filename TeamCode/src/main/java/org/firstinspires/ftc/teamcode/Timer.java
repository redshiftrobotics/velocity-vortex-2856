package org.firstinspires.ftc.teamcode;

/**
 * Class to time events in order to have delay between events.
 * This class can calculate the change in time or check if a specific amount of time has passed.
 * This class also can calculate the loopTime for use in PID.
 * This class works in a millisecond based system.
 * @author Duncan McKee
 * @version 1.1
 */
public class Timer {
    //region Public Variables
    public long loopTime; //The current time 1 loop takes.
    //endregion

    //region Private Variables
    private long startTime; //The start time of this timer.
    private long lastTime; //The time the last loop started.
    //endregion

    /**
     * Constructor for the Timer.
     * Sets the start time to the current time.
     */
    public Timer(){
        startTime = System.currentTimeMillis();
    }

    //region Public Methods
    /**
     * Starts or restarts the timer at the current time.
     */
    public void StartTimer(){
        startTime = System.currentTimeMillis();
        lastTime = System.currentTimeMillis();
    }

    /**
     * Function to calculate the change in time.
     * @return The difference in time between when the timer started and now.
     */
    public long DeltaTime(){
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Function to determine if a specific amount of time has passed.
     * @param time The amount to check if it has passed yet.
     * @return A boolean value if the amount of time has passed.
     */
    public boolean TimePassed(long time){
        return time <= DeltaTime();
    }

    /**
     * Function to calculate and store the loop time of a function.
     */
    public void SetLoopTime(){
        loopTime = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
    }
    //endregion
}