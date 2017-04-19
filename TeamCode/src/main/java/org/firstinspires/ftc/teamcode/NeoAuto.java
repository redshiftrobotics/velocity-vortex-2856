package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by matt on 10/15/16.
 */
@Autonomous(name = "New 2856 Autonomous")
public class NeoAuto extends LinearOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    DcMotor shooter;
    Robot robot;
    OpticalDistanceSensor csb;
    UltrasonicSensor us;
    Servo actuator;
    Servo bAlign;
    Servo fAlign;
    ColorSensor bs; // beacon sensor
    int side;
    boolean hasPressed = false;

    Float[] forward = new Float[]{1f,0f};
    Float[] backward = new Float[]{-1f,0f};

    @Override
    public void runOpMode() throws InterruptedException {
        side = getSide();
        initDevices();

        waitForStart();

        robot.Data.PID.I = 0;


        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 1f;
        straightConst();
//        robot.Straight(0.2f, forward, 4, telemetry);
//        lineConst();
//        robot.MoveToLine(forward, csf, .2f, 10, telemetry);

        //begin alignment
        robot.Straight(2f, forward, 10, telemetry); // 2.45 to 2.37



        turnConst();

        //robot.AngleTurn(-60*side, 10, telemetry);


        /////////////////////////////////////////////////////////
        // IF IT IS -35, TRY UNCOMMENTING THE UPDATE TARGET -5 //
        /////////////////////////////////////////////////////////
        robot.AngleTurn(-25*side, 10, telemetry); //-35 for 10 degrees towards wall, but -30 for 15 degrees
        robot.AngleTurn(0, 4, telemetry);


        bAlign.setPosition(1);
        fAlign.setPosition(1);


        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.4f;
        robot.Data.PID.PTuning = 16;
        robot.Data.PID.ITuning = 0;
        robot.Straight(1.3f, forward, 3, telemetry);

        robot.UpdateTarget(-5*side); //less steep

        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.4f;

        //lineConst();
        robot.Data.PID.ITuning = 0;

        robot.MoveToLine(forward, csb, 0.25f * (35f/45f), 10, telemetry); //o.35
        robot.Straight(0.4f, forward, 10, telemetry); // shift forward so that when we do the backwards line detect we reach acceleration, thus overshooting in a planned way
        robot.UpdateTarget(-30*side);
        Thread.sleep(100);
        robot.MoveToLine(backward, csb, 0.25f * (35f/45f), 3, telemetry);

        push(0);

        //turnConst();
        //robot.AngleTurn(-20*side, 3, telemetry);


        //lineConst();
        robot.Data.PID.ITuning = 0;

        robot.Straight(0.4f, backward, 10, telemetry);
        robot.MoveToLine(backward, csb, 0.25f * (35f/45f), 15, telemetry); //o.35
        Thread.sleep(100);
        //robot.MoveToLine(forward, csb, 0.15 * (35f/45f), 3, telemetry);

        push(0);

        bAlign.setPosition(0);
        fAlign.setPosition(0);

        //turn and shoot... this is ONLY A SINGLE BALL under the assumption our alliance partner will take the other two
        robot.Data.PID.P = 20f;

        if(side == 1) {
            robot.AngleTurnSingleSided(-70f * side, 3, 0, telemetry); // 0 indicates to move m1, 0 would indicate m0
        } else {
            robot.AngleTurnSingleSided(-70f * side, 3, 1, telemetry); // 1 indicates to move m1, 0 would indicate m0
        }
        shooter.setPower(1);
        Thread.sleep(300);
        shooter.setPower(0);
        robot.Data.PID.P = 10f;


        // CAP BALLIN'
        robot.Straight(2f, forward, 10, telemetry);

        //CORNER VORTEX
        /*robot.Straight(0.5f, forward, 10, telemetry);
        robot.AngleTurn(70f*side, 4, telemetry);
        robot.Straight(3f, forward, 10, telemetry);*/

    }


    private void straightConst() {
        robot.Data.PID.PTuning = 10f;
        robot.Data.PID.ITuning = 5f;
        robot.Data.PID.DTuning = 0f;
    }

    private void turnConst() {
        robot.Data.PID.PTuning = 10f; // 7f
        robot.Data.PID.ITuning = 8f; // 5f
        robot.Data.PID.DTuning = 0f;
    }

    private void lineConst() {
        robot.Data.PID.PTuning = 4f; // working at 4
        robot.Data.PID.ITuning = 5f; // working at 5
        robot.Data.PID.DTuning = 0f;
    }

    private void initDevices() {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);
        Servo aim = hardwareMap.servo.get("shooterServo");
        aim.setPosition(0.51);
        csb = hardwareMap.opticalDistanceSensor.get("csb");


        Servo capServo1 = hardwareMap.servo.get("cap1");
        Servo capServo2 = hardwareMap.servo.get("cap2");
        Servo capHold = hardwareMap.servo.get("hold");
        capServo1.setPosition(0.95);
        capServo2.setPosition(0.25);

        robot = new Robot(this, imu, m0, m1, m2, m3, telemetry);
        telemetry.addData("IMU:", robot.Data.imu.getAngularOrientation());

        bs = hardwareMap.colorSensor.get("rbs");
        telemetry.addData("Color Sensor: ", bs.red());

        bAlign = hardwareMap.servo.get("balign");
        fAlign = hardwareMap.servo.get("falign");
        bAlign.setPosition(0);
        fAlign.setPosition(0.1);

        capHold.setPosition(0.15);


        actuator = hardwareMap.servo.get("ra");
        //actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);
    }

    public void push(int degrees) {

        float pushShift = -.2f;

        if(hasPressed) {
            pushShift += .05f;
        }

        if(side == -1) {
            forward = new Float[]{1f, 0f};
            backward = new Float[]{-1f, 0f};
        }


        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.2f;

        int blue = bs.blue();
        int red = bs.red();

        if (bs.blue() != bs.red()) {
            if (side == -1) { // on red side
                if (bs.blue() > bs.red()) {
                    telemetry.addData("color", "BLUE > RED");
                    robot.Straight((0.30f + pushShift) * (35f / 45f), forward, 10, telemetry);
                    if (bs.blue() > bs.red()) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        actuate();
                    } else {
                        actuate();
                    }
                } else {
                    telemetry.addData("color", "RED > BLUE");
                    actuate();
                }
            } else { // on blue side
                if (bs.blue() > bs.red()) {
                    telemetry.addData("color", "BLUE > RED");
                    actuate();
                } else {
                    telemetry.addData("color", "RED > BLUE");
                    robot.Straight((0.30f + pushShift) * (35f / 45f), forward, 10, telemetry);
                    if (bs.red() > bs.blue()) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        actuate();
                    } else {
                        actuate();
                    }
                }
            }
        }

        telemetry.clear();
        telemetry.update();

        telemetry.addData("red|blue ", red + "|" + blue);
        telemetry.update();

        hasPressed = true;

        if(side == -1) {
            forward = new Float[]{-1f, 0f};
            backward = new Float[]{1f, 0f};
        }

        //actuate();

    }

    private void actuate() {
        // out first then turn into it... worked well with turning first too
        actuator.setPosition(1.0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //robot.AngleTurn(-degrees*side, 3, telemetry);
        actuator.setPosition(0);
        //robot.AngleTurn(degrees*side, 5, telemetry);
        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.7f;
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        actuator.setPosition(1.0);
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        actuator.setPosition(0);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
