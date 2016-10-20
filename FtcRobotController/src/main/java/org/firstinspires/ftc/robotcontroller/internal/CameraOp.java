package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;

@TeleOp(name = "center vortex dbg", group = "Image Proc")
public class CameraOp extends OpMode {

    //globals
    private Camera camera;
    public CameraPreview preview;
    public Bitmap image;
    private int width;
    private int height;
    private YuvImage yuvImage = null;
    private int looped = 0;
    private String data;

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera)
        {
            Camera.Parameters parameters = camera.getParameters();
            width = parameters.getPreviewSize().width;
            height = parameters.getPreviewSize().height;
            yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
            looped += 1;
        }
    };

    private void convertImage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 0, out);
        byte[] imageBytes = out.toByteArray();
        image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @Override public void init_loop() {}

    @Override public void init() {
        telemetry.addData("Status: ", "Initialized");
        camera = ((FtcRobotControllerActivity)hardwareMap.appContext).camera;
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        data = parameters.flatten();
        ((FtcRobotControllerActivity) hardwareMap.appContext).initPreview(camera, this, previewCallback);
    }

    @Override public void loop() {
        int averageRed;
        telemetry.addData("Status: ", "Running...");
        if (yuvImage != null) {
            telemetry.addData("Status: ", "Converting image");
            convertImage();
            if (this.image != null) telemetry.addData("Image: ", "Image != null");
            telemetry.addData("Image Dimensions: ", "Width: " + Integer.toString(width) + "Height: " + Integer.toString(height));
            averageRed = averageRed();
            //telemetry.addData("Average Red: ", Integer.toString(averageRed));
        }

    }

    @Override public void stop() {}

    public int averageRed() {
        int totalRed = 0;
        for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    if (h % 2 == 0) {
                        totalRed += Color.red(image.getPixel(w, h));
                    }
                }
        }

        telemetry.addData("Red sum: ", Integer.toString(totalRed));
        int averageRed = Math.round(totalRed / ((this.width/2) * this.height));
        telemetry.addData("average red: ", Integer.toString(averageRed));
       // return averageRed;
        return averageRed;
    }


}