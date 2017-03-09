package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Duncan on 11/5/2016.
 */
@TeleOp(name="2856 Stealth Teleop")
public class StealthTeleop extends OpMode {
    DcMotor motors[] = new DcMotor[4];
    DcMotor shooter;
    DcMotor collector;
    DcMotor capballLift;
    Servo capServo;
    int rotations;
    int collecting;
    boolean collectSwitch;
    boolean reseting;
    int directionModifier;

    int constantMult = 1;

    //Servo capArm;
    //float capArmPos;

    DirectionObject direction;

    @Override
    public void init() {
        directionModifier = 1;
        motors[0] = hardwareMap.dcMotor.get("m0");
        motors[1] = hardwareMap.dcMotor.get("m1");
        motors[2] = hardwareMap.dcMotor.get("m2");
        motors[3] = hardwareMap.dcMotor.get("m3");
        shooter = hardwareMap.dcMotor.get("shooter");
        collector = hardwareMap.dcMotor.get("collector");
        capballLift = hardwareMap.dcMotor.get("capballLift");
        capServo = hardwareMap.servo.get("cap");
        capServo.setPosition(0.3);
        Servo actuator = hardwareMap.servo.get("ra");
        actuator.setDirection(Servo.Direction.REVERSE);
        actuator.setPosition(0);
//        motors[0].setDirection(DcMotor.Direction.REVERSE);
//        motors[1].setDirection(DcMotor.Direction.REVERSE);
//        motors[2].setDirection(DcMotor.Direction.REVERSE);
//        motors[3].setDirection(DcMotor.Direction.REVERSE);
        direction = new DirectionObject(0, 0, 0);
        rotations = shooter.getCurrentPosition();
        //capArm = hardwareMap.servo.get("capArm");
        //capArm.setPosition(1.0);
        //capArmPos = 1.0f;
    }

    @Override
    public void loop() {
        Move(gamepad1);
        if(!reseting) {
            SpinMotor(Leftpower(gamepad1), Leftpower(gamepad2), collector);
            SpinMotor(Rightpower(gamepad1), Rightpower(gamepad2), shooter);
        }
        //Sweep(gamepad1);
        resetMotors(gamepad1);
        controlLift(gamepad2);
        switchDirection(gamepad1);
        //controlCap(gamepad2);

        try {
            constantMultChange(gamepad1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        telemetry.addData("Speed", constantMult);
        telemetry.update();
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

    /*void controlCap(Gamepad pad){
        if(pad.a){
            capArmPos = 0.0f;
        }else if(pad.b){
            capArmPos = 0.5f;
        }
        capArm.setPosition(capArmPos);
    }*/

    void controlLift(Gamepad pad){
        capballLift.setPower(Range.clip((pad.left_stick_y * Math.abs(pad.left_stick_y)),-1,1));

        if(pad.y) {
            capServo.setPosition(1);
        } else if (pad.x) {
            capServo.setPosition(0.6);
        }
    }

    void resetMotors(Gamepad pad)
    {
        if(!reseting) {
            if (pad.a) {
                reseting = true;
            }
        }else{
            if(shooter.getCurrentPosition()%1440<rotations){
                shooter.setPower(-1.0);
            }else{
                reseting = false;
            }
        }
    }

    void Move(Gamepad pad){
        direction.setValues(/*pad.right_stick_x * directionModifier*/ 0, -(pad.right_stick_y) * directionModifier, -(pad.left_stick_x * pad.left_stick_x * pad.left_stick_x));

        motors[0].setPower(direction.frontLeftSpeed()/constantMult);
        motors[1].setPower(direction.frontRightSpeed()/constantMult);
        motors[2].setPower(direction.backRightSpeed()/constantMult);
        motors[3].setPower(direction.backLeftSpeed()/constantMult);
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
        }else if(power2==1||power==-1){
            motor.setPower(power2);
        }else{
            motor.setPower(0);
        }
    }
}
