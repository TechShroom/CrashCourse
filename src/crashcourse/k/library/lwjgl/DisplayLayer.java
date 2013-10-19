package crashcourse.k.library.lwjgl;

import java.awt.Canvas;
import java.awt.Toolkit;
import java.lang.instrument.IllegalClassFormatException;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import crashcourse.k.exst.mods.Mods;
import crashcourse.k.library.debug.FPS;
import crashcourse.k.library.lwjgl.control.Keys;
import crashcourse.k.library.lwjgl.control.MouseHelp;
import crashcourse.k.library.lwjgl.render.GLState;
import crashcourse.k.library.main.KMain;
import crashcourse.k.library.util.LUtils;
import crashcourse.k.library.util.StackTraceInfo;
import de.matthiasmann.twl.renderer.Renderer;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;

public class DisplayLayer {

	private static String reqTitle = "";
	private static Canvas theCanvas = new Canvas();
	private static JFrame screen;
	private static boolean wasResizable;
	private static LWJGLRenderer renderer;

	public static void initDisplay(boolean fullscreen, int width, int height,
			String title, boolean resizable, String[] args) throws Exception {
		System.out.println("[CrashCourse] Using LWJGL v" + Sys.getVersion());
		try {
			initDisplay(fullscreen, width, height, title, resizable, args,
					Class.forName(StackTraceInfo.getInvokingClassName())
							.asSubclass(KMain.class));
			System.out.println("[CrashCourse] Using OpenGL v"
					+ LUtils.getGLVer());
		} catch (ClassCastException cce) {
			if (cce.getStackTrace()[StackTraceInfo.CLIENT_CODE_STACK_INDEX]
					.getClassName().equals(DisplayLayer.class.getName())) {
				cce.printStackTrace();
				throw new IllegalClassFormatException("Class "
						+ Class.forName(StackTraceInfo.getInvokingClassName())
						+ " not implementing KMain!");
			} else {
				throw cce;
			}
		}
	}

	public static void initDisplay(boolean fullscreen, int width, int height,
			String title, boolean resizable, String[] args, KMain main)
			throws Exception {
		DisplayMode dm = LUtils.getDisplayMode(width, height, fullscreen);
		if (!dm.isFullscreenCapable() && fullscreen) {
			System.err
					.println("Warning! Fullscreen is not supported with width "
							+ width + " and height " + height);
			fullscreen = false;
		}
		reqTitle = title.toString();
		Display.setDisplayMode(dm);
		screen = new JFrame();
		theCanvas.setSize(width, height);
		screen.add(theCanvas);
		screen.pack();
		screen.setResizable(resizable && !fullscreen);
		screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// screen.setVisible(true);
		// Display.setParent(theCanvas);
		if (!fullscreen) {
			screen.setTitle(reqTitle);
			Display.setTitle(reqTitle);
		}
		Display.create();
		Display.setFullscreen(fullscreen);
		Display.setResizable(resizable && !fullscreen);
		KMain.setInst(main);
		Mods.findAndLoad();
		GLState.initOpenGL();
		FPS.init(0);
		FPS.setTitle(reqTitle);
		main.init(args);
	}

	public static void initDisplay(boolean fullscreen, int width, int height,
			String title, boolean resizable, String[] args,
			Class<? extends KMain> cls) throws Exception {
		KMain main = cls.newInstance();
		DisplayMode dm = LUtils.getDisplayMode(width, height, fullscreen);
		if (!dm.isFullscreenCapable() && fullscreen) {
			System.err
					.println("Warning! Fullscreen is not supported with width "
							+ width + " and height " + height);
			fullscreen = false;
		}
		reqTitle = title.toString();
		Display.setDisplayMode(dm);
		screen = new JFrame();
		theCanvas.setSize(width, height);
		screen.add(theCanvas);
		screen.pack();
		screen.setResizable(resizable && !fullscreen);
		screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// screen.setVisible(true);
		// Display.setParent(theCanvas);
		if (!fullscreen) {
			screen.setTitle(reqTitle);
			Display.setTitle(reqTitle);
		}
		Display.create();
		Display.setFullscreen(fullscreen);
		Display.setResizable(resizable && !fullscreen);
		GLState.initOpenGL();
		FPS.init(0);
		FPS.setTitle(reqTitle);
		main.init(args);
		KMain.setInst(main);
	}

	public static void loop(int dfps) throws LWJGLException {
		Display.sync(dfps);
		Keys.read();
		MouseHelp.read();
		int delta = FPS.update(0);
		if (Display.wasResized()) {
			GLState.resizedRefresh();
		}
		GLState.update(delta);
		MouseHelp.onDisplayUpdate();
		KMain.getInst().onDisplayUpdate(delta);
		Display.update();
	}

	public static void intoFull() throws LWJGLException {
		Display.setFullscreen(true);
		wasResizable = Display.isResizable();
		Display.setResizable(false);
	}

	public static void outOfFull() throws LWJGLException {
		Display.setResizable(wasResizable);
		Display.setFullscreen(false);
	}

	public static void destroy() {
		Display.destroy();
		screen.dispose();
	}

	public static void toggleFull() {
		try {
			if (Display.isFullscreen()) {
				outOfFull();
			} else {
				intoFull();
			}
		} catch (LWJGLException e) {
		}
	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @param fullscreen
	 * @return if the aspect ratio matches with the current screen
	 */
	public static boolean compatibleWithFullscreen(int width, int height,
			boolean fullscreen) {
		int dw, dh;
		dw = Toolkit.getDefaultToolkit().getScreenSize().width;
		dh = Toolkit.getDefaultToolkit().getScreenSize().height;
		float aspect_requested = (float) width / (float) height;
		float aspect_required = (float) dw / (float) dh;
		return aspect_requested == aspect_required;
	}

	public static Renderer getLWJGLRenderer() {
		try {
			return renderer != null ? renderer
					: (renderer = new LWJGLRenderer());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
