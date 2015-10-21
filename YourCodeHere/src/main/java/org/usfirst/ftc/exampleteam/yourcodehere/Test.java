package org.usfirst.ftc.exampleteam.yourcodehere;

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

        IMU Robot = new IMU(hardwareMap, telemetry, this);

        waitForStart();

		DriveMotor Left = new DriveMotor(hardwareMap, telemetry, "left_drive", DcMotor.Direction.FORWARD);

		float Speed = 0;

		while(true)
		{
			Speed += .001;
			Left.SetSpeed(Speed);

			Left.Update();
			telemetry.addData("12", Speed);
			telemetry.update();
		}

//		while(true) {
//
//			IMU.Forward(2);
//
//			//IMU.Stop();
//
		//Robot.Forward(5);
		//Robot.Turn(90);
		//Robot.Stop();
//
//			//IMU.Stop();
//		}
//		while (true)
//		{
//			IMU.Forward(4);
//			IMU.Turn(180);
//			IMU.Forward(4);
//			IMU.Turn(-180);
//		}
    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
