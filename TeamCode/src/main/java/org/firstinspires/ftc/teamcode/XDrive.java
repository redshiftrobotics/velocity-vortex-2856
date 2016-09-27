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

@TeleOp(name="XDrive", group="TestChassis")
public class XDrive extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    public DcMotor[] motors;

    @Override
    public void init() {
        motors = new DcMotor[]{
                hardwareMap.dcMotor.get("motor1"),
                hardwareMap.dcMotor.get("motor2"),
                hardwareMap.dcMotor.get("motor3"),
                hardwareMap.dcMotor.get("motor4")
        };

    }

    /*
       * Code to run when the op mode is first enabled goes here
       * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
       */
    @Override
    public void init_loop() {}

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
        //set motor values
        double[] power = new double[4];

        double roll = gamepad1.right_stick_x;
        double pitch = -gamepad1.right_stick_y;
        double yaw = gamepad1.left_stick_x; //rotation
        /*power[0] = pitch;
        power[1] = -pitch;
        power[2] = -pitch;
        power[3] = pitch;*/

        //quickly initialize array to zero
        for (int i = 0; i < power.length; i++) power[i] = 0.0;

        //set pitch
        for (int i = 0; i < power.length; i++) {
            //if motor 1 or motor 4...
            if (i == 0 || i == 3) {
                power[i] += pitch;
            }
            //if motor 2 or motor 3 ...
            if (i == 1 || i == 2) {
                power[i] -= pitch;
            }
        }

        //set roll
        for (int i = 0; i < power.length; i++) {
           switch (i) {
               //if motors 3 or 4...
               case 2:
               case 3:
                   //increase power
                   power[i] += roll;
                   break;
               //if motors 1 or 2...
               case 0:
               case 1:
                   //decrease power
                   power[i] -= roll;
                   break;
           }
            //yaw is added to all motors regardless, they all turn the same direction
            power[i] += yaw;
        }

        //set power
        for (int i = 0; i < power.length; i++) {
            motors[i].setPower(power[i]);
        }

        updateTelemetry(telemetry);
    }
}
