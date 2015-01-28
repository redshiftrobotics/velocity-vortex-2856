#pragma config(Sensor, S1,     ,               sensorI2CCustom)
#pragma config(Sensor, S2, IROne,               sensorI2CCustom)
#pragma config(Sensor, S3, IRTwo,               sensorI2CCustom)

#include "JoystickDriver.c"
#include "Motors.h"
#include "Servos.h"
#include "Menu.c"
#include "IR.c"

bool WaitForStartBool = false;
int Threashold = 30;

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

int CheckPosition()
{
	IR_Update();

	if(IR_RightValue.C > 100)
	{
		writeDebugStreamLine("Running Config 3");
		return 3;

	}
	else if(IR_LeftValue.C > 70)
	{
		writeDebugStreamLine("Running Config 2");
		return 2;
	}
	else
	{
		writeDebugStreamLine("Running Config 1");
		return 1;
	}
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
	CheckPosition();

	//raise the servos
	RaiseServos();

	////go straight
	MoveLeft(20);
	MoveRight(20);

	sleep(2900);

	MoveRight(0);
	MoveLeft(0);

	//turn right slowly
	MoveLeft(50);
	sleep(1000);

	//move forward
	MoveLeft(30);
	MoveRight(50);

	sleep(1000);

	MoveRight(0);
	MoveLeft(0);
}

task main()
{
	//turn off joystick debug
	bDisplayDiagnostics = false;

	//lets the used pick the program
	//WaitForStartMenu();

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
