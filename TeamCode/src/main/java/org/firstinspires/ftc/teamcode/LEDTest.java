package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;

import java.util.Date;

/**
 * Created by Duncan on 10/8/2016.
 */

@TeleOp(name = "LEDTest", group = "Test")
public class LEDTest extends OpMode {
    DeviceInterfaceModule InterfaceModule;

    long date;
    double mult = 5;
    boolean blue = false;
    int max = 5;
    int min = 3;

    @Override
    public void init() {
        InterfaceModule = hardwareMap.deviceInterfaceModule.get("interface_module");
    }

    @Override
    public void loop() {
        //mult = -gamepad1.left_stick_y;
        date = System.currentTimeMillis();
        if(mult!=0){
            if(Math.round(date/(1000/mult))%max>=min){
                if(blue){InterfaceModule.setLED(0,true);}
                else{InterfaceModule.setLED(1,true);}
            }else {
                InterfaceModule.setLED(0,false);
                InterfaceModule.setLED(1,false);
            }
        }
    }
}
