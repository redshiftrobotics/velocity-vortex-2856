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
public class CameraOp extends OpMode {

    @Override public void init_loop() {}

    @Override public void init() {
        this.vortexProc = new VortexProcessor(hardwareMap.appContext, ColorOption.BLUE);
        this.vortexProc.start();
      try {
          comm = new Socket(InetAddress.getLocalHost(), VortexProcessor.COMMUNICATION_PORT);
          //SocketAddress addr = comm.getRemoteSocketAddress();

          out = new DataOutputStream(comm.getOutputStream());
          in = new DataInputStream(comm.getInputStream());
      } catch (Exception e) {
          e.printStackTrace();
      }
    }

   // public boolean determineRed = false;
   // public boolean determineBlue = false;
    private Socket comm;
    private DataInputStream in;
    private DataOutputStream out;

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

        telemetry.addData("Offset: ", Integer.toString(offset));
    }

    public enum ColorOption {
        BLUE,
        RED,
    }

    @Override public void stop() {
        try {
            out.writeBoolean(true);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}