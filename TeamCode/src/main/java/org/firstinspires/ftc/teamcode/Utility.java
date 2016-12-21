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
    /**
     * This is a function that makes an generic object array out of several objects.
     * @param $objects Each individual object.
     * @return An array of objects.
     */
    public static Object[] MakeArray(Object... $objects){
        return $objects;
    }
}
