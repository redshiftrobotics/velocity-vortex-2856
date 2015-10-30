package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An example of a synchronous opmode that implements a simple drive-a-bot. 
 */
@TeleOp(name="Samin TeleOp")
public class SynchTeleOp extends SynchronousOpMode
{
	//motors declarations
	DcMotor motorLeft = null;
	DcMotor motorRight = null;
	DcMotor backDrive = null;
	DcMotor frontDrive = null;
	DcMotor frontPaddle = null;

	//encoder positions
	float FrontTargetEncoder = 0;
	float BackTargetEncoder = 0;


	@Override
	protected void main() throws InterruptedException
	{
		//initialize motors
		this.motorLeft = this.hardwareMap.dcMotor.get("left_drive");
		this.motorRight = this.hardwareMap.dcMotor.get("right_drive");
		this.backDrive = this.hardwareMap.dcMotor.get("back_brace");
		this.frontDrive = this.hardwareMap.dcMotor.get("front_brace");

		frontDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		backDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		//this.frontPaddle = this.hardwareMap.dcMotor.get("front_paddle");

		//reverse the left motor
		this.motorRight.setDirection(DcMotor.Direction.REVERSE);

		//set the initial encoder values
		FrontTargetEncoder = frontDrive.getCurrentPosition();
		BackTargetEncoder = backDrive.getCurrentPosition();

		// Wait until we've been given the ok to go
		this.waitForStart();

		// Enter a loop processing all the input we receive
		while (this.opModeIsActive())
		{
			updateGamepads();

			this.DriveControl(this.gamepad1);
			this.AuxilaryControl(this.gamepad2);

			// Emit telemetry with the freshest possible values
			this.telemetry.update();

			// Let the rest of the system run until there's a stimulus from the robot controller runtime.
			this.idle();
		}
	}

	void AuxilaryControl(Gamepad pad)
	{
		if (Math.abs(pad.left_stick_y) > .1) {
			FrontTargetEncoder += pad.left_stick_y * 50;
		}


		if (Math.abs(pad.right_stick_y) > .1) {
			BackTargetEncoder += pad.right_stick_y * 50;
		}

		float FrontDifference = ((float)FrontTargetEncoder - (float)frontDrive.getCurrentPosition()) / 500f;
		float BackDifference = ((float)BackTargetEncoder - (float)backDrive.getCurrentPosition()) / 500f;


		telemetry.addData("00", "FrontDifference: " + FrontDifference);
		telemetry.addData("01", "BackDifference: " + BackDifference);
		telemetry.addData("02", "FrontEncoder: " + frontDrive.getCurrentPosition());
		telemetry.addData("03", "BackEncorder: " + backDrive.getCurrentPosition());
		telemetry.addData("04", "TargetFrontEncoder: " + FrontTargetEncoder);
		telemetry.addData("05", "TargetBackEncorder: " + BackTargetEncoder);

		//if the left trigger is pressed
//		if(pad.left_trigger > .05)
//		{
//			frontPaddle.setPower(-pad.left_trigger);
//		}
//		//if the right trigger is pressed
//		else if(pad.right_trigger > .05)
//		{
//			frontPaddle.setPower(pad.right_trigger);
//		}

		if (FrontDifference > 1) {
			FrontDifference = 1;
		}
		if (FrontDifference < -1) {
			FrontDifference = -1;
		}

		if (BackDifference > 1) {
			BackDifference = 1;
		}
		if (FrontDifference < -1) {
			FrontDifference = -1;
		}

		frontDrive.setPower(FrontDifference);
		backDrive.setPower(BackDifference);
	}

	void DriveControl(Gamepad pad) throws InterruptedException {
		// Remember that the gamepad sticks range from -1 to +1, and that the motor
		// power levels range over the same amount
		float ctlPower = pad.left_stick_y;
		float ctlSteering = pad.left_stick_x;


		ctlPower = Range.clip(ctlPower, ctlSteering - 1, ctlSteering + 1);
		ctlPower = Range.clip(ctlPower, -ctlSteering - 1, -ctlSteering + 1);

		// Figure out how much power to send to each motor. Be sure
		// not to ask for too much, or the motor will throw an exception.
		float powerLeft = Range.clip(ctlPower - ctlSteering, -1f, 1f);
		float powerRight = Range.clip(ctlPower + ctlSteering, -1f, 1f);

		// Tell the motors
		this.motorLeft.setPower(powerLeft / 4);
		this.motorRight.setPower(powerRight / 4);
	}
}
