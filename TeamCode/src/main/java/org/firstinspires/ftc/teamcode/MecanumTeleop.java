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
@TeleOp(name="Mechanum")
public class MecanumTeleop extends OpMode {
    DcMotor motors[] = new DcMotor[4];
    DcMotor shooter;
    DcMotor collector;
    DcMotor capballLift;
    int rotations;
    int collecting;
    boolean collectSwitch;
    boolean reseting;
    int directionModifier;
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
//        motors[0].setDirection(DcMotor.Direction.REVERSE);
//        motors[1].setDirection(DcMotor.Direction.REVERSE);
//        motors[2].setDirection(DcMotor.Direction.REVERSE);
//        motors[3].setDirection(DcMotor.Direction.REVERSE);
        direction = new DirectionObject(0, 0, 0);
        rotations = shooter.getCurrentPosition();
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
    }

    void switchDirection(Gamepad pad){
        if(pad.dpad_up){
            directionModifier = 1;
        }if(pad.dpad_down){
            directionModifier = -1;
        }
    }

    void controlLift(Gamepad pad){
        capballLift.setPower(Range.clip(pad.left_stick_y,-1,1));
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
        direction.setValues(pad.right_stick_x * directionModifier, -pad.right_stick_y * directionModifier, pad.left_stick_x);

        motors[0].setPower(direction.frontLeftSpeed());
        motors[1].setPower(direction.frontRightSpeed());
        motors[2].setPower(direction.backRightSpeed());
        motors[3].setPower(direction.backLeftSpeed());
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

    void Sweep(Gamepad pad){

        if(collecting!=1&&pad.right_trigger>0.1&&collectSwitch){
            collector.setPower(-1.0);
            collecting = 1;
        }else if(collecting==1&&pad.right_trigger>0.1&&collectSwitch){
            collector.setPower(0.0);
            collecting = 0;
        }
        if(collecting!=-1&&pad.right_bumper&&collectSwitch){
            collector.setPower(1.0);
            collecting = -1;
        }else if(collecting==-1&&pad.right_bumper&&collectSwitch){
            collector.setPower(0.0);
            collecting = 0;
        }

        if(pad.right_bumper||pad.right_trigger>0.1){
            collectSwitch = false;
        }else{
            collectSwitch = true;
        }

//        if(pad.right_trigger>0.2){
//            collector.setPower(-1.0);
//        }else if(pad.right_bumper){
//            collector.setPower(1.0);
//        }else{
//            collector.setPower(0.0);
//        }
    }
}
