package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;


/**
 * Created by Duncan on 7/28/2017.
 */

@Autonomous(name="Sensor: Vuforia", group="Sensor")
public class Vuforia extends OpMode{
    VuforiaMaster vuforiaMaster = new VuforiaMaster();

    @Override
    public void init() {
        vuforiaMaster.Setup(VuforiaMaster.ImageType.Gears, VuforiaLocalizer.CameraDirection.BACK);
    }

    @Override
    public void loop() {
        vuforiaMaster.UpdateLocation();
        if(vuforiaMaster.listener.isVisible()) {
            telemetry.addLine().addData("Target", "Gears").addData("Position", vuforiaMaster.position).addData("Angle", vuforiaMaster.angle);
        }else{
            telemetry.addLine().addData("Target", "None");
        }
        telemetry.update();
    }


}
