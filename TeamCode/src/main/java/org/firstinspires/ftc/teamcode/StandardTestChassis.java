package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by matt on 9/24/16.
 */
@TeleOp(name="Standard", group="TestChassis")
public class StandardTestChassis extends OpMode {
    public DcMotor left   = null;
    public DcMotor  right  = null;

    HardwareMap hwMap =  null;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        Log.e("OPMODE STATUS:", "started");
        hwMap = hardwareMap;

        left   = hwMap.dcMotor.get("left");
        right = hwMap.dcMotor.get("right");

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
        left.setPower(-gamepad1.left_stick_y);
        right.setPower(gamepad1.right_stick_y);

        updateTelemetry(telemetry);
    }
}
