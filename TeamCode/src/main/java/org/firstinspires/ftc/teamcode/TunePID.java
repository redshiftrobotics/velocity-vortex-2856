package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

@TeleOp(name = "Tune PID", group = "Util")
public class TunePID extends LinearOpMode {

    Robot myRobot;
    TuneState state;
    DcMotorSimple.Direction direction;
    boolean driving = false;

    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    ColorSensor cs;

    public void hw_init() {
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        cs = hardwareMap.colorSensor.get("cs");
        imu = hardwareMap.i2cDeviceSynch.get("imu");
    }
    @Override
    public void runOpMode() throws InterruptedException {
        // Map hardware
        hw_init();

        // Create our driver
        myRobot = new Robot(imu, m0, m1, m2, m3, cs, cs, telemetry);
        // Give it default tunings
        myRobot.Data.PID.PTuning = 20;
        myRobot.Data.PID.ITuning = 20;
        myRobot.Data.PID.DTuning = 20;

        // Give a default state
        state = TuneState.P;
        direction = DcMotor.Direction.FORWARD;

        while (true) {
            if (!driving) {
                // Declare what we are tuning based on the
                if (gamepad1.a) {
                    telemetry.addData("Tuning", "P");
                    state = TuneState.P;
                } else if (gamepad1.b) {
                    telemetry.addData("Tuning", "I");
                    state = TuneState.I;
                } else if (gamepad1.x) {
                    telemetry.addData("Tuning", "D");
                    state = TuneState.D;
                }  else if(gamepad1.dpad_up){
                    telemetry.addData("drive Direction", "Forwards");
                    direction = DcMotor.Direction.FORWARD;
                } else if(gamepad1.dpad_down){
                    telemetry.addData("drive Direction", "Backwards");
                    direction = DcMotor.Direction.REVERSE;
                } else if (gamepad1.back) {
                    // Start the auto drive. Functionality should halt until the drive stops
                    myRobot.Straight(40f, new Float[]{1f, 0f}, 10, telemetry);
                }
                // Allow tuning of the values
                switch (state) {
                    case P:
                        if (gamepad1.right_bumper) {
                            myRobot.Data.PID.PTuning += .1;
                        } else if (gamepad1.right_trigger == 1) {
                            myRobot.Data.PID.PTuning += 1;
                        } else if (gamepad1.left_bumper) {
                            myRobot.Data.PID.PTuning -= .1;
                        } else if (gamepad1.left_trigger == 1) {
                            myRobot.Data.PID.PTuning -= 1;
                        }
                        break;

                    case I:
                        if (gamepad1.right_bumper) {
                            myRobot.Data.PID.ITuning += .1;
                        } else if (gamepad1.right_trigger == 1) {
                            myRobot.Data.PID.ITuning += 1;
                        } else if (gamepad1.left_bumper) {
                            myRobot.Data.PID.ITuning -= .1;
                        } else if (gamepad1.left_trigger == 1) {
                            myRobot.Data.PID.ITuning -= 1;
                        }
                        break;
                    case D:
                        if (gamepad1.right_bumper) {
                            myRobot.Data.PID.DTuning += .1;
                        } else if (gamepad1.right_trigger == 1) {
                            myRobot.Data.PID.DTuning += 1;
                        } else if (gamepad1.left_bumper) {
                            myRobot.Data.PID.DTuning -= .1;
                        } else if (gamepad1.left_trigger == 1) {
                            myRobot.Data.PID.DTuning -= 1;
                        }
                        break;
                }
                telemetry.addData("P", myRobot.Data.PID.PTuning);
                telemetry.addData("I", myRobot.Data.PID.ITuning);
                telemetry.addData("D", myRobot.Data.PID.DTuning);
                telemetry.update();
                Thread.sleep(100);
            }
        }
    }

    enum TuneState {
        P, I, D
    }
}