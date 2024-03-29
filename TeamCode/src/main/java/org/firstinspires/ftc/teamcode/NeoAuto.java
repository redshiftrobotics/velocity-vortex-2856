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
    I2cDevice rs;
    DcMotor m0;
    DcMotor m1;
    //DcMotor m2;
    //DcMotor m3;
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

        bAlign = hardwareMap.servo.get("balign");
        fAlign = hardwareMap.servo.get("falign");
        bAlign.setPosition(0.2);
        fAlign.setPosition(0.1);

        Servo capServo = hardwareMap.servo.get("cap");
        capServo.setPosition(0.3);

        bs = hardwareMap.colorSensor.get("rbs");
        actuator = hardwareMap.servo.get("ra");
        rs = hardwareMap.i2cDevice.get("rrs");
        actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);

        initDevices();

        waitForStart();

        robot.Data.PID.I = 0;

        bAlign.setPosition(1);
        fAlign.setPosition(1);

        straightConst();
        robot.Straight(1.2f * (35f/45f), forward, 10, telemetry); //1.4
        turnConst();
        if(side == -1) {
            //turn a little to the right
            robot.Data.PID.turnPrecision = 1f;
            robot.AngleTurn(-30*side, 10, telemetry); //-25 SEE BELOW UPDATE
            shooter.setPower(1);
            Thread.sleep(1000);
            shooter.setPower(0);



            //robot.Straight(.2f * (35f/45f), forward, 10, telemetry);


            turnConst();
            robot.Data.PID.turnPrecision = 5f;
            robot.AngleTurn((180)*side, 10, telemetry);
            robot.Data.PID.turnPrecision = 1f;
            robot.AngleTurn((75+30)*side, 10, telemetry); //UPDATE THIS ASWELL
            robot.AngleTurn(0, 10, telemetry);
            forward = new Float[]{-1f,0f};
            backward = new Float[]{1f,0f};
        } else {
            robot.Data.PID.turnPrecision = 1f;
            robot.AngleTurn(20*side, 10, telemetry);
            //robot.AngleTurn(0, 10, telemetry); // reset
            shooter.setPower(1);
            Thread.sleep(1000);
            shooter.setPower(0);


            robot.Straight(.2f * (35f/45f), forward, 10, telemetry);


            turnConst();
            robot.Data.PID.turnPrecision = 1f;
            robot.AngleTurn((75-20)*side, 10, telemetry);
            robot.AngleTurn(0, 10, telemetry);
        }

        straightConst();
//        robot.Straight(0.2f, forward, 4, telemetry);
//        lineConst();
//        robot.MoveToLine(forward, csf, .2f, 10, telemetry);

        //begin alignment
        robot.Straight(2.17f * (35f/45f), forward, 2, telemetry); // 2.45 to 2.37



        turnConst();

        //robot.AngleTurn(-60*side, 10, telemetry);


        /////////////////////////////////////////////////
        // IF IT IS 60, UNCOMMENT THE UPDATE TARGET -5 //
        /////////////////////////////////////////////////
        robot.AngleTurn(-65*side, 10, telemetry); //-65 for 10 degrees towards wall, but -60 for 15 degrees
        robot.AngleTurn(0, 4, telemetry);


        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.4f;
        robot.Data.PID.PTuning = 16;
        robot.Data.PID.ITuning = 0;
        robot.Straight(1.1f, forward, 3, telemetry);
        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.7f;

        //robot.UpdateTarget(-5*side); //less steep

        //Thread.sleep(1000);

        lineConst();
        robot.Data.PID.ITuning = 0;

        robot.MoveToLine(forward, csb, 0.35f * (35f/45f), 10, telemetry);
        Thread.sleep(100);
        robot.MoveToLine(backward, csb, 0.25f * (35f/45f), 3, telemetry);
        push(0);



        turnConst();
        //robot.AngleTurn(-20*side, 3, telemetry);
        robot.UpdateTarget(-20*side);


        lineConst();
        robot.Data.PID.ITuning = 0;

        robot.Straight(0.4f, backward, 10, telemetry);
        robot.MoveToLine(backward, csb, 0.35f * (35f/45f), 15, telemetry);
        Thread.sleep(100);
        robot.MoveToLine(forward, csb, 0.25f * (35f/45f), 3, telemetry);

        push(0);

        bAlign.setPosition(0);
        fAlign.setPosition(0);


        robot.UpdateTarget(30*side);
        robot.Data.PID.PTuning = 20f;
        //robot.AngleTurn(45*side, 10, telemetry);


        // ONLY ON BLUE, FIX THIS
        if(side != -1) {
            robot.Straight(2f, backward, 3, telemetry);
        }

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
        //m2 = hardwareMap.dcMotor.get("m2");
        //m3 = hardwareMap.dcMotor.get("m3");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setDirection(DcMotor.Direction.REVERSE);
        csb = hardwareMap.opticalDistanceSensor.get("csb");
        robot = new Robot(this, imu, m0, m1, /*m2, m3,*/ rs, telemetry);
        telemetry.addData("IMU:", robot.Data.imu.getAngularOrientation());
        telemetry.addData("Color Sensor: ", bs.red());
    }

    public void push(int degrees) {

        float pushShift = -.03f;

        if(hasPressed) {
            pushShift = .05f;
        }

        if(side == -1) {
            forward = new Float[]{1f, 0f};
            backward = new Float[]{-1f, 0f};
        }


        robot.Data.Drive.STRAIGHT_POWER_CONSTANT = 0.2f;

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
