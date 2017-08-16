package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Duncan on 11/5/2016.
 */
@Disabled
@TeleOp(name="5619")
public class Drive5619 extends OpMode {
    DcMotor motors[] = new DcMotor[4];
    DcMotor hook;
    Servo leftArm;
    Servo rightArm;
    Servo hookLift;

    DirectionObject direction;

    @Override
    public void init() {
        motors[0] = hardwareMap.dcMotor.get("m0");
        motors[1] = hardwareMap.dcMotor.get("m1");
        motors[2] = hardwareMap.dcMotor.get("m2");
        motors[3] = hardwareMap.dcMotor.get("m3");
        hook = hardwareMap.dcMotor.get("hook");
//        motors[0].setDirection(DcMotor.Direction.REVERSE);
//        motors[1].setDirection(DcMotor.Direction.REVERSE);
//        motors[2].setDirection(DcMotor.Direction.REVERSE);
//        motors[3].setDirection(DcMotor.Direction.REVERSE);
        leftArm = hardwareMap.servo.get("leftArm");
        rightArm = hardwareMap.servo.get("rightArm");
        hookLift = hardwareMap.servo.get("hookLift");
        direction = new DirectionObject(0, 0, 0);
    }

    @Override
    public void loop() {
        Move(gamepad1);
        Hooking(gamepad1);

    }

    void Move(Gamepad pad){
        direction.setValues(/*pad.right_stick_x*/ 0, -(pad.right_stick_y), -pad.right_stick_x);

        motors[0].setPower(direction.frontLeftSpeed());
        motors[1].setPower(direction.frontRightSpeed());
        motors[2].setPower(direction.backRightSpeed());
        motors[3].setPower(direction.backLeftSpeed());
    }

    void Hooking(Gamepad pad){
        hook.setPower(pad.left_stick_y);
        hookLift.setPosition(pad.left_stick_x);
    }

    void ExtendArms(Gamepad pad){
        if(pad.left_trigger>0.1){
            leftArm.setPosition(0.5);
        }else{
            leftArm.setPosition(0);
        }
        if(pad.right_trigger>0.1){
            rightArm.setPosition(0.5);
        }else{
            rightArm.setPosition(0);
        }
    }
}
