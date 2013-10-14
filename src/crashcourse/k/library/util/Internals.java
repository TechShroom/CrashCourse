package crashcourse.k.library.util;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Internals {
	static GraphicsEnvironment ge = GraphicsEnvironment
			.getLocalGraphicsEnvironment();
	static GraphicsDevice gd = ge.getDefaultScreenDevice();

	private static Runtime r = Runtime.getRuntime();

	public static void exec(String string) {
		try {
			r.exec(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void say(String text) {
		exec("say " + text);
	}

	private static String[] mentions = { "You have no need to worry!",
			"Everything will be alright.", "This statement will not hurt you.",
			"Have fun!" };

	public static void mention(String text) {
		exec("say " + text + ". "
				+ ArrayHelper.repeatRandomArray(mentions, 6)[0]);
	}

	public static void fullscreen(JFrame win) {
		if (win == null) {
			gd.setFullScreenWindow(null);
			return;
		}
		if (gd.isFullScreenSupported()) {
			win.setVisible(false);
			win.setUndecorated(true);
			gd.setFullScreenWindow(win);
		} else {
			win.setSize(10000, 10000);
		}
	}

	public static void disable() {
		gd.setFullScreenWindow(null);
	}

}
