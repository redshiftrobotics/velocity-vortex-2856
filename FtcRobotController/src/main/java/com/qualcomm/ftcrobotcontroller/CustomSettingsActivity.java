package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isaac Zinda on 10/20/2015.
 */

public class CustomSettingsActivity extends Activity {

	FileOutputStream outputStream;
	TextView sideText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_settings);
		sideText = (TextView) findViewById(R.id.sideText);
		sideText.setText(readFromFile());
	}

	public void SendData(final ArrayList<String> ToSend)
	{
		Thread thread = new Thread() {
			@Override
			public void run() {

				// connect to the socket then get the output stream
				try {
					//connect
					Socket soc = new Socket();
					InetSocketAddress addr = new InetSocketAddress(2856);

					Log.d("[client]", "initialized socket");

					soc.connect(addr, 50);
					OutputStream socketData = soc.getOutputStream();
					Log.d("[client]", "received output stream");
					socketData.write(("restart-robot\n").getBytes());
					Log.d("[client]", "written data");

					//Thread.sleep(1000);
					//socketData.close();
					//Log.d("[client]", "closing");
				}
				catch (Exception e)
				{
					Log.d("[client]", "error: " + e.toString());
				}
			}
		};

		thread.start();
	}

	public void blueClicked(View v){
		ArrayList<String> Temp = new ArrayList<String>();
		Temp.add("restart-robot");
		SendData(Temp);

		writeToFile("blue");
		sideText.setText(readFromFile());
	}

	public void redClicked(View v) {
		writeToFile("red");
		sideText.setText(readFromFile());
	}

	public void writeToFile (String string) {
		try {
			File file = new File("/sdcard/Pictures", "prefs");

			//outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
			outputStream = new FileOutputStream(file,false);
			outputStream.write(string.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readFromFile () {
		// Read from preferences file written by the CustomSettingsActivity to determine what side we are on.
		// Retrieve file.
		File file = new File("/sdcard/Pictures","prefs");
		StringBuilder text = new StringBuilder();
		// Attempt to load line from file into the buffer.
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			// Ensure that the first line is not null.
			while ((line = br.readLine()) != null) {
				text.append(line);
			}
			// Close the buffer reader
			br.close();
		}
		// Catch exceptions... Or don't because that would require effort.
		catch (IOException e) {
		}

		// Provide in a more user friendly form.
		return text.toString();
	}



}
