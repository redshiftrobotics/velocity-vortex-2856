package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;
import org.usfirst.ftc.exampleteam.yourcodehere.FollowLine;

@TeleOp(name="Line Follow test")
public class LineFollowOp extends SynchronousOpMode {
	//motors declarations
	DcMotor motorLeft = null;
	DcMotor motorRight = null;

	@Override
	protected void main() throws InterruptedException {
		//initialize motors
		this.motorLeft = this.hardwareMap.dcMotor.get("left_motor");
		this.motorRight = this.hardwareMap.dcMotor.get("right_motor");

		//reverse the left motor
		this.motorRight.setDirection(DcMotor.Direction.REVERSE);

		// Wait until we've been given the ok to go
		this.waitForStart();

		// Enter a loop processing all the input we receive
		while (this.opModeIsActive()) {
			updateGamepads();

			FollowLine.FollowLine(hardwareMap, this);

			// Emit telemetry with the freshest possible values
			this.telemetry.update();

			// Let the rest of the system run until there's a stimulus from the robot controller runtime.
			this.idle();
		}
	}
}
