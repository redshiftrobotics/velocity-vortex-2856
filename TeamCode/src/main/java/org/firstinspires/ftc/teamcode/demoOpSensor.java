package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Duncan on 8/15/2017.
 */

@TeleOp(name="Demo Op Sensor",group="Demo")
public class demoOpSensor extends OpMode{

    UltrasonicSensor ultrasonicSensor;

    @Override
    public void init() {
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("us");
    }

    @Override
    public void loop() {
        telemetry.addData("Distance", ultrasonicSensor.getUltrasonicLevel());
        telemetry.update();
    }
}
