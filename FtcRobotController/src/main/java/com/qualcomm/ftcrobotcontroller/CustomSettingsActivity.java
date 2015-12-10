package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.FileOutputStream;

/**
 * Created by Isaac Zinda on 10/20/2015.
 */

public class CustomSettingsActivity extends Activity {

	String filename = "/sdcard/Pictures/prefs";
	FileOutputStream outputStream;

	public static enum FieldSide {
		RED,
		BLUE
	}
	public static enum RampCloseness {
		NEAR,
		FAR
	}
	public static FieldSide fieldSide;
	public static RampCloseness rampCloseness;
	public static int startDelay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_settings);
	}

	public void onFieldRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
		int id = view.getId();
		if (id == R.id.radioButtonBlue) {
			if (checked) fieldSide = FieldSide.BLUE;
			writeToFile("blue");

		} else if (id == R.id.radioButtonRed) {
			if (checked) fieldSide = FieldSide.RED;
			writeToFile("red");
		}
	}

	public void onRampRadioButtonClicked (View view) {
		boolean checked = ((RadioButton) view).isChecked();
		int id = view.getId();
		if (id == R.id.rampCloseRadioButton) {
			if (checked) rampCloseness = RampCloseness.NEAR;
		} else if (id == R.id.rampFarRadioButton) {
			if (checked) rampCloseness = RampCloseness.FAR;
		}
	}

	public void onDelayTextboxChanged (View view) {
		if (((EditText) view).getText().toString().equals("")) {
			startDelay = 0;
			return;
		}

		startDelay = Integer.parseInt(((EditText) view).getText().toString());
	}

	public void writeToFile (String string) {
		try {
			outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
			outputStream.write(string.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
