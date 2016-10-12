package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Duncan on 10/8/2016.
 */

@Autonomous(name = "LEDTest", group = "Test")
public class LEDTest extends OpMode {
    DeviceInterfaceModule InterfaceModule;

    @Override
    public void init() {
        InterfaceModule = hardwareMap.deviceInterfaceModule.get("interface_module");
        InterfaceModule.setPulseWidthPeriod(0,4);
    }

    @Override
    public void loop() {

    }
}
