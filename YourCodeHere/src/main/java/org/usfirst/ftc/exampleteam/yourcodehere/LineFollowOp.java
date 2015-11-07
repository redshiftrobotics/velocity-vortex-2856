package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;
import org.usfirst.ftc.exampleteam.yourcodehere.FollowLine;

@TeleOp(name="Line Follow test")
public class LineFollowOp extends SynchronousOpMode {
	@Override
	protected void main() throws InterruptedException {
		// Enter a loop processing all the input we receive
		FollowLine Line = new FollowLine(hardwareMap, this);

		// Wait until we've been given the ok to go
		waitForStart();

		Line.Straight(2);
	}
}
