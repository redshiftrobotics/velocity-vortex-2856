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
@TeleOp(name="2856 Shooter Calibration")
public class CalibrationTeleop extends OpMode {
    public static int MAX_ENCODER_COUNT = 1680 * 16 / 9;
    DcMotor motors[] = new DcMotor[2];
    private float[] driveSpeeds = new float[2];
    DcMotor shooter;
    DcMotor collector;
    DcMotor capballLift;
    DcMotor ledMotors;
    Servo shooterServo;
    Servo capServo;
    ColorSensor rejector;
    int rotations;
    int directionModifier;
    int side;
    double ShooterPos = 0.5;

    int colorControler = 0;

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

    public boolean bButtonPressed = false;
    public boolean aButtonPressed = false;

    private DcMotor capLift1;
    private DcMotor capLift2;


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

        shooterServo.setPosition(ShooterPos);

        collector = hardwareMap.dcMotor.get("collector");
        capballLift = hardwareMap.dcMotor.get("capballLift");

        capLift1 = hardwareMap.dcMotor.get("caplift1");
        capLift2 = hardwareMap.dcMotor.get("caplift2");
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
        rejector = hardwareMap.colorSensor.get("rejector");
        rejector.setI2cAddress(new I2cAddr(0x11));
        rejector.enableLed(true);
//        motors[0].setDirection(DcMotor.Direction.REVERSE);
//        motors[1].setDirection(DcMotor.Direction.REVERSE);
//        motors[2].setDirection(DcMotor.Direction.REVERSE);
//        motors[3].setDirection(DcMotor.Direction.REVERSE);
        collector.setDirection(DcMotorSimple.Direction.REVERSE);
        direction = new DirectionObject(0, 0, 0, DirectionObject.DriveTrain.AllWheelDrive);
        rotations = shooter.getCurrentPosition();
        //capArm = hardwareMap.servo.get("capArm");
        //capArm.setPosition(1.0);
        //capArmPos = 1.0f;
        ledMotors = hardwareMap.dcMotor.get("leds");
        side = getSide();
        aimer = new ConcurrentAimer(distance, sharedDistance);
        aimer.start();
    }

    int uFar = 110;
    int uMedium = 60;
    int uNear = 50;

    @Override
    public void loop() {

        Move(gamepad1);
        ControlCollector(gamepad1);
        //SpinMotor(Leftpower(gamepad1), Leftpower(gamepad2), collector);
        ControlShooter(gamepad1, gamepad2);
        //SpinMotor(Rightpower(gamepad1), Rightpower(gamepad2), shooter);
        controlLift(gamepad2);
        switchDirection(gamepad1);
        controlLed(gamepad2);
        //telemetry.addData("Shooter position: ", Integer.toString(Math.abs(shooter.getCurrentPosition() % MAX_ENCODER_COUNT)));
        try {
            constantMultChange(gamepad1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (gamepad1.b && !bButtonPressed) {
            ShooterPos -= 0.01;
            int shareCache = sharedDistance.get();
            telemetry.addData("Distance: ", shareCache);
            telemetry.addData("ShooterPos: ", ShooterPos);
            telemetry.update();
            shooterServo.setPosition(ShooterPos);
            bButtonPressed = true;
        } else if (!gamepad1.b) {
            bButtonPressed = false;
        }

        if (gamepad1.a && !aButtonPressed) {
            ShooterPos += 0.01;
            int shareCache = sharedDistance.get();
            telemetry.addData("Distance: ", shareCache);
            telemetry.addData("ShooterPos: ", ShooterPos);
            telemetry.update();
            shooterServo.setPosition(ShooterPos);
            aButtonPressed = true;
        } else if (!gamepad1.a) {
            aButtonPressed = false;
        }


        capLift1.setPower(scaleJoystickForCap(gamepad2.left_stick_y));
        capLift2.setPower(scaleJoystickForCap(gamepad2.right_stick_y));
    }

    public float scaleJoystickForCap(float joystickValue) {
        return Range.clip(joystickValue * Math.abs(joystickValue), 1, -1 );
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

    void controlLed(Gamepad pad){
        if(colorControler<10){
            ledMotors.setPower(0);
        }else if(colorControler<20){
            ledMotors.setPower(1);
        }else if(colorControler>20){
            colorControler=1;
        }

        if(pad.left_trigger>0.1){
            colorControler++;
        }else if(colorControler>0){
            colorControler++;
        }
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
        driveSpeeds = direction.drive(pad.right_stick_x * directionModifier, pad.right_stick_y * directionModifier, pad.left_stick_x * pad.left_stick_x * pad.left_stick_x);

        motors[0].setPower(driveSpeeds[0]/constantMult);
        motors[1].setPower(driveSpeeds[1]/constantMult);
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

    public void ControlCollector(Gamepad pad) {
        timeDiff = System.currentTimeMillis() - lastRejection;
        Float colorThreshold = 0f; // WAS 4f tune this, if rejection is too aggressive or not aggressive enough

        if (side == 1) { // 1 indicates blue side
            // color sensor is at the top of the if statement because we want it to override joystick collection
            if (rejector.red() > rejector.blue() + colorThreshold) { // if red is significantly larger than blue, spit out ball
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
        } else { // 1 indicates blue side
            if (rejector.blue() > rejector.red() + colorThreshold) { // if blue is significantly larger than red, spit out ball
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
        }

    }

    public void ControlShooter(Gamepad pad, Gamepad pad2) {
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

    @Override
    public void stop() {
       aimer.stop();
    }
}
