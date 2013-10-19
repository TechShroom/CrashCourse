package crashcourse.k.library.internalstate;

public abstract class Entity {
	public float posX, posY, posZ;
	public float motionX, motionY, motionZ;
	
	/**
	 * Attempts to move with the current motion X-Z values
	 */
	public void attemptMove() {
		move(motionX, motionY, motionZ);
	}
	
	/**
	 *  Moves with the given motion X-Z values
	 * @param mX - the motionX
	 * @param mY - the motionY
	 * @param mZ - the motionZ
	 */
	public void move(float mX, float mY, float mZ) {
		posX += mX;
		posY += mY;
		posZ += mZ;
	}
}
