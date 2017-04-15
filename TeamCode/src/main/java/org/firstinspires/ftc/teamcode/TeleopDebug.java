package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adam on 4/13/17.
 */

@TeleOp(name = "Teleop Debug", group = "test")
public class TeleopDebug extends OpMode {
    public static int MAX_ENCODER_COUNT = 1680 * 16 / 9;
    DcMotor motors[] = new DcMotor[2];
    DcMotor shooter;
    DcMotor collector;
    DcMotor capballLift;
    DcMotor ledMotors;
    DcMotor ledDisplay;
    Servo shooterServo;
    Servo capServo;
    ColorSensor rejector;
    OpticalDistanceSensor ODS;
    int rotations;
    int directionModifier;
    int side;

    boolean ballInHopper = false;

    public final float REJECT_LATENCY = 500;

    int constantMult = 1;
    public long timeDiff;
    public long lastRejection;

    public static int START_SHOOTER_POSITION = 300;

    private final AtomicInteger sharedDistance = new AtomicInteger(0);
    private ConcurrentAimer aimer;

    //Servo capArm;
    //float capArmPos;

    DirectionObject direction;


    @Override
    public void init() {
       I2cDevice distance;
        directionModifier = 1;
        motors[0] = hardwareMap.dcMotor.get("m0");
        motors[1] = hardwareMap.dcMotor.get("m1");
        //motors[2] = hardwareMap.dcMotor.get("m2");
        //motors[3] = hardwareMap.dcMotor.get("m3");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooterServo = hardwareMap.servo.get("shooterServo");

        shooterServo.setPosition(0.5);

        collector = hardwareMap.dcMotor.get("collector");
        //capballLift1 = hardwareMap.dcMotor.get("capballLift1");
        capServo = hardwareMap.servo.get("cap");
        distance = hardwareMap.i2cDevice.get("distance");
        capServo.setPosition(0.3);
        telemetry.addData("###", " Made it!");
        telemetry.update();
       Servo actuator = hardwareMap.servo.get("ra");
        actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);
        Servo ba = hardwareMap.servo.get("balign");
        Servo fa = hardwareMap.servo.get("falign");
        ba.setPosition(0.2);
        fa.setPosition(0.1);
        rejector = hardwareMap.colorSensor.get("rejector1");
        rejector.setI2cAddress(new I2cAddr(0x11));
        rejector.enableLed(true);
//        motors[0].setDirection(DcMotor.Direction.REVERSE);
//        motors[1].setDirection(DcMotor.Direction.REVERSE);
//        motors[2].setDirection(DcMotor.Direction.REVERSE);
//        motors[3].setDirection(DcMotor.Direction.REVERSE);
        collector.setDirection(DcMotorSimple.Direction.REVERSE);
        direction = new DirectionObject(0, 0, 0);
        rotations = shooter.getCurrentPosition();
        //capArm = hardwareMap.servo.get("capArm");
        //capArm.setPosition(1.0);
        //capArmPos = 1.0f;
        ledMotors = hardwareMap.dcMotor.get("leds");
        ledDisplay = hardwareMap.dcMotor.get("display");
        side = getSide();
        aimer = new ConcurrentAimer(distance, sharedDistance);
        aimer.start();
        ODS = hardwareMap.opticalDistanceSensor.get("hopper");


        telemetry.addData("init", "init");
    }

    @Override public void loop() {
        telemetry.addData("Made it", "to loop");
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

        /**The following glorious comment has been preserved within the depths of this source file,
         * as a reminder to Matthew Kelsey  (https://gihub.com/mattkelsey)
         * that he should always handle errors and exceptions!
         * */

        /** ______________________________________________________________**/
        /**
         * Catch exceptions... Or don't because that would require effort
         **/


        catch (IOException e) {
            telemetry.addData("Error", "Failed to read from preferences file!");
            telemetry.update();
            e.printStackTrace();
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