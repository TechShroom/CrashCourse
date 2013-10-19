package crashcourse.k.library.lwjgl.render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import crashcourse.k.library.lwjgl.render.RenderManager;

/** All OpenGL will be handled here */
public class GLState {
	static int angle = 0;

	public static void update(int delta) {
		clearAndLoad();
	}

	private static void clearAndLoad() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT
				| GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glLoadIdentity();
	}

	public static void initOpenGL() {
		resizedRefresh();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0); // Enables Clearing Of The Depth Buffer
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		RenderManager.registerRenders();
	}

	public static void resizedRefresh() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity(); // Resets any previous projection matrices
		GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1000, -1000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}

}
