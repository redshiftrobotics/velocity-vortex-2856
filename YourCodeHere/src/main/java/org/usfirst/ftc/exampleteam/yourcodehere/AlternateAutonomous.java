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
@TeleOp(name="Alternate Autonomous")
public class AlternateAutonomous extends SynchronousOpMode {
    /* Declare here any fields you might find useful. */
    // DcMotor motorLeft = null;
    // DcMotor motorRight = null;
	public IMU Robot;

    @Override
    public void main() throws InterruptedException {
		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		LeftMotor.setDirection(DcMotor.Direction.REVERSE);

		Robot = new IMU(LeftMotor, RightMotor, hardwareMap, telemetry, this);
		FollowLine follower = new FollowLine(LeftMotor, RightMotor, hardwareMap, this, Robot);

		waitForStart();

		double InitialRotation = Robot.Rotation();

		//get set up on the line
		Robot.Straight(.8f);
		Robot.Turn(-45);

		Robot.Straight(3.9f);

		Robot.Stop();

		Robot.Straight(-.5f);

		Robot.SlowStop(-1);

		//current rotation minus initial rotation
		double AdditionalTurnDegrees = (Robot.Rotation() - InitialRotation) + 45;
		telemetry.log.add(AdditionalTurnDegrees + " additional degrees to turn.");

		//turn, accounting for additional degrees
		Robot.Turn(135 - (float)AdditionalTurnDegrees);

		Robot.Stop();
    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
