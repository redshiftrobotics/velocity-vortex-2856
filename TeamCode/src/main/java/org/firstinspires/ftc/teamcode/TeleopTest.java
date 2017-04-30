package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * A test Teleop program to control the robot with the TeleopController and DriveController.
 * @author Duncan McKee
 */
@TeleOp(name="Teleop Test")
public class TeleopTest extends OpMode {
    //region Private Variables
    private TeleopController teleopController; //A TeleopController to drive the robot's drive train.
    private boolean initialized = false; //A boolean to stop the OpMode if not all of the devices were initialized.
    //endregion

    @Override
    public void init() {
        telemetry.addData("Teleop Initializing", 0);
        telemetry.update();

        teleopController = new TeleopController(hardwareMap);
        initialized = true; //Set the status of the initialization process to true.

        telemetry.addData("Everything is Ready to Go", 1);
        telemetry.update();
    }

    @Override
    public void loop() {
        if(!initialized){ //If the init function was not fully run stop the OpMode.
            requestOpModeStop();
        }
        teleopController.Drive(gamepad1);
    }
}
