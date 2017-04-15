package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Duncan on 11/5/2016.
 */
@TeleOp(name="2856 Stealth Teleop")
public class StealthTeleop extends OpMode {
    public static int MAX_ENCODER_COUNT = 1680 * 16 / 9;
    DcMotor motors[] = new DcMotor[2];
    DcMotor shooter;
    DcMotor collector;
    DcMotor capballLift1;
    DcMotor capballLift2;

    DcMotor ledMotors;
    DcMotor ledDisplay;

    Servo shooterServo;
    Servo capServo1;
    Servo capServo2;
    Servo capHold;

    ColorSensor rejector1;
    ColorSensor rejector2;

    int rotations;
    int directionModifier;
    int side;

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
        shooter = hardwareMap.dcMotor.get("shooter");
        shooterServo = hardwareMap.servo.get("shooterServo");

        shooterServo.setPosition(ShooterAim.NEAR.get());

        collector = hardwareMap.dcMotor.get("collector");
        capballLift1 = hardwareMap.dcMotor.get("capballLift1");
        capballLift2 = hardwareMap.dcMotor.get("capballLift2");
        capServo1 = hardwareMap.servo.get("cap1");
        capServo2 = hardwareMap.servo.get("cap2");
        capHold = hardwareMap.servo.get("hold");
        capServo1.setPosition(1);
        capServo2.setPosition(0.2);
        capHold.setPosition(0.15);
        distance = hardwareMap.i2cDevice.get("distance");
        Servo actuator = hardwareMap.servo.get("ra");
        actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);
        Servo ba = hardwareMap.servo.get("balign");
        Servo fa = hardwareMap.servo.get("falign");
        ba.setPosition(0.2);
        fa.setPosition(0.1);
        rejector1 = hardwareMap.colorSensor.get("rejector1");
        rejector1.setI2cAddress(new I2cAddr(0x11));
        rejector1.enableLed(true);
        rejector2 = hardwareMap.colorSensor.get("rejector2");
        rejector2.setI2cAddress(new I2cAddr(0x12));
        rejector2.enableLed(true);
 //       motors[0].setDirection(DcMotor.Direction.REVERSE);
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
    }

    @Override
    public void loop() {

        Move(gamepad1);
        ControlCollector(gamepad1);
        ControlShooter(gamepad1, gamepad2);
        controlLift(gamepad2);
        switchDirection(gamepad1);
        controlLed();
        try {
            constantMultChange(gamepad1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (gamepad2.b) {

            //the current distance read from our
            //ultrasonic sensor via an atomic shared variable
            int currentUSDistance = sharedDistance.get();
            //telemetry.addData("Distance: ", shareCache);
            //telemetry.update();
//            if(shareCache >= uFar) { // far
//                shooterServo.setPosition(ShooterAim.FAR.get());
//            } else if (shareCache < uFar && shareCache >= uMedium) { // medium
//                shooterServo.setPosition(ShooterAim.MEDIUM.get());
//            } else if (shareCache < uMedium && shareCache >= uNear) { // near
//                shooterServo.setPosition(ShooterAim.NEAR.get());
//            }
            //shooterServo.setPosition(2.5049/shareCache + 0.49513);

            //interpolate the shooter servo angle from the current
            //distance read from the ultrasonic sensor.
            telemetry.addData("Distance: ", Integer.toString(currentUSDistance));
            telemetry.update();
            shooterServo.setPosition(interpolateShooterPosition(currentUSDistance));
        }

    }

    /** Interpolates shooter servo angle from an ultrasonic
     * distance measurement. Uses a linear function for
     * the transformation
     *
     * @param distance the distance returned by the ultrasonic sensor
     * @return the interpolated shooter angle
     */

    private double interpolateShooterPosition(int distance) {
        //linear function to interpolate shooter angle from distance.
        //f(x) = -0.0010861x + 0.64079
        return  Range.clip(-0.0010861 * (double) distance + 0.64079,0.51, 0.60);
    }

    void constantMultChange(Gamepad pad) throws InterruptedException {
        if(pad.right_stick_button || pad.left_stick_button) {
            constantMult = 2;
        } else {
            constantMult = 1;
        }
    }

    void switchDirection(Gamepad pad){
        if(pad.dpad_up){
            directionModifier = 1;
            constantMult = 1;
        }if(pad.dpad_down){
            directionModifier = -1;
            constantMult = 2;
        }
    }

    private static long shooterWaitTime = 1000;

    void controlLed() {
        ledMotors.setPower(1.0);
    }

    void controlLift(Gamepad pad){
        capballLift1.setPower(Range.clip(-(pad.left_stick_y * Math.abs(pad.left_stick_y)),-1,1));
        capballLift2.setPower(Range.clip(-(pad.left_stick_y * Math.abs(pad.left_stick_y)),-1,1));

        if(pad.y) {
            capServo1.setPosition(0);
            capServo2.setPosition(1);
            capHold.setPosition(1);
        } else if (pad.x) {
            capServo1.setPosition(0.5);
            capServo2.setPosition(0.5);
        }else if(pad.right_bumper&&pad.back){
            capServo1.setPosition(1);
            capServo2.setPosition(0.2);
        }
        if(pad.left_trigger>0.1){
            capHold.setPosition(0.15);
        }else if(pad.right_trigger>0.1){
            capHold.setPosition(0.5);
        }else if(pad.left_bumper){
            capHold.setPosition(1);
        }
    }

    void Move(Gamepad pad){
        direction.setValues(/*pad.right_stick_x * directionModifier*/ 0, -(pad.right_stick_y) * directionModifier, -(pad.left_stick_x * pad.left_stick_x * pad.left_stick_x));

        motors[0].setPower(direction.frontLeftSpeed()/constantMult);
        motors[1].setPower(direction.frontRightSpeed()/constantMult);
        //motors[2].setPower(direction.backRightSpeed()/constantMult);
        //motors[3].setPower(direction.backLeftSpeed()/constantMult);
    }

    public void ControlCollector(Gamepad pad) {
        timeDiff = System.currentTimeMillis() - lastRejection;
        Float colorThreshold = 0f; // WAS 4f tune this, if rejection is too aggressive or not aggressive enough

        if (side == 1) { // 1 indicates blue side
            // color sensor is at the top of the if statement because we want it to override joystick collection
            //telemetry.addData("collector sensor (red, blue)", Integer.toString(rejector1.red()) + " " + Integer.toString(rejector1.blue()));
            if (rejector1.red() > rejector1.blue() + colorThreshold || rejector2.red() > rejector2.blue() + colorThreshold) { // if red is significantly larger than blue, spit out ball
                collector.setPower(-1);
                lastRejection = System.currentTimeMillis();
            } else if (timeDiff < REJECT_LATENCY) {
                collector.setPower(-1);
            } else if(pad.left_trigger > 0.1) {
                collector.setPower(1);
            } else if (pad.left_bumper) {
                collector.setPower(-1);
            } else {
                collector.setPower(0);
            }
            if(rejector1.blue() > rejector1.red() + colorThreshold || rejector2.blue() > rejector2.red() + colorThreshold){
                ledDisplay.setPower(1);
            }else{
                ledDisplay.setPower(0);
            }
        } else { // 1 indicates blue side
            //telemetry.addData("collector sensor (red, blue)", Integer.toString(rejector1.red()) + " " + Integer.toString(rejector1.blue()));
            if (rejector1.blue() > rejector1.red() + colorThreshold || rejector2.blue() > rejector2.red() + colorThreshold) { // if blue is significantly larger than red, spit out ball
                collector.setPower(-1);
                lastRejection = System.currentTimeMillis();
            } else if (timeDiff < REJECT_LATENCY) {
                collector.setPower(-1);
            } else if(pad.left_trigger > 0.1) {
                collector.setPower(1);
            } else if (pad.left_bumper) {
                collector.setPower(-1);
            }else{
                collector.setPower(0);
            }
            if(rejector1.red() > rejector1.blue() + colorThreshold || rejector2.red() > rejector2.blue() + colorThreshold){
                ledDisplay.setPower(1);
            }else{
                ledDisplay.setPower(0);
            }
        }

    }

    public void ControlShooter(Gamepad pad, Gamepad pad2) {

        if (gamepad2.dpad_up) {
            shooterServo.setPosition(ShooterAim.NEAR.get());
        } else if (gamepad2.dpad_right) {
            shooterServo.setPosition(ShooterAim.MEDIUM.get());
        } else if (gamepad2.dpad_down) {
            shooterServo.setPosition(ShooterAim.FAR.get());
        } else if (gamepad2.dpad_left) {
            //shooterServo.setPosition(ShooterAim.LOB.get());
            shooterServo.setPosition(ShooterAim.MEDIUM.get());
        }

        if(pad.right_trigger>0.1) {
            shooter.setPower(-1.0);
        } else if(pad.right_bumper) {
            shooter.setPower(1.0);
        } else if (pad2.a && Math.abs(shooter.getCurrentPosition() % MAX_ENCODER_COUNT) > START_SHOOTER_POSITION) {
            shooter.setPower(-1.0);
        } else if (pad2.a) {
            shooter.setPower(0.0);
        } else {
            shooter.setPower(0.0);
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

    @Override

    public void stop() {
       aimer.stop();
    }
}
