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

@TeleOp(name = "center vortex", group = "Image Proc")
public class CameraOpOld extends OpMode {
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

        //((FtcRobotControllerActivity) hardwareMap.appContext).initPreview(camera, this, previewCallback);
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */

    //algorithms
    public boolean Red = false;
    public boolean Blue = false;

    @Override
    public void loop() {
        Blue = true;
        Red = false;
        if (gamepad1.x) {
            Blue = true;
            Red = false;
        }

        if (gamepad1.b) {
            Red = true;
            Blue = false;
        }
        if (yuvImage != null) {
            convertImage();

           int offset = 0;
            offset = (Blue) ? determineOffset(ColorOption.RED) : determineOffset(ColorOption.BLUE);

          /*  if (this.Blue) {
                offset = determineOffset(ColorOption.BLUE);
            }
            if (this.Red) {
                offset = determineOffset(ColorOption.RED);
            }
            if (!this.Red && !this.Blue) {
                telemetry.addData("Color Status:" ," not set");
                return;
            }*/

            telemetry.addData( (Red) ? "Red Offset: " : "Blue Offset: ", Integer.toString(offset));
        }


        telemetry.addData("Looped","Looped " + Integer.toString(looped) + " times");
        telemetry.update();
    }

    //options for the potential alliance color.
    public enum ColorOption {
        BLUE,
        RED,
    }

    public int determineOffset(ColorOption c) {

        int threshold;

        if (c == ColorOption.RED) {
            threshold = 0;
        } else {
            threshold = 40;
        }
        int blueAverage = this.averageBlue();
        int redAverage = this.averageRed();
        Vector<int[]> coloredPixels = new Vector<>(); //vector of all tagged pixels...

        if (c == ColorOption.BLUE) { //if we're testing for blue
            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    if (Color.blue(image.getPixel(w, h)) >= blueAverage + threshold) {
                        coloredPixels.add(new int[] {w, h});
                    }
                }
            }
        } else { // if we're testing for red
            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    if (Color.red(image.getPixel(w, h)) >= redAverage + threshold) {
                        coloredPixels.add(new int[] {w, h});
                    }
                }
            }
        }
        int xSum = 0;
        int ySum = 0;

        for (int[] point : coloredPixels) {
            xSum += point[0];
            ySum += point[1];
        }

       // telemetry.addData("xSum is:" , Integer.toString(xSum));

      //  telemetry.addData("xSum / pixels.size()", Float.toString(Math.round(xSum / coloredPixels.size())));
        int xAvg = 0;
        int yAvg = 0; // not necessary right now, but may be necessary later for determining distance.
        if (coloredPixels.size() != 0) {
            xAvg = Math.round(xSum / coloredPixels.size());
            yAvg = Math.round(ySum / coloredPixels.size());
        }
        //center x coordinate...
        int centerX = Math.round(width / 2);
      //  telemetry.addData("Image center:", "" + centerX);
        //offset from rounded
        //if value is negative,
        // center is to the right of the center,
        //otherwise, it's on the left.


       // telemetry.addData("Offset is: ", "" + offset);
        telemetry.addData("center should be: ", "(" + Integer.toString(xAvg) + ")");
        telemetry.addData("actual center is: ", Integer.toString(centerX));
        return centerX - xAvg;
    }

    public int averageRed() {
        int totalRed = 0;
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                totalRed += Color.red(this.image.getPixel(w, h));
            }
        }

        int averageRed = Math.round(totalRed / (this.width * this.height));
        telemetry.addData("average red: ", Integer.toString(averageRed));
        return averageRed;
    }

    public int averageBlue() {
        int totalBlue = 0;
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                totalBlue += Color.blue(image.getPixel(w, h));
            }
        }
        int averageBlue = Math.round(totalBlue / (this.width * this.height));
        telemetry.addData("average blue: ", Integer.toString(averageBlue));
        return averageBlue;
    }
}