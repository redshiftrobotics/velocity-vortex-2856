import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by alex on 1/3/16.
 */
public class Music  {
	private static MediaPlayer sound;

	public static void playMusic(Context context) {
		sound = MediaPlayer.create(context, );
	}
}
