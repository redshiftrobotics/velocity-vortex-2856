package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * Class to hold and calculate all data related to moving the chassis.
 * This class can function with multiple drive trains.
 * The Y axis is forward and back, the X axis is left and right,
 * and the Z rotation is rotation around the center of the chassis.
 * @author Duncan McKee
 * @version 2.0
 */
public class DriveController {
    //region Public Variables
    public enum DriveType {Tank, Holonomic, Swerve, Slide} //These are all the types of drive trains that this drive train controller can handle (Swerve is not implemented yet).
    public DriveType dT; //The type of drive train that this drive controller is set to.
    //endregion

    //region Private Variables
    private DcMotor[] driveMotors; //The set of motors that are used in the drive train.
    private double[] drivePower; //The specific powers to set the the robots motors to.
    private double xSpeed, ySpeed, zRotation; //The x (side to side) and y (forward to back) speeds and the rotation around the center of the robot turning clockwise.
    private double rotationAngle = 0; //The angle at which the robot has rotated from
    private double max;
    //endregion

    /**
     * Constructor for the DriveController.
     * @param driveType The type of drive train in use, uses the DriveType enum.
     * @param hardwareMap The hardware map that sets up the motors.
     */
    public DriveController(DriveType driveType, HardwareMap hardwareMap){
        dT = driveType;
        switch (dT) {
            case Tank:
                driveMotors = new DcMotor[2];
                drivePower = new double[2];
                driveMotors[0] = hardwareMap.dcMotor.get("L");
                driveMotors[1] = hardwareMap.dcMotor.get("R");
                break;
            case Holonomic:
                driveMotors = new DcMotor[4];
                drivePower = new double[4];
                driveMotors[0] = hardwareMap.dcMotor.get("FL");
                driveMotors[1] = hardwareMap.dcMotor.get("BL");
                driveMotors[2] = hardwareMap.dcMotor.get("FR");
                driveMotors[3] = hardwareMap.dcMotor.get("BR");
                break;
            case Swerve:

                break;
            case Slide:
                driveMotors = new DcMotor[3];
                drivePower = new double[3];
                driveMotors[0] = hardwareMap.dcMotor.get("L");
                driveMotors[1] = hardwareMap.dcMotor.get("R");
                driveMotors[2] = hardwareMap.dcMotor.get("S");
                break;
        }
    }

    //region Public Methods
    /**
     * Sets the power of all the motors to 0.
     */
    public void Stop(){
        Drive(0, 0, 0, 0);
    }

    /**
     * Sets the power to the motors of the robot.
     * @param x The speed on the X axis of the robot.
     * @param y The speed on the Y axis of the robot.
     * @param z The rotation around the Z axis of the robot.
     * @param angle The angle to orient the robot around.
     */
    public void Drive(double x, double y, float z, float angle){
        drivePower = GetDrivePower(x, y, z, angle);
        for(int i = 0; i < driveMotors.length; i++){
            driveMotors[i].setPower(Range.clip(drivePower[i],-1, 1));
        }
    }

    /**
     * Calculates the array to control the robot with.
     * @param x The speed on the X axis of the robot.
     * @param y The speed on the Y axis of the robot.
     * @param z The rotation around the Z axis of the robot.
     * @param angle The angle to orient the robot around.
     * @return The array of movement speeds for each motor following: {FL, BL, FR, BR}.
     */
    public double[] GetDrivePower(double x, double y, float z, float angle){
        SetMovements(x, y, z);
        SetRotation(angle);
        return GetValues();
    }
    //endregion

    //region Private Methods
    /**
     * Sets the current angle of the robot.
     * @param angle The angle to set the current angle of the robot to.
     */
    private void SetRotation(float angle){
        rotationAngle = angle;
    }

    /**
     * Sets the powers of the robot in each axis.
     * @param x The power in the x axis.
     * @param y The power in the y axis.
     * @param z The power around the z axis.
     */
    private void SetMovements(double x, double y, float z){
        xSpeed = x;
        ySpeed = y;
        zRotation = z;
    }

    /**
     * Gets the values to set each motor power to.
     * For Tank Drive the array is set up as {Left Power, Right Power}
     * For Holonomic Drive the array is set up as {Front Left Power, Back Left Power, Front Right Power, Back Right Power}
     * For Slide Drive the array is set up as {Left Power, Right Power, Slide Power}
     * @return The values to set each motor power to.
     */
    private double[] GetValues(){
        switch (dT) {
            case Tank: //For Tank Drive the array is set up as {Left Power, Right Power}
                max = Math.abs(ySpeed)+Math.abs(zRotation);
                if(max < 1){
                    max = 1;
                }
                return new double[]
                        {Normalize(ySpeed+zRotation,max),
                         Normalize(ySpeed-zRotation,max)};
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
                        {Normalize(ySpeed*Math.cos(rotationAngle)-ySpeed*Math.sin(rotationAngle)+xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)+zRotation,max),
                         Normalize(ySpeed*Math.cos(rotationAngle)+ySpeed*Math.sin(rotationAngle)-xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)+zRotation,max),
                         Normalize(ySpeed*Math.cos(rotationAngle)+ySpeed*Math.sin(rotationAngle)+xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)-zRotation,max),
                         Normalize(ySpeed*Math.cos(rotationAngle)-ySpeed*Math.sin(rotationAngle)-xSpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)-zRotation,max)};
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
                        {Normalize(ySpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)+zRotation,max),
                         Normalize(ySpeed*Math.cos(rotationAngle)+xSpeed*Math.sin(rotationAngle)-zRotation,max),
                         Normalize(xSpeed*Math.cos(rotationAngle)+ySpeed*Math.sin(rotationAngle),Math.abs(xSpeed*Math.cos(rotationAngle))+Math.abs(ySpeed*Math.sin(rotationAngle)))};
        }
        return null;
    }

    /**
     * Normalizes all motor powers to a value between -1 and 1.
     * @param inTop The un-normalized value calculated.
     * @param inBottom The maximum value that is being set to one of the motors to scale around.
     * @return The normalized value of the power.
     */
    private double Normalize(double inTop, double inBottom){
        return inTop == 0 ? 0 : inTop/inBottom;
    }
    //endregion
}