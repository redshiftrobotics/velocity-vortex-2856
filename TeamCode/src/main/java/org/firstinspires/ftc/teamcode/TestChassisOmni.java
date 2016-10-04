package org.firstinspires.ftc.teamcode;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by matt on 9/17/16.
 */

@TeleOp(name="Omni", group="TestChassis")
public class TestChassisOmni extends OpMode {
    public DcMotor m1   = null;
    public DcMotor  m2  = null;
    public DcMotor  m3  = null;
    public DcMotor  m4  = null;
    public DcMotor[] motors;

    HardwareMap hwMap =  null;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        Log.e("OPMODE STATUS:", "started");
        hwMap = hardwareMap;

        m1   = hwMap.dcMotor.get("m0");
        m2 = hwMap.dcMotor.get("m1");
        m3   = hwMap.dcMotor.get("m2");
        m4 = hwMap.dcMotor.get("m3");

        motors = new DcMotor[]{m1, m2, m3, m4};

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
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop() cvvc vvcvcvbvccvccvbvcvbvcvbv
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
        double[] power = new double[4];

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        roll = gamepad1.right_stick_x; //forward
        pitch = -gamepad1.right_stick_y; //side to side
        yaw = gamepad1.left_stick_x; //turning

        for (int i = 0; i < power.length; i++) {
            if(i == 2 || i == 3) {
                power[i] -= pitch;
            } else {
                power[i] += pitch;
            }
            power[i] += yaw;
            if(i < 2) { //on the first two motors (0 and 1)
                power[i] += roll;
            } else {
                power[i] -= roll;
            }
            if(i == 1 || i == 3) {
                power[i] = -power[i];
            }
            //power[i] /= 128; //scalar
            telemetry.addData("Motor", power[i]);
            motors[i].setPower(power[i]/2);
        }
        updateTelemetry(telemetry);
    }
}
