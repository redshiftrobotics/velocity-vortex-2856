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

void MoveArm(int Power)
{
	Motors_SetSpeed(S4, 2, 1, Power);
	nxtDisplayString(4, "ArmPower: %i", Power);
}

task main()
{


MoveArm(-100);
sleep(1000);
MoveArm(0);
}
