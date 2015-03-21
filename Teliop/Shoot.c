#pragma config(Hubs,  S3, HTMotor,  none,     none,     none)
#pragma config(Sensor, S1,     ,               sensorI2CCustom)
#pragma config(Sensor, S4,     ,               sensorI2CCustom)
#pragma config(Sensor, S2,     Gyro,           sensorI2CCustom)
#pragma config(Sensor, S3,     ,               sensorI2CMuxController)

#include "Motors.h"
#include "Servos.h"

void Shoot(int Power)
{
	Motors_SetSpeed(S1, 2, 2, Power);
}


task main()
{
	while(true)
	{
		Shoot(-100);
		sleep(3000);
		Shoot(0);

		sleep(5000)
	}
}
