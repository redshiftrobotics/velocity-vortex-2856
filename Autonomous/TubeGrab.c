#pragma config(Sensor, S1,     ,               sensorI2CCustom)

#include "JoystickDriver.c"
#include "Motors.h"
#include "Servos.h"
#include "Menu.c"

bool WaitForStartBool = false;

void MoveLeft(int Power)
{
	Motors_SetSpeed(S1, 1, 1, Power);
	//motor[LeftDriveMotor] = Power;
	nxtDisplayString(1, "LeftPower: %i", Power);
}

void RaiseServos()
{
	Servos_SetPosition(S1, 4, 1, 240);
}

void LowerServos()
{
	Servos_SetPosition(S1, 4, 1, 70);
}

void MoveRight(int Power)
{
	Motors_SetSpeed(S1, 1, 2, -Power);
	nxtDisplayString(2, "RightPower: %i", -Power);
}

void PickupBlocks(int Power)
{
	Motors_SetSpeed(S1, 3, 1, Power);
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
	//raise the servos
	RaiseServos();

	int StartEncoder = Motors_GetPosition(S1, 1, 2);

	//go backward
	MoveLeft(-15);
	MoveRight(-15);

	sleep(1000);

	MoveLeft(-128);
	MoveRight(-128);

	sleep(2000);

	MoveLeft(-15);
	MoveRight(-15);

	while(Motors_GetPosition(S1, 1, 2) < StartEncoder + 1440 * 3.5)
	{
		writeDebugStreamLine("%i", Motors_GetPosition(S1, 1, 2));
	}

	//stop
	MoveLeft(0);
	MoveRight(0);

	//grab the tube
	LowerServos();
	sleep(500);

	//spin the robot
	MoveRight(-40);
	MoveLeft(40);

	sleep(400);

	MoveRight(0);
	MoveLeft(0);

	//Go Forward
	int EndEncoder = Motors_GetPosition(S1, 1, 2);

	MoveLeft(40);
	MoveRight(40);

	while(Motors_GetPosition(S1, 1, 2) > EndEncoder - 1440 * 4)
	{
		writeDebugStreamLine("%i", Motors_GetPosition(S1, 1, 2));
	}

	//Stop
	MoveLeft(0);
	MoveRight(0);

	//turn right
	MoveRight(-40);
	MoveLeft(40);

	sleep(500);

	MoveRight(0);
	MoveLeft(0);

	//go Straight
	MoveLeft(40);
	MoveRight(40);

	sleep(500);

	MoveRight(0);
	MoveLeft(0);
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

	eraseDisplay();

	nxtDisplayString(3, "Running...");

	MainProgram();
}
