package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * DirectionObject is the class for all motor assignment values.
 * A DirectionObject encapsulates the information
 * used to run the {@link Autonomous2856} and {@link MecanumTeleop}. This information includes:
 * <ul>
 *     <li>A xSpeed and a ySpeed to move towards</li>
 *     <li>A zAngle to turn to</li>
 * </ul>
 * @author Duncan McKee
 * @version 1.2, 12/18/2016
 * @deprecated Replaced with {@link HardwareController} as of 12/19/2016.
 */
public class DirectionObject {
    private float xSpeed;
    private float ySpeed;
    private float zAngle;

    /**
     * Constructor for the DirectionObject class using
     * x, y, and z values to set the speeds.
     * @param x The value to set the xSpeed.
     * @param y The value to set the ySpeed.
     * @param z The value to set the zAngle.
     */
    public DirectionObject(float x, float y, float z){
        xSpeed = x;
        ySpeed = y;
        zAngle = z;
    }

    /**
     * Allows for changing of the DirectionObject's values.
     * @param x The value to set the xSpeed.
     * @param y The value to set the ySpeed.
     * @param z The value to set the zAngle.
     */
    public void SetDirection(float x, float y, float z){
        xSpeed = x;
        ySpeed = y;
        zAngle = z;
    }

    /**
     * Sets a given array of DcMotors' speeds to the current values.
     * @param motors An array of 4 DcMotors following the structure of {@link HardwareData}.
     */
    public void SetMotors(DcMotor[] motors){
        SetMotors(motors[0], motors[1], motors[2], motors[3]);
    }

    /**
     * Sets four DcMotors speeds to the current values.
     * @param m0 DcMotor 0 following the structure
     *                of the {@link HardwareData} Object.
     * @param m1 DcMotor 1 following the structure
     *                of the {@link HardwareData} Object.
     * @param m2 DcMotor 2 following the structure
     *                of the {@link HardwareData} Object.
     * @param m3 DcMotor 3 following the structure
     *                of the {@link HardwareData} Object.
     */
    public void SetMotors(DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3){
        m0.setPower(FrontLeftSpeed());
        m1.setPower(FrontRightSpeed());
        m2.setPower(BackRightSpeed());
        m3.setPower(BackLeftSpeed());
    }

    /**
     * Sets a given array of DcMotors' speeds to the current values.
     * Along with changing the DirectionObject's values.
     * @param x The value to set the xSpeed.
     * @param y The value to set the YSpeed.
     * @param z The value to set the zAngle.
     * @param motors An array of 4 DcMotors following the structure of {@link HardwareData}.
     */
    public void SetMotors(float x, float y, float z, DcMotor[] motors){
        SetDirection(x, y, z);
        SetMotors(motors[0], motors[1], motors[2], motors[3]);
    }

    /**
     * Sets four DcMotors speeds to the current values.
     * Along with changing the DirectionObject's values.
     * @param x The value to set the xSpeed.
     * @param y The value to set the YSpeed.
     * @param z The value to set the zAngle.
     * @param m0 DcMotor 0 following the structure
     *                of the {@link HardwareData} Object.
     * @param m1 DcMotor 1 following the structure
     *                of the {@link HardwareData} Object.
     * @param m2 DcMotor 2 following the structure
     *                of the {@link HardwareData} Object.
     * @param m3 DcMotor 3 following the structure
     *                of the {@link HardwareData} Object.
     */
    public void SetMotors(float x, float y, float z, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3){
        SetDirection(x, y, z);
        SetMotors(m0, m1, m2, m3);
    }

    /**
     * Returns the rotation speed of the front left DcMotor.
     * @return The rotation speed of the front left DcMotor.
     */
    public float FrontLeftSpeed(){
        float speed = 0;

        speed -= ySpeed;
        speed += zAngle;
        speed += xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the front right DcMotor.
     * @return The rotation speed of the front right DcMotor.
     */
    public float FrontRightSpeed(){
        float speed = 0;

        speed += ySpeed;
        speed += zAngle;
        speed += xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the back right DcMotor.
     * @return The rotation speed of the back right DcMotor.
     */
    public float BackRightSpeed(){
        float speed = 0;

        speed += ySpeed;
        speed += zAngle;
        speed -= xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }

    /**
     * Returns the rotation speed of the back left DcMotor.
     * @return The rotation speed of the back left DcMotor.
     */
    public float BackLeftSpeed(){
        float speed = 0;

        speed -= ySpeed;
        speed += zAngle;
        speed -= xSpeed;

        return (float) Range.clip(speed,-1.0,1.0);
    }
}
