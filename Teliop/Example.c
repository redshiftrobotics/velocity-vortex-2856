// example.c

/*

	This file is part of the FTC team 2856 application code.

	FTC team 2856 application code is free software: you can
	redistribute it and/or modify it under the terms of the GNU
	General Public License as published by the Free Software
	Foundation, either version 2 of the License, or (at your
	option) any later version.

	FTC team 2856 application code is distributed in the hope that
	it will be useful, but WITHOUT ANY WARRANTY; without even the
	implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
	PURPOSE.  See the GNU General Public License for more details.

	You should have received a copy of the GNU General Public
	License along with FTC team 2856 application code. If not, see
	<http://www.gnu.org/licenses/>.

*/

/*

	(C) Copyright 2014 John Doe

	This is an example teliop program.
	
	You should copy it to a new file, remove this comment, fix the filename at the top, do motors
	and sensors setup, replace "John Doe" with your name(s), then write. If you want a second
	teliop implementation, just copy this file again and make the necessary changes.
	
	Please make sure to leave the license block above intact. It's important.

*/

#include "JoystickDriver.c"

task main()
{


	while(true)
	{
		getJoystickSettings(joystick);
		
		// do something
	}

}
