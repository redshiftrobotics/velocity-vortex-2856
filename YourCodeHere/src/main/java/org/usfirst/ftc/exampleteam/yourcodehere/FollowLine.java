package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.SynchronousOpMode;

public class FollowLine {

	private static ColorSensor mColorSensor = null;
	private static DcMotor mRightMotor = null;
	private static DcMotor mLeftMotor = null;
	private static Integer threshold = 130;

	private static boolean onLine() {
		if (mColorSensor.red() < threshold
		    && mColorSensor.green() < threshold
		    && mColorSensor.blue() < threshold) {
			return false;
		} else {
			return true;
		}
	}

	public static void FollowLine(HardwareMap hardwareMap, SynchronousOpMode op) throws InterruptedException {
		// Veer to the right by default
		float highPower = 0.2f;
		float lowPower = 0.1f;
		float lpower, rpower;

		mColorSensor = hardwareMap.colorSensor.get("color_sensor");
		mRightMotor = hardwareMap.dcMotor.get("left_motor");
		mLeftMotor = hardwareMap.dcMotor.get("right_motor");

		mRightMotor.setDirection(DcMotor.Direction.REVERSE);
		mLeftMotor.setDirection(DcMotor.Direction.REVERSE);

		// TODO: make sure that we've actually found the line

		// We should be just to the left of the line now, so start the loop
		while (true) {
			if (onLine()) {
				lpower = lowPower;
				rpower = highPower;

			} else {
				lpower = highPower;
				rpower = lowPower;
			}

			mRightMotor.setPower(rpower);
			mLeftMotor.setPower(lpower);

			// Release control back to the system
			op.telemetry.update();
			op.idle();
		}
	}
}
