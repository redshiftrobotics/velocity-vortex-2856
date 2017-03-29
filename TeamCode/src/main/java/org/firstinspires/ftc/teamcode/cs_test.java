package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by adam on 3/27/17.
 */
@TeleOp(name="Tester")
public class cs_test extends OpMode {

    I2cDevice colorSensor;
    I2cDeviceSynch cs;
    ColorSensor csensor;
    byte[] values;

    @Override
    public void loop() {
       // values = cs.read(0x05,3 );
        telemetry.addData("red ", csensor.red());
        telemetry.addData("blue ", csensor.blue());
        telemetry.addData("green ", csensor.green());
        telemetry.update();
    }

    @Override
    public void init() {
       /* colorSensor = hardwareMap.i2cDevice.get("rejector");
        cs = new I2cDeviceSynchImpl(colorSensor, new I2cAddr(0x11), false);
        cs.engage();*/
        csensor = hardwareMap.colorSensor.get("rejector");
        csensor.setI2cAddress(new I2cAddr(0x11));
        csensor.enableLed(true);
    }

    @Override
    public void stop() {
        //cs.disengage();
    }
}
