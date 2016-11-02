package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by adam on 10/26/16.
 */

@TeleOp(name = "sleeper", group = "test")
public class SleepTest extends OpMode {
    @Override
    public void init() {

    }

    public int looped = 0;

    @Override
    public void loop() {
        telemetry.addData("Looped: ", Integer.toString(looped));
        looped++;

        try {
            Thread.sleep(4800);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
