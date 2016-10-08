package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

@TeleOp(name = "center vortex", group = "Image Proc")
public class CameraOp extends OpMode {
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
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        camera = ((FtcRobotControllerActivity)hardwareMap.appContext).camera;
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        data = parameters.flatten();

        ((FtcRobotControllerActivity) hardwareMap.appContext).initPreview(camera, this, previewCallback);
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */

    //algorithms

    @Override
    public void loop() {
        if (yuvImage != null) {
            convertImage();

            //assume on blue alliance
            int threshold = 40; // how much farther above the average the blue value has to be to be tagged as blue
            int totalBlue = 0;

            for (int h = 0; h < height; h++){
                for (int w = 0; w < width; w++) {
                    totalBlue += Color.blue(image.getPixel(w, h));
                }
            }

            int blueAvg = totalBlue/(width*height); // take
            int[][] columnPixels = new int[width][2];
            for (int w = 0; w < width; w++){
                for (int h = 0; h < height; h++) {
                    if (Color.blue(image.getPixel(w, h)) > blueAvg + threshold) {
                        
                        break;
                    }
                }
                for (int h = height; h > height; h--) {
                    break;
                }


            }

            String colorString = "";

            telemetry.addData("Color:", "Color detected is: " + colorString);
        }
        telemetry.addData("Looped","Looped " + Integer.toString(looped) + " times");
        //Log.d("DEBUG:", data);
        telemetry.update();
    }
}