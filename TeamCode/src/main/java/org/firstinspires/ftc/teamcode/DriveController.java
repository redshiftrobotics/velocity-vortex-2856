package org.firstinspires.ftc.teamcode;


/**
 * Class to hold and calculate all data related to moving the chassis.
 * This class can function with multiple drive trains.
 * The Y axis is forward and back, the X axis is left and right,
 * and the Z rotation is rotation around the center of the chassis.
 * @author Duncan McKee
 * @version 2.0
 */
public class DriveController {
    public enum DriveType {Tank, Holonomic, Swerve, Slide} //These are all the types of drive trains that this drive train controller can handle
    public DriveType dT; //The type of drive train that this drive controller is set to

    private double xSpeed, ySpeed, zRotation; //The x (side to side) and y (forward to back) speeds and the rotation around the center of the robot turning clockwise
    private double rotationAngle = 0; //The angle at which the robot has rotated from
    private double max;

    /**
     * Constructor for the DriveController,
     * has two parameters to control the type of movement.
     * @param driveType The type of drive train in use, uses the DriveType enum.
     */
    public DriveController(DriveType driveType){
        dT = driveType;
    }

    /**
     * Sets the power of the robot and calculates the array to control the robot with.
     * @param x The speed on the X axis of the robot.
     * @param y The speed on the Y axis of the robot.
     * @param z The rotation around the Z axis of the robot.
     * @param angle The angle to orient the robot around.
     * @return The array of movement speeds for each motor following: {FL, BL, FR, BR}.
     */
    public double[] Drive(float x, float y, float z, float angle){
        SetMovements(x, y, z);
        SetRotation(angle);
        return GetValues();
    }

    private void SetRotation(float angle){
        rotationAngle = angle;
    }

    private void SetMovements(float x, float y, float z){
        xSpeed = x;
        ySpeed = y;
        zRotation = z;
    }

    private double[] GetValues(){
        switch (dT) {
            case Tank: //For Tank Drive the array is set up as {Left Power, Right Power}
                max = Math.abs(ySpeed)+Math.abs(zRotation);
                if(max < 1){
                    max = 1;
                }
                return new double[]
                        {GetPercentage(ySpeed+zRotation,max),
                         GetPercentage(ySpeed-zRotation,max)};
            case Holonomic: //For Holonomic Drive the array is set up as {Front Left Power, Back Left Power, Front Right Power, Back Right Power}
                max = Math.abs(ySpeed*Math.cos(rotationAngle)) +
                      Math.abs(ySpeed*Math.sin(rotationAngle)) +
                      Math.abs(xSpeed*Math.cos(rotationAngle)) +
                      Math.abs(xSpeed*Math.sin(rotationAngle)) +
                      Math.abs(zRotation);
                if(max < 1){
                    max = 1;
                }
                return new double[]
                        {GetPercentage(ySpeed*Math.cos(rotationAngle)-ySpeed*Math.sin(rotationAngle)+xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)+zRotation,max),
                         GetPercentage(ySpeed*Math.cos(rotationAngle)+ySpeed*Math.sin(rotationAngle)-xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)+zRotation,max),
                         GetPercentage(ySpeed*Math.cos(rotationAngle)+ySpeed*Math.sin(rotationAngle)+xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)-zRotation,max),
                         GetPercentage(ySpeed*Math.cos(rotationAngle)-ySpeed*Math.sin(rotationAngle)-xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)-zRotation,max)};
            case Swerve:

                break;
            case Slide: //For Slide Drive the array is set up as {Left Power, Right Power, Slide Power}
                max = Math.abs(ySpeed*Math.cos(rotationAngle)) +
                      Math.abs(xSpeed*Math.sin(rotationAngle)) +
                      Math.abs(zRotation);
                if(max < 1){
                    max = 1;
                }
                return new double[]
                        {GetPercentage(ySpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)+zRotation,max),
                         GetPercentage(ySpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)-zRotation,max),
                         GetPercentage(xSpeed*Math.cos(rotationAngle)+ySpeed*Math.sin(rotationAngle),Math.abs(xSpeed*Math.cos(rotationAngle))+Math.abs(ySpeed*Math.sin(rotationAngle)))};
        }
        return null;
    }

    private double GetPercentage(double inTop, double inBottom){
        return inTop == 0 ? 0 : inTop/inBottom;
    }
}
