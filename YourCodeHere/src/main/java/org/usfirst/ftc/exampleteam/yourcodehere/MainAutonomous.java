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
@TeleOp(name="Main Autonomous")
public class MainAutonomous extends SynchronousOpMode {
	public IMU Robot;

    @Override
    public void main() throws InterruptedException {
		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		RightMotor.setDirection(DcMotor.Direction.REVERSE);

		Robot = new IMU(LeftMotor, RightMotor, hardwareMap, telemetry, this);
		//FollowLine follower = new FollowLine(LeftMotor, RightMotor, hardwareMap, this, Robot);

		waitForStart();

		//get set up on the line
		Robot.Straight(1.5f);
		Robot.Turn(-45);
		Robot.Straight(1.5f);
		Robot.Turn(-45);
		Robot.Straight(1);

		Robot.Stop();
    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
