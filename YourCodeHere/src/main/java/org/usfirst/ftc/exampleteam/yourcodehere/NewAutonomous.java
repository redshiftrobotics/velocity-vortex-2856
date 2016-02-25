package org.usfirst.ftc.exampleteam.yourcodehere;

import android.media.Image;
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
@TeleOp(name="New 2856 Autonomous")
public class NewAutonomous extends SynchronousOpMode {

	public NewIMU Robot;
	public String side = "";

	void SelectSide()
	{
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
	}

	public String TakePicture()
	{
		// get the image here
		String ImageSide = Trigger.determineSides();

		if (this.side == "red")
		{
			if(ImageSide == "right")
			{
				ImageSide = "left";
			}
			else if(ImageSide == "left")
			{
				ImageSide = "right";
			}
		}

		return ImageSide;
	}

	@Override
	public void main() throws InterruptedException {
		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		DcMotor BackBrace = hardwareMap.dcMotor.get("back_brace");
		Servo climberDeploy = hardwareMap.servo.get("climber_deploy");
		Servo blockConveyer = hardwareMap.servo.get("block_conveyor");
		ColorSensor colorSensor = hardwareMap.colorSensor.get("color_sensor");
		Servo leftWing = hardwareMap.servo.get("left_wing");
		Servo rightWing = hardwareMap.servo.get("right_wing");
		Servo rightGate = hardwareMap.servo.get("right_ramp");
		Servo leftGate = hardwareMap.servo.get("left_ramp");
		Servo hangLock = hardwareMap.servo.get("hang_stop");
		Servo hangingControl = this.hardwareMap.servo.get("hang_adjust");

		//we don't know which one to reverse yet
		LeftMotor.setDirection(DcMotor.Direction.REVERSE);

		//setup the PID stuff
		Robot = new NewIMU(LeftMotor, RightMotor, colorSensor, hardwareMap, telemetry, this);

		//select the side
		SelectSide();

		//wait for start
		waitForStart();

		//initialize the servos
		blockConveyer.setPosition(.55);
		climberDeploy.setPosition(.5);
		leftGate.setPosition(0);
		rightGate.setPosition(1);
		leftWing.setPosition(.3);
		rightWing.setPosition(.6);
		hangLock.setPosition(.72);
		hangingControl.setPosition(.6);

		//this is the initial rotation; it will be referenced through the entire program
		double InitialRotation = Robot.Rotation();

		Robot.Power = 1f;

		Robot.Straight(1, 3);

		Robot.Stop();

		//maximize turning power
		Robot.TurningPower = 1f;

		//turn so that blocks go away
		int RedFirstTurnOffset = 11;
		int BlueFirstTurnOffset = 6;

		if (side.equals("blue")) {
			Robot.TurnToAngle((float)InitialRotation + 45 - BlueFirstTurnOffset, "Left", 3);
		}
		else
		{
			Robot.TurnToAngle((float)InitialRotation -45 + RedFirstTurnOffset, "Right", 3);
		}

		//set the robot to stop when it hits the white line
		Robot.StopAtLight = true;

		// move backwards, the encoder count is arbitrary
		Robot.Straight(10f, 15);
		Robot.Stop();

		//prevent the robot from stopping at the light again
		Robot.StopAtLight = false;

		if (side.equals("blue")) {
			Robot.Turn(-50, "Right", 2);
			Robot.Turn(50, "Right", 2);
		}
		else
		{
			Robot.Turn(50, "Left", 2);
			Robot.Turn(-50, "Left", 2);
		}

		// this is the offset that each turn will have
		int RedOffset = 5;
		int BlueOffset = 10;

		// turn to be at a 70 degree angle from the start, this is where we will take a picture
		Robot.TurnToAngle((float) InitialRotation + 70, "Left", 5);

		// get the image here
		String ImageSide = TakePicture();

		// turn the rest of the way
		if (side.equals("blue")) {
			Robot.TurnToAngle((float) InitialRotation + 90 + BlueOffset, "Left", 5);
		}
		else
		{
			Robot.TurnToAngle((float) InitialRotation - 90 - RedOffset, "Right", 5);
		}

		telemetry.log.add("side is " + ImageSide);

		//setup the variables for the backup timeout
		Date a = new Date();
		long BackupStartTime = a.getTime();
		long BackupCurrentTime = BackupStartTime;

		//set the light sensor threshold
		int Threshold = 60;

		// start scoring the climbers
		climberDeploy.setPosition(0);

		//do this for 3 seconds
		while (Math.abs(BackupStartTime - BackupCurrentTime) < 4000)
		{
			Date b = new Date();
			BackupCurrentTime = b.getTime();

			if(side.equals("red")) {
				if (colorSensor.green() < Threshold) {
					LeftMotor.setPower(-.3);
					RightMotor.setPower(.8);
				} else {
					LeftMotor.setPower(.8);
					RightMotor.setPower(-.3);
				}
			}
			else
			{
				if (colorSensor.green() < Threshold) {
					RightMotor.setPower(-.3);
					LeftMotor.setPower(.8);
				} else {
					RightMotor.setPower(.8);
					LeftMotor.setPower(-.3);
				}
			}
		}

		Robot.Stop();

		Thread.sleep(2000);
		climberDeploy.setPosition(.5);

		Robot.TurnToAngle((float)InitialRotation + 90f, "Left", 5);

		Robot.Straight(-1);

		// determine the offset based on the side
		int FinalTurnOffset = 0;

		if (ImageSide == "left")
		{
			FinalTurnOffset = -20;
		}
		else if (ImageSide == "right")
		{
			FinalTurnOffset = 20;
		}

		// turn to the correct position
		Robot.Turn(180 + FinalTurnOffset, "Neither", 3);

		Robot.Straight(-1.5f);

		Robot.Stop();

		idle();
	}

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
