package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

/**
 * Object to hold all data related to moving the chassis.
 * Able to work with multiple drive trains.
 * Y axis is forward back, X axis is left right, and Z axis is up and down.
 * @author Duncan McKee
 */

class DirectionObject {
    private float xSpeed; //Holds the speed the robot moves on the x axis
    private float ySpeed; //Holds the speed the robot moves on the y axis
    private float zRotation; //Holds the speed the robot rotates around the z axis
    private static final float min = -1.0f; //The minimum speed that can be returned by the DirectionObject
    private static final float max = 1.0f; //The maximum speed that can be returned by the DirectionObject
    private float[] returnSpeed; //The value that is returned by all function to get the speed of specific motors

    public enum DriveTrain {Mecanum, AllWheelDrive, XDrive};
    private DriveTrain driveTrain;

    DirectionObject(float x, float y, float z, DriveTrain dT){
        xSpeed = x;
        ySpeed = y;
        zRotation = z;

        driveTrain = dT;
        switch(dT){
            case Mecanum:
                returnSpeed = new float[4];
                break;
            case AllWheelDrive:
                returnSpeed = new float[2];
                break;
            case XDrive:
                returnSpeed = new float[4];
                break;
        }
    }

    float[]drive(float x, float y, float z){
        setValues(x, y, z);
        return getValues();
    }

    void setValues(float x, float y, float z){
        xSpeed = x;
        ySpeed = y;
        zRotation = z;
    }

    float[] getValues(){
        switch(driveTrain){
            case Mecanum:
                returnSpeed[0] = frontLeftMecanum();
                returnSpeed[1] = frontRightMecanum();
                returnSpeed[2] = backRightMecanum();
                returnSpeed[3] = backLeftMecanum();
                break;
            case AllWheelDrive:
                returnSpeed[0] = leftAllWheel();
                returnSpeed[1] = rightAllWheel();
                break;
            case XDrive:
                //We have not implemented this drive train yet, don't use.
                break;
        }
        return returnSpeed;
    }

    private float frontLeftMecanum(){ return Range.clip(zRotation-xSpeed+ySpeed,min,max); }
    private float frontRightMecanum(){ return Range.clip(zRotation-xSpeed-ySpeed,min,max); }
    private float backRightMecanum(){ return Range.clip(zRotation+xSpeed-ySpeed,min,max); }
    private float backLeftMecanum(){ return Range.clip(zRotation+xSpeed+ySpeed,min,max); }

    private float leftAllWheel() { return Range.clip(zRotation+ySpeed,min,max); }
    private float rightAllWheel() { return Range.clip(zRotation-ySpeed,min,max); }


}
