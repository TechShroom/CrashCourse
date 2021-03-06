package crashcourse.k.library.lwjgl;

import java.awt.Frame;
import java.awt.Toolkit;
import java.lang.instrument.IllegalClassFormatException;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import crashcourse.k.exst.mods.Mods;
import crashcourse.k.library.debug.FPS;
import crashcourse.k.library.lwjgl.control.Keys;
import crashcourse.k.library.lwjgl.control.MouseHelp;
import crashcourse.k.library.lwjgl.render.GLPrep;
import crashcourse.k.library.lwjgl.tex.Texture;
import crashcourse.k.library.main.KMain;
import crashcourse.k.library.util.LUtils;
import crashcourse.k.library.util.StackTraceInfo;
import de.matthiasmann.twl.renderer.Renderer;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;

public class DisplayLayer {

	private static String reqTitle = "";
	private static boolean wasResizable;
	private static LWJGLRenderer renderer;

	/**
	 * Initializes the display and KMain instance. Parameter notes are found on
	 * the longest argument version.
	 * 
	 * @param fullscreen
	 *            - is fullscreen on at start?
	 * @param width
	 *            - initial width of screen
	 * @param height
	 *            - initial height of screen
	 * @param title
	 *            - title of screen
	 * @param resizable
	 *            - is the screen resizeable?
	 * @param args
	 *            - main() args
	 * @throws Exception
	 *             any exceptions will be thrown
	 */
	public static void initDisplay(boolean fullscreen, int width, int height,
			String title, boolean resizable, String[] args) throws Exception {
		DisplayLayer.initDisplay(fullscreen, width, height, title, resizable,
				true, args);
	}

	/**
	 * Initializes the display and KMain instance. Parameter notes are found on
	 * the longest argument version.
	 * 
	 * @param fullscreen
	 *            - is fullscreen on at start?
	 * @param width
	 *            - initial width of screen
	 * @param height
	 *            - initial height of screen
	 * @param title
	 *            - title of screen
	 * @param resizable
	 *            - is the screen resizeable?
	 * @param args
	 *            - main() args
	 * @param vsync
	 *            - overrides default vsync option, true
	 * @throws Exception
	 *             any exceptions will be thrown
	 */
	public static void initDisplay(boolean fullscreen, int width, int height,
			String title, boolean resizable, boolean vsync, String[] args)
			throws Exception {
		System.out.println("[CrashCourse] Using LWJGL v" + Sys.getVersion());
		try {
			DisplayLayer.initDisplay(
					fullscreen,
					width,
					height,
					title,
					resizable,
					vsync,
					args,
					Class.forName(
							LUtils.getFirstEntryNotThis(DisplayLayer.class
									.getName())).asSubclass(KMain.class));
			System.out.println("[CrashCourse] Using OpenGL v"
					+ LUtils.getGLVer());
		} catch (ClassCastException cce) {
			if (cce.getStackTrace()[StackTraceInfo.CLIENT_CODE_STACK_INDEX]
					.getClassName().equals(DisplayLayer.class.getName())) {
				throw new IllegalClassFormatException("Class "
						+ Class.forName(StackTraceInfo.getInvokingClassName())
						+ " not implementing KMain!");
			} else {
				throw cce;
			}
		}
	}

	/**
	 * Initializes the display and KMain instance. Parameter notes are found on
	 * the longest argument version.
	 * 
	 * @param fullscreen
	 *            - is fullscreen on at start?
	 * @param width
	 *            - initial width of screen
	 * @param height
	 *            - initial height of screen
	 * @param title
	 *            - title of screen
	 * @param resizable
	 *            - is the screen resizeable?
	 * @param args
	 *            - main() args
	 * @param vsync
	 *            - is vsync enabled?
	 * @param cls
	 *            - overrides the default class for KMain, which is the class
	 *            that called the method
	 * @throws Exception
	 *             any exceptions will be thrown
	 */

	public static void initDisplay(boolean fullscreen, int width, int height,
			String title, boolean resizable, boolean vsync, String[] args,
			Class<? extends KMain> cls) throws Exception {
		KMain main = cls.newInstance();
		DisplayLayer.initDisplay(fullscreen, width, height, title, resizable,
				vsync, args, main);
	}

	public static void initDisplay(boolean fullscreen, int width, int height,
			String title, boolean resizable, boolean vsync, String[] args,
			KMain main) throws Exception {
		DisplayMode dm = LUtils.getDisplayMode(width, height, fullscreen);
		if (!dm.isFullscreenCapable() && fullscreen) {
			System.err
					.println("Warning! Fullscreen is not supported with width "
							+ width + " and height " + height);
			fullscreen = false;
		}
		DisplayLayer.reqTitle = title.toString();
		Display.setDisplayMode(dm);
		if (!fullscreen) {
			Display.setTitle(DisplayLayer.reqTitle);
		}
		Display.create();
		Display.setFullscreen(fullscreen);
		Display.setResizable(resizable && !fullscreen);
		Display.setVSyncEnabled(vsync);
		KMain.setDisplayThread(Thread.currentThread());
		KMain.setInst(main);
		Mods.findAndLoad();
		GLPrep.initOpenGL();
		FPS.init(0);
		FPS.setTitle(DisplayLayer.reqTitle);
		main.init(args);
	}

	public static void loop(int dfps) throws LWJGLException {
		Display.sync(dfps);
		int delta = FPS.update(0);
		if (Display.wasResized()) {
			GLPrep.resizedRefresh();
		}
		GLPrep.update(delta);
		KMain.getInst().onDisplayUpdate(delta);
		MouseHelp.onDisplayUpdate();
		Display.update(false);
		OrgLWJGLOpenGLPackageAccess.updateImplementation();
		Texture.doBindings();
	}

	public static void readDevices() {
		OrgLWJGLOpenGLPackageAccess.pollDevices();
		Keys.read();
		MouseHelp.read();
	}

	public static void intoFull() throws LWJGLException {
		Display.setFullscreen(true);
		DisplayLayer.wasResizable = Display.isResizable();
		Display.setResizable(false);
	}

	public static void outOfFull() throws LWJGLException {
		Display.setResizable(DisplayLayer.wasResizable);
		Display.setFullscreen(false);
	}

	public static void destroy() {
		Display.destroy();
		Frame[] frms = Frame.getFrames();
		for (Frame frm : frms) {
			if (frm.isVisible()) {
				frm.setVisible(false);
				frm.dispose();
				System.err
						.println("CrashCourse has closed a JFrame called "
								+ frm.getTitle()
								+ ", which would have stalled the application's closing state. Please fix this!");
			}
		}
	}

	public static void toggleFull() {
		try {
			if (Display.isFullscreen()) {
				DisplayLayer.outOfFull();
			} else {
				DisplayLayer.intoFull();
			}
		} catch (LWJGLException e) {
		}
	}

	/**
	 * @deprecated Not useful anymore, calls should go to
	 *             {@link DisplayMode#isFullscreenCapable()}
	 * @param width
	 * @param height
	 * @param fullscreen
	 * @return if the aspect ratio matches with the current screen
	 */
	@Deprecated
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
			return DisplayLayer.renderer != null
					? DisplayLayer.renderer
					: (DisplayLayer.renderer = new LWJGLRenderer());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
