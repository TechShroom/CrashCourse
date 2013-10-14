package crashcourse.k.library.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import crashcourse.k.library.lwjgl.DisplayLayer;

public class LUtils {
	public static File TOP_LEVEL = null;
	static {
		try {
			TOP_LEVEL = new File(LUtils.class.getResource("LUtils.class")
					.toURI().getPath()).getParentFile().getParentFile()
					.getParentFile().getParentFile().getParentFile()
					.getParentFile().getParentFile().getAbsoluteFile();
			TOP_LEVEL.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean getArgB(String[] args, int index, boolean def) {
		return Boolean.valueOf(getArgS(args, index, Boolean.valueOf(def)
				.toString()));
	}

	public static int getArgI(String[] args, int index, int def) {
		return Integer.valueOf(getArgS(args, index, Integer.valueOf(def)
				.toString()));
	}

	public static float getArgF(String[] args, int index, float def) {
		return Float
				.valueOf(getArgS(args, index, Float.valueOf(def).toString()));
	}

	public static double getArgD(String[] args, int index, double def) {
		return Double.valueOf(getArgS(args, index, Double.valueOf(def)
				.toString()));
	}

	public static String getArgS(String[] args, int index, String def) {
		return args.length <= index ? def : args[index] == null ? def
				: args[index];
	}

	public static <T> T getArg(T[] src, int index, T def) {
		return src.length <= index ? def : src[index] == null ? def
				: src[index];
	}

	public static boolean isVersionAvaliable(String vers) {
		String cver = GL11.glGetString(GL11.GL_VERSION);
		if (cver.indexOf(' ') > -1) {
			cver = cver.substring(0, cver.indexOf(' '));
		}
		System.out.println("Comparing " + cver + " to " + vers);
		String[] cver_sep = cver.split("\\.", 3);
		String[] vers_sep = vers.split("\\.", 3);
		int[] cver_sepi = new int[3];
		int[] vers_sepi = new int[3];
		int min = minAll(cver_sep.length, vers_sep.length, 3);
		for (int i = 0; i < min; i++) {
			cver_sepi[i] = Integer.parseInt(cver_sep[i]);
			vers_sepi[i] = Integer.parseInt(vers_sep[i]);
		}
		boolean ret = cver_sepi[0] >= vers_sepi[0]
				&& cver_sepi[1] >= vers_sepi[1] && cver_sepi[2] >= vers_sepi[2];
		System.out.println("Returning " + ret);
		return ret;
	}

	public static int minAll(int... ints) {
		int min = Integer.MAX_VALUE;
		for (int i : ints) {
			// System.out.println("Comparing " + i + " and " + min);
			min = Math.min(min, i);
		}
		// System.out.println("Result is " + min);
		return min;
	}

	public static void checkAccessor(String[] accepts, String className)
			throws Exception {
		boolean oneDidntThrow = false;
		for (int i = 0; i < accepts.length; i++) {
			String s = accepts[i];
			try {
				checkAccessor(s, className);
				oneDidntThrow = true;
			} catch (Exception e) {
				if (e instanceof IllegalArgumentException) {
					accepts[i] += " --(DEBUG: This threw a IAE)--";
				}
				continue;
			}
		}
		if (oneDidntThrow) {
			return;
		}
		throw new IllegalAccessException("Access denied to " + className
				+ " because it wasn't in the following list: "
				+ ArrayHelper.dump0(accepts));
	}

	public static void checkAccessor(String accept, String className)
			throws Exception {
		int star = accept.indexOf('*'); // Star in package name
		if (star > -1 && accept.length() == 1) {
			// If any package is accepted, it's okay.
			return;
		}
		Class.forName(className); // make sure this is a REAL class
		if (star > -1) {
			// Any packages within the specified package
			if (accept.charAt(star - 1) != '.') {
				// Weird (invalid) package ex. com.package*.malformed
				throw new IllegalArgumentException("Package malformed");
			}
			String sub = accept.substring(0, star - 1);
			if (className.startsWith(sub)) {
				return;
			}
			throw new IllegalAccessException("Access denied to " + className
					+ " because it wasn't in " + accept);
		}
	}

	public static BufferedImage scaledBufferedImage(BufferedImage image,
			int sw, int sh) {
		System.err.println("Requested x and y of image is " + sw + " " + sh);
		System.err.println("Actual x and y is " + image.getWidth() + " "
				+ image.getHeight());
		int type = 0;
		type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image
				.getType();
		BufferedImage resizedImage = new BufferedImage(sw, sh, type);
		Graphics2D g = resizedImage.createGraphics();
		boolean isDone = g.drawImage(image, 0, 0, sw, sh, null);
		if (!isDone) {
			System.err.println("Scaler not done?!");
		}
		g.dispose();
		System.err.println("Resultant x and y of image is "
				+ resizedImage.getWidth() + " " + resizedImage.getHeight());
		return resizedImage;
	}

	public static DisplayMode getDisplayMode(int width, int height,
			boolean fullscreen) {
		try {
			for (DisplayMode m : Display.getAvailableDisplayModes()) {
				if ((m.isFullscreenCapable() || !fullscreen)) {
					if (m.getWidth() == width) {
						if (m.getHeight() == height) {
							if (DisplayLayer.compatibleWithFullscreen(width,
									height, fullscreen)) {
								return m;
							} else {
								System.err
										.println(String
												.format("A non-aspect-compat mode"
														+ " is being used:"
														+ " Width: %s Height: %s"
														+ " Fullscreen: %s."
														+ " Fullscreen may not work as expected!",
														width, height,
														fullscreen));
							}
						}
					}
				}
				if (DisplayLayer.compatibleWithFullscreen(m.getWidth(),
						m.getHeight(), m.isFullscreenCapable())) {
					System.err.println(String.format("A non-args-compat mode"
							+ " is avaliable:" + " Width: %s Height: %s"
							+ " Fullscreen: %s", m.getWidth(), m.getHeight(),
							m.isFullscreenCapable()));
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return new DisplayMode(width, height);
	}

	public static Dimension[] getAspectCompatDimensions() {
		try {
			ArrayList<Dimension> ret = new ArrayList<Dimension>();
			for (DisplayMode m : Display.getAvailableDisplayModes()) {
				if (DisplayLayer.compatibleWithFullscreen(m.getWidth(),
						m.getHeight(), m.isFullscreenCapable())) {
					ret.add(new Dimension(m.getWidth(), m.getHeight()));
				}
			}
			return ret.toArray(new Dimension[ret.size()]);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return new Dimension[0];
	}

	public static Dimension[] getDimensions() {
		try {
			ArrayList<Dimension> ret = new ArrayList<Dimension>();
			for (DisplayMode m : Display.getAvailableDisplayModes()) {
				ret.add(new Dimension(m.getWidth(), m.getHeight()));
			}
			return ret.toArray(new Dimension[ret.size()]);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return new Dimension[0];
	}

	public static String[] getAspectCompatDimensionsSimple() {
		return getDimensionsSimple(getAspectCompatDimensions());
	}

	public static String[] getDimensionsSimple(Dimension[] compat) {
		Dimension[] cmpt = compat;
		String[] s = new String[cmpt.length];
		for (int i = 0; i < cmpt.length; i++) {
			Dimension d = cmpt[i];
			s[i] = String.format("%s x %s", d.width, d.height);
		}
		return s;
	}

	public static Dimension getDimensionFromUser() {
		return getDimensionFromUser(getAspectCompatDimensions());
	}

	public static Dimension getDimensionFromUser(
			Dimension[] availableDisplayModes) {
		Dimension[] compat = availableDisplayModes;
		String[] compat_s = LUtils.getDimensionsSimple(compat);
		String ret_s = (String) JOptionPane.showInputDialog(new JFrame(),
				"Avaliable sizes:", "Choose a window size",
				JOptionPane.DEFAULT_OPTION, null, compat_s, compat_s[0]);
		if (ret_s == null) {
			return null;
		}
		return compat[Arrays.asList(compat_s).indexOf(ret_s)];
	}

	public static List<String> getInfoAsString(Info[] info) {
		List<String> out = new ArrayList<String>();
		for (Info i : info) {
			out.add(i + "" + i.getClass().getName());
		}
		return out;
	}

	public static Dimension getDimensionFromUserAndArgs(String[] normalized) {
		return getDimensionFromUserAndArgs(getAspectCompatDimensions(),
				normalized);
	}

	public static Dimension getDimensionFromUserAndArgs(Dimension[] dimensions,
			String[] normalized) {
		if (normalized.length >= 4) {
			System.out.println("This is the args sector");
			List<String> strs = Arrays.asList(normalized);
			System.err.println(strs);
			if (strs.indexOf("-width") == -1 || strs.indexOf("-height") == -1) {
			} else {
				String w = strs.get(strs.indexOf("-width") + 1);
				String h = strs.get(strs.indexOf("-height") + 1);
				if (isInt(w) && isInt(h)) {
					return new Dimension(Integer.parseInt(w),
							Integer.parseInt(h));
				}
			}
		}
		Dimension get = getDimensionFromUser(dimensions);
		if (get == null) {
			System.out.println("This is the args length " + normalized.length);
			get = new Dimension(600, 600);
		}

		return get;
	}

	public static boolean isInt(String test) {
		try {
			Integer.parseInt(test);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getGLVer() {
		return GL11.glGetString(GL11.GL_VERSION);
	}

	/**
	 * Chops a buffer to the specified width, height, and possibly an extra
	 * dimension.
	 * 
	 * @param buf
	 *            - the buffer to shrink
	 * @param oldWidthHeight
	 *            - the old width and height
	 * @param choppedWidth
	 *            - the new (smaller) width
	 * @param choppedHeight
	 *            - the new (smaller) height
	 * @param extraDimensions
	 *            - any extra dimensions, like bytes of color that should be
	 *            kept together. Should be 1 if there is none.
	 * @return the chopped buffer
	 */
	public static ByteBuffer chopBuffer(ByteBuffer buf,
			Dimension oldWidthHeight, int choppedWidth, int choppedHeight,
			int extraDimensions) {
		if (buf.capacity() < choppedWidth * choppedHeight * extraDimensions
				|| oldWidthHeight.height < choppedHeight
				|| oldWidthHeight.width < choppedWidth) {
			throw new IndexOutOfBoundsException(
					"One of the dimensions is breaking the capacity of the buffer!");
		}
		if (buf.capacity() > buf.remaining()) {
			buf.rewind();
		}
		ByteBuffer newBuf = ByteBuffer.allocateDirect(choppedWidth
				* choppedHeight * extraDimensions);
		for (int x = 0; x < oldWidthHeight.width; x++) {
			for (int y = 0; y < oldWidthHeight.height; y++) {
				for (int pix = 0; pix < 4; pix++) {
				}
			}
		}
		return null;
	}
}
