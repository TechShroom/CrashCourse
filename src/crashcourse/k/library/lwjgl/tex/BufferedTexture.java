package crashcourse.k.library.lwjgl.tex;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BufferedTexture extends Texture {
	BufferedImage img = null;

	public BufferedTexture(BufferedImage image) {
		img = image;
		dim = new Dimension(img.getWidth(), img.getHeight());
		super.init();
	}

	@Override
	public void setup() {
		buf = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);
		for (int i = 0; i < buf.capacity() / 4; i++) {
			try {
				int argb = img.getRGB(i % img.getWidth(), i / img.getHeight());
				System.err.println("recived " + argb);
				byte alpha = (byte) (argb & 0xFF);
				byte red = (byte) (argb << 8 & 0xFF);
				byte green = (byte) (argb << 16 & 0xFF);
				byte blue = (byte) (argb << 24 & 0xFF);
				byte[] split = new byte[] { red, green, blue, alpha };
				System.err.println("split to  " + red + ":" + green + ":"
						+ blue + ":" + alpha);
				buf.put(split);
			} catch (Exception e) {
				System.err.println("Failed on " + i + " or (x,y) (" + i
						% img.getWidth() + ", " + i / img.getHeight() + ")");
			}
		}
		buf.flip();
	}

	@Override
	public boolean isLookAlike(Texture t) {
		if (t instanceof BufferedTexture) {
			BufferedTexture other = (BufferedTexture) t;
			if (other.dim.equals(dim)) {
				int[] c_one = new int[img.getWidth() * img.getHeight()];
				int[] c_two = new int[c_one.length];
				img.getData().getPixels(0, 0, dim.width, dim.height, c_one);
				other.img.getData().getPixels(0, 0, dim.width, dim.height,
						c_two);
				return Arrays.equals(c_one, c_two);
			}
		}
		return false;
	}
}
