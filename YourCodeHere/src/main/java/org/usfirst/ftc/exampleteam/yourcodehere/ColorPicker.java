package org.usfirst.ftc.exampleteam.yourcodehere;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.TeleOp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

//@TeleOp(name="ColorPicker")
public class ColorPicker extends SynchronousOpMode {

    @Override
    public void main() throws InterruptedException {
		DcMotor LeftMotor = hardwareMap.dcMotor.get("left_drive");
		DcMotor RightMotor = hardwareMap.dcMotor.get("right_drive");
		LeftMotor.setDirection(DcMotor.Direction.REVERSE);

        IMU Robot = new IMU(LeftMotor, RightMotor, hardwareMap, telemetry, this);
        waitForStart();


		Robot.Straight(1, 1);
		Robot.Turn(-35);
		//Robot.Straight(3.1f);
		//linefollower.Straight(3.1f);
		//Robot.Turn(-45);
		Robot.Turn(180);
		Robot.Straight(-0.87f, 1);
		Robot.Stop();
		Thread.sleep(300);

		Trigger.takeImage();
		Thread.sleep(300);

		String side = Trigger.findAvgSides();
		Thread.sleep(1000);
		Robot.Straight(1.0f, 1);
		if(side == "left") {
			Log.d("left", "Blue is on the left");
			Robot.Turn(-100);
			Robot.Stop();
		} else if (side == "right") {
			Log.d("right", "Blue is on the right");
			Robot.Turn(100);
			Robot.Stop();
		} else {
			Log.d("FindAVGSides", "Inconclusive Result");
		}

    }

	public void RunIdle() throws InterruptedException
	{
		idle();
	}
}
