package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * A skeletal example of a do-nothing first OpMode. Go ahead and change this code
 * to suit your needs, or create sibling OpModes adjacent to this one in the same
 * Java package.
 */
@TeleOp(name="Right of Mountain Autonomous")
public class RightOfMountainAutonomous extends SynchronousOpMode {
	public IMU Robot;

    @Override
    public void main() throws InterruptedException {
		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		DcMotor BackBrace = hardwareMap.dcMotor.get("back_brace");
		DcMotor BackWheel = this.hardwareMap.dcMotor.get("back_wheel");

		BackWheel.setDirection(DcMotor.Direction.REVERSE);
		RightMotor.setDirection(DcMotor.Direction.REVERSE);

		Robot = new IMU(LeftMotor, RightMotor, hardwareMap, telemetry, this);

		waitForStart();

		double InitialRotation = Robot.Rotation();
		double BackBraceInitial = BackBrace.getCurrentPosition();

		Robot.Straight(.7f);
		Robot.Stop();

		//back brace to correct height
		while (Math.abs(BackBraceInitial - BackBrace.getCurrentPosition()) < 1440 * 3.5)
		{
			//need to do this whenever not using rotation libraries
			Robot.UpdateAngles();

			telemetry.addData("11", Math.abs(BackBraceInitial - BackBrace.getCurrentPosition()));
			BackBrace.setPower(.5);
		}
		BackBrace.setPower(0);

		//get set up on the line
		Robot.Straight(.5f);

		//current rotation minus initial rotation
		double AdditionalTurnDegrees = (Robot.Rotation() - InitialRotation) + 45;
		telemetry.log.add(AdditionalTurnDegrees + " additional degrees to turn.");
		Robot.Turn(-135 + (float)AdditionalTurnDegrees, "Right");

		Robot.Straight(2.0f);

		Robot.Stop();
    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
