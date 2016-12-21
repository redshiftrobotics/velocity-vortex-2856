package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * CalculateValues is a test teleop for 2856
 * in the use of {@link PIDController}.
 * CalculateValues has three things that it does:
 * <ul>
 *     <li>Display the Average Color to the user</li>
 *     <li>Test the current PID values with forward and turn</li>
 *     <li>Change the PID values using the controller</li>
 * </ul>
 * @author Duncan McKee
 * @version 1.0, 12/19/2016
 */
@TeleOp(name="Test and Calculate Values")
public class CalculateValues extends OpMode {
    I2cDeviceSynch imu;
    DcMotor[] motors;
    PIDController pidController;
    ColorSensor colorSensor1;
    ColorSensor colorSensor2;

    Character mode;
    float p, i, d;

    @Override
    public void init() {
        pidController = new PIDController(imu, motors, colorSensor1, colorSensor2, telemetry, hardwareMap);
        pidController.SetPIDConstants(63f, 10f, 0f, 50f);
        pidController.SetDefaultMultipliers();
        mode = 'p';
        p = 10;
        i = 5;
        d = 1;
    }

    @Override
    public void loop() {
        telemetry.addData("CS1", CalculateAverageColor(colorSensor1));
        telemetry.addData("CS2", CalculateAverageColor(colorSensor2));
        telemetry.addData("P", p);
        telemetry.addData("I", i);
        telemetry.addData("D", d);
        TestPID(gamepad1);
        SetPID(gamepad1);
        telemetry.update();
    }

    private void TestPID(Gamepad $pad){
        pidController.SetPIDConstants(p, i, d);
        if($pad.back){
            pidController.AngularTurn(90f);
            pidController.LinearMove(0f, 1f);
        }
    }

    private void SetPID(Gamepad $pad){
        if($pad.a){
            mode = 'p';
        }else if($pad.b){
            mode = 'i';
        }else if($pad. x){
            mode = 'd';
        }

        if($pad.right_bumper){
            switch (mode){
                case 'p':
                    p--;
                    break;
                case 'i':
                    i--;
                    break;
                case 'd':
                    d--;
                    break;
                default:
                    break;
            }
        }else if($pad.left_bumper){
            switch (mode){
                case 'p':
                    p++;
                    break;
                case 'i':
                    i++;
                    break;
                case 'd':
                    d++;
                    break;
                default:
                    break;
            }
        }
    }

    private float CalculateAverageColor(ColorSensor $colorSensor){
        return ($colorSensor.blue() + $colorSensor.green() + $colorSensor.red())/3;
    }
}
