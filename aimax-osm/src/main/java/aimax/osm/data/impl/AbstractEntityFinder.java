package aimax.osm.data.impl;

import java.util.ArrayList;
import java.util.List;

import aimax.osm.data.EntityFinder;
import aimax.osm.data.MapDataStorage;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;

/**
 * Base class suitable to implement different entity finders. Just the
 * method {@link #find(boolean)} has to be overridden.
 * @author Ruediger Lunde
 */
public abstract class AbstractEntityFinder implements EntityFinder {

	protected static enum Mode {
		ENTITY, NODE, WAY, ADDRESS
	}

	private final MapDataStorage storage;

	protected Mode mode;
	protected String pattern;
	protected Position position;
	protected MapWayFilter wayFilter;
	private int minRadius;
	private int maxRadius;

	private List<MapEntity> intermediateResults;
	private List<MapEntity> results;

	/** Creates a new entity finder for the given map data storage. */
	public AbstractEntityFinder(MapDataStorage storage) {
		this.storage = storage;
		minRadius = 2;
		maxRadius = 25;
		intermediateResults = new ArrayList<MapEntity>();
		results = new ArrayList<MapEntity>();
	}
	
	/** {@inheritDoc} */ 
	@Override
	public int getMinRadius() {
		return minRadius;
	}
	
	/** {@inheritDoc} */ 
	@Override
	public void setMinRadius(int km) {
		minRadius = km;
	}
	
	/** {@inheritDoc} */ 
	@Override
	public int getMaxRadius() {
		return maxRadius;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setMaxRadius(int km) {
		maxRadius = km;
	}
	
	/** {@inheritDoc} */
	@Override
	public void findEntity(String pattern, Position pos) {
		mode = Mode.ENTITY;
		this.pattern = pattern;
		position = pos;
		wayFilter = null;
		clearResults();
		find(false);
	}

	/** {@inheritDoc} */
	@Override
	public void findNode(String pattern, Position pos) {
		mode = Mode.NODE;
		this.pattern = pattern;
		position = pos;
		clearResults();
		find(false);
	}

	/** {@inheritDoc} */
	@Override
	public void findWay(String pattern, Position pos, MapWayFilter filter) {
		mode = Mode.WAY;
		this.pattern = pattern;
		position = pos;
		wayFilter = filter;
		clearResults();
		find(false);
	}

	/** {@inheritDoc} */
	@Override
	public void findAddress(String pattern, Position pos) {
		mode = Mode.ADDRESS;
		this.pattern = pattern;
		position = pos;
		wayFilter = null;
		clearResults();
		find(false);
	}

	/** {@inheritDoc} */
	@Override
	public void findMore() {
		find(true);
	}

	/** Abstract operation which makes the interesting work. */
	protected abstract void find(boolean findMore);

	public Position getRefPosition() {
		return position;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<MapEntity> getIntermediateResults() {
		return intermediateResults;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<MapEntity> getResults() {
		return results;
	}

	/** {@inheritDoc} */
	@Override
	public void selectIntermediateResult(MapNode node) {
		intermediateResults.clear();
		intermediateResults.add(node);
	}

	private void clearResults() {
		intermediateResults.clear();
		results.clear();
	}
	
	protected MapDataStorage getStorage() {
		return storage;
	}
}