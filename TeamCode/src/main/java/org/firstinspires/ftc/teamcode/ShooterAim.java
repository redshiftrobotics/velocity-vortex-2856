package org.firstinspires.ftc.teamcode;

import java.util.HashMap;


/**
 * Created by matt on 3/23/17.
 */


public enum ShooterAim {

    // our actual fields. Change accordingly.
    LOB (0.2f), //haven't tested lob yet...
    FAR (0.51f),
    MEDIUM (0.54f),
    NEAR (0.59f);

    //represents internal enum state, i.e. LOB | FAR | MEDIUM | NEAR
    private float dist;

    //Hash table mapping distances to angles.
    public static HashMap<Float, Float> Angles = new HashMap<>();

    static {
        //TODO: change these values to actual shooter measurements.
        Angles.put(ShooterAim.LOB.get(), 0.0f);
        Angles.put(ShooterAim.FAR.get(), 0.01f);
        Angles.put(ShooterAim.FAR.get(), 0.02f);
        Angles.put(ShooterAim.MEDIUM.get(), 0.03f);
        Angles.put(ShooterAim.NEAR.get(), 0.04f);
    }

    public float get() { return dist; }


    public static ShooterAim resolveDistance(float angle) {
        if (angle <= ShooterAim.FAR.get()) {
            return ShooterAim.LOB;
        }

        if (angle > ShooterAim.FAR.get() && angle <= ShooterAim.MEDIUM.get()) {
            return ShooterAim.FAR;
        }

        if (angle > ShooterAim.MEDIUM.get() && angle <= ShooterAim.NEAR.get()) {
            return ShooterAim.MEDIUM;
        }

        return ShooterAim.NEAR;
    }

    public float getServoAngle() {
        return ShooterAim.Angles.get(this.get());
    }

    ShooterAim(float dist) {
        this.dist = dist;
    }
}


