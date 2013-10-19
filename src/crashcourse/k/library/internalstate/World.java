package crashcourse.k.library.internalstate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
	protected ArrayList<Entity> loadedEntities = new ArrayList<Entity>();
	
	/**
	 * Returns an unmodifiable List for access to the entities.
	 * @return an unmodifiable List of entities in this world
	 */
	public List<Entity> getEntityList() {
		return Collections.unmodifiableList(new ArrayList<Entity>(loadedEntities));
	}
}
