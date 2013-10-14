package crashcourse.k.library.lwjgl.control;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;

import crashcourse.k.library.lwjgl.Shapes;
import crashcourse.k.library.lwjgl.tex.BufferedTexture;
import crashcourse.k.library.lwjgl.tex.Texture;
import crashcourse.k.library.util.LUtils;

public class MouseHelp {
	private static class FakeCursor {
		Texture display = null;
		int hx, hy;

		public FakeCursor(Texture image, int hotx, int hoty) {
			display = image;
			hx = hotx;
			hy = hoty;
		}

		public void drawAt(int x, int y) {
			Shapes.glQuad(x - hx, y - hy, 0, display.dim.width,
					display.dim.height, 0, Shapes.XYF, display);
		}

	}

	public static final int LMB = 0, RMB = 1;

	private static MouseHelp inst = new MouseHelp();
	private int dx = 0;
	private int dy = 0;
	private int x = 0;
	private int y = 0;
	private FakeCursor fake;
	public static int buttons;
	private boolean[] thisFrameClick = new boolean[buttons];
	private boolean[] lastFrameClick = new boolean[buttons];
	private static Cursor nativec = null;

	private MouseHelp() {
		try {
			Mouse.create();
			nativec = Mouse.getNativeCursor();
			Mouse.setClipMouseCoordinatesToWindow(false);
			inst = this;
			buttons = Mouse.getButtonCount();
			thisFrameClick = new boolean[buttons];
			lastFrameClick = new boolean[buttons];
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void read() {
		inst.dx = Mouse.getDX();
		inst.dy = Mouse.getDY();
		inst.x = Mouse.getX();
		inst.y = Mouse.getY();
		for (int i = 0; i < buttons; i++) {
			inst.lastFrameClick[i] = inst.thisFrameClick[i];
			inst.thisFrameClick[i] = Mouse.isButtonDown(i);
		}
	}

	private void onValueChanges() {
		if (fake != null) {
			fake.drawAt(x, y);
		}
	}

	public static void replaceCursor(BufferedImage image, int hotx, int hoty) {
		int length = image.getWidth() * image.getHeight();
		IntBuffer imgbuf = BufferUtils.createIntBuffer(length);
		int ih = image.getHeight();
		int iw = image.getWidth();
		for (int i = 0; i < length; i++) {
			int x = i % iw;
			int y = i / ih;
			int rev_y = image.getHeight() - y - 1;
			imgbuf.put(image.getRGB(x, rev_y));
		}
		imgbuf.rewind();
		try {
			Cursor c = new Cursor(image.getWidth(), image.getHeight(), hotx,
					hoty, 1, imgbuf, null);
			Mouse.setNativeCursor(c);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.err.println("Mouse cursor couldn't be created/implemented!");
		}
	}

	public static void replaceCursor(Texture texture, int hotx, int hoty) {
		replaceCursor(texture.toBufferedImage(), hotx, hoty);
	}

	/**
	 * createFollowCursor is used when you want to hide the mouse cursor while
	 * in your window, and use your own that will always stay inside the window,
	 * active.
	 * 
	 * @param image
	 *            - a {@link java.awt.image.BufferedImage} for the mouse
	 * @param hotx
	 *            - the hotspotX of the mouse (where the user will click
	 *            relative to the top left corner)
	 * @param hoty
	 *            - the hotspotY of the mouse (where the user will click
	 *            relative to the top left corner)
	 */
	public static void createFollowCursor(BufferedImage image, int hotx,
			int hoty) {
		createFollowCursor(new BufferedTexture(image), hotx, hoty);
	}

	/**
	 * createFollowCursor is used when you want to hide the mouse cursor while
	 * in your window, and use your own that will always stay inside the window,
	 * active.
	 * 
	 * @param texture
	 *            - a {@link crashcourse.k.library.lwjgl.tex.Texture Texture}
	 *            for the mouse
	 * @param hotx
	 *            - the hotspotX of the mouse (where the user will click
	 *            relative to the top left corner)
	 * @param hoty
	 *            - the hotspotY of the mouse (where the user will click
	 *            relative to the top left corner)
	 * @see {@link MouseHelp#createFollowCursor(BufferedImage, int, int)
	 *      createFollowCursor(BufferedImage, int, int)}
	 */
	public static void createFollowCursor(Texture texture, int hotx, int hoty) {
		/*
		 * Hide mouse cursor in our window (prevents it from derping like
		 * Synthesia)
		 */
		replaceCursor(Texture.invisible, 0, 0);
		inst.fake = new FakeCursor(texture, hotx, hoty);
	}

	/**
	 * createFollowCursor is used when you want to hide the mouse cursor while
	 * in your window, and use your own that will always stay inside the window,
	 * active.
	 * 
	 * @param texture
	 *            - a {@link crashcourse.k.library.lwjgl.tex.Texture Texture}
	 *            for the mouse
	 * @param width
	 *            - the wanted width of the cursor
	 * 
	 * @param height
	 *            - the wanted height of the cursor
	 * 
	 * @param hotx
	 *            - the hotspotX of the mouse (where the user will click
	 *            relative to the top left corner)
	 * @param hoty
	 *            - the hotspotY of the mouse (where the user will click
	 *            relative to the top left corner)
	 * @see {@link MouseHelp#createFollowCursor(BufferedImage, int, int)
	 *      createFollowCursor(BufferedImage, int, int)}
	 */
	public static void createFollowCursor(Texture texture, int width,
			int height, int hotx, int hoty) {
		/*
		 * Hide mouse cursor in our window (prevents it from derping like
		 * Synthesia)
		 */
		replaceCursor(Texture.invisible, 0, 0);
		texture = new BufferedTexture(LUtils.scaledBufferedImage(
				texture.toBufferedImage(), width, height));
		inst.fake = new FakeCursor(texture, hotx, hoty);
	}

	public static void resetCursor() {
		try {
			Mouse.setNativeCursor(nativec);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static int getDX() {
		return inst.dx;
	}

	public static int getDY() {
		return inst.dy;
	}

	public static int getX() {
		return inst.x;
	}

	public static int getY() {
		return inst.y;
	}

	public static boolean isButtonDown(int buttonID) {
		return inst.thisFrameClick[buttonID];
	}

	public static boolean wasButtonClicked(int buttonID) {
		return inst.lastFrameClick[buttonID] && !isButtonDown(buttonID);
	}

	public static void onDisplayUpdate() {
		inst.onValueChanges();
	}

	public static boolean clickedInRect(Rectangle check, int buttonToCheck) {
		return check.contains(getX(), getY())
				&& wasButtonClicked(buttonToCheck);
	}
}
