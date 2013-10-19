package crashcourse.k.library.lwjgl.render;

import crashcourse.k.library.internalstate.Entity;

public class Render<T extends Entity> {
	/**
	 * Renders the given entity at it's location, yaw, and pitch.
	 * @param entity
	 */
	public void doRender(T entity) {
		doRender(entity, entity.posX, entity.posY, entity.posZ, entity.yaw, entity.pitch);
	}

	/**
	 * Renders an entity with the <i>given</i> arguments instead of the entity's.
	 * @param entity - the entity to render
	 * @param posX - the positionX to render at
	 * @param posY - the positionY to render at
	 * @param posZ - the positionZ to render at
	 * @param yaw - the requested yaw of the entity
	 * @param pitch - the requested pitch of the entity
	 */
	public void doRender(T entity, float posX, float posY, float posZ,
			float yaw, float pitch) {

	}
}
