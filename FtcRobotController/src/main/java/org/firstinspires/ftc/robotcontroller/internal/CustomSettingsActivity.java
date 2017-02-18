package org.firstinspires.ftc.robotcontroller.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qualcomm.ftcrobotcontroller.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Isaac Zinda on 10/20/2015.
 */

public class CustomSettingsActivity extends Activity {

    FileOutputStream outputStream;
    TextView sideText;
    EditText autoConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_settings);
        sideText = (TextView) findViewById(R.id.sideText);
        sideText.setText(readFromFile("/sdcard/Pictures/prefs"));

        autoConf = (EditText) findViewById(R.id.autoConf);
        autoConf.setText(readFromFile("/sdcard/autoprefs"));
    }

    public void blueClicked(View v) {
        writeToFile("blue");
        sideText.setText(readFromFile("/sdcard/Pictures/prefs"));
    }

    public void redClicked(View v) {
        writeToFile("red");
        sideText.setText(readFromFile("/sdcard/Pictures/prefs"));
    }

    public void updateAuto(View v) {
        writeAutoFile(autoConf.getText().toString());
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

    public void writeAutoFile (String string) {
        try {
            File file = new File("/sdcard", "autoprefs");

            //outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream = new FileOutputStream(file,false);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFromFile (String filePath) {
        // Read from preferences file written by the CustomSettingsActivity to determine what side we are on.
        // Retrieve file.
        File file = new File(filePath);
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