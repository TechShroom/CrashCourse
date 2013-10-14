package crashcourse.k.library.lwjgl.tex;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.opengl.PNGDecoder.Format;

public class FileTexture extends Texture {
	private File tex = null;

	public FileTexture(String parentDir, String name) {
		if (parentDir == null) {
			parentDir = System.getProperty("user.home", "/");
		}
		if(name == null) {
			System.err.println("Creating a null file!");
			return;
		}
		tex = new File(parentDir, name);
		super.init();
	}

	@Override
	public void setup() {
		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream(tex);
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);

			// Get the width and height of the texture
			dim = new Dimension(decoder.getWidth(), decoder.getHeight());

			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(4 * decoder.getWidth()
					* decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			buf.flip();

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public boolean isLookAlike(Texture t) {
		if (t instanceof FileTexture) {
			return tex.equals(((FileTexture) t).tex);
		} else {
			return super.isLookAlike(t);
		}
	}

}
