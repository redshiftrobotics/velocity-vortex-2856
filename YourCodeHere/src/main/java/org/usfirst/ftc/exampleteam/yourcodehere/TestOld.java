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
@TeleOp(name="TestOld")
public class TestOld extends SynchronousOpMode {
    /* Declare here any fields you might find useful. */
    // DcMotor motorLeft = null;
    // DcMotor motorRight = null;


    @Override
    public void main() throws InterruptedException {
        /* Initialize our hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names you assigned during the robot configuration
         * step you did in the FTC Robot Controller app on the phone.
         */

//		FollowLine follower = new FollowLine(hardwareMap, this);
//		IMU Robot = new IMU(hardwareMap, telemetry, this);

		waitForStart();

		Trigger.takeImage();
		Thread.sleep(1000);
		Log.d("##Blue is on the: ###", Trigger.determineSides());

//		Float startRotations = Robot.Rotation();
//		follower.Straight(5);
//		Robot.Turn(startRotations - Robot.Rotation());



    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
