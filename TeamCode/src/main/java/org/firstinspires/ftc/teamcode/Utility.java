package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This is a class with several functions that are often used.
 * <ul>
 *     <li>MakeArray: makes an array of objects</li>
 *     <li>InitMotors: initializes the motors</li>
 * </ul>
 * @author Duncan McKee
 * @version 1.0, 12/19/2016
 */
public class Utility {
    public static Object[] MakeArray(Object... objects){
        return objects;
    }
    public static void InitMotors(HardwareMap hardwareMap, DcMotor[] motors){
        motors[0] = hardwareMap.dcMotor.get("m0");
        motors[1] = hardwareMap.dcMotor.get("m0");
        motors[2] = hardwareMap.dcMotor.get("m0");
        motors[3] = hardwareMap.dcMotor.get("m0");
        //Reverse specific motors based on gears and chains, comment out which ones should not be flipped
//        motors[0].setDirection(DcMotor.Direction.REVERSE);
//        motors[1].setDirection(DcMotor.Direction.REVERSE);
//        motors[2].setDirection(DcMotor.Direction.REVERSE);
//        motors[3].setDirection(DcMotor.Direction.REVERSE);
    }
}
