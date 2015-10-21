package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Isaac Zinda on 10/20/2015.
 */

public class CustomSettingsActivity extends Activity {
	public static float P = 1;
	public static float I = 0;
	public static float D = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.custom_settings);

		//setup buttons
		Button btn = (Button) findViewById(R.id.Send);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//P
				P = Float.parseFloat(((EditText) findViewById(R.id.P)).getText().toString());
				//I
				I = Float.parseFloat(((EditText) findViewById(R.id.I)).getText().toString());
				//D
				D = Float.parseFloat(((EditText) findViewById(R.id.D)).getText().toString());
			}
		});
	}
}
