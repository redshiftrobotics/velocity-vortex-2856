package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by Duncan on 7/27/2017.
 */

@Autonomous(name = "Sensor: BNO055 IMU", group = "Sensor")
public class RevImu extends OpMode{
    BNO055IMU imu;

    Timer timer = new Timer();

    @Override
    public void init() {
        telemetry.addData("imu", "Initializing");
        telemetry.update();
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode

        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        telemetry.addData("imu", "Initialized");
        telemetry.update();
        timer.StartTimer();
    }

    @Override
    public void loop() {
        telemetry.addData("Angle", "Heading: " + imu.getAngularOrientation().firstAngle + ", Roll: " + imu.getAngularOrientation().secondAngle + ", Pitch: " + imu.getAngularOrientation().thirdAngle);
        telemetry.addData("Time", timer.DeltaTime());
        telemetry.update();
    }
}