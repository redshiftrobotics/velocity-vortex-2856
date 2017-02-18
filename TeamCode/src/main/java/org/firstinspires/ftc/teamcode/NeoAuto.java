package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.lasarobotics.vision.opmode.LinearVisionOpMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by matt on 10/15/16.
 */
@Autonomous(name = "2856 Button Autonomous")
public class NeoAuto extends LinearVisionOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;
    ColorSensor csf;
    ColorSensor csb;
    UltrasonicSensor us;
    Servo la;
    int side;

    @Override
    public void runOpMode() throws InterruptedException {
        Float[] forward = new Float[]{1f,0f};
        Float[] backward = new Float[]{-1f,0f};
        initDevices();
        straightConst();
        side = getSide();
        waitForStart();
        robot.Straight(.6f, forward, 10, telemetry);
        if(side == -1) {
            //turn a little to the right
            side = side; // useless to get rid of empty if body warnings
        }
        robot.AngleTurn(-45, 10, telemetry);
        robot.Straight(.4f, forward, 10, telemetry);
        robot.MoveToLine(forward, csf, .4f, 10, telemetry);
        robot.AngleTurn(45f, 10, telemetry);
        robot.MoveToLine(forward, csb, .4f, 10, telemetry);
    }


    private void straightConst() {
        robot.Data.PID.PTuning = 10f;
        robot.Data.PID.ITuning = 5f;
        robot.Data.PID.DTuning = 0f;
    }

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        csf = hardwareMap.colorSensor.get("csf");
        csf = hardwareMap.colorSensor.get("csb");
        robot = new Robot(imu, m0, m1, m2, m3, us, telemetry);
    }

    private int getSide() {
        int s;
        // Retrieve file.
        File file = new File("/sdcard/Pictures", "prefs");
        StringBuilder text = new StringBuilder();
        // Attempt to load line from file into the buffer.
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            // Ensure that the first line is not null.
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            // Close the buffer reader
            br.close();
        }
        // Catch exceptions... Or don't because that would require effort.
        catch (IOException e) {
        }

        // Provide in a more user friendly form.
        String sideText = text.toString();
        if(sideText.equals("red")) {
            s = -1;
        } else if (sideText.equals("blue")) {
            s = 1;
        } else { //this should never happen
            s = 1;
        }
        return s;
    }

}
