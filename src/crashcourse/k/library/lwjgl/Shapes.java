package crashcourse.k.library.lwjgl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import crashcourse.k.library.lwjgl.tex.ColorTexture;
import crashcourse.k.library.lwjgl.tex.Texture;
import crashcourse.k.library.util.ArrayHelper;
import crashcourse.k.library.util.LUtils;

public class Shapes {
	private static final Texture texRed = new ColorTexture(Color.RED);
	private static final Texture texBlue = new ColorTexture(Color.BLUE);
	private static final Texture texGreen = new ColorTexture(Color.GREEN);
	private static final Texture texYellow = new ColorTexture(Color.YELLOW);
	private static final Texture texWhite = new ColorTexture(Color.WHITE);
	private static final Texture texPurple = new ColorTexture(Color.MAGENTA);

	public static final int XYF = 0x01;
	public static final int XZT = 0x02;
	public static final int YZL = 0x03;
	public static final int XYB = -0x01;
	public static final int XZB = -0x02;
	public static final int YZR = -0x03;

	private static HashMap<String, Integer> shapes = new HashMap<String, Integer>();
	{
		shapes.put("cube", 0);
		shapes.put("quad", 1);
		shapes.put("sphere", 2);
	}

	public static ArrayList<String> getSupportedShapes() {
		return new ArrayList<String>(shapes.keySet());
	}

	public static void glShapeByName(int x, int y, int z, int[] verticies,
			Object[] extra, String name) {
		int shape = shapes.get(name) == null ? 0 : shapes.get(name);
		switch (shape) {
		case 0:
			glCube(x, y, z, verticies[0], verticies[1], verticies[2],
					ArrayHelper.arrayTranslate(Texture.class, extra));
			break;
		case 1:
			glQuad(x, y, z, verticies[0], verticies[1], verticies[2],
					(Integer) extra[0], (Texture) extra[1]);
			break;
		case 2:
			glSphere(x, y, z, (Float) extra[0], (Integer) extra[1],
					(Texture) extra[2]);
			break;
		}
	}

	public static void glCube(int x, int y, int z, int w, int h, int l,
			Texture[] tex) {
		glQuad(x, y, z, w, h, l, XZT, LUtils.getArg(tex, 2, texGreen));
		glQuad(x, y, z, w, h, l, XZB, LUtils.getArg(tex, 3, texYellow));
		glQuad(x, y, z, w, h, l, XYF, LUtils.getArg(tex, 0, texRed));
		glQuad(x, y, z, w, h, l, XYB, LUtils.getArg(tex, 1, texBlue));
		glQuad(x, y, z, w, h, l, YZL, LUtils.getArg(tex, 4, texWhite));
		glQuad(x, y, z, w, h, l, YZR, LUtils.getArg(tex, 5, texPurple));
	}

	public static void glQuad(int x, int y, int z, int w, int h, int l,
			int dir, Texture t) {
		t.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_QUADS);
		if (dir == XZT) {
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(w, h, 0);// Top (x:0,y=h,z:0)
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, h, 0);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(0, h, l);
			t.glTextureVertex(1, 0);
			GL11.glVertex3f(w, h, l);
		}
		if (dir == XZB) {
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(w, 0, l);// Bottom (x:0,y=0,z:0)
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, 0, l);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(0, 0, 0);
			t.glTextureVertex(1, 0);
			GL11.glVertex3f(w, 0, 0);
		}
		if (dir == XYF) {
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(w, h, l);// Front (x:0,y:0,z=l)
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, h, l);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(0, 0, l);
			t.glTextureVertex(1, 0);
			GL11.glVertex3f(w, 0, l);
		}
		if (dir == XYB) {
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(w, 0, 0);// Back (x:0,y:0,z=0)
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, 0, 0);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(0, h, 0);
			t.glTextureVertex(1, 0);
			GL11.glVertex3f(w, h, 0);
		}
		if (dir == YZL) {
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(0, h, l);// Left (x=0,y:0,z:0)
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, h, 0);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(0, 0, 0);
			t.glTextureVertex(1, 0);
			GL11.glVertex3f(0, 0, l);
		}
		if (dir == YZR) {
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(w, h, 0);// Right (x=w,y:0,z:0)
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(w, h, l);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(w, 0, l);
			t.glTextureVertex(1, 0);
			GL11.glVertex3f(w, 0, 0);
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	public static void glSphere(int x, int y, int z, float r, int quality,
			Texture t) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		Sphere s = new Sphere();
		s.setTextureFlag(true);
		t.bind();
		s.draw(r, quality, quality);
		GL11.glPopMatrix();
	}
}
