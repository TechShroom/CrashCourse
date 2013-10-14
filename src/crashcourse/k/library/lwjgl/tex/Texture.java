package crashcourse.k.library.lwjgl.tex;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import crashcourse.k.library.debug.Memory;
import crashcourse.k.library.exceptions.lwjgl.TextureBindException0;

public abstract class Texture {
	public static class DestTexture extends Texture {

		public DestTexture(Texture texture) {
			buf = ByteBuffer.allocateDirect(1);
			dim = new Dimension(1, 1);
			setConstructingOverrideId(texture.id);
			init();
			texlist.remove(getID());
			currentSpace -= buf.capacity();
			removedIDs.add(this.getID());
		}

		@Override
		public void setup() {
			// TODO Auto-generated method stub

		}

	}

	private int id = -1;
	private static int useID = -1;
	static {
		System.gc();
	}
	public static final long TOTAL_TEXTURE_SPACE = Runtime.getRuntime()
			.maxMemory() / 2;
	// private static IntBuffer ids = BufferUtils.createIntBuffer(1);
	private static HashMap<Integer, Texture> texlist = new HashMap<Integer, Texture>();
	private static ArrayList<Integer> removedIDs = new ArrayList<Integer>();
	public static long currentSpace = 0;
	protected static boolean mipmapsEnabled = true;
	public ByteBuffer buf = null;
	public Dimension dim = null;
	static {
		System.gc();
		Memory.printAll();
		Field f;
		try {
			f = ContextCapabilities.class.getDeclaredField("glGenerateMipmap");
			f.setAccessible(true);
			mipmapsEnabled = f.getLong(GLContext.getCapabilities()) != 0;
		} catch (Exception e) {
			mipmapsEnabled = false;
		}
		System.err.println("3.0 mipmaps? " + mipmapsEnabled);
	}

	// Define static Textures AFTER this comment

	public static final Texture invisible = new ColorTexture(new Color(0, 0, 0,
			0), new Dimension(1, 1));

	public Texture() {
	}

	private void bind0() {
		if ((buf == null || dim == null)) {
			throw new TextureBindException0(
					"A required variable is null when creating textures!");
		} else if (buf.capacity() < dim.height * dim.width * 4) {
			ByteBuffer tmp = ByteBuffer.allocateDirect(dim.height * dim.width
					* 4);
			tmp.put(buf);
			buf = tmp;
			buf.rewind();
		}
		buf.rewind();
		Texture lookAlike;
		if ((lookAlike = similar(this)) != null) {
			this.id = lookAlike.id;
			System.out.println("Overrode id: " + id);
			texlist.put(lookAlike.id, this);
		} else {
			boolean override = false;
			currentSpace += buf.capacity();
			if (removedIDs.size() > 0 && useID == -1) {
				id = removedIDs.get(0);
				removedIDs.remove(0);
			}
			if (useID > -1) {
				System.out.println("Force-overrode id: " + id);
				override = true;
				id = useID;
				currentSpace -= texlist.get(id).buf.capacity();
			}
			if ((currentSpace < TOTAL_TEXTURE_SPACE) && id == -1 && useID == -1) {
				id = GL11.glGenTextures();
			} else {
				System.out.println("WARNING! Texture limit reached, "
						+ "not adding new textures!");
				return;
			}
			// Create a new texture object in memory and bind it
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

			// All RGB bytes are aligned to each other and each component is 1
			// byte
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			if (!mipmapsEnabled) {
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
						GL_LINEAR_MIPMAP_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL_TRUE);
			}
			// Upload the texture data and generate mip maps (for scaling)
			// ByteBuffer tmp = buf.duplicate();
			// buf.rewind();
			if (override) {
				GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, dim.width,
						dim.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
			} else {
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
						dim.width, dim.height, 0, GL11.GL_RGBA,
						GL11.GL_UNSIGNED_BYTE, buf);
			}
			// buf = tmp;
			if (mipmapsEnabled) {
				GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			}
			texlist.put(id, this);
		}
	}

	private static Texture similar(Texture texture) {
		Texture t1 = null;
		for (Texture t : texlist.values()) {
			if (t.isLookAlike(texture)) {
				t1 = t;
				break;
			}
		}
		return t1;
	}

	public boolean isLookAlike(Texture t) {
		return t.buf.equals(buf) && t.dim.equals(dim);
	}

	public boolean equals(Object o) {
		return (o instanceof Texture) && isLookAlike((Texture) o);
	}

	public abstract void setup();

	public int getID() {
		return id;
	}

	public void bind() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getID());
	}

	public void glTextureVertex(float s, float t) {
		GL11.glTexCoord2f(s, t);
	}

	protected void init() {
		setup();
		bind0();
	}

	protected void setConstructingOverrideId(int id) {
		useID = id;
	}

	public BufferedImage toBufferedImage() {
		int width = dim.width;
		int height = dim.height;
		int[] packedPixels = new int[width * height * 4];
		buf.rewind();
		// System.out.println("id " + id + " buf capacity " + buf.capacity());
		int bufferInd = 0;
		for (int row = height - 1; row >= 0; row--) {
			for (int col = 0; col < width; col++) {
				int R, G, B, A;
				R = buf.get(bufferInd++);
				G = buf.get(bufferInd++);
				B = buf.get(bufferInd++);
				A = buf.get(bufferInd++) & 0xff;
				int index = (row * width + col);
				// System.out.println("recv " + R + "-" + G + "-" + B + "-" +
				// A);
				packedPixels[index] = A + (R << 8) + (G << 16) + (B << 24);
			}
		}
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		int bitMask[] = new int[] { 0xff0000, 0xff00, 0xff, 0xff000000 };
		SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(
				DataBuffer.TYPE_INT, width, height, bitMask);
		WritableRaster wr = Raster.createWritableRaster(sampleModel, null);
		wr.setPixels(0, 0, width, height, packedPixels);
		img.setData(wr);
		return img;
	}

	public void kill() {
		new DestTexture(this);
		System.gc();
	}

}
