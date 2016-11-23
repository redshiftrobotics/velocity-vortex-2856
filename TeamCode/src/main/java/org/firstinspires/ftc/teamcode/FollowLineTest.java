package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Duncan on 11/22/2016.
 */

@Autonomous(name="Read Line")
public class FollowLineTest extends LinearOpMode{
    public ColorSensor colorSensor;


    @Override
    public void runOpMode() throws InterruptedException {
        colorSensor = hardwareMap.colorSensor.get("color_sensor");

        waitForStart();

        while(opModeIsActive()){
//            telemetry.addData("Red: ", colorSensor.red());
//            telemetry.addData("Green: ", colorSensor.green());
//            telemetry.addData("Blue: ", colorSensor.blue());

            if(colorSensor.red()>colorSensor.green()&&colorSensor.red()>colorSensor.blue()){
                telemetry.addData("Red: ", colorSensor.red());
            }else if(colorSensor.green()>colorSensor.blue()){
                telemetry.addData("Green: ", colorSensor.green());
            }else{
                telemetry.addData("Blue: ", colorSensor.blue());
            }

            int average = (colorSensor.red() + colorSensor.blue() + colorSensor.green())/3;

            if(average>100){
                telemetry.addData("Line!", 1);
            }else{
                telemetry.addData("No Line!", 0);
            }

            telemetry.addData("Average: ", average);

            telemetry.update();
        }
    }
}
