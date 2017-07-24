package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Duncan on 7/23/2017.
 */

@Autonomous(name="rev")
public class RevTest extends OpMode{

    DcMotor dcMotor;

    @Override
    public void init() {
        dcMotor = hardwareMap.dcMotor.get("motor");
    }

    @Override
    public void loop() {
        dcMotor.setPower(1.0);
    }
}
