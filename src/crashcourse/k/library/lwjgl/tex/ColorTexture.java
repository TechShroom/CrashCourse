package crashcourse.k.library.lwjgl.tex;

import java.awt.Dimension;
import java.nio.ByteBuffer;

import org.lwjgl.util.Color;

public class ColorTexture extends Texture {
	private Color c = null;

	public ColorTexture(java.awt.Color from, Dimension size) {
		c = new Color(from.getRed(), from.getGreen(), from.getBlue(),
				from.getAlpha());
		// System.out.println(String.format("color is %s %s %s %s",
		// from.getRed(),
		// from.getGreen(), from.getBlue(), from.getAlpha()));
		// System.out.println(String.format("Lcolor is %s %s %s %s", c.getRed(),
		// c.getGreen(), c.getBlue(), c.getAlpha()));
		dim = new Dimension(size);
		super.init();
	}

	public ColorTexture(java.awt.Color from) {
		this(from, new Dimension(2, 2));
	}

	@Override
	public void setup() {
		System.err.println("The RGBA values from c are these "
				+ String.format("%s %s %s %s", c.getRedByte() & 0xFF,
						c.getGreenByte() & 0xFF, c.getBlueByte() & 0xFF,
						c.getAlphaByte() & 0xFF));
		buf = ByteBuffer.allocateDirect(4 * dim.width * dim.height);
		for (int i = 0; i < buf.capacity(); i += 4) {
			c.writeRGB(buf);
			buf.put((byte) (c.getAlphaByte() & 0xFF));
		}
	}

	@Override
	public boolean isLookAlike(Texture t) {
		if (t instanceof ColorTexture) {
			return c.equals(((ColorTexture) t).c) && dim.equals(t.dim);
		} else {
			return super.isLookAlike(t);
		}
	}

	public java.awt.Color getRawColor() {
		return new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue(),
				c.getAlpha());
	}
}
