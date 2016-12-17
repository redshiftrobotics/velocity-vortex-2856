package org.firstinspires.ftc.teamcode;

/**
 * Created by adam on 12/13/16.
 */

public abstract class RobotPIDControl {
    /**
     *
     * @param corrected
     * takes the number
     */
    public abstract void applyPIDStraight(float corrected);
    public abstract void applyPIDTurn(float corrected);
}
