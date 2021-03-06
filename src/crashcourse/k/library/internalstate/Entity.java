package crashcourse.k.library.internalstate;

import crashcourse.k.library.internalstate.world.World;
import crashcourse.k.library.lwjgl.tex.ColorTexture;
import crashcourse.k.library.lwjgl.tex.Texture;

public abstract class Entity {
	protected Victor pos = new Victor(), posInter = new Victor(),
			vel = new Victor(), velInter = new Victor();
	protected float pitch, yaw, roll; // Use rotations with caution, their
										// bounding boxes will remain straight.
	protected float deltaT;
	protected float elapsed;
	protected Texture tex;
	private boolean isDead;

	public Entity() {
		pos.update(0, 0, 0);
		tex = ColorTexture.RED;
	}
	public Entity(Texture texture) {
		pos.update(0, 0, 0);
		tex = texture;
	}

	public Entity(float posX, float posY, float posZ, Texture texture) {
		pos.update(posX, posY, posZ);
		tex = texture;
	}

	public Entity(float posX, float posY, float posZ, float xVel, float yVel,
			float zVel, Texture texture) {
		pos.update(posX, posY, posZ);
		vel.update(xVel, yVel, zVel);
		tex = texture;
	}

	public Entity(float posX, float posY, float posZ, float xVel, float yVel,
			float zVel, float pitchRot, float yawRot, float rollRot,
			Texture texture) {
		pos.update(posX, posY, posZ);
		vel.update(xVel, yVel, zVel);
		pitch = pitchRot;
		yaw = yawRot;
		roll = rollRot;
		tex = texture;
	}

	/**
	 * Update positions based on velocities. In superclasses, overridden to do..
	 * stuff. You know. stuff.
	 * 
	 * @param delta
	 *            - the float value of the time elapsed to update.
	 */
	public void updateOnTick(float delta, World w) {
		elapsed = 0;

		pos.update(pos.x + vel.x * delta, pos.y + vel.y * delta, pos.z + vel.z
				* delta);

		deltaT = delta;
	}

	/**
	 * Sets X, Y, and Z positions all in one. wooo.
	 * 
	 * @param newX
	 *            - new posX
	 * @param newY
	 *            - new posY
	 * @param newZ
	 *            - new posZ
	 */
	public void setXYZ(float newX, float newY, float newZ) {
		pos.init(newX, newY, newZ);
	}

	/**
	 * Sets relative X, Y, and Z positions all in one. Even more wooooo.
	 * 
	 * @param relX
	 *            - relative change in posX.
	 * @param relY
	 *            - relative change in posY.
	 * @param relZ
	 *            - relative change in posZ.
	 */
	public void setRelativeXYZ(float relX, float relY, float relZ) {
		pos.update(pos.x + relX, pos.y + relY, pos.z + relZ);
	}

	/**
	 * Sets X, Y, and Z velocities all in one. wow, neat.
	 * 
	 * @param newXVel
	 *            - new velX
	 * @param newYVel
	 *            - new velY
	 * @param newZVel
	 *            - new velZ
	 */
	public void setXYZVel(float newXVel, float newYVel, float newZVel) {
		vel.update(newXVel, newYVel, newZVel);
	}

	/**
	 * Sets relative X, Y, and Z velocities all in one. The most wowiest.
	 * 
	 * @param relXVel
	 *            - Relative velX
	 * @param relYVel
	 *            - Relative velY
	 * @param relZVel
	 *            - Relative velZ
	 */
	public void setRelativeXYZVel(float relXVel, float relYVel, float relZVel) {
		vel.update(vel.x + relXVel, vel.y + relYVel, vel.z + relZVel);
	}

	/**
	 * Get entity x-pos
	 * 
	 * @return thisEntity.posX
	 */
	public float getX() {
		return pos.x;
	}

	/**
	 * Get entity y-pos
	 * 
	 * @return thisEntity.posY
	 */
	public float getY() {
		return pos.y;
	}

	/**
	 * Get entity z-pos
	 * 
	 * @return thisEntity.posZ
	 */
	public float getZ() {
		return pos.z;
	}

	/**
	 * Get entity x-vel
	 * 
	 * @return thisEntity.velX
	 */
	public float getXVel() {
		return vel.x;
	}

	/**
	 * Get entity y-vel
	 * 
	 * @return thisEntity.velY
	 */
	public float getYVel() {
		return vel.y;
	}

	/**
	 * Get entity z-vel
	 * 
	 * @return thisEntity.velZ
	 */
	public float getZVel() {
		return vel.z;
	}

	/**
	 * Set posX to newX
	 * 
	 * @param newX
	 *            the new value for posX
	 */
	public void setXPos(float newX) {
		setXYZ(newX, pos.y, pos.z);
	}

	/**
	 * Set posY to newY
	 * 
	 * @param newY
	 *            the new value for posY
	 */
	public void setYPos(float newY) {
		setXYZ(pos.x, newY, pos.z);
	}

	/**
	 * Set posZ to newZ
	 * 
	 * @param newZ
	 *            the new value for posZ
	 */
	public void setZPos(float newZ) {
		setXYZ(pos.x, pos.y, newZ);
	}

	/**
	 * Set velX to newXVel
	 * 
	 * @param newXVel
	 *            the new value for velX
	 */
	public void setXVel(float newXVel) {
		vel.update(newXVel, vel.y, vel.z);
	}

	/**
	 * Set velY to newYVel
	 * 
	 * @param newYVel
	 *            the new value for velY
	 */
	public void setYVel(float newYVel) {
		vel.update(vel.x, newYVel, vel.z);
	}

	/**
	 * Set velZ to newZVel
	 * 
	 * @param newZVel
	 *            the new value for velZ
	 */
	public void setZVel(float newZVel) {
		vel.update(vel.x, newZVel, vel.z);
	}

	public void setDead() {
		isDead = true;
	}

	public boolean isDead() {
		return isDead;
	}
	public void interpolate(float elap) {
		elapsed += elap;
		float del = elapsed / deltaT;
		if (Float.isInfinite(del)) {
			del = 0;
		}
		if (del > 1) {
			del = 1;
		}
		synchronized (posInter) {
			synchronized (pos) {
				posInter = pos.interpolate(del);
			}
		}
		synchronized (velInter) {
			velInter = vel.interpolate(del);
		}
	}
	public Victor getInterpolated() {
		synchronized (posInter) {
			synchronized (velInter) {
				posInter.update(posInter.x + velInter.x, posInter.y
						+ velInter.y, posInter.z + velInter.z);
				return posInter;
			}
		}
	}

	public Victor getPosition() {
		synchronized (pos) {
			return pos;
		}
	}

	public Texture getTex() {
		return tex;
	}
}