package org.usfirst.ftc.exampleteam.yourcodehere;

import android.util.Log;

import com.qualcomm.robotcore.robot.Robot;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.TeleOp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@TeleOp(name="ColorPicker")
public class ColorPicker extends SynchronousOpMode {

    @Override
    public void main() throws InterruptedException {

        IMU Robot = new IMU(hardwareMap, telemetry, this);
        waitForStart();

		Robot.Straight(1);
		Robot.Turn(-45);
		Robot.Straight(3.1f);
		Robot.Turn(-45);
		Robot.Turn(180);
		Robot.Straight(-0.7f);
		Robot.Stop();
		Thread.sleep(1000);

		Trigger.takeImage();
		Thread.sleep(2000);

		String side = Trigger.findAvgSides();
		Thread.sleep(4000);
		Robot.Straight(1.0f);
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
