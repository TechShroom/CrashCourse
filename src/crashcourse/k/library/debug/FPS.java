package crashcourse.k.library.debug;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class FPS {
	public static final long millis = 1000, micro = 1000 * millis;
	/** time at last frame */
	private static long[] lastFrame = new long[100];
	/** frames per second */
	private static int fps;
	/** last fps time */
	private static long[] lastFPS = new long[100];
	private static String permTitle = "";
	private static boolean enabled;

	public static int update(int index) {
		int del = getDelta(index);
		updateFPS(index);
		return del;
	}

	public static void init(int index) {
		init(index, millis);
	}

	public static void init(int index, long divis) {
		getDelta(index, divis);
		lastFPS[index] = getTime(divis);
		permTitle = Display.getTitle();
	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @param index
	 *            - index of the FPS counter
	 * 
	 * @return milliseconds passed since last frame
	 */
	public static int getDelta(int index) {
		return getDelta(index, millis);
	}

	/**
	 * Calculate how many divisions have passed since last frame.
	 * 
	 * @param index
	 *            - index of the FPS counter
	 * @param divis
	 *            - the division to return
	 * 
	 * @return divisions passed since last frame
	 */
	public static int getDelta(int index, long divis) {
		long time = getTime(divis);
		int delta = (int) (time - lastFrame[index]);
		lastFrame[index] = time;

		return delta;
	}

	public static long getTime(long divis) {
		return (Sys.getTime() * divis) / Sys.getTimerResolution();
	}

	/**
	 * Get the accurate system time
	 * 
	 * @param index
	 * 
	 * @return The system time in milliseconds
	 */
	public static long getTime() {
		return getTime(millis);
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	private static void updateFPS(int index) {
		if (getTime() - lastFPS[index] > 1000) {
			if (enabled) {
				Display.setTitle(permTitle + " FPS: " + fps);
			} else {
				Display.setTitle(permTitle);
			}
			fps = 0;
			lastFPS[index] += 1000;
		}
		fps++;
	}

	public static void setTitle(String reqTitle) {
		permTitle = reqTitle;
	}

	public static void enable() {
		enabled = true;
	}

	public static void disable() {
		enabled = false;
	}

}
