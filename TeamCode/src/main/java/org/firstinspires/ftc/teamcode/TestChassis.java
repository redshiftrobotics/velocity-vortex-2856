package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * Created by matt on 9/17/16.
 */

@TeleOp(name="Mechanum", group="TestChassis")
public class TestChassis extends OpMode {
    public DcMotor m1   = null;
    public DcMotor  m2  = null;
    public DcMotor  m3  = null;
    public DcMotor  m4  = null;

    HardwareMap hwMap =  null;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {

        hwMap = hardwareMap;

        m1   = hwMap.dcMotor.get("motor1");
        m2 = hwMap.dcMotor.get("motor2");
        m3   = hwMap.dcMotor.get("motor3");
        m4 = hwMap.dcMotor.get("motor4");

        telemetry.addData("Status", "Initialized");


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        updateTelemetry(telemetry);
    }

    /*
       * Code to run when the op mode is first enabled goes here
       * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
       */
    @Override
    public void init_loop() {

    }

    /*
     * This method will be called ONCE when start is pressed
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        double roll;
        double pitch;
        double yaw;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        roll = gamepad1.right_stick_x; //forward
        pitch = -gamepad1.right_stick_y; //side to side
        yaw = gamepad1.left_stick_x; //turning

        double m1p = 0;
        double m2p = 0;
        double m3p = 0;
        double m4p = 0;

        m1p += pitch;
        m2p += pitch;
        m3p += pitch;
        m4p += pitch;

        m1p += roll;
        m2p += roll;
        m3p -= roll;
        m4p -= roll;

        m1p += yaw;
        m2p -= yaw;
        m3p += yaw;
        m4p -= yaw;



        updateTelemetry(telemetry);
    }
}
