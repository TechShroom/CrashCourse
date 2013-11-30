package crashcourse.k.library.lwjgl.render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/** All OpenGL will be handled here */
public class GLPrep {
	static int angle = 0;

	public static void update(int delta) {
		GLPrep.clearAndLoad();
	}

	private static void clearAndLoad() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT
				| GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glLoadIdentity();
	}

	public static void initOpenGL() {
		GLPrep.resizedRefresh();
		GL11.glEnable(GL11.GL_ALPHA_TEST); // allows alpha channels or
											// transperancy
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f); // set alpha aceptance
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_DITHER);
		GL11.glShadeModel(GL11.GL_SMOOTH); // smooth shading
		GL11.glClearDepth(1.0); // Enables Clearing Of The Depth Buffer
		GL11.glDepthMask(true); // turn on depth mask
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_ALWAYS); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D); // enable 2d textures
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
