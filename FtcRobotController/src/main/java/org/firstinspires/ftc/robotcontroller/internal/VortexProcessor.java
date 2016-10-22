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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by adam on 10/12/16.
 */

public class VortexProcessor  {

    static int COMMUNICATION_PORT = 2856;
    public Thread thread;
    public ServerSocket serverSocket;

    Runnable vortexRunnable =  new Runnable() {
        @Override
        public void run() {
            while (true) {
                Log.d("running", "running");
                int offset = 0;
                try (
                        Socket client = serverSocket.accept();
                        DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
                        DataInputStream inputStream = new DataInputStream(client.getInputStream())
                ) {
                    boolean stop = inputStream.read() > 0;
                    if (stop) {
                        Log.d("Thread Status: ", "interrupting thread");

                        //camera.release();
                        Thread.currentThread().stop();
                        return;
                    }

                    if (yuvImage != null) {
                        Log.d("Converting Image: ", "");
                        convertImage();
                        offset = determineOffset();
                    }

                    Log.d("Offset: ", Integer.toString(offset));
                    outputStream.write(offset);

                } catch (java.io.IOException e) {
                    Log.d("Exception", "Exception");
                    e.printStackTrace();
                }

            }
        }
    };

    public VortexProcessor(Context context, CameraOp.ColorOption color) {



        try {
            this.serverSocket = new ServerSocket(COMMUNICATION_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        this.color = color;
        camera = ((FtcRobotControllerActivity)context).camera;
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        data = parameters.flatten();
        ((FtcRobotControllerActivity) context).initPreview(camera, this, previewCallback);

        this.thread = new Thread(vortexRunnable);
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
    private AtomicReference<YuvImage> yuvImage = new AtomicReference<>();
    private String data;

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            width.set(parameters.getPreviewSize().width);
            height.set(parameters.getPreviewSize().height);
            yuvImage.set(new YuvImage(data, ImageFormat.NV21, width.get(), height.get(), null));
        }
    };

    private void convertImage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.get().compressToJpeg(new Rect(0, 0, width.get(), height.get()), 0, out);
        byte[] imageBytes = out.toByteArray();
        image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }


    public int determineOffset() {
        int width = this.width.get();
        int height = this.height.get();
        int threshold;
        if (this.color == CameraOp.ColorOption.RED) {
            threshold = 0;
        } else {
            threshold = 40;
        }
        int blueAverage = this.averageBlue();
        int redAverage = this.averageRed();
        // int redAverage = this.averageRed();
        Vector<int[]> coloredPixels = new Vector<>(); //vector of all tagged pixels...

        if (color == CameraOp.ColorOption.BLUE) { //if we're testing for blue
            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    if (h % 2 == 0) {
                        if (Color.blue(image.getPixel(w, h)) >= blueAverage + threshold) {
                            coloredPixels.add(new int[]{w, h});
                        }
                    }
                }
            }
        } else { // if we're testing for red
            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    if (h % 2 == 0) {
                        if (Color.red(image.getPixel(w, h)) >= redAverage + threshold) {
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
        return centerX - xAvg;
    }

    public int averageRed() {
        int width = this.width.get(), height = this.height.get();
        int totalRed = 0;
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                    totalRed += Color.red(image.getPixel(w, h));
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
                    totalBlue += Color.blue(image.getPixel(w, h));
            }
        }

        int averageBlue = Math.round(totalBlue / (width * height));
        return averageBlue;
    }

    public void start() {
        Log.d("Thread: ", "Starting...");
        this.thread.start();
    }
}
