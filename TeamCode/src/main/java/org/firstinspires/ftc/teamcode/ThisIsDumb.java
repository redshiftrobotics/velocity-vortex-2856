package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.util.ArrayList;

/**
 * Created by matt on 2/15/17.
 */

@Disabled
@Autonomous(name = "ThisIsDumb")
public class ThisIsDumb extends LinearOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor cs;
    ColorSensor cs1;
    ColorSensor csFront;
    UltrasonicSensor us;

    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        //robot = new Robot(imu, m0, m1, m2, m3, us, telemetry);

        // make a dummy MG because we need a pre-initialized one to parse the settings file
        MovementGenerator settingsGetter = new MovementGenerator(robot, new ArrayList<Movement>(), telemetry);

        // use the settingsGetter as a way to parse config file, then pass in robot and tm to get a completed deal
        MovementGenerator mG = new MovementGenerator(robot, settingsGetter.parceSettings("/sdcard/autoprefs"), telemetry);

        waitForStart();

        // do the motions
        mG.move();

    }
}
