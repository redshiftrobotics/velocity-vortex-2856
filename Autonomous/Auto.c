#pragma config(Sensor, S2,     ,               sensorI2CCustom)
#pragma config(Sensor, S1,     ,               sensorI2CCustom)
#pragma config(Sensor, S4,     ,               sensorTouch)

#include "Servos.h"
#include "Motors.h"
#include "Arm.c"
#include "JoystickDriver.c"

//two posative powers take jarvis forward
void MoveLeft(int LeftPower)
{
	Motors_SetSpeed(S1, 1, 1, LeftPower);
	Motors_SetSpeed(S1, 1, 2, LeftPower);
}
void MoveRight(int RightPower)
{
	Motors_SetSpeed(S1, 2, 1, -RightPower);
	Motors_SetSpeed(S1, 2, 2, -RightPower);
}

task main()
{
	waitForStart();

	MoveLeft(50);
	MoveRight(50);

	sleep(4000);

	MoveLeft(0);
	MoveRight(0);
}
