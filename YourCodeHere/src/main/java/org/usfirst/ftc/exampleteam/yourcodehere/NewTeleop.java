package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An example of a synchronous opmode that implements a simple drive-a-bot. 
 */
@TeleOp(name="New 2856 TeleOp")
public class NewTeleop extends SynchronousOpMode
{
	//motor / servo declarations
	DcMotor leftDrive = null;
	DcMotor rightDrive = null;
	DcMotor backBrace = null;
	DcMotor backWheel = null;
	DcMotor blockCollector = null;
	DcMotor hangingArm = null;
	Servo hangingControl = null;
	Servo blockConveyer = null;
	Servo leftGate = null;
	Servo rightGate = null;
	Servo leftWing = null;
	Servo rightWing = null;

	//this is the default servo position
	double ServoPosition = .5;

	@Override
	protected void main() throws InterruptedException
	{
		//initialize motors
		this.leftDrive = this.hardwareMap.dcMotor.get("left_drive");
		this.rightDrive = this.hardwareMap.dcMotor.get("right_drive");
		this.backBrace = this.hardwareMap.dcMotor.get("back_brace");
		this.backWheel = this.hardwareMap.dcMotor.get("back_wheel");
		this.blockCollector = this.hardwareMap.dcMotor.get("block_collector");
		this.hangingArm = this.hardwareMap.dcMotor.get("hanging_motor");
		this.hangingControl = this.hardwareMap.servo.get("hanging_control");
		this.blockConveyer = this.hardwareMap.servo.get("block_conveyor");
		this.rightGate = this.hardwareMap.servo.get("right_gate");
		this.leftGate = this.hardwareMap.servo.get("left_gate");
		this.leftWing = this.hardwareMap.servo.get("left_wing");
		this.rightWing = this.hardwareMap.servo.get("right_wing");

		backWheel.setDirection(DcMotor.Direction.REVERSE);
		this.leftDrive.setDirection(DcMotor.Direction.REVERSE);

		// Wait until we've been given the ok to go
		this.waitForStart();

		// Enter a loop processing all the input we receive
		while (this.opModeIsActive())
		{
			updateGamepads();

			this.DriveControl(this.gamepad1);
			this.BackBraceControl(this.gamepad1);
			this.Hanging(this.gamepad1);
			this.Blocks(this.gamepad1);
			this.BlockDepoly(this.gamepad2);
			this.Zipliners(this.gamepad2);

			// Emit telemetry with the freshest possible values
			this.telemetry.update();

			// Let the rest of the system run until there's a stimulus from the robot controller runtime.
			this.idle();
		}
	}

	void Blocks(Gamepad pad)
	{
		if (pad.left_bumper)
		{
			this.blockCollector.setPower(.9);
		}
		else if(pad.right_bumper)
		{
			this.blockCollector.setPower(-0.9);
		}
		else
		{
			this.blockCollector.setPower(0);
		}
	}

	void BackBraceControl(Gamepad pad)
	{
		if (Math.abs(pad.right_trigger) > .1) {
			backBrace.setPower(pad.right_trigger);
		}
		else if (Math.abs(pad.left_trigger) > .1)
		{
			backBrace.setPower(-pad.left_trigger);
		}
		else
		{
			backBrace.setPower(0);
		}
	}

	void DriveControl(Gamepad pad) throws InterruptedException {
		// Remember that the gamepad sticks range from -1 to +1, and that the motor
		// power levels range over the same amount
		float leftPower = pad.left_stick_y;
		float rightPower = pad.right_stick_y;

		float backWheelPower = (leftPower + rightPower) / 2f;

		// drive the motors
		this.leftDrive.setPower(leftPower);
		this.rightDrive.setPower(rightPower);
		this.backWheel.setPower(backWheelPower);
	}

	void Hanging(Gamepad pad)
	{
		//moves the arm up and down
		if (Math.abs(pad.left_stick_y) > .1)
		{
			this.hangingArm.setPower(pad.left_stick_y);
		}
		else
		{
			this.hangingArm.setPower(0);
		}

		//moves the servo that angles the tape measure
		if (Math.abs(pad.right_stick_y) > .1)
		{
			ServoPosition += pad.right_stick_y / 100;
		}

		hangingControl.setPosition(ServoPosition);
	}

	void BlockDepoly(Gamepad pad)
	{
		if(pad.left_trigger > 0.1) {
			blockConveyer.setPosition(0);
		} else if (pad.right_trigger > 0.1) {
			blockConveyer.setPosition(1);
		} else {
			blockConveyer.setPosition(0.55);
		}

		if(pad.left_bumper)
		{
			this.leftGate.setPosition(.55); //open
			this.rightGate.setPosition(.95); //close
		}

		if(pad.right_bumper)
		{
			this.leftGate.setPosition(0); //close
			this.rightGate.setPosition(.20); //open
		}

		if(pad.dpad_up)
		{
			this.leftGate.setPosition(0); //close
			this.rightGate.setPosition(.95); //open
		}


	}

	void Zipliners(Gamepad pad)
	{
		if(pad.x) {
			this.leftWing.setPosition(.3);
		} else if(pad.b) {
			this.rightWing.setPosition(.3);
		} else {
			this.leftWing.setPosition(.7);
			this.rightWing.setPosition(.7);
		}
	}

}
