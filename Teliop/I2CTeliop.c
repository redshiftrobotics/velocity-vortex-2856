#pragma config(Hubs,  S3, HTMotor,  none,     none,     none)
#pragma config(Sensor, S1,     ,               sensorI2CCustom)
#pragma config(Sensor, S2,     Gyro,           sensorI2CCustom)
#pragma config(Sensor, S3,     ,               sensorI2CMuxController)
#pragma config(Motor,  mtr_S3_C1_1,     motorD,        tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S3_C1_2,     motorE,        tmotorTetrix, openLoop)

#include "JoystickDriver.c"
#include "Motors.h"
#include "Servos.h"

float Constant = 1.5;
long ArmPosition = 0;
bool EndgameMode = false;
int TopChanellerPosition = 100;
int BottomChanellerPosition = 100;

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

	if(Power == 0)
	{
		Servos_SetPosition(S1, 3, 3, 90);
	}
	else
	{
		Servos_SetPosition(S1, 3, 3, 0);
	}
	//also move the servo here: add later
}

void TopChaneller(int Position)
{
	Servos_SetPosition(S1, 3, 1, Position);
}

void BottomChaneller(int Position)
{
	Servos_SetPosition(S1, 3, 2, Position);
}

void BallContainer(int Position)
{
	Servos_SetPosition(S1, 3, 2, Position);
}

void Shoot(int Power)
{
	Motors_SetSpeed(S1, 2, 2, Power);
}

void CorrallerLeftPosition(int Position)
{
	Servos_SetPosition(S1, 3, 4, Position);
}

void CorrallerRightPosition(int Position)
{
	Servos_SetPosition(S1, 3, 5, Position);
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
	//gets the start position
	long DownPosition = Motors_GetPosition(S1, 4, 2);
	long UpPosition = DownPosition + 230;

	//move the servos in the wait for start
	TopChaneller(100);
	BottomChaneller(100);

	waitForStart();
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


		if(EndgameMode)
		{
			//controls the top servo controls
			if(joystick.joy2_y1 > 10 && TopChanellerPosition < 255)
			{
				TopChanellerPosition += 5;
			}
			else if(joystick.joy2_y1 < -10 && TopChanellerPosition > 0)
			{
				TopChanellerPosition -= 5;
			}

			//controls the bottom servo controls
			if(joystick.joy2_y2 > 10 && BottomChanellerPosition < 255)
			{
				BottomChanellerPosition += 1;
			}
			else if(joystick.joy2_y2 < -10 && BottomChanellerPosition > 0)
			{
			  BottomChanellerPosition -= 1;
			}

			//actually moves the servos
			Servos_SetPosition(S1, 3, 1, TopChanellerPosition);
			Servos_SetPosition(S1, 3, 2, BottomChanellerPosition);

			//controls the arm with the triggers
			if(joy2Btn(7) == 1)
			{
				MoveArm(100);
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
			//set the servo to the right position every cycle
			TopChaneller(100);
			BottomChaneller(100);

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
				MoveArm(-15);
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
			Motors_SetPosition(S1, 4, 2, DownPosition, 50);

			nxtDisplayString(5, "Raise");
		}
		else if(joy1Btn(8) == 1)
		{
			Motors_SetPosition(S1, 4, 2, UpPosition, 50);
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
		if((joy1Btn(2) == 1 && joy2Btn(2) == 1) || time1[T1] > 119000)
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
