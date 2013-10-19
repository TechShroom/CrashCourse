package crashcourse.k.library.debug;

import java.awt.Dimension;
import java.nio.ByteBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import crashcourse.k.library.lwjgl.DisplayLayer;
import crashcourse.k.library.lwjgl.control.Keys;
import crashcourse.k.library.main.KMain;
import crashcourse.k.library.util.LUtils;

public class Tests extends KMain {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		DisplayLayer.initDisplay(false, 800, 500, "Testing CrashCourse", true,
				args);
		while (!Display.isCloseRequested()) {
			DisplayLayer.loop(120);
		}
		DisplayLayer.destroy();
		System.exit(0);
	}

	private ByteBuffer split_4_buf = ByteBuffer.allocateDirect(2 * 2 * 4);
	{
		for (int i = 0; i < 2 * 2 * 4; i++) {
			split_4_buf.put((byte) (i));
			System.err.println("put "
					+ split_4_buf.get(split_4_buf.position() - 1));
		}
	}

	@Override
	public void onDisplayUpdate(int delta) {
		if (Keys.keyWasReleased(Keyboard.KEY_ESCAPE)) {
			DisplayLayer.toggleFull();
		}
		DisplayLayer.destroy();
		System.exit(0);
	}

	@Override
	public void init(String[] args) {
		ByteBuffer choppedBuffer = LUtils.chopBuffer(split_4_buf,
				new Dimension(2, 2), 1, 2, 4);
		System.err.println("Recived " + choppedBuffer
				+ " with the following data:");
		for (int i = 0; i < choppedBuffer.capacity(); i++) {
			System.err.println(i + ": " + choppedBuffer.get(i));
		}
	}
}
