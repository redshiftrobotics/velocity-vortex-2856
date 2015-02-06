#pragma config(Hubs,  S3, HTMotor,  none,     none,     none)
#pragma config(Sensor, S1,     ,               sensorI2CCustom)
#pragma config(Sensor, S2,     Gyro,           sensorI2CCustom)
#pragma config(Sensor, S3,     ,               sensorI2CMuxController)
#pragma config(Motor,  mtr_S3_C1_1,     motorD,        tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S3_C1_2,     motorE,        tmotorTetrix, openLoop)

#include "JoystickDriver.c"
#include "Motors.h"
#include "Servos.h"

bool ServosDown = false;
float Constant = 1.5;
long ArmPosition = 0;

void MoveLeft(int Power)
{
	Motors_SetSpeed(S1, 1, 1, Power);
	nxtDisplayString(1, "LeftPower: %i", Power);
	writeDebugStreamLine("Left %i", Power);
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
	ServosDown = false;
}

void LowerServos()
{
	ServosDown = true;
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

void UpdateServos()
{
	if(ServosDown)
	{
		nxtDisplayString(4, "Down");
		Servos_SetPosition(S1, 3, 1, 0);
	}
	else
	{
		nxtDisplayString(4, "Up");
		Servos_SetPosition(S1, 3, 1, 120);
	}
}

void MoveRight(int Power)
{
	Motors_SetSpeed(S1, 1, 2, -Power);
	nxtDisplayString(2, "RightPower: %i", -Power);
	writeDebugStreamLine("Right %i", -Power);
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

task main()
{
	//move the servos in the wait for start
	BallContainer(100);

	waitForStart();
	//stop the debugger from printing
	bDisplayDiagnostics = false;

	while(true)
	{
		//set the servo to the right position every cycle
		BallContainer(250);

		//update the servos
		UpdateServos();

		//sleep so that it can draw
		sleep(50);

		//erases display
		eraseDisplay();

		//updates each loop
		getJoystickSettings(joystick);

		//moves left side
		if(abs(joystick.joy1_y1) > 10)
		{
			MoveLeft(joystick.joy1_y1 / (1.26 * Constant));
		}
		else
		{
			MoveLeft(0);
		}

		//moves right side
		if(abs(joystick.joy1_y2) > 10)
		{
			MoveRight(joystick.joy1_y2 / (1.26 * Constant));
		}
		else
		{
			MoveRight(0);
		}

		//moves block picker-upper
		if(joystick.joy2_y1 > 10)
		{
			PickupBlocks(100);
		}
		else if (joystick.joy2_y1 < -10)
		{
			PickupBlocks(-100);
		}
		else
		{
			PickupBlocks(0);
		}


		//moves arm
		if(joystick.joy2_y2 > 10)
		{
			ArmPosition = 0;
			MoveArm(50);
		}
		else if(joystick.joy2_y2 < -10)
		{
			ArmPosition = 0;
			MoveArm(-10);
		}
		else
		{
			writeDebugStreamLine("Else", 7);
			if(ArmPosition == 0)
			{
				ArmPosition = Motors_GetPosition(S1, 2, 1);
			}
			if(Motors_GetPosition(S1, 2, 1) > ArmPosition)
			{
				MoveArm(-5);
			}
			else
			{
				MoveArm(0);
			}
		}

		//if the right trigger is pressed, raise the servos
		if(joy1Btn(7) == 1)
		{
			RaiseServos();
			nxtDisplayString(5, "Raise");
		}
		//if the left trigger is pressed, lower the servos
		else if(joy1Btn(8) == 1)
		{
			LowerServos();
			nxtDisplayString(5, "Lower");
		}


		if(!joy2Btn(4))
		{
			CorrallerLeftPosition(125);
			CorrallerRightPosition(135);
		}
		else
		{
			CorrallerLeftPosition(225);
			CorrallerRightPosition(35);
		}

		//if the y button is clicked, move the block corallers
		if(!joy2Btn(3))
		{
			MoveCorraller(100);
		}
		else
		{
			MoveCorraller(0);
		}

		//if the left trigger is pressed, start shooting
		if(joy2Btn(7) == 1 || joy2Btn(8) == 1)
		{
			nxtDisplayString(6, "Shoot");
			Shoot(100);
		}
		//if x is pressed make the shooter go backwards
		else if(joy2Btn(1) == 1)
		{
			nxtDisplayString(6, "Reverse Shoot");
			Shoot(-50);
		}
		//if neither trigger is pressed, stop shooting
		else
		{
			nxtDisplayString(6, "No Shoot");
			Shoot(0);
		}

		//lock the robot if both people press a
		if(joy1Btn(2) == 1 && joy2Btn(2) == 1)
		{

				Servos_SetPosition(S1, 3, 1, 50);
				MoveArm(0);
				MoveLeft(0);
				MoveRight(0);

				sleep(1000);

				Motors_SetPosition(S1, 1, 1, (int)Motors_GetPosition(S1, 1, 1), 10);
				Motors_SetPosition(S1, 1, 2, (int)Motors_GetPosition(S1, 1, 2), 10);
				while(true)
				{
				}
		}
	}
}
