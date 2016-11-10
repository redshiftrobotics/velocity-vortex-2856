package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * Created by matt on 10/15/16.
 */
@Disabled @Autonomous(name = "ExampleAutonomous", group = "pid-test")
public class ExampleAutonomous extends LinearOpMode {
    I2cDeviceSynch imu;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Robot robot;


    @Override
    public void runOpMode() throws InterruptedException {
        imu = hardwareMap.i2cDeviceSynch.get("imu");
        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");

        robot = new Robot(imu, m0, m1, m2, m3, telemetry);


        //working PIDs
        //P: 100
        //I: 30
        //D: 0

        //loop
        robot.Data.PID.PTuning = 100f;
        robot.Data.PID.ITuning = 30f;
        robot.Data.PID.DTuning = 0f;

        boolean driving = false;
        TuneState state;

        state = TuneState.P;

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
                } else if (gamepad1.back) {
                    // Start the auto drive. Functionality should halt until the drive stops
                    //forward and right are positive
                    //back and left are negative
                    //pick values using a unit circle: https://www.desmos.com/calculator/qmzx2skkzy
                    //robot.Straight(7f, new Float[]{1f,0f}, 4, telemetry);
                    robot.AngleTurn(90, 5, telemetry);
                }

                // Allow tuning of the values
                switch (state) {
                    case P:
                        if (gamepad1.right_bumper) {
                            robot.Data.PID.PTuning += .1;
                        } else if (gamepad1.right_trigger == 1) {
                            robot.Data.PID.PTuning += 1;
                        } else if (gamepad1.left_bumper) {
                            robot.Data.PID.PTuning -= .1;
                        } else if (gamepad1.left_trigger == 1) {
                            robot.Data.PID.PTuning -= 1;
                        }
                        break;

                    case I:
                        if (gamepad1.right_bumper) {
                            robot.Data.PID.ITuning += .1;
                        } else if (gamepad1.right_trigger == 1) {
                            robot.Data.PID.ITuning += 1;
                        } else if (gamepad1.left_bumper) {
                            robot.Data.PID.ITuning -= .1;
                        } else if (gamepad1.left_trigger == 1) {
                            robot.Data.PID.ITuning -= 1;
                        }
                        break;
                    case D:
                        if (gamepad1.right_bumper) {
                            robot.Data.PID.DTuning += .1;
                        } else if (gamepad1.right_trigger == 1) {
                            robot.Data.PID.DTuning += 1;
                        } else if (gamepad1.left_bumper) {
                            robot.Data.PID.DTuning -= .1;
                        } else if (gamepad1.left_trigger == 1) {
                            robot.Data.PID.DTuning -= 1;
                        }
                        break;
                }
                telemetry.addData("P", robot.Data.PID.PTuning);
                telemetry.addData("I", robot.Data.PID.ITuning);
                telemetry.addData("D", robot.Data.PID.DTuning);
                telemetry.update();
                Thread.sleep(100);
            }

        }


    }

    enum TuneState {
        P, I, D
    }
}