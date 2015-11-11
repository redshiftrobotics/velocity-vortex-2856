package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;

public class FollowLine {

	private static ColorSensor mColorSensor = null;
	private static DcMotor mRightMotor = null;
	private static DcMotor mLeftMotor = null;
	SynchronousOpMode ParentOpMode;

	// Veer to the right by default
	float highPower = 0.3f;
	float lowPower = .1f;

	private static Integer threshold = 60;

	private boolean onLine() {
		return (mColorSensor.red() > threshold || mColorSensor.blue() > threshold);
	}

	public FollowLine(DcMotor LeftMotor, DcMotor RightMotor, HardwareMap hardwareMap, SynchronousOpMode op) throws InterruptedException {
		ParentOpMode = op;

		this.mRightMotor = RightMotor;
		this.mLeftMotor = LeftMotor;

		mColorSensor = hardwareMap.colorSensor.get("color_sensor");
	}

	public void Straight(double Encoder) throws InterruptedException
	{

		long StartPosition = mLeftMotor.getCurrentPosition();

		// We should be just to the left of the line now, so start the loop
		while (Encoder * 1400 > Math.abs((StartPosition) - mLeftMotor.getCurrentPosition())) {
			ParentOpMode.telemetry.addData("00", "red: " + mColorSensor.red());
			ParentOpMode.telemetry.addData("01", mColorSensor.blue());
			ParentOpMode.telemetry.addData("02", mColorSensor.green());


			if (onLine()) {
				mRightMotor.setPower(highPower);
				mLeftMotor.setPower(lowPower);

			} else {
				mRightMotor.setPower(lowPower);
				mLeftMotor.setPower(highPower);
			}

			ParentOpMode.updateGamepads();

			// Release control back to the system
			ParentOpMode.telemetry.update();
			ParentOpMode.idle();
		}

		//stop the robot
		mRightMotor.setPower(0);
		mLeftMotor.setPower(0);
	}
}
