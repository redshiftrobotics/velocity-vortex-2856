#pragma config(Hubs,  S3, HTMotor,  none,     none,     none)
#pragma config(Sensor, S1,     ,               sensorI2CCustom)
#pragma config(Sensor, S4,     ,               sensorI2CCustom)
#pragma config(Sensor, S2,     Gyro,           sensorI2CCustom)
#pragma config(Sensor, S3,     ,               sensorI2CMuxController)

#include "Motors.h"
#include "Servos.h"

string Position = "Bottom";


task main()
{
	//gets the start position
	long DownPosition = Motors_GetPosition(S4, 1, 2);
	long UpPosition = DownPosition + 230;

	while(true)
	{
		Motors_SetPosition(S4, 1, 2, DownPosition, 50);
		sleep(5000);
		Motors_SetPosition(S4, 1, 2, UpPosition, 50);
		sleep(5000);
	}
}
