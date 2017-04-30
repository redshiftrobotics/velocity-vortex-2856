package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * A test Auto program to control the robot with the PIDController and DriveController.
 * @author Duncan McKee
 */
@Autonomous(name="Autonomous Test")
public class AutoTest extends LinearOpMode{
    //region Private Variables
    private PIDController pidController; //A PIDController to drive the robot's drive train.
    //endregion

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Auto Initializing", 0);
        telemetry.update();

        pidController = new PIDController(hardwareMap);
        pidController.SetTuningXY(1, 0, 0);
        pidController.SetTuningZ(1, 0, 0);

        telemetry.addData("Everything is Ready to Go", 1);
        telemetry.update();

        waitForStart();

        pidController.Drive(new float[]{0,1}, 1, 2);
        pidController.Drive(90, 1, 2);
    }
}
