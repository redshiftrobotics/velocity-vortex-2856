package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * Created by matt on 10/15/16.
 */
@Autonomous(name = "ExampleAutonomous", group = "pid-test")
public class ExampleAutonomous extends LinearOpMode {
    I2cDeviceSynch imu;
    DcMotor[] motors;
    PIDController pidController;
    ColorSensor cs1;
    ColorSensor cs2;


    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        Utility.InitMotors(hardwareMap, motors);
        cs1 = hardwareMap.colorSensor.get("cs1");
        cs2 = hardwareMap.colorSensor.get("cs2");

        pidController = new PIDController(imu, motors, cs1, cs2, telemetry);


        //working PIDs
        //P: 100
        //I: 30
        //D: 0

        //loop
        pidController.SetPIDConstatns(63f, 10f, 0f, 50f);
        pidController.SetDefaultMultipliers();
        waitForStart();

        pidController.LinearMove(0f, 1f);

    }

    enum TuneState {
        P, I, D
    }
}
