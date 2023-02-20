package cc.allio.uno.gis.jackson.geojson.document;

import cc.allio.uno.core.annotation.document.Document;
import cc.allio.uno.core.annotation.document.DocumentFactory;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a <em>GeoJSON document</em> for a <em>FeatureCollection</em>.
 *
 * @see DocumentFactory
 */
public interface FeatureCollectionDocument extends Document {

	/**
	 * The features of the collection.
	 *
	 * @return the features
	 */
	@Nullable
	List<Object> getFeatures();

	/**
	 * The optional Bbox object.
	 *
	 * @return the optional Bbox
	 */
	@Nullable
	double[] getBbox();
}
