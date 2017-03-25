package org.firstinspires.ftc.teamcode;

import java.util.HashMap;


/**
 * Created by matt on 3/23/17.
 */


public enum ShooterAim {

    // our actual fields. Change accordingly.
    LOB (0.2f),
    FAR (0.3f),
    MEDIUM (0.4f),
    NEAR (0.55f);



    //represents internal enum state, i.e. LOB | FAR | MEDIUM | NEAR
    private float dist;

    //Hash table mapping distances to angles.
    public static HashMap<Float, Float> Angles = new HashMap<>();

    static {
        //TODO: change these values to actual shooter measurements.
        Angles.put(ShooterAim.LOB.dist(), 0.0f);
        Angles.put(ShooterAim.FAR.dist(), 0.01f);
        Angles.put(ShooterAim.FAR.dist(), 0.02f);
        Angles.put(ShooterAim.MEDIUM.dist(), 0.03f);
        Angles.put(ShooterAim.NEAR.dist(), 0.04f);
    }

    public float dist() { return dist; }


    public static ShooterAim resolveDistance(float angle) {
        if (angle <= ShooterAim.FAR.dist()) {
            return ShooterAim.LOB;
        }

        if (angle > ShooterAim.FAR.dist() && angle <= ShooterAim.MEDIUM.dist()) {
            return ShooterAim.FAR;
        }

        if (angle > ShooterAim.MEDIUM.dist() && angle <= ShooterAim.NEAR.dist()) {
            return ShooterAim.MEDIUM;
        }

        return ShooterAim.NEAR;
    }

    public float getServoAngle() {
        return ShooterAim.Angles.get(this.dist());
    }

    ShooterAim(float dist) {
        this.dist = dist;
    }
}


