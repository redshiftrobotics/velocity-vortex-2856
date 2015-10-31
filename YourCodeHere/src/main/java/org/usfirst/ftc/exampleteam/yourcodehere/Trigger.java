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

	public static void toast(String text) {
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
}
