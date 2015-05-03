package com.me.skifun.controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import com.badlogic.gdx.Gdx;

/**
 * Class used to save the game settings.
 */

public class Settings {
	public static boolean soundEnabled = true; // Is sound enabled
	public final static int[] highscores = new int[] {100, 80, 50, 30, 10};
	public static boolean isTutorial=true; // Is tutorial shuold be shown up
	public final static String file = "data/SkiFreeSettings.xml";

	public static void load () {
		/**
		 * Load the settings
		 */
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(Gdx.files.local(file).read()));
			soundEnabled = Boolean.parseBoolean(in.readLine());
			isTutorial=Boolean.parseBoolean(in.readLine());
			for (int i = 0; i < 5; i++) {
				highscores[i] = Integer.parseInt(in.readLine());
			}
		} catch (Throwable e) {
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
			}
		}
	}
	public static void enableSound(boolean bool)
	{
		soundEnabled=bool;
	}
	public static void setTutorial(boolean bool)
	{
		isTutorial=bool;
	}
	public static void save () {
		/**
		 * Save changed settings
		 */
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(Gdx.files.local(file).write(false)));
			out.write(Boolean.toString(soundEnabled));
			out.write('\n');
			out.write(Boolean.toString(isTutorial));
			out.write('\n');
			for (int i = 0; i < 5; i++) {
				out.write(Integer.toString(highscores[i]));
				out.write('\n');

			}

		} catch (Throwable e) {
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
			}
		}
	}
	public static void addScore (int score) {
		/**
		 * Add score to the highscores
		 */
		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
                System.arraycopy(highscores, i, highscores, i + 1, 4 - i);
				highscores[i] = score;
				break;
			}
		}
	}
}
