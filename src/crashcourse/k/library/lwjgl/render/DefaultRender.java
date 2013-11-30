package crashcourse.k.library.lwjgl.render;

import crashcourse.k.library.internalstate.Entity;
import crashcourse.k.library.lwjgl.Shapes;

public class DefaultRender extends Render<Entity> {

	@Override
	public void doRender(Entity entity, float posX, float posY, float posZ) {
		Shapes.glQuad(posX, posY, posZ, 10, 10, 10, Shapes.XYF, entity.getTex());
	}

}
