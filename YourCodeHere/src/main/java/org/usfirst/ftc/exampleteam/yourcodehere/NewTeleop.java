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
	Servo hangLock = null;
	Servo allClear = null;
	Servo climberDeploy = null;


	//this is the default servo position for the hang adjust servo
	double HangingServoPosition = 1;
	int backBraceShouldBe;


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
		this.hangingControl = this.hardwareMap.servo.get("hang_adjust");
		this.blockConveyer = this.hardwareMap.servo.get("block_conveyor");
		this.rightGate = this.hardwareMap.servo.get("right_ramp");
		this.leftGate = this.hardwareMap.servo.get("left_ramp");
		this.leftWing = this.hardwareMap.servo.get("left_wing");
		this.rightWing = this.hardwareMap.servo.get("right_wing");
		this.hangLock = this.hardwareMap.servo.get("hang_stop");
		this.allClear = this.hardwareMap.servo.get("all_clear");
		this.climberDeploy = this.hardwareMap.servo.get("climber_deploy");

		this.backWheel.setDirection(DcMotor.Direction.REVERSE);
		this.leftDrive.setDirection(DcMotor.Direction.REVERSE);
		this.leftGate.setPosition(0); //close
		this.rightGate.setPosition(1); //close
		this.hangLock.setPosition(0.72);
		this.blockConveyer.setPosition(.55);
		this.leftWing.setPosition(.2);
		this.rightWing.setPosition(.6);

		this.backBraceShouldBe = backBrace.getCurrentPosition();

		// Wait until we've been given the ok to go
		this.waitForStart();

		// Enter a loop processing all the input we receive
		while (this.opModeIsActive())
		{
			updateGamepads();

			this.DriveControl(this.gamepad1);
			this.BackBraceControl(this.gamepad1);
			this.Hanging(this.gamepad2);
			this.Blocks(this.gamepad1);
			this.BlockDeploy(this.gamepad2);
			this.Zipliners(this.gamepad2);
			this.allClear(this.gamepad1);

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
			this.blockCollector.setPower(1);
		}
		else if(pad.right_bumper)
		{
			this.blockCollector.setPower(-1);
		}
		else
		{
			this.blockCollector.setPower(0);
		}
	}

	void BackBraceControl(Gamepad pad)
	{

		if (Math.abs(pad.right_trigger) > .1)
		{
			backBraceShouldBe = backBrace.getCurrentPosition();
			backBrace.setPower(pad.right_trigger);
			telemetry.addData("00", "right trigger pressed");
		}
		else if (Math.abs(pad.left_trigger) > .1)
		{
			backBraceShouldBe = backBrace.getCurrentPosition();
			backBrace.setPower(-pad.left_trigger);
			telemetry.addData("00", "left trigger pressed");
		}
//		else if (backBrace.getCurrentPosition() > backBraceShouldBe) {
//			backBrace.setPower(0.5);
//		} else if (backBrace.getCurrentPosition() < backBraceShouldBe) {
//			backBrace.setPower(-0.5);
//		}
		else
		{
			backBrace.setPower(0);
			telemetry.addData("00", "no trigger pressed");
		}

		telemetry.addData("01", String.valueOf(backBraceShouldBe));

	}

	void DriveControl(Gamepad pad) throws InterruptedException {
		// Remember that the gamepad sticks range from -1 to +1, and that the motor
		// power levels range over the same amount
		float leftPower = pad.left_stick_y;
		float rightPower = pad.right_stick_y;

		float backWheelPower = (leftPower + rightPower) / 2f;

		telemetry.addData("01", backWheelPower);

		// drive the motors
		this.leftDrive.setPower(leftPower);
		this.rightDrive.setPower(rightPower);
		this.backWheel.setPower(backWheelPower);
	}

	void Hanging(Gamepad pad)
	{
		//engage hang lock servo
		if (pad.dpad_up) {
			this.hangLock.setPosition(.9);
		} else if (pad.dpad_down) {
			this.hangLock.setPosition(.72);
		}

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
			HangingServoPosition += pad.right_stick_y / 200;
		}

		if(HangingServoPosition >= 0 && HangingServoPosition <= 1) {
			hangingControl.setPosition(HangingServoPosition);
		} else if (HangingServoPosition > 1) {
			hangingControl.setPosition(1);
		} else if (HangingServoPosition < 0) {
			hangingControl.setPosition(0);
		}




	}

	void allClear(Gamepad pad) {
		if(pad.y) {
			this.allClear.setPosition(0); //engaged

		} else if(pad.a) {
			this.allClear.setPosition(1); //disengaged
		}
	}

	void BlockDeploy(Gamepad pad)
	{
		double LeftRandom;
		if (pad.left_bumper) {
			LeftRandom = Math.floor(Math.random() / 10);
		}
		else
		{
			LeftRandom = 0;
		}

		double RightRandom;
		if (pad.left_bumper) {
			RightRandom = Math.floor(Math.random() / 10);
		}
		else
		{
			RightRandom = 0;
		}


		if(pad.left_trigger > 0.1)
		{
			blockConveyer.setPosition(0);
			telemetry.addData("01", "deploying blocks left");
		}
		else if (pad.right_trigger > 0.1)
		{
			blockConveyer.setPosition(1);
			telemetry.addData("01", "deploying blocks right");
		}
		else {
			blockConveyer.setPosition(0.55);
			telemetry.addData("01", "not deploying blocks");
		}

		if(pad.left_bumper)
		{
			this.leftGate.setPosition(.55); //open
			this.rightGate.setPosition(.9 + LeftRandom); //close
		}

		if(pad.right_bumper)
		{
			this.leftGate.setPosition(0); //close
			this.rightGate.setPosition(.10 + RightRandom); //open
		}

		// close both gates for collection
		if(pad.y)
		{
			this.leftGate.setPosition(0); //close
			this.rightGate.setPosition(.9); //close
		}
	}

	void Zipliners(Gamepad pad)
	{
		if(pad.x)
		{
			this.leftWing.setPosition(.8);
		}
		else if(pad.b)
		{
			this.rightWing.setPosition(.05);
		}
		else if(!pad.x && !pad.b)
		{
			this.leftWing.setPosition(.2);
			this.rightWing.setPosition(.6);
		}

		if (pad.a) {
			this.climberDeploy.setPosition(1); //go forward
		} else {
			this.climberDeploy.setPosition(0.55);
		}
	}


}
