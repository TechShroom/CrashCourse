package crashcourse.k.library.debug;

import crashcourse.k.library.internalstate.GravEntity;
import crashcourse.k.library.internalstate.world.World;
import crashcourse.k.library.lwjgl.tex.ColorTexture;

public class TestGravEntity extends GravEntity {

	private boolean bounce;

	public TestGravEntity() {
		super(100, 800, 0, ColorTexture.RED, 0.1f);
	}

	@Override
	public void updateOnTick(float delta, World w) {
		if (pos.y <= 0 && !bounce) {
			bounce = true;
			setYVel(-vel.y);
		}
		if (bounce && pos.y > 0) {
			bounce = false;
		}
		super.updateOnTick(delta, w);
	}

}
