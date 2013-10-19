package crashcourse.k.library.lwjgl.render;

import java.util.HashMap;

import crashcourse.k.exst.mods.Mods;
import crashcourse.k.library.internalstate.Entity;
import crashcourse.k.library.internalstate.World;

public class RenderManager {
	private static HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender = new HashMap<Class<? extends Entity>, Render<? extends Entity>>();

	public static void registerRenders() {
		Mods.registerRenders();
		classToRender.put(Entity.class, new Render<Entity>());
	}

	public void doRender(World w) {
		for (Entity e : w.getEntityList()) {
			doRender(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doRender(Entity e) {
		Render r = getRendererForClass(e.getClass());
		if (r == null) {
			throw new IllegalStateException("Missing default render!");
		}
		r.doRender(e);
	}

	@SuppressWarnings("unchecked")
	private Render<?> getRendererForClass(Class<? extends Entity> cls) {
		Render<?> r = null;
		if (classToRender.containsKey(cls)) {
			r = classToRender.get(cls);
		} else if (cls.getSuperclass() != null
				&& Entity.class.isAssignableFrom(cls.getSuperclass())) {
			r = getRendererForClass((Class<? extends Entity>) cls
					.getSuperclass());
		}
		return r;
	}

}
