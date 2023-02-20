package cc.allio.uno.gis.jackson.geojson.document;

import org.locationtech.jts.geom.Geometry;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Default implementation of a {@link GeometryCollectionDocument}.
 */
public class IntrospectionGeometryCollectionDocument implements GeometryCollectionDocument {

	private final List<Geometry> geometries;

	/**
	 * Constructor
	 *
	 * @param geometries the geometries
	 */
	public IntrospectionGeometryCollectionDocument(@Nullable List<Geometry> geometries) {
		this.geometries = geometries;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Geometry> getGeometries() {
		return geometries;
	}
}
