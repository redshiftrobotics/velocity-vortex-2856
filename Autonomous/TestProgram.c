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
	MoveArm(50);

	ClearTimer(T1);


	while(true)
	{

		//BallContainer(240);

		if(time1[T1] > 2300)
		{
			MoveArm(0);
		}

		nxtDisplayString(5, "%i", time1[T1]);
	}
}

task main()
{
	//turn off joystick debug
	bDisplayDiagnostics = false;

	MainProgram();
}
