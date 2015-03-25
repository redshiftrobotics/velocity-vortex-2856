#pragma config(Hubs,  S3, HTMotor,  none,     none,     none)
#pragma config(Sensor, S4,     ,               sensorI2CCustom)
#pragma config(Sensor, S2,     Gyro,           sensorI2CCustom)
#pragma config(Sensor, S3,     ,               sensorI2CMuxController)
#pragma config(Motor,  mtr_S3_C1_1,     motorD,        tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S3_C1_2,     motorE,        tmotorTetrix, openLoop)

#include "JoystickDriver.c"
#include "Motors.h"
#include "Servos.h"
#include "Menu.c"
#include "Gyro.c"

task CheckTime;
int TopChanellerPosition = 133;
int BottomChanellerPosition = 250;
task main;


bool WaitForStartBool = false;
int ArmPosition = 0;

void ServosDeployBalls()
{
	Servos_SetPosition(S4, 3, 1, TopChanellerPosition);
	Servos_SetPosition(S4, 3, 2, BottomChanellerPosition);
}

void MoveLeft(int Power)
{
	Motors_SetSpeed(S4, 1, 1, Power);
	nxtDisplayString(1, "LeftPower: %i", Power);
}

void MoveCorraller(int Power)
{
	nxtDisplayString(5, "Corraller: %i", Power);
	motor[motorE] = Power;
}

void BallContainer(int Position)
{
	Servos_SetPosition(S4, 3, 2, Position);
}

void RaiseServos()
{
	Servos_SetPosition(S4, 3, 1, 120);
}

void LowerServos()
{
	Servos_SetPosition(S4, 3, 1, 0);
}

void Shoot(int Power)
{
	Motors_SetSpeed(S4, 2, 2, Power);
}

void CorrallerLeftPosition(int Position)
{
	Servos_SetPosition(S4, 3, 3, Position);
}

void CorrallerRightPosition(int Position)
{
	Servos_SetPosition(S4, 3, 4, Position);
}

void MoveRight(int Power)
{
	Motors_SetSpeed(S4, 1, 2, -Power);
	nxtDisplayString(2, "RightPower: %i", -Power);
}

void PickupBlocks(int Power)
{
	motor[motorD] = Power;
	nxtDisplayString(3, "PickupPower: %i", Power);

}

void MoveArm(int Power)
{
	Motors_SetSpeed(S4, 2, 1, Power);
	nxtDisplayString(4, "ArmPower: %i", Power);
}

void WaitForStartMenu()
{
	string t = "Wait for Start";
	string b = "Yes";
	string c = "No";
	int MenuReturn = Menu(t, b, c);

	if(MenuReturn == 0)
	{
		WaitForStartBool = true;
	}
	else if(MenuReturn == 1)
	{
		WaitForStartBool = false;
	}
}

task CheckTime()
{
	while(true)
	{
		sleep(50);
		if(time1[T2] > 29000)
		{
			while(true)
			{
				MoveLeft(0);
				MoveRight(0);
				MoveArm(0);
				Shoot(0);
				MoveCorraller(0);
				PickupBlocks(0);
				stoptask(main);
			}
		}
	}
}

task main()
{
	long DownPosition = Motors_GetPosition(S4, 4, 2);
	long UpPosition = DownPosition + 230;

	//turn off joystick debug
	bDisplayDiagnostics = false;

	//lets the used pick the program
	WaitForStartMenu();

	eraseDisplay();

	//waits for start it bool is set
	if(WaitForStartBool)
	{
		waitForStart();
	}

	ClearTimer(T2);

	StartTask(CheckTime);

	Motors_SetPosition(S4, 4, 2, UpPosition, 50);

	Gyro_Start();
	sleep(2500);
	Gyro_Reset();



	CorrallerLeftPosition(225);
	CorrallerRightPosition(35);

	eraseDisplay();

	nxtDisplayString(3, "Running...");

	int InitialPosition = Motors_GetPosition(S4, 1, 2);

	while(Motors_GetPosition(S4, 1, 2) < InitialPosition + 1440 * .3)
	{
		MoveLeft(-20);
		MoveRight(-20);
	}

	writeDebugStreamLine("Initial Move Done");

	while(Motors_GetPosition(S4, 1, 2) < InitialPosition + 1440 * 1.7)
	{
		eraseDisplay();

		if(Gyro_Heading() < 0)
		{
			MoveRight(-10);
			MoveLeft(0);
		}
		else
		{
			MoveLeft(-10);
			MoveRight(0);
		}

		sleep(10);
	}

	writeDebugStreamLine("Gyro Down Ramp Done");

	while(Motors_GetPosition(S4, 1, 2) < InitialPosition + 1440 * 4.2)
	{
		MoveLeft(-30 - Gyro_Heading());
		MoveRight(-30 + Gyro_Heading());

		ServosDeployBalls();
	}

	MoveRight(0);
	MoveLeft(0);

	ClearTimer(T1);

	MoveArm(100);





	while(true)
	{
		sleep(100);
		if(time1[T1] > 6000)
		{
			break;
		}
	}

	MoveArm(0);

	writeDebugStreamLine("Moved Gyro to Tube");

	sleep(200);

	//grab the tube
	Motors_SetPosition(S4, 4, 2, DownPosition, 50);
	sleep(500);

	//go Straight
	MoveLeft(40);
	MoveRight(40);

	sleep(500);

	MoveLeft(0);
	MoveRight(0);

	//go Straight
	MoveLeft(-80);
	MoveRight(-80);

	sleep(200);

	MoveRight(0);
	MoveLeft(0);

	ServosDeployBalls();

	sleep(500);

	PickupBlocks(100);

	Shoot(100);
	sleep(2000);

	Shoot(0);
	PickupBlocks(0);

	writeDebugStreamLine("Shot Ball");

	//turn slightly right
	Gyro_Reset();
	while(Gyro_Heading() < 25)
	{
		MoveRight(0);
		MoveLeft(65);
		sleep(10);
	}

	writeDebugStreamLine("Turned");

	MoveRight(0);
	MoveLeft(0);
}
