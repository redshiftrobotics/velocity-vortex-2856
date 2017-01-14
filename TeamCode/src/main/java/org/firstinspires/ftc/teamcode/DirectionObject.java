package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

/**
 * Created by Duncan on 11/9/2016.
 */

public class DirectionObject {
    public float xSpeed;
    public float ySpeed;
    public float zAngle;

    public DirectionObject(float x, float y, float z){
        xSpeed = -x;
        ySpeed = y;
        zAngle = -z;
    }

    public void setValues(float x, float y, float z){
        xSpeed = -x;
        ySpeed = y;
        zAngle = -z;
    }

    public float frontLeftSpeed(){
        float speed = 0;

        speed -= ySpeed;
        speed += zAngle;
        speed += xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }
    public float frontRightSpeed(){
        float speed = 0;

        speed += ySpeed;
        speed += zAngle;
        speed += xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }
    public float backRightSpeed(){
        float speed = 0;

        speed += ySpeed;
        speed += zAngle;
        speed -= xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }
    public float backLeftSpeed(){
        float speed = 0;

        speed -= ySpeed;
        speed += zAngle;
        speed -= xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }
}
