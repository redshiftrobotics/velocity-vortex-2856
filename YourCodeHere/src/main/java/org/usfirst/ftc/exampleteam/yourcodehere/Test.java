package org.usfirst.ftc.exampleteam.yourcodehere;

import android.util.Log;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;


import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * A skeletal example of a do-nothing first OpMode. Go ahead and change this code
 * to suit your needs, or create sibling OpModes adjacent to this one in the same
 * Java package.
 */
@TeleOp(name="Test")
public class Test extends SynchronousOpMode {
    /* Declare here any fields you might find useful. */
    // DcMotor motorLeft = null;
    // DcMotor motorRight = null;


    @Override
    public void main() throws InterruptedException {
        /* Initialize our hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names you assigned during the robot configuration
         * step you did in the FTC Robot Controller app on the phone.
         */


		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		LeftMotor.setDirection(DcMotor.Direction.REVERSE);

		FollowLine follower = new FollowLine(LeftMotor, RightMotor, hardwareMap, this);
		IMU Robot = new IMU(LeftMotor, RightMotor, hardwareMap, telemetry, this);

		waitForStart();

		//get set up on the line
		Robot.Straight(2);
		Robot.Turn(-45);

		//follow the line
		follower.Straight(1.2);
		Robot.Stop();

		//turn
		Robot.Turn(-45);
		Robot.Straight(1.5f);
		Robot.Stop();

		Trigger.takeImage();
		Thread.sleep(2000);

		String side = Trigger.findAvgSides();
		Thread.sleep(4000);

		if(side == "left")
		{
			telemetry.addData("31", "Left");
			Robot.Turn(-100);
			Robot.Stop();
		} else if (side == "right")
		{
			telemetry.addData("31", "Right");
			Robot.Turn(100);
			Robot.Stop();
		} else {
			Log.d("FindAVGSides", "Inconclusive Result");
		}

		//Float startRotations = Robot.Rotation();
		//follower.Straight(5);
		//Robot.Turn(startRotations - Robot.Rotation());




    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
