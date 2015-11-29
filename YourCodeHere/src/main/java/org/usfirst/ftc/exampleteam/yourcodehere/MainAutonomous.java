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
@TeleOp(name="Main Autonomous")
public class MainAutonomous extends SynchronousOpMode {
	public IMU Robot;

    @Override
    public void main() throws InterruptedException {
		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		DcMotor BackBrace = hardwareMap.dcMotor.get("back_brace");
		DcMotor BackWheel = hardwareMap.dcMotor.get("back_wheel");
		Servo dumper = this.hardwareMap.servo.get("climber_control");
		Servo leftDebris = this.hardwareMap.servo.get("left_debris");
		Servo rightDebris = this.hardwareMap.servo.get("right_debris");

		LeftMotor.setDirection(DcMotor.Direction.REVERSE);

		Robot = new IMU(LeftMotor, RightMotor, hardwareMap, telemetry, this);
		//FollowLine follower = new FollowLine(LeftMotor, RightMotor, hardwareMap, this, Robot);

		//set positions of servos
		rightDebris.setPosition(0);
		leftDebris.setPosition(1);
		dumper.setPosition(0);

		waitForStart();

		double InitialRotation = Robot.Rotation();
		double BackBraceInitial = BackBrace.getCurrentPosition();

		Robot.Straight(1.5f);
		Robot.Stop();

		//back brace to correct height
		while (Math.abs(BackBraceInitial - BackBrace.getCurrentPosition()) < 1440 * 3.7)
		{
			//need to do this whenever not using rotation libraries
			Robot.UpdateAngles();

			BackBrace.setPower(1);
		}
		BackBrace.setPower(0);

		Robot.Turn(-45);

		Robot.Straight(6.5f);

		//current rotation minus initial rotation
		double AdditionalTurnDegrees = (Robot.Rotation() - InitialRotation) + 45;
		telemetry.log.add(AdditionalTurnDegrees + " additional degrees to turn.");

		Robot.Stop();

		//back brace to correct height
		while (Math.abs(BackBraceInitial - BackBrace.getCurrentPosition()) > 200)
		{
			//need to do this whenever not using rotation libraries
			Robot.UpdateAngles();

			BackBrace.setPower(-.1);
		}
		BackBrace.setPower(0);

		Robot.Turn(135 - (float) AdditionalTurnDegrees, "Left");

		Robot.Straight(-2);

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


    }



	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
