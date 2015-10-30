package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.swerverobotics.library.TelemetryDashboardAndLog;

import java.util.ArrayList;

/**
 * Created by Isaac Zinda on 10/21/2015.
 */
public class DriveMotor {
	HardwareMap hardwareMap;
	TelemetryDashboardAndLog telemetry;
	DcMotorController DriveController;
	DcMotor Object;
	float CurrentRPS = 0;
	double TargetRPS = 1;
	boolean Stop = true;

	int EncorderCountsPerRotation = 1440;

	ArrayList Positions = new ArrayList<Double>();
	ArrayList Times = new ArrayList<Double>();
	ElapsedTime ProgramTime;


	//constructor
	public DriveMotor(HardwareMap map, TelemetryDashboardAndLog tel, String MotorName, DcMotor.Direction MotorDirection)
	{
		//sets the important globals
		hardwareMap = map;
		telemetry = tel;

		//assume that the drive controller is named drive_controller
		DriveController = hardwareMap.dcMotorController.get("drive_controller");
		Object = hardwareMap.dcMotor.get(MotorName);
		Object.setDirection(MotorDirection); //motor direction

		//start the timer
		ProgramTime = new ElapsedTime();
	}

	public long Position()
	{
		return Object.getCurrentPosition();
	}

	public void Update() throws InterruptedException
	{
		//break out if we are stopped
		if(Stop == true)
		{
			return;
		}

		Times.add((Double) ProgramTime.time());
		Positions.add((Double) ((double) Object.getCurrentPosition()));

		//if there are more than 10 values, delete the first ones
		if(Positions.size() > 5)
		{
			Positions.remove(0);
			Times.remove(0);
		}

		//if we can actually create data...
		if(Positions.size() > 1)
		{
			Double EncodersPerSecond = (((Double) Positions.get(0)) - ((Double) Positions.get(Positions.size() - 1))) / (((Double) Times.get(0)) - ((Double) Times.get(Times.size() - 1)));

			//here we add to the RPS the difference between the desired and target RPS
			CurrentRPS += (TargetRPS - (EncodersPerSecond / EncorderCountsPerRotation));

			float Constant = .01f;

			Object.setPower(Bound(CurrentRPS * Constant));

			telemetry.addData("13", EncodersPerSecond);
			telemetry.addData("11", Bound(CurrentRPS * Constant));
		}
	}

	private float Bound(float Number)
	{
		if(Number > 1)
		{
			Number = 1;
		}
		else if (Number < -1)
		{
			Number = -1;
		}

		return Number;
	}


	public void SetSpeed(double Speed)
	{
		if(Speed == 0)
		{
			Stop = true;

			Times.clear();
			Positions.clear();

			Object.setPower(0);
		}
		else
		{
			Stop = false;

			//the speed should be in RPS
			TargetRPS = Speed;
		}



		//we don't clear the CurrentRPS because we don't want the speed to reset at every SetSpeed call
	}
}
