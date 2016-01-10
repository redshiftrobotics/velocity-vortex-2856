package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * An example of a synchronous opmode that implements a simple drive-a-bot. 
 */
@TeleOp(name="Test Chassis TeleOp")
public class TestChassisTeleop extends SynchronousOpMode
{
	//motor / servo declarations
	DcMotor leftDrive = null;
	DcMotor rightDrive = null;
	DcMotor sucker = null;
	Servo blockDriver = null;
	Servo leftGate = null;
	Servo rightGate = null;
	protected void main() throws InterruptedException
	{
		//initialize motors
		this.leftDrive = this.hardwareMap.dcMotor.get("left_drive");
		this.rightDrive = this.hardwareMap.dcMotor.get("right_drive");
		this.rightDrive.setDirection(DcMotor.Direction.REVERSE);

		this.sucker = this.hardwareMap.dcMotor.get("sucker");

		this.leftGate = this.hardwareMap.servo.get("left_gate");
		this.rightGate = this.hardwareMap.servo.get("right_gate");
		this.blockDriver = this.hardwareMap.servo.get("block_driver");



		// Wait until we've been given the ok to go
		this.waitForStart();

		// Enter a loop processing all the input we receive
		while (this.opModeIsActive())
		{
			updateGamepads();

			this.DriveControl(this.gamepad1);
			this.GateControl(this.gamepad2);

			// Emit telemetry with the freshest possible values
			this.telemetry.update();

			// Let the rest of the system run until there's a stimulus from the robot controller runtime.
			this.idle();
		}
	}


	void DriveControl(Gamepad pad) throws InterruptedException {
		// Remember that the gamepad sticks range from -1 to +1, and that the motor
		// power levels range over the same amount
		float leftPower = pad.right_stick_y;
		float rightPower = pad.left_stick_y;
		// drive the motors
		this.leftDrive.setPower(leftPower / 2);
		this.rightDrive.setPower(rightPower / 2);



	}

	void GateControl(Gamepad pad)
	{
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

//		if(pad.b) {
//			this.blockDriver.setPosition(1);
//		} else if(pad.x) {
//			this.blockDriver.setPosition(0);
//		} else {
//			this.blockDriver.setPosition(0.55);
//		}

		if(pad.right_stick_x > 0.1) {
			this.blockDriver.setPosition(1);
		} else if (pad.right_stick_x < -0.1) {
			this.blockDriver.setPosition(0);
		} else {
			this.blockDriver.setPosition(0.55);
		}

		sucker.setPower(pad.left_stick_y / 2);

	}
}
