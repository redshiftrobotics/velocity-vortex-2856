#pragma config(Hubs,  S3, HTMotor,  none,     none,     none)
#pragma config(Sensor, S1,     ,               sensorI2CCustom)
#pragma config(Sensor, S2,     Gyro,           sensorI2CCustom)
#pragma config(Sensor, S3,     ,               sensorI2CMuxController)
#pragma config(Motor,  mtr_S3_C1_1,     motorD,        tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S3_C1_2,     motorE,        tmotorTetrix, openLoop)

#include "JoystickDriver.c"
#include "Motors.h"
#include "Servos.h"
#include "Menu.c"
#include "Gyro.c"

bool WaitForStartBool = false;
int ArmPosition = 0;

void MoveLeft(int Power)
{
	Motors_SetSpeed(S1, 1, 1, Power);
	nxtDisplayString(1, "LeftPower: %i", Power);
}

void MoveCorraller(int Power)
{
	nxtDisplayString(5, "Corraller: %i", Power);
	motor[motorE] = Power;
}

void BallContainer(int Position)
{
	Servos_SetPosition(S1, 3, 2, Position);
}

void RaiseServos()
{
	Servos_SetPosition(S1, 3, 1, 120);
}

void LowerServos()
{
	Servos_SetPosition(S1, 3, 1, 0);
}

void Shoot(int Power)
{
	Motors_SetSpeed(S1, 2, 2, Power);
}

void CorrallerLeftPosition(int Position)
{
	Servos_SetPosition(S1, 3, 3, Position);
}

void CorrallerRightPosition(int Position)
{
	Servos_SetPosition(S1, 3, 4, Position);
}

void MoveRight(int Power)
{
	Motors_SetSpeed(S1, 1, 2, -Power);
	nxtDisplayString(2, "RightPower: %i", -Power);
}

void PickupBlocks(int Power)
{
	motor[motorD] = Power;
	nxtDisplayString(3, "PickupPower: %i", Power);

}

void MoveArm(int Power)
{
	Motors_SetSpeed(S1, 2, 1, Power);
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

void MainProgram()
{
	//grab the tube
	LowerServos();
	sleep(500);

	//TRY TO SCORE NOW
CorrallerLeftPosition(225);
			CorrallerRightPosition(35);

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



	BallContainer(240);

	sleep(500);

	PickupBlocks(100);

	Shoot(100);
	sleep(5000);
	////do this for five seconds
	//for(int i = 0; i < 500; i++)
	//{
	//	sleep(10);

	//	if(nMotorEncoder[motorF] > ArmPosition)
	//	{
	//		MoveArm(5);
	//	}
	//	else
	//	{
	//		MoveArm(-5);
	//	}
	//}

	Shoot(0);
	PickupBlocks(0);

	writeDebugStreamLine("Shot Ball");

	//turn slightly right
	Gyro_Reset();
	while(Gyro_Heading() < 25)
	{
		MoveRight(0);
		MoveLeft(35 - Gyro_Heading());
		LowerServos();
		sleep(10);
	}

	writeDebugStreamLine("Turned");

	MoveRight(0);
	MoveLeft(0);

	int StartEndEncoder = Motors_GetPosition(S1, 1, 2);

	MoveLeft(50);
	MoveRight(50);

	while(Motors_GetPosition(S1, 1, 2) > StartEndEncoder - 1440 * 4.7)
	{
		LowerServos();
	}

	MoveLeft(0);
	MoveRight(0);

	writeDebugStreamLine("Arrived");
}

task main()
{
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

CorrallerLeftPosition(225);
			CorrallerRightPosition(35);

	Gyro_Start();
	sleep(3000);
	Gyro_Reset();

	eraseDisplay();

	nxtDisplayString(3, "Running...");

	int InitialPosition = Motors_GetPosition(S1, 1, 2);

	while(Motors_GetPosition(S1, 1, 2) < InitialPosition + 1440 * .3)
	{
		MoveLeft(-20);
		MoveRight(-20);
	}

	writeDebugStreamLine("Initial Move Done");

	while(Motors_GetPosition(S1, 1, 2) < InitialPosition + 1440 * 1.7)
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

	int TimePassed = 0;
	MoveArm(20);

	RaiseServos();

	while(Motors_GetPosition(S1, 1, 2) < InitialPosition + 1440 * 4.2)
	{
		MoveLeft(-30 - Gyro_Heading());
		MoveRight(-30 + Gyro_Heading());
		TimePassed += 10;
		sleep(10);
		BallContainer(240);

		if(TimePassed > 500)
		{
			MoveArm(0);
		}
	}

	writeDebugStreamLine("Moved Gyro to Tube");

	MoveRight(0);
	MoveLeft(0);
	sleep(1000);

	MainProgram();
}
