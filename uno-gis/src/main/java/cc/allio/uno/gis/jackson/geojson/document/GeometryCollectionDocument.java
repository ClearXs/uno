package cc.allio.uno.gis.jackson.geojson.document;

import cc.allio.uno.core.annotation.document.Document;
import cc.allio.uno.core.annotation.document.DocumentFactory;
import org.locationtech.jts.geom.Geometry;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a <em>GeoJSON document</em> for a <em>GeometryCollection</em>.
 *
 * @see DocumentFactory
 */
public interface GeometryCollectionDocument extends Document {

	/**
	 * The geometries of the collection.
	 *
	 * @return the geometries
	 */
	@Nullable
	List<Geometry> getGeometries();
}
