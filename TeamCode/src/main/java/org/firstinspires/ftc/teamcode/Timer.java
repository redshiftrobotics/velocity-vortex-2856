package org.firstinspires.ftc.teamcode;

/**
 * Class to time events in order to have delay between events.
 * This class can calculate the change in time or check if a specific amount of time has passed.
 * @author Duncan McKee
 * @version 1.0
 */
public class Timer {
    private long startTime;
    public Timer(){
        startTime = System.currentTimeMillis();
    }

    /**
     * Starts or restarts the timer at the current time.
     */
    public void StartTimer(){
        startTime = System.currentTimeMillis();
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
}
