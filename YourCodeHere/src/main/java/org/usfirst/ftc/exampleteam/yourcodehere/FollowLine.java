package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.*;

public class FollowLine {

	private static ColorSensor mColorSensor = null;

	private static boolean onLine() {
		if (mColorSensor.red() < 150
		    && mColorSensor.green() < 150
		    && mColorSensor.blue() < 150) {
			return false;
		} else {
			return true;
		}
	}

	public static void FollowLine(IMU imu, ColorSensor colorSensor) throws InterruptedException {
		boolean foundLine = false;
		int turnModifier = 1;

		mColorSensor = colorSensor;

		// TODO: make sure that we've actually found the line

		// We should be just to the left of the line now, so start the loop
		while (true) {

			imu.Turn(3 * turnModifier);

			while (onLine()) {
				// When we're on the line, there's no point in turning more
				imu.Straight(1);
				foundLine = true;
			}

			// This is executed conditionally because this runs even when we've been away from the line for a while
			if (foundLine) {
				// We just now drove off the line, so reverse the direction
				turnModifier *= -1;
				foundLine = false;
			}
		}
	}
}
