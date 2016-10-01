package org.firstinspires.ftc.teamcode;

import android.os.Environment;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.redshiftrobotics.config.Config;
import org.firstinspires.ftc.redshiftrobotics.config.ConfigVariable;

/**
 * Created by adam on 10/1/16.
 */
@TeleOp(name="testconfig", group = "test")
public class testOp extends OpMode {
    public Config<Double> conf = new Config<>();

    @Override
    public void loop() {}
    @Override
    public void init() {
        try {
            conf.readFrom("/sdcard/conf.json");
        } catch (Exception e) {
            e.printStackTrace();
            telemetry.addData("err", "json exception occurred");
        }
        if (conf.get("P") != null) {
            Log.d("CONFIGDATA", conf.get("P").getValue().toString());
        } else {
            Log.d("CONFIGDATA", "error");
        }
        //conf.add("P", new ConfigVariable<>(1.0, 0.5, 2.0));
    }
}