#pragma config(Hubs,  S3, HTMotor,  none,     none,     none)
#pragma config(Sensor, S4,     ,               sensorI2CCustom)
#pragma config(Sensor, S2,     Gyro,           sensorI2CCustom)
#pragma config(Sensor, S3,     ,               sensorI2CMuxController)
#pragma config(Motor,  mtr_S3_C1_1,     motorD,        tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S3_C1_2,     motorE,        tmotorTetrix, openLoop)

#include "JoystickDriver.c"
#include "Motors.h"
#include "Servos.h"

float Constant = 1.5;
bool EndgameMode = false;
int TopChanellerPosition = 133;
int BottomChanellerPosition = 250;
int LoadBalls = false;

void MoveLeft(int Power)
{
	Motors_SetSpeed(S4, 1, 1, Power);
	writeDebugStreamLine("Left %i", Power);
}

void MoveCorraller(int Power)
{
	nxtDisplayString(5, "Corraller: %i", Power);
	motor[motorE] = Power;

	if(Power == 0)
	{
		Servos_SetPosition(S4, 3, 3, 128);
	}
	else
	{
		Servos_SetPosition(S4, 3, 3, 0);
	}
	//also move the servo here: add later
}

void TopChaneller(int Position)
{
	Servos_SetPosition(S4, 3, 1, Position);
}

void BottomChaneller(int Position)
{
	Servos_SetPosition(S4, 3, 2, Position);
}

void Shoot(int Power)
{
	Motors_SetSpeed(S4, 2, 2, Power);
}

void CorrallerLeftPosition(int Position)
{
	Servos_SetPosition(S4, 3, 4, Position);
}

void CorrallerRightPosition(int Position)
{
	Servos_SetPosition(S4, 3, 5, Position);
}

void MoveRight(int Power)
{
	Motors_SetSpeed(S4, 1, 2, -Power);
	writeDebugStreamLine("Right %i", -Power);
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

task main()
{
	//gets the start position
	long DownPosition = Motors_GetPosition(S4, 4, 2);
	long UpPosition = DownPosition + 230;

	//move the servos in the wait for start
	TopChaneller(181);
	BottomChaneller(214);

	waitForStart();

	//sets up the tube grabber
	Motors_SetPosition(S4, 4, 2, UpPosition, 50);

	//stop the debugger from printing
	bDisplayDiagnostics = false;

	ClearTimer(T1);

	while(true)
	{
		//sleep so that it can draw
		sleep(50);

		//erases display
		eraseDisplay();

		//updates each loop
		getJoystickSettings(joystick);

		//looks at the endgame mode butotn
		if(joy2Btn(4) == 1 && joy1Btn(4) == 1)
		{
			EndgameMode = true;
			BottomChanellerPosition = 50;
		}

		if(EndgameMode)
		{
			if(joy2Btn(3) == 1)
			{
				PickupBlocks(100);
			}
			else if(joy2Btn(1) == 1)
			{
				PickupBlocks(-100);
			}
			else
			{
				PickupBlocks(0);
			}

			if(joy2Btn(2) == 1)
			{
				Shoot(100);
			}
			else
			{
				Shoot(0);
			}

			//eraseDisplay();

			nxtDisplayString(1, "Top: %i", TopChanellerPosition);
			nxtDisplayString(2, "Bottom: %i", BottomChanellerPosition);

			//controls the top servo controls
			if(joystick.joy2_y1 > 10 && TopChanellerPosition < 181)
			{
				TopChanellerPosition += 4;
			}
			else if(joystick.joy2_y1 < -10 && TopChanellerPosition > 113)
			{
				TopChanellerPosition -= 4;
			}

			//controls the bottom servo controls
			if(joystick.joy2_y2 > 10 && BottomChanellerPosition < 250)
			{
				BottomChanellerPosition += 4;
			}
			else if(joystick.joy2_y2 < -10 && BottomChanellerPosition > 54)
			{
			  BottomChanellerPosition -= 4;
			}

			//actually moves the servos
			Servos_SetPosition(S4, 3, 1, TopChanellerPosition);
			Servos_SetPosition(S4, 3, 2, BottomChanellerPosition);

			//controls the arm with the triggers
			if(joy2Btn(7) == 1)
			{
				MoveArm(50);
			}
			else if(joy2Btn(8) == 1)
			{
				MoveArm(-50);
			}
			//if neither trigger is pressed, stop shooting
			else
			{
				MoveArm(0);
			}
		}
		else
		{
			if(joy2Btn(5) == 1)
			{
				LoadBalls = true;
			}
			if(joy2Btn(6) == 1)
			{
				LoadBalls = false;
			}
			//set the servo to the right position every cycle
			if(LoadBalls)
			{
				TopChaneller(TopChanellerPosition);
				BottomChaneller(50);
			}
			else
			{
				TopChaneller(TopChanellerPosition);
				BottomChaneller(BottomChanellerPosition);
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
				MoveArm(-100);
			}
			else if(joystick.joy2_y2 < -10)
			{
				MoveArm(100);
			}
			else
			{
				MoveArm(0);
			}

			//controls the servos raising the tube grabber
			if(!joy2Btn(4))
			{
				CorrallerLeftPosition(140);
				CorrallerRightPosition(120);
			}
			else
			{
				CorrallerLeftPosition(225);
				CorrallerRightPosition(35);
			}

			//if either trigger is pressed, start shooting
			if(joy2Btn(7) == 1 || joy2Btn(8) == 1)
			{
				nxtDisplayString(6, "Shoot");
				Shoot(100);
			}
			//if neither trigger is pressed, stop shooting
			else
			{
				nxtDisplayString(6, "No Shoot");
				Shoot(0);
			}
		}

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


		//controls the tube grabber
		if(joy1Btn(7) == 1)
		{
			Motors_SetPosition(S4, 4, 2, DownPosition, 50);

			nxtDisplayString(5, "Raise");
		}
		else if(joy1Btn(8) == 1)
		{
			Motors_SetPosition(S4, 4, 2, UpPosition, 50);
			//lower the motor for the tube grabber
			nxtDisplayString(5, "Lower");
		}

		//if the y button is pressed, stop the block corallers
		if(!joy2Btn(3))
		{
			MoveCorraller(100);
		}
		else
		{
			MoveCorraller(0);
		}



		//lock the robot if both people press a
		if((joy1Btn(2) == 1 && joy2Btn(2) == 1) || time1[T1] > 1190000)
		{

				Servos_SetPosition(S4, 3, 1, 50);
				MoveArm(0);
				MoveLeft(0);
				MoveRight(0);

				sleep(1000);

				Motors_SetPosition(S4, 1, 1, (int)Motors_GetPosition(S4, 1, 1), 10);
				Motors_SetPosition(S4, 1, 2, (int)Motors_GetPosition(S4, 1, 2), 10);
				while(true)
				{
				}
		}
	}
}
