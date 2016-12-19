package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * Autonomous2856 is the autonomous op mode for 2856
 * in the FTC game Velocity Vortex. Autonomous2856 uses
 * PID to maintain its rotation throughout the robots journey.
 * This op mode extends LinearOpMode, and makes the robot do
 * the following actions:
 * <ul>
 *     <li>Move forward 1 tile</li>
 *     <li>Fire two projectiles into the Center Vortex</li>
 *     <li>Move forward 1 tile</li>
 *     <li>Rotate to face the beacons, while knocking off the cap ball</li>
 *     <li>Move forward 3 tiles</li>
 *     <li>Rotate to be parallel with the beacons</li>
 *     <li>Move forward to the farther line, to press the first beacon</li>
 *     <li>Move backward to the closer line, to press the second beacon</li>
 *     <li>Continue backward onto the Corner Vortex</li>
 * </ul>
 * @author Duncan McKee
 * @author Matthew Kesley
 * @version 1.0, 12/18/2016
 */
//@Autonomous(name = "2856 Autonomous")
public class Autonomous2856 extends LinearOpMode {
    I2cDeviceSynch imu;
    DcMotor[] motors;
    PIDControllerOld pidController;
    ColorSensor colorSensor;

    DcMotor shooter;

    @Override
    /**
     * Runs the Op Mode
     */
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        motors[0] = hardwareMap.dcMotor.get("m0");
        motors[1] = hardwareMap.dcMotor.get("m1");
        motors[2] = hardwareMap.dcMotor.get("m2");
        motors[3] = hardwareMap.dcMotor.get("m3");
        colorSensor = hardwareMap.colorSensor.get("cs");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);

        pidController = new PIDControllerOld(imu, motors, colorSensor, telemetry);
        pidController.SetPIDValues(100f, 30f, 0f);

        waitForStart();
        pidController.LinearMove(0f, 0.5f, 10);
        Thread.sleep(1000);
        shooter.setPower(1);
        Thread.sleep(3000);
        shooter.setPower(0);
        pidController.AngularTurn(55f);
        Thread.sleep(1000);
        pidController.LinearMove(0f, 3.5f, 10);
        pidController.AngularTurn(0f);
    }
}
