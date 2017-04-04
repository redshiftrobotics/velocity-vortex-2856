package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
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
    private static int MAX_ENCODER_COUNT = 1680 * 16 / 9;
    private DcMotor motors[] = new DcMotor[2]; //2 Drive train motors, left is 0 and right is 1
    private DcMotor shooter; //Motor to control shooter
    private DcMotor collector; //Motor to control collector
    private DcMotor capballLift; //Capball motor
    private DcMotor ledMotors; //This and ledDisplay are used to control leds on the chassis
    private DcMotor ledDisplay;
    private Servo shooterServo; //Used to angle the shooter
    private Servo capServo; //Used to grab the capball
    private ColorSensor rejector1; //This and rejector2 are used to reject balls of the wrong color from entering
    private ColorSensor rejector2;
    private OpticalDistanceSensor ODS; //Used to check if a ball in in the hopper

    private int directionModifier; //Changes direction of chassis movement
    private int side; //Tells the robot which side we are on

    private boolean ballInHopper = false; //Bool to control the lights when shooting

    private static final float REJECT_LATENCY = 500; //How long to run the collector after seeing incorrect ball

    private int constantMult = 1; //Used to control the maximum movement speed when reversing
    private long timeDiff; //Used to count how long the ball has been in the hopper
    private long lastRejection; //Time in miliseconds when the last rejection happened used to calculate timeDiff
    private static final float colorThreshold = 1f; // WAS 0f; WAS 4f tune this, if rejection is too aggressive or not aggressive enough

    private static final int START_SHOOTER_POSITION = 300; //Encoder position where stop range ends

    /** Shared variable for use with ConcurrentAimer class.
     * @see ConcurrentAimer
     */

    private final AtomicInteger sharedDistance = new AtomicInteger(0);
    private ConcurrentAimer aimer;

    /** Variable to hold the movement speeds of the robot to be able to change the drive train easily
     * @see DirectionObject
     */
    
    DirectionObject direction;


    @Override
    public void init() {
        I2cDevice distance;
        directionModifier = 1;
        motors[0] = hardwareMap.dcMotor.get("m0");
        motors[1] = hardwareMap.dcMotor.get("m1");
        shooter = hardwareMap.dcMotor.get("shooter");
        shooterServo = hardwareMap.servo.get("shooterServo");

        shooterServo.setPosition(0.5);

        collector = hardwareMap.dcMotor.get("collector");
        capballLift = hardwareMap.dcMotor.get("capballLift");
        capServo = hardwareMap.servo.get("cap");
        distance = hardwareMap.i2cDevice.get("distance");
        capServo.setPosition(0.3);
        Servo actuator = hardwareMap.servo.get("ra");
        actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);
        Servo ba = hardwareMap.servo.get("balign");
        Servo fa = hardwareMap.servo.get("falign");
        ba.setPosition(0.2);
        fa.setPosition(0.1);
        rejector1 = hardwareMap.colorSensor.get("rejector1");
        rejector2 = hardwareMap.colorSensor.get("rejector2");
        rejector1.setI2cAddress(new I2cAddr(0x11));
        rejector1.enableLed(true);
        rejector2.setI2cAddress(new I2cAddr(0x12));
        rejector2.enableLed(true);
        collector.setDirection(DcMotorSimple.Direction.REVERSE);
        direction = new DirectionObject(0, 0, 0);
        ledMotors = hardwareMap.dcMotor.get("leds");
        ledDisplay = hardwareMap.dcMotor.get("display");
        side = getSide();
        aimer = new ConcurrentAimer(distance, sharedDistance);
        aimer.start();
        ODS = hardwareMap.opticalDistanceSensor.get("hopper");
        timeDiff = 0;
    }

    int uFar = 110;
    int uMedium = 60;
    int uNear = 50;

    @Override
    public void loop() {

        Move(gamepad1);
        ControlCollector(gamepad1);
        ControlShooter(gamepad1, gamepad2);
        controlLift(gamepad2);
        switchDirection(gamepad1);
        controlLed();
        telemetry.addData("Light", ODS.getLightDetected() * 1024);
        telemetry.update();
        ballInHopper = (ODS.getLightDetected()*1024 < 22);
        try {
            constantMultChange(gamepad1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (gamepad2.b) {
            int shareCache = sharedDistance.get();
//            if(shareCache >= uFar) { // far
//                shooterServo.setPosition(ShooterAim.FAR.get());
//            } else if (shareCache < uFar && shareCache >= uMedium) { // medium
//                shooterServo.setPosition(ShooterAim.MEDIUM.get());
//            } else if (shareCache < uMedium && shareCache >= uNear) { // near
//                shooterServo.setPosition(ShooterAim.NEAR.get());
//            }
            shooterServo.setPosition(2.5049/shareCache + 0.49513);
        }

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
        capballLift.setPower(Range.clip((pad.left_stick_y * Math.abs(pad.left_stick_y)),-1,1));

        if(pad.y) {
            capServo.setPosition(1);
        } else if (pad.x) {
            capServo.setPosition(0.6);
        }
    }

    void Move(Gamepad pad){
        direction.setValues(/*pad.right_stick_x * directionModifier*/ 0, -(pad.right_stick_y) * directionModifier, -(pad.left_stick_x * pad.left_stick_x * pad.left_stick_x));

        motors[0].setPower(direction.frontLeftSpeed()/constantMult);
        motors[1].setPower(direction.frontRightSpeed()/constantMult);
        //motors[2].setPower(direction.backRightSpeed()/constantMult);
        //motors[3].setPower(direction.backLeftSpeed()/constantMult);
    }


    int Leftpower(Gamepad pad){
        if(pad.left_trigger>0.1){
            return -1;
        }else if(pad.left_bumper) {
            return 1;
        }
        return 0;
    }
    int Rightpower(Gamepad pad){
        if(pad.right_trigger>0.1){
            return -1;
        }else if(pad.right_bumper) {
            return 1;
        }
        return 0;
    }

    void SpinMotor(int power, int power2, DcMotor motor){
        if(power==1||power==-1){
            motor.setPower(power);
        }else if(power2==1) {
            motor.setPower(power2);
        } else {
            motor.setPower(0);
        }
    }

    private void ControlCollector(Gamepad pad) {
        timeDiff = System.currentTimeMillis() - lastRejection;

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




    private void ControlShooter(Gamepad pad, Gamepad pad2) {
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
