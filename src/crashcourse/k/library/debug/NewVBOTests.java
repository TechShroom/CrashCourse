package crashcourse.k.library.debug;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import crashcourse.k.library.internalstate.Entity;
import crashcourse.k.library.lwjgl.DisplayLayer;
import crashcourse.k.library.lwjgl.Shapes;
import crashcourse.k.library.lwjgl.render.Render;
import crashcourse.k.library.main.KMain;

public class NewVBOTests extends KMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DisplayLayer.initDisplay(false, 800, 600, "VBOVBOVBOVBOVBO", false,
					args);
			while (!Display.isCloseRequested()) {
				DisplayLayer.loop(120);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// Destroy
			// Disable the VBO index from the VAO attributes list
			GL20.glDisableVertexAttribArray(0);

			// Delete the VBO
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL15.glDeleteBuffers(vbo_quad);

			// Delete the VAO
			GL30.glBindVertexArray(0);
			GL30.glDeleteVertexArrays(vao_quad);
			DisplayLayer.destroy();
		}
	}

	private static int vbo_quad, vao_quad;
	private int vertexCount;

	@Override
	public void onDisplayUpdate(int delta) {
		Shapes.renderVBO(vao_quad, vertexCount);
	}

	@Override
	public void init(String[] args) {

		// Create
		// OpenGL expects vertices to be defined counter clockwise by default
		float[] vertices = {
				// Left bottom triangle
				0, 100, 0f,
				//
				0, 0, 0f,
				//
				100, 0, 0f,
				// Right top triangle
				100, 0, 0f,
				//
				100, 100, 0f,
				//
				0, 100, 0f};
		// Sending data to OpenGL requires the usage of (flipped) byte buffers
		FloatBuffer verticesBuffer = BufferUtils
				.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		vertexCount = 6;

		// Create a new Vertex Array Object in memory and select it (bind)
		// A VAO can have up to 16 attributes (VBO's) assigned to it by default
		vao_quad = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao_quad);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		// A VBO is a collection of Vectors which in this case resemble the
		// location of each vertex.
		vbo_quad = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_quad);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer,
				GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
	}

	@Override
	public void registerRenders(
			HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender) {

	}

}
