package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by adam on 12/16/16.
 */

/**
 * abstract class pid hardware implementations should subclass, in order to be usable by the PID
 * controller class.
 *
 */
public abstract class PIDHardware {
    public BNO055IMU imu;
    private BNO055IMU.Parameters imuParameters;

    /**
     *
     * @param i2cDeviceSynch takes an imu device as a parameter, to avoid having
     *                       the user set up the imu correctly.
     */
    public PIDHardware(I2cDeviceSynch i2cDeviceSynch) {
        initializeIMU(i2cDeviceSynch);
    }

    private void initializeIMU(I2cDeviceSynch i2cSync) {
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = new AdafruitBNO055IMU(i2cSync);
        imu.initialize(imuParameters);
    }

    /**
     *
     * @return an Orientation object, which will contain the current orientation of the imu,
     * accessible through Orientation.firstAngle
     */
    public AngleData currentImuOrientation() {
        Orientation current = imu.getAngularOrientation();
        float firstAngle = current.firstAngle * -1;
        float secondAngle = current.secondAngle * -1;
        return new AngleData(firstAngle, secondAngle);
    }

    /**
     *
     * @return the encoder count from an implementation specified motor...
     */
    public abstract int getMeasuredEncoderValue();
    public abstract void stop();
}

class AngleData { //prettified IMU data
    float firstAngle;
    float secondAngle;

    AngleData(float firstAngle, float secondAngle) {
        this.firstAngle = firstAngle;
        this.secondAngle = secondAngle;
    }
}

