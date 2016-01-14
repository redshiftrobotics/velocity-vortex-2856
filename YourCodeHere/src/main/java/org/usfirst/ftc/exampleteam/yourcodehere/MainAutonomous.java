package org.usfirst.ftc.exampleteam.yourcodehere;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;
import com.qualcomm.ftcrobotcontroller.CustomSettingsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * A skeletal example of a do-nothing first OpMode. Go ahead and change this code
 * to suit your needs, or create sibling OpModes adjacent to this one in the same
 * Java package.
 */
@TeleOp(name="2856 Autonomous")
public class MainAutonomous extends SynchronousOpMode {

	public IMU Robot;
	public double BackBraceInitial;
	public double LastStageRotation;
	// Read from preferences file written by the CustomSettingsActivity to determine what side we are on.
	String side;

	@Override
	public void main() throws InterruptedException {

		// Retrieve file.
		File file = new File("/sdcard/Pictures","prefs");
		StringBuilder text = new StringBuilder();
		// Attempt to load line from file into the buffer.
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			// Ensure that the first line is not null.
			while ((line = br.readLine()) != null) {
				text.append(line);
			}
			// Close the buffer reader
			br.close();
		}
		// Catch exceptions... Or don't because that would require effort.
		catch (IOException e) {
		}

		// Provide in a more user friendly form.
		side = text.toString();

		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		DcMotor BackBrace = hardwareMap.dcMotor.get("back_brace");
		DcMotor BackWheel = hardwareMap.dcMotor.get("back_wheel");
		DcMotor ClimberDeployment = this.hardwareMap.dcMotor.get("climber_control");
		Servo leftDebris = this.hardwareMap.servo.get("left_debris");
		Servo rightDebris = this.hardwareMap.servo.get("right_debris");
		Servo leftClimberServo = this.hardwareMap.servo.get("left_climber");
		Servo rightClimberServo = this.hardwareMap.servo.get("right_climber");
		ColorSensor colorSensor = hardwareMap.colorSensor.get("color_sensor");
		Servo backDebris = this.hardwareMap.servo.get("back_debris");
		Servo beaconServo = this.hardwareMap.servo.get("beacon_servo");


		LeftMotor.setDirection(DcMotor.Direction.REVERSE);

		Robot = new IMU(LeftMotor, RightMotor, BackBrace, hardwareMap, telemetry, this);

		//set positions of servos
		rightDebris.setPosition(.7);
		leftDebris.setPosition(.2);
		leftClimberServo.setPosition(0);
		rightClimberServo.setPosition(1);

		backDebris.setPosition(.5);

		waitForStart();

		double InitialRotation = Robot.Rotation();

		telemetry.log.add("Initial rotation of " + InitialRotation);

		//this is the back brace initial position, its a global so that threading can access it
		BackBraceInitial = BackBrace.getCurrentPosition();

		// set the power for the whole program
		Robot.Power = 1f;
		Robot.Straight(1.5f, 5, "BackBraceLower");
		Robot.Stop();

		telemetry.log.add("side " + side);

		if (side.equals("blue")) {
			Robot.TurnToAngle(35 + (float)InitialRotation, "Left", "BackBraceLower", 5);
		}
		else
		{
			Robot.TurnToAngle(-45 + (float)InitialRotation, "Right", "BackBraceLower", 5);
		}

		//move slightly farther for blue so that it can stay on the left side of the white line
		if (side.equals("blue")) {
			Robot.Straight(6.3f, 10);
		}
		else
		{
			Robot.Straight(5.0f, 10);
		}

		Robot.Stop();

		telemetry.log.add("Backbrace Encoder Dif: " + Math.abs(BackBraceInitial - BackBrace.getCurrentPosition()));

		BackBrace.setPower(0);

		if (side.equals("blue")) {
			Robot.TurnToAngle((float) (InitialRotation - 90), "Right", "BackBraceRaise", 3);
		}
		else
		{
			Robot.TurnToAngle((float)(InitialRotation + 90), "Left", "BackBraceRaise", 3);
		}

		BackBrace.setPower(0);

		// set the last stage rotation
		LastStageRotation = Robot.Rotation();

		Robot.Straight(-.6f, 2);

		// color sensor threshold
		int Threshold = 60;

		while (colorSensor.green() < Threshold)
		{
			LeftMotor.setPower(-.6);
			RightMotor.setPower(0);
		}
		if(side.equals("red")) {
			Robot.TurnToAngle((float) (InitialRotation + 90), "Left", "None", 5);
		}
		else
		{
			Robot.TurnToAngle((float) (InitialRotation - 90), "Left", "None", 5);
		}

		int BackupStartEncoder = LeftMotor.getCurrentPosition();

		Date a = new Date();
		long BackupStartTime = a.getTime();
		long BackupCurrentTime = BackupStartTime;


		Robot.Stop();
		Thread.sleep(5000);

		String beaconBlue = Trigger.determineSides();
		telemetry.log.add("Blue is on the " + beaconBlue);
		if(side.equals("blue")) {

			if (beaconBlue == "left") {
				//flip left
				beaconServo.setPosition(0);
			} else {
				//flip right
				beaconServo.setPosition(1);
			}
		} else {
			if (beaconBlue == "left") {
				//flip to right
				beaconServo.setPosition(1);
			} else {
				//flip to left
				beaconServo.setPosition(0);
			}
		}



		while (Math.abs(LeftMotor.getCurrentPosition() - BackupStartEncoder) < 3000 && Math.abs(BackupStartTime - BackupCurrentTime) < 3000)
		{
			Date b = new Date();
			BackupCurrentTime = b.getTime();

			if(colorSensor.green() < Threshold) {
				LeftMotor.setPower(-.6);
				RightMotor.setPower(0);
			}
			else
			{
				LeftMotor.setPower(0);
				RightMotor.setPower(-6.0f);
			}
		}

		Robot.Stop();

		//deploy climbers
		ClimberDeployment.setPower(-.3);
		Thread.sleep(1000);
		ClimberDeployment.setPower(.3);
		Thread.sleep(1000);
		ClimberDeployment.setPower(0);

		if (side.equals("red"))
		{
			Robot.TurnToAngle((float) (InitialRotation - 45 + 180), "Left", "None", 5);
		}
		else
		{
			Robot.TurnToAngle((float) (InitialRotation + 45 - 180), "Right", "None", 5);
		}

		telemetry.log.add("moving straight");

		if(side.equals("red")) {
			//move forward while lowering the back brace
			Robot.Straight(2.6f, 5, "BackBraceLower");
		}
		else
		{
			Robot.Straight(2.2f, 5, "BackBraceLower");
		}
		BackBrace.setPower(0);
		Robot.Stop();

		//raise the front blocker servos
		rightDebris.setPosition(.5);
		leftDebris.setPosition(.6);

		if (side.equals("red")) {
			Robot.Turn(90, "Left");
		}
		else
		{
			Robot.Turn(-90, "Right");
		}

		//move up the mountain
		BackWheel.setPower(-1);
		LeftMotor.setPower(1);
		RightMotor.setPower(1);

		Thread.sleep(4000);

		BackWheel.setPower(0);

		Robot.Stop();
	}

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
