package org.firstinspires.ftc.robotcontroller.internal;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


@TeleOp(name = "center vortex dbg", group = "Image Proc")
public class CameraOpConcurrent extends OpMode {

    @Override public void init_loop() {}

    @Override public void init() {
       this.vortexProc = new VortexProcessor(hardwareMap.appContext, CameraOpConcurrent.ColorOption.BLUE);
        vortexProc.start();
    }

   // public boolean determineRed = false;
   // public boolean determineBlue = false;

    public VortexProcessor vortexProc;

    @Override public void loop() {
        int offset = 0;
      /*  int data;
        try {
            if ((data = in.read()) < 0) {
                return;
            } else {
                offset = data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //do shit

        telemetry.addData("Opmode: ", "Thread Running Concurrently");
    }

    public enum ColorOption {
        BLUE,
        RED,
    }

    @Override public void stop() {
       this.vortexProc.interrupt();
    }
}