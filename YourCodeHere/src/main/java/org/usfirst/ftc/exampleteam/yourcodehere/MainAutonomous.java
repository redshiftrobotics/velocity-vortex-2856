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

		LeftMotor.setDirection(DcMotor.Direction.REVERSE);

		Robot = new IMU(LeftMotor, RightMotor, BackBrace, hardwareMap, telemetry, this);

		//set positions of servos
		rightDebris.setPosition(1);
		leftDebris.setPosition(.1);
		leftClimberServo.setPosition(0);
		rightClimberServo.setPosition(1);

		waitForStart();

		double InitialRotation = Robot.Rotation();

		telemetry.log.add("Initial rotation of " + InitialRotation);

		//this is the back brace initial position, its a global so that threading can access it
		BackBraceInitial = BackBrace.getCurrentPosition();

		// set the power for the whole program
		Robot.Power = 1f;
		Robot.Straight(1.5f, 5, "BackBraceLower");
		Robot.Stop();

		float Offset = (float)(InitialRotation - Robot.Rotation());

		telemetry.log.add("Offset of " + Offset);

		telemetry.log.add("side " + side);

		if (side.equals("blue")) {
			Robot.Turn(45 + Offset, "Left", "BackBraceLower");
		}
		else
		{
			Robot.Turn(-45 + Offset, "Right", "BackBraceLower");
		}

		//move slightly farther for blue so that it can stay on the left side of the white line
		if (side.equals("blue")) {
			Robot.Straight(6.2f, 10);
		}
		else
		{
			Robot.Straight(5.7f, 10);
		}

		Robot.Stop();

		telemetry.log.add("Backbrace Encoder Dif: " + Math.abs(BackBraceInitial - BackBrace.getCurrentPosition()));

		// Current rotation minus initial rotation
		// There was a bug where the robot would turn 360 degrees and add 360, so take the mod
		double AdditionalTurnDegrees = (((Robot.Rotation() - InitialRotation)));

		telemetry.log.add(AdditionalTurnDegrees + " additional degrees to turn.");

		BackBrace.setPower(0);

		if (side.equals("blue")) {
			float DegreeOffset = 10;
			Robot.Turn((float)(InitialRotation - 90 + DegreeOffset), "Right", "BackBraceRaise");
		}
		else
		{
			float DegreeOffset = 10;
			Robot.TurnToAngle((float)(InitialRotation + 90 + DegreeOffset), "Left", "BackBraceRaise");
		}

		BackBrace.setPower(0);

		// set the last stage rotation
		LastStageRotation = Robot.Rotation();

		Robot.Straight(-.7f, 2);

//		if (side.equals("blue"))
//		{
//			Robot.Straight(-1.5f, 3, "ControlRotation");
//		}
//		else
//		{
//			Robot.Straight(-1.6f, 3);
//		}
//
//		telemetry.log.add("done backing up");
//
//		//deploy climbers
//		ClimberDeployment.setPower(-.15);
//		Thread.sleep(2000);
//		ClimberDeployment.setPower(0);
//		Thread.sleep(500);
//		ClimberDeployment.setPower(.15);
//		Thread.sleep(1500);
//		ClimberDeployment.setPower(0);
//
//		telemetry.log.add("done scoring climbers");

//		Robot.Stop();
//
//		if (side.equals("red"))
//		{
//			Robot.TurnToAngle((float) (InitialRotation - 45 + 180), "Left", "None");
//		}
//		else
//		{
//			Robot.TurnToAngle((float) (InitialRotation + 45 - 180), "Right", "None");
//		}
//
//		telemetry.log.add("moving straight");
//
//		//move forward while lowering the back brace
//		Robot.Straight(2.6f, 5, "BackBraceLower");
//		BackBrace.setPower(0);
//		Robot.Stop();
//
//		if (side.equals("red")) {
//			Robot.Turn(90, "Left");
//		}
//		else
//		{
//			Robot.Turn(-90, "Right");
//		}
//
//		//raise the front blocker servos
//		rightDebris.setPosition(.5);
//		leftDebris.setPosition(.6);
//
//
//		//move up the mountain
//		BackWheel.setPower(-1);
//		LeftMotor.setPower(1);
//		RightMotor.setPower(1);
//
//		Thread.sleep(4000);
//
//		BackWheel.setPower(0);
//
//		Robot.Stop();

		int Threshold = 60;

		while(true)
		{
			if(colorSensor.green() > Threshold)
			{
				LeftMotor.setPower(0);
				RightMotor.setPower(-.4);
			}
			else
			{
				LeftMotor.setPower(-.4);
				RightMotor.setPower(0);
			}

			telemetry.addData("03", colorSensor.green());
		}

		
	}

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
