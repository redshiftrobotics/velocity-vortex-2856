package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.TeleOp;


/**
 *Do NOT USE!
 */


/**
 * A skeletal example of a do-nothing first OpMode. Go ahead and change this code
 * to suit your needs, or create sibling OpModes adjacent to this one in the same
 * Java package.
 */
@TeleOp(name="LeftOfRamp")
public class LeftOfRamp extends SynchronousOpMode {
	public IMU Robot;

    @Override
    public void main() throws InterruptedException {
		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		DcMotor BackBrace = hardwareMap.dcMotor.get("back_brace");
		DcMotor BackWheel = hardwareMap.dcMotor.get("back_wheel");
		//Servo ButtonServo = hardwareMap.servo.get("button_servo");
		Servo hooker = this.hardwareMap.servo.get("hooker");
		Servo dumper = this.hardwareMap.servo.get("climber_control");

		RightMotor.setDirection(DcMotor.Direction.REVERSE);

		Robot = new IMU(LeftMotor, RightMotor, hardwareMap, telemetry, this);
		//FollowLine follower = new FollowLine(LeftMotor, RightMotor, hardwareMap, this, Robot);

		hooker.setPosition(.3);
		dumper.setPosition(0);
		waitForStart();

		double InitialRotation = Robot.Rotation();
		double BackBraceInitial = BackBrace.getCurrentPosition();


		Robot.Straight(.7f);
		Robot.Stop();

		//back brace to correct height
		while (Math.abs(BackBraceInitial - BackBrace.getCurrentPosition()) < 1440 * 3.7)
		{
			//need to do this whenever not using rotation libraries
			Robot.UpdateAngles();

			telemetry.addData("11", Math.abs(BackBraceInitial - BackBrace.getCurrentPosition()));
			BackBrace.setPower(.5);
		}
		BackBrace.setPower(0);

		//get set up on the line
		Robot.Straight(.55f);
		Robot.Turn(135, "Left");
		BackWheel.setPower(-1);
		Robot.Straight(1.0f);
		BackWheel.setPower(0);
//		//current rotation minus initial rotation
//		double AdditionalTurnDegrees = (Robot.Rotation() - InitialRotation) + 45;
//		telemetry.log.add(AdditionalTurnDegrees + " additional degrees to turn.");
//
//		//turn, accounting for additional degrees
//		Robot.Turn(135 - (float) AdditionalTurnDegrees, "Left");
//
//		Robot.Straight(-1);


		Robot.Stop();

//		Trigger.takeImage();
//		Thread.sleep(1000);
//
//		if(Trigger.determineSides() == "left") {
//
//		} else if (Trigger.determineSides() == "right") {
//			//ButtonServo.setPosition(180);
//		} else {
//			Robot.Straight(-1);
//		}
//

    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
