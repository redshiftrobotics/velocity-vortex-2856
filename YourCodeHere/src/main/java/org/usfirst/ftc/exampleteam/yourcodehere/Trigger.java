package org.usfirst.ftc.exampleteam.yourcodehere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by matt on 10/30/15.
 */
public class Trigger {

	public static void takeImage() {

		Socket soc = new Socket();
		InetSocketAddress addr = new InetSocketAddress(2856);
		String s = "lol";
		byte[] b = s.getBytes();

		try {
			soc.connect(addr, 50);
			OutputStream socketData = soc.getOutputStream();
			socketData.write(b);
			socketData.close();

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static String findAvgSides() {

		Bitmap bitproc = BitmapFactory.decodeFile("/sdcard/Pictures/processing/proc.jpg");
		//1/6
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {

					android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
					//findAvgSides();
					boolean lol = false;
					if(lol) {
						throw new InterruptedException("Nothing really errored, the try catch block just needed an exception... So here it is.");
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		thread.start();
		int readArray[];
		readArray= new int[100];
		int arrayLoadNumber = -1;
		for (int i = 0; i < 4; i++) {
			int xcord = (bitproc.getWidth()/8)*((2*i) + 1);
			for (int j = 0; j < 4; j++) {
				int ycord = (bitproc.getHeight()/8)*((2*j) + 1);
				arrayLoadNumber++;
				//Log.d("someshit", String.valueOf(arrayLoadNumber));
				readArray[arrayLoadNumber] = Color.blue(bitproc.getPixel(xcord, ycord));
			}
		}


		int leftsum = 0;
		int rightsum = 0;
		for (int i = 0; i <= 7; i++) {
			leftsum += readArray[i];
		}
		for (int i = 8; i <= 15; i++) {
			//Log.d("readArrayval", String.valueOf(readArray[i]));
			rightsum += readArray[i];
		}
		//Log.d("rightsum", String.valueOf(rightsum));
		int leftavg = leftsum/8;
		int rightavg = rightsum/8;



		int duration = Toast.LENGTH_SHORT;


		String text;
		if (leftavg > rightavg){
			//Blue is on the:
			text = "left";
			Log.d("bitproc", text);

		} else if (leftavg < rightavg){
			//Blue is on the:
			text = "right";
			Log.d("bitproc", text);
		} else {
			text = "broken";
			Log.d("bitproc", text);

		}

		return text;
	}


	public static int[] determineRed () {

		Bitmap bitproc = BitmapFactory.decodeFile("/sdcard/Pictures/processing/proc.jpg");
		int readArray[];
		readArray = new int[bitproc.getWidth()];

		//find change over 20 points

		for(int i = 0; i < readArray.length; i++) {

			int sumsauce = 0;
			//for int potassium = bananas
			for(int k = bitproc.getHeight()/3; k < 2*(bitproc.getHeight()/3); k++) {
				//isolate black and remove it so as not to skew average
				if(Color.red(bitproc.getPixel(i, k)) > 10) {
					sumsauce += Color.red(bitproc.getPixel(i, k));
				}

			}

			readArray[i] = sumsauce/(bitproc.getHeight()/3);

			//deprecated line... this samples just one line along the image in the very center
//			readArray[i]=Color.blue(bitproc.getPixel(i, bitproc.getHeight() / 2));


		}

		//average above threshold and change is under threshold

		int max = -999;
		int min = -999;
		int pointSample = 10;
		int master = 0;
		int redStart = -1;
		int i = 0;
		int redEnd = -1;
		boolean onRed = false;
		int returner[];
		returner = new int[3];



		Log.d("red", Arrays.toString(readArray));

		//TUNE THESE

		int redThresh = 170;
		int changeThresh = 20;

		for(int j = 0; j < readArray.length-pointSample; j++) {
			for (i = 0; i < pointSample; i++) {
				if (max == -999) {
					max = readArray[i+j];
					min = readArray[i+j];
				} else {
					if (readArray[i+j] > max) {
						max = readArray[i+j];
					} else if (readArray[i+j] < min) {
						min = readArray[i+j];
					}
				}

			}


			//Log.d("min and max", String.valueOf(min) + "-" + String.valueOf(max));
			if (max - min < changeThresh && min > redThresh  && !onRed) {
				redStart = j;
				returner[0] = redStart;
				onRed = true;
				Log.d("found red", String.valueOf(redStart));
			} else if (onRed && max - min > changeThresh && min < redThresh) {
				redEnd = j;
				returner[0] = redEnd;
				console.log("red ends " + redEnd);
				onRed = false;
				return returner;

			}

			min = -999;
			max = -999;
		}

//		Log.d("min", String.valueOf(min));
//		Log.d("max", String.valueOf(max));
		returner[0] = -1;
		returner[1] = -1;
		returner[2] = bitproc.getWidth();
		return returner;
	}





	public static int[] determineBlue() {

		Bitmap bitproc = BitmapFactory.decodeFile("/sdcard/Pictures/processing/proc.jpg");
		int readArray[];
		readArray = new int[bitproc.getWidth()];

		//find change over 20 points

		for(int i = 0; i < readArray.length; i++) {

			int sumsauce = 0;
			//for int potassium = bananas
			for(int k = bitproc.getHeight()/3; k < 2*(bitproc.getHeight()/3); k++) {
				//isolate black and remove it so as not to skew average
				if(Color.blue(bitproc.getPixel(i, k)) > 10) {
					sumsauce += Color.blue(bitproc.getPixel(i, k));
				}

			}

			readArray[i] = sumsauce/(bitproc.getHeight()/3);

			//deprecated line... this samples just one line along the image in the very center
//			readArray[i]=Color.blue(bitproc.getPixel(i, bitproc.getHeight() / 2));


		}

		//average above threshold and change is under threshold

		int max = -999;
		int min = -999;
		int pointSample = 10;
		int master = 0;
		int blueStart = -1;
		int i = 0;
		int blueEnd = -1;
		boolean onBlue = false;
		int returner[];
		returner = new int[4];

		int blueThresh = 200;
		int changeThresh = 25;

		Log.d("blue", Arrays.toString(readArray));


		for(int j = 0; j < readArray.length-pointSample; j++) {
			for (i = 0; i < pointSample; i++) {
				if (max == -999) {
					max = readArray[j+i];
					min = readArray[j+i];
				} else {
					if (readArray[j+i] > max) {
						max = readArray[j+i];
					} else if (readArray[j+i] < min) {
						min = readArray[j+i];
					}
				}

			}
			//Log.d("min and max", String.valueOf(min) + "-" + String.valueOf(max));
			if (max - min < changeThresh && min > blueThresh && !onBlue) {
				blueStart = j;
				returner[0] = blueStart;
				onBlue = true;
				Log.d("found blue", String.valueOf(blueStart));


				//return blueStart;
			} else if (onBlue && max - min > changeThresh && min < blueThresh) {
				blueEnd = j;
				returner[0] = blueEnd;
				console.log("blue ends" + blueEnd);
				onBlue = false;
				return returner;


			}

			min = -999;
			max = -999;
		}

//		Log.d("min", String.valueOf(min));
//		Log.d("max", String.valueOf(max));
		returner[0] = -1;
		returner[1] = -1;
		returner[2] = bitproc.getWidth();
		return returner;
	}




	////////////////////////USE INFORMATION ///////////////////////////////


	public static void seek(DcMotor rightMotor, DcMotor leftMotor) {

		int blueIndex;
		int redIndex;
		if(determineSides() == "left") {
			blueIndex = 1;
			redIndex = 0;
		} else {
			blueIndex = 0;
			redIndex = 1;
		}

		int center;
		takeImage();
		int[] blue = determineBlue();
		int[] red = determineRed();
		center = (blue[blueIndex] + red[redIndex])/2;

		while(center < blue[2]/2/*width / 2*/) {
			takeImage();
			blue = determineBlue();
			red = determineRed();
			center = (blue[blueIndex]+red[redIndex])/2;
			//center is further to left, robot must turn right
			leftMotor.setPower(0.2);
		}
		while(center > blue[2]/2) {
			takeImage();
			blue = determineBlue();
			red = determineRed();
			center = (blue[blueIndex]+red[redIndex])/2;
			//center is further to right, robot must turn left
			rightMotor.setPower(0.2);
		}

	}


	public static String determineSides() {
		int[] blue = determineBlue();
		int[] red = determineRed();


		String blueison = "error";
		Log.d("########## BLUE", String.valueOf(blue));
		Log.d("########## RED", String.valueOf(red));
		if(blue[0] != -1 && red[0] != -1) {

			if(blue[0] > red[0]) {
				blueison = "right";
			} else {
				blueison = "left";
			}

		} else {

			if(blue[0] == -1) {
				blueison = "no blue";
			} else if (red[0] == -1){
				blueison = "no red";
			}
		}


		return blueison;
	}


}
