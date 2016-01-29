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

	@Override
	public void main() throws InterruptedException {
		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		DcMotor BackBrace = hardwareMap.dcMotor.get("back_brace");
		//Servo ClimberDeployment = this.hardwareMap.servo.get("climber_control");
		ColorSensor colorSensor = hardwareMap.colorSensor.get("color_sensor");

		//we don't know which one to reverse yet
		LeftMotor.setDirection(DcMotor.Direction.REVERSE);

		//setup the PID stuff
		Robot = new NewIMU(LeftMotor, RightMotor, colorSensor, hardwareMap, telemetry, this);

		//select the side
		SelectSide();

		//wait for start
		waitForStart();

		//this is the initial rotation; it will be referenced through the entire program
		double InitialRotation = Robot.Rotation();

		//maximize turning power
		Robot.TurningPower = 1f;

		int FirstTurnOffset = 0;

		if (side.equals("blue")) {
			Robot.Turn(45 - FirstTurnOffset, "Left", 3);
		}
		else
		{
			Robot.Turn(-45 + FirstTurnOffset, "Right", 3);
		}

		// set the power to max
		Robot.Power = 1f;

		//set the robot to stop when it hits the white line
		Robot.StopAtLight = true;

		// move backwards, the encoder count is arbitrary
		Robot.Straight(10f, 15);
		Robot.Stop();

		//prevent the robot from stopping at the light again
		Robot.StopAtLight = false;

		// this is the offset that each turn will have
		int Offset = 5;

		// turn to line up with
		if (side.equals("blue")) {
			// rotate 10 degrees too few
			Robot.TurnToAngle((float) InitialRotation + 90 - Offset, "Left", 5);
		}
		else
		{
			//rotate 10 degrees too many
			Robot.TurnToAngle((float) InitialRotation - 90 - Offset, "Right", 5);
		}

		//get the position of the encoder at the backup
		int BackupStartEncoder = LeftMotor.getCurrentPosition();

		//setup the variables for the backup timeout
		Date a = new Date();
		long BackupStartTime = a.getTime();
		long BackupCurrentTime = BackupStartTime;

		//set the light sensor threshold
		int Threshold = 60;

		//do this for 3 seconds
		while (Math.abs(BackupStartTime - BackupCurrentTime) < 3000)
		{
			Date b = new Date();
			BackupCurrentTime = b.getTime();

			if(colorSensor.green() < Threshold) {
				LeftMotor.setPower(0);
				RightMotor.setPower(.6);
			}
			else
			{
				LeftMotor.setPower(.6);
				RightMotor.setPower(0);
			}
		}

		Robot.Stop();
	}

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
