package org.usfirst.ftc.exampleteam.yourcodehere;

import android.content.Context;

import org.swerverobotics.library.SwerveUtil;
import org.usfirst.ftc.exampleteam.yourcodehere.R;

/**
 * Created by alex on 1/3/16.
 */
public class Music  {

	public static void playElectronica(Context context) {
		SwerveUtil.playSound(context, R.raw.electronica);
	}
}
