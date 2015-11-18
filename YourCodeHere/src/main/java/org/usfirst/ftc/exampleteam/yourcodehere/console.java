package org.usfirst.ftc.exampleteam.yourcodehere;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * Created by matt on 11/17/15.
 */
public class console {

	public static void log(String text) {

		Log.d("console.log", text);

	}

	public static void log(int i) {

		Log.d("console.log", String.valueOf(i));

	}

	public static void log(float i) {

		Log.d("console.log", String.valueOf(i));

	}

	public static void log(double i) {

		Log.d("console.log", String.valueOf(i));

	}

	public static void log(String[] text) {

		Log.d("console.log", Arrays.toString(text));

	}

	public static void log(int[] i) {

		Log.d("console.log", Arrays.toString(i));

	}

	public static void log(float[] i) {

		Log.d("console.log", Arrays.toString(i));

	}

	public static void log(double[] i) {

		Log.d("console.log", Arrays.toString(i));

	}
}
