package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

/**
 * Created by adam on 3/27/17.
 */
@TeleOp(name="Tester")
public class cs_test extends OpMode {
    public ColorSensor cs;

    @Override
    public void loop() {
        telemetry.addData("Test", cs.getI2cAddress().get7Bit());
        telemetry.update();
    }

    @Override
    public void init() {
        cs = hardwareMap.colorSensor.get("rejector");
        cs.setI2cAddress(new I2cAddr(0x22));
    }
}
