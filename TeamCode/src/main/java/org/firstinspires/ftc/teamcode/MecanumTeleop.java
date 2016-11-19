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
    //DcMotor capballLift
    Servo hopper;
    int collecting = 0;
    boolean collectSwitch = false;
    boolean num2 = false;
    @Override
    public void init() {
        motors[0] = hardwareMap.dcMotor.get("m0");
        motors[1] = hardwareMap.dcMotor.get("m1");
        motors[2] = hardwareMap.dcMotor.get("m2");
        motors[3] = hardwareMap.dcMotor.get("m3");
        shooter = hardwareMap.dcMotor.get("shooter");
        collector = hardwareMap.dcMotor.get("collector");
        //capballLift = hardwareMap.dcMotor.get("capballLift");
        hopper = hardwareMap.servo.get("hopper");
        motors[0].setDirection(DcMotor.Direction.REVERSE);
        motors[1].setDirection(DcMotor.Direction.REVERSE);
        hopper.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void loop() {
        Move(gamepad1);
        Shoot(gamepad1);
        Sweep(gamepad1);
        StopShoot(gamepad2);
    }

    void Move(Gamepad pad){
        DirectionObject direction = new DirectionObject(pad.right_stick_x, -pad.right_stick_y, pad.left_stick_x);

        motors[0].setPower(direction.frontLeftSpeed());
        motors[1].setPower(direction.frontRightSpeed());
        motors[2].setPower(direction.backRightSpeed());
        motors[3].setPower(direction.backLeftSpeed());
    }

    void StopShoot(Gamepad pad){
        if(pad.a){
            shooter.setPower(0);
            hopper.setPosition(0.48);
        }
        if(pad.b){
            collecting = 0;
            collector.setPower(0.0);
        }
        if(pad.left_trigger>0.1){
            shooter.setPower(-1.0);
            num2 = true;
        }else{
            num2 = false;
        }
    }

    void Shoot(Gamepad pad){

        if(pad.left_trigger>0.1){
            shooter.setPower(-1.0);
            hopper.setPosition(0.0);
        }else if(pad.left_bumper) {
            shooter.setPower(1.0);
            hopper.setPosition(1.0);
        }else if(!num2){
            shooter.setPower(0.0);
            hopper.setPosition(0.48);
        }


//        if(pad.left_trigger>0.1){
//            hopper.setPosition(1.0);
//            shooter.setPower(-1.0);
//        }else if(pad.left_bumper){
//            shooter.setPower(1.0);
//        }else{
//            shooter.setPower(0.0);
//        }
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
