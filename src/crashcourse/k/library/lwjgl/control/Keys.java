package crashcourse.k.library.lwjgl.control;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class Keys {

	public static boolean[][] key = new boolean[Keyboard.KEYBOARD_SIZE][10];

	static {
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(42);
		}
	}

	public static void read() {
		shiftKeys();
		while (Keyboard.next()) {
			int eventKey = Keyboard.getEventKey();
			key[eventKey][0] = Keyboard.getEventKeyState();
		}
	}

	private static void shiftKeys() {
		for (int i = 0; i < key.length; i++) {
			boolean[] array = key[i];
			for (int j = 0; j < array.length - 1; j++) {
				boolean b = array[j];
				array[j + 1] = b;
			}
		}
	}

	public static boolean keyWasReleased(int keyCode) {
		return key[keyCode][1] && !key[keyCode][0];
	}

}
