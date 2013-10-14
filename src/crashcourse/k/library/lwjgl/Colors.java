package crashcourse.k.library.lwjgl;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

public class Colors {

	public static void glColor4(Color from) {
		GL11.glColor4d(from.getRed() / 255.0, from.getGreen() / 255.0,
				from.getBlue() / 255.0, from.getAlpha() / 255.0);
	}

	public static void glColor3(Color from) {
		GL11.glColor3d(from.getRed() / 255.0, from.getGreen() / 255.0,
				from.getBlue() / 255.0);
	}

	/**
	 * Work in progress...
	 */
	public static Color glGetColor() {
		FloatBuffer color = ByteBuffer.allocateDirect(64).asFloatBuffer();
		GL11.glGetFloat(GL11.GL_CURRENT_COLOR, color);
		float[] c = new float[color.capacity()];
		color.get(c);
		int in = 0;
		for (float f : c) {
			System.out.println((Float.compare(f, 0)) + "@" + in++);
		}
		return Color.WHITE;
	}
}
