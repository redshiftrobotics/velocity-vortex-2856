package org.firstinspires.ftc.teamcode;

/**
 * Created by Duncan on 4/25/2017.
 */

public class DriveController {
    public enum DriveType {Tank, Holonomic, Swerve, Slide} //These are all the types of drive trains that this drive train controller can handle
    public DriveType dT; //The type of drive train that this drive controller is set to
    public Boolean global = false; //Bool to determine if the movement direction should be in terms of the robot or the field

    private double xSpeed, ySpeed, zRotation; //The x (side to side) and y (forward to back) speeds and the rotation around the center of the robot turning clockwise
    private double rotationAngle = 0; //The angle at which the robot has rotated from
    private double max;

    public DriveController(DriveType driveType, boolean globalSystem){
        dT = driveType;
        if(dT!=DriveType.Tank) {
            global = globalSystem;
        }
    }

    public double[] Drive(float x, float y, float z){
        SetMovements(x, y, z);
        return GetValues();
    }

    public double[] Drive(float x, float y, float z, float angle){
        SetMovements(x, y, z);
        SetRotation(angle);
        return GetValues();
    }

    public void SetRotation(float angle){
        rotationAngle = angle;
    }

    public void SetMovements(float x, float y, float z){
        xSpeed = x;
        ySpeed = y;
        zRotation = z;
    }

    public double[] GetValues(){
        max = Math.abs(ySpeed*Math.cos(rotationAngle)) +
              Math.abs(ySpeed*Math.sin(rotationAngle)) +
              Math.abs(xSpeed*Math.cos(rotationAngle)) +
              Math.abs(xSpeed*Math.sin(rotationAngle)) +
              Math.abs(zRotation);
        if(max < 1){
            max = 1;
        }
        switch (dT) {
            case Tank: //For tank drive the array is set up as {Left power, Right power}
                return new double[]
                        {GetPercentage(ySpeed+zRotation,Math.abs(ySpeed)+Math.abs(zRotation)),
                         GetPercentage(ySpeed-zRotation,Math.abs(ySpeed)+Math.abs(zRotation))};
            case Holonomic: //For Holonomic drive the array is set up as {Front Left Power, Back Left Power, Front Right Power, Back Right Power}
                return new double[]
                        {GetPercentage(ySpeed*Math.cos(rotationAngle)-ySpeed*Math.sin(rotationAngle)+xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)+zRotation,max),
                         GetPercentage(ySpeed*Math.cos(rotationAngle)+ySpeed*Math.sin(rotationAngle)-xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)+zRotation,max),
                         GetPercentage(ySpeed*Math.cos(rotationAngle)+ySpeed*Math.sin(rotationAngle)+xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)-zRotation,max),
                         GetPercentage(ySpeed*Math.cos(rotationAngle)-ySpeed*Math.sin(rotationAngle)-xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)-zRotation,max)};
            case Swerve:

                break;
            case Slide:

                break;
        }
        return null;
    }

    private double GetPercentage(double inTop, double inBottom){
        return inTop == 0 ? 0 : inTop/inBottom;
    }
}
