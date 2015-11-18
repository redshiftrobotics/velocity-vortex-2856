package org.usfirst.ftc.exampleteam.yourcodehere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

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



	//////////////////////////BLUE////////////////////////////////////



	public static int determineBlue () {

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


		int blueThresh = 200;
		int changeThresh = 10;

		Log.d("blue", Arrays.toString(readArray));


		for(int j = 0; j < readArray.length/pointSample; j++) {
			master = j*10;
			for (i = 0; i < pointSample; i++) {
				if (max == -999) {
					max = readArray[i+master];
					min = readArray[i+master];
				} else {
					if (readArray[i+master] > max) {
						max = readArray[i+master];
					} else if (readArray[i+master] < min) {
						min = readArray[i+master];
					}
				}

			}
			//Log.d("min and max", String.valueOf(min) + "-" + String.valueOf(max));
			if (max - min < changeThresh && min > blueThresh) {
				blueStart = ((j-1)*10)+i;

				Log.d("found blue", String.valueOf(blueStart));
				return blueStart;
			}

			min = -999;
			max = -999;
		}

//		Log.d("min", String.valueOf(min));
//		Log.d("max", String.valueOf(max));
		return -1;
	}





	//////////////////////////RED////////////////////////////////////






	public static int determineRed () {

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


		//TUNE THESE

		int redThresh = 120;
		int changeThresh = 34950;

		for(int j = 0; j < readArray.length/pointSample; j++) {
			master = j*10;
			for (i = 0; i < pointSample; i++) {
				if (max == -999) {
					max = readArray[i+master];
					min = readArray[i+master];
				} else {
					if (readArray[i+master] > max) {
						max = readArray[i+master];
					} else if (readArray[i+master] < min) {
						min = readArray[i+master];
					}
				}

			}

			console.log(min);

			//Log.d("min and max", String.valueOf(min) + "-" + String.valueOf(max));
			if (max - min < changeThresh && min > redThresh) {
				redStart = ((j-1)*10)+i;
				Log.d("found red", String.valueOf(redStart));
				return redStart;
			}

			min = -999;
			max = -999;
		}

//		Log.d("min", String.valueOf(min));
//		Log.d("max", String.valueOf(max));
		return -1;
	}



	////////////////////////USE INFORMATION ///////////////////////////////




	public static String determineSides() {
		int blue = determineBlue();
		int red = determineRed();


		String blueison = "error";
		Log.d("########## BLUE", String.valueOf(blue));
		Log.d("########## RED", String.valueOf(red));
		if(blue != -1 && red != -1) {

			if(blue > red) {
				blueison = "right";
			} else {
				blueison = "left";
			}

		} else {

			if(blue == -1) {
				blueison = "no blue";
			} else if (red == -1){
				blueison = "no red";
			}
		}


		return blueison;
	}




}
