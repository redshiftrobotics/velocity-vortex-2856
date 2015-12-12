package org.usfirst.ftc.exampleteam.yourcodehere;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.internal.I2cDeviceClient;

/**
 * A skeletal example of a do-nothing first OpMode. Go ahead and change this code
 * to suit your needs, or create sibling OpModes adjacent to this one in the same
 * Java package.
 */
//@TeleOp(name="I2C")
public class I2C extends SynchronousOpMode
    {
    /* Declare here any fields you might find useful. */
    // DcMotor motorLeft = null;
    // DcMotor motorRight = null;

    DeviceInterfaceModule InterfaceModule;
    I2cDevice Ultrasonic;

    @Override public void main() throws InterruptedException
        {
            InterfaceModule = hardwareMap.deviceInterfaceModule.get("interface_module");

            Ultrasonic = new I2cDevice(InterfaceModule, 1);





        /* Initialize our hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names you assigned during the robot configuration
         * step you did in the FTC Robot Controller app on the phone.
         */
        // this.motorLeft = this.hardwareMap.dcMotor.get("motorLeft");
        // this.motorRight = this.hardwareMap.dcMotor.get("motorRight");

        // Wait for the game to start
        waitForStart();

        // Go go gadget robot!
        while (opModeIsActive())
            {
            if (updateGamepads())
            {
                // The game pad state has changed. Do something with that!
            }

            boolean PortReady = Ultrasonic.isI2cPortReady();
            String DeviceName = Ultrasonic.getDeviceName();
                //Ultrasonic.

            telemetry.addData("00", PortReady);
            telemetry.addData("01", DeviceName);

            telemetry.update();
            idle();
            }
        }
    }
