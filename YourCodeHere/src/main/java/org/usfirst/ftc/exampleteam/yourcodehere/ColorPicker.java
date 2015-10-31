package org.usfirst.ftc.exampleteam.yourcodehere;

import android.util.Log;

import com.qualcomm.robotcore.robot.Robot;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.TeleOp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A skeletal example of a do-nothing first OpMode. Go ahead and change this code
 * to suit your needs, or create sibling OpModes adjacent to this one in the same
 * Java package.
 */
@TeleOp(name="ColorPicker")
public class ColorPicker extends SynchronousOpMode {
    /* Declare here any fields you might find useful. */
    // DcMotor motorLeft = null;
    // DcMotor motorRight = null;


    @Override
    public void main() throws InterruptedException {
        /* Initialize our hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names you assigned during the robot configuration
         * step you did in the FTC Robot Controller app on the phone.
         */




//
        IMU Robot = new IMU(hardwareMap, telemetry, this);
//
        waitForStart();
		Robot.Forward(1);
		//true for left, false for right
		//Robot.timedTurn(300, true);
		Robot.Turn(90);
		Robot.Forward(2);



//		Trigger.takeImage();
//
//		String side = Trigger.findAvgSides();
//
//		if(side == "left") {
//			Log.d("right", "Blue is on the left");
//		} else if (side == "right") {
//			Log.d("right", "Blue is on the right");
//		} else {
//			Log.d("rekt", "Something broke");
//		}

    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
