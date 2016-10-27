package org.firstinspires.ftc.robotcontroller.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by adam on 10/12/16.
 */

public class VortexProcessor  extends Thread {
    @Override
    public void run() {
        while (true) {
            Log.d("running", "running");
            int offset = 0;
            synchronized (VortexProcessor.this) {
                if (yuvImage != null) {
                    Log.d("NOT", "NULL");
                    Log.d("Converting Image: ", "");
                    convertImage();
                    offset = determineOffset();
                    Log.d("offset: ", "determined");
                }
                Log.d("Offset: ", Integer.toString(offset));
            }
        }
    }

    public VortexProcessor(Context context, CameraOp.ColorOption color) {

        this.color = color;
        camera = ((FtcRobotControllerActivity)context).camera;
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        data = parameters.flatten();
        ((FtcRobotControllerActivity) context).initPreview(camera, this, previewCallback);
        /**
         * Check this one out!!! Some of the references might need to be Atomic.
         */
    }

   CameraOp.ColorOption color;

    private Camera camera;
    public CameraPreview preview;
    public Bitmap image;
    //private int width;
    private AtomicInteger width = new AtomicInteger();
    private AtomicInteger height = new AtomicInteger();
    //private int height;
    private YuvImage yuvImage = null;
    private String data;

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame ( byte[] data, Camera camera) {
            synchronized (VortexProcessor.this) {
                Camera.Parameters parameters = camera.getParameters();
                width.set(parameters.getPreviewSize().width);
                height.set(parameters.getPreviewSize().height);
                Log.d("dimensions: ", "Width: " + Integer.toString(width.get()) + " Height: " + Integer.toString(height.get()));

                yuvImage = new YuvImage(data, ImageFormat.NV21, width.get(), height.get(), null);
            }
        }
    };

    private void convertImage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width.get(), height.get()), 0, out);
        byte[] imageBytes = out.toByteArray();
        image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public int determineOffset() {
        Log.d("Determining offset ", "determining");
       int width = this.width.get();
        int height = this.height.get();
        int threshold;

        if (this.color == CameraOp.ColorOption.RED) {
            threshold = 0;
        } else {
            threshold = 40;
        }
       // int blueAverage = this.averageBlue();
     //   int redAverage = this.averageRed();

       int average = (color == CameraOp.ColorOption.BLUE) ? this.averageBlue() : this.averageRed();

        //int average = 250;
        Log.d("Determining offset", "Averages finished");
        // int redAverage = this.averageRed();
        Vector<int[]> coloredPixels = new Vector<>(); //vector of all tagged pixels...

        if (color == CameraOp.ColorOption.BLUE) {
            Log.d("pixels", "iterating");//if we're testing for blue
            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    if (h % 2 == 0) {
                        if (Color.blue(image.getPixel(w, h)) >= average + threshold) {
                            coloredPixels.add(new int[]{w, h});
                        }
                    }
                }
            }
            Log.d("pixels:", "done");
        } else { // if we're testing for red
            for (int w = 0; w < width; w++) {
                Log.d("Pixels: ", "iterating");
                for (int h = 0; h < height; h++) {
                    if (h % 2 == 0) {
                        if (Color.red(image.getPixel(w, h)) >= average + threshold) {
                            coloredPixels.add(new int[]{w, h});
                        }
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

        int xAvg = 0;
        int yAvg = 0; // not necessary right now, but may be necessary later for determining distance.

        if (coloredPixels.size() != 0) {
            xAvg = Math.round(xSum / coloredPixels.size());
            yAvg = Math.round(ySum / coloredPixels.size());
        }
        //center x coordinate...
        int centerX = Math.round(width / 2);
        Log.d("Thang", Integer.toString(centerX - xAvg));
        return centerX - xAvg;
    }

    public int averageRed() {
        int width = this.width.get(), height = this.height.get();
        int totalRed = 0;
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                if (h % 2 == 0) {
                    totalRed += Color.red(image.getPixel(w, h));
                }
            }
        }

        int averageRed = Math.round(totalRed / (width * height));
        return averageRed;
    }

    public int averageBlue() {
        int width = this.width.get(), height = this.height.get();
        int totalBlue = 0;

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                if (h % 2 == 0) {
                    totalBlue += Color.blue(image.getPixel(w, h));
                }
            }
        }

        int averageBlue = Math.round(totalBlue / (width * height));
        return averageBlue;
    }
}
