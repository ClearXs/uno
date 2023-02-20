package cc.allio.uno.gis.jackson.geojson.serializer;


import cc.allio.uno.gis.jackson.geojson.GeoJson;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonType;

/**
 * The constants of this enumerated type are used to specify the type of <em>GeoJson Object</em> to be generated.
 *
 * @see GeoJsonType
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-1.4" target="_blank">RFC 7946 - Definitions</a>
 */
public enum FeatureType {

	/**
	 * Geometry type <em>GeometryCollection</em>
	 */
	GEOMETRY_COLLECTION(GeoJson.GEOMETRY_COLLECTION, true),

	/**
	 * GeoJson type <em>Feature</em>
	 */
	FEATURE(GeoJson.FEATURE, false),

	/**
	 * GeoJson type <em>FeatureCollection</em>
	 */
	FEATURE_COLLECTION(GeoJson.FEATURE_COLLECTION, false);

	private final String name;
	private final boolean geometryType;

	FeatureType(String name, boolean geometryType) {
		this.name = name;
		this.geometryType = geometryType;
	}

	/**
	 * Returns the name of the type according to the <em>GeoJSON</em> specification.
	 *
	 * @return the name of the type, e.g. "FeatureCollection" for GeoJsonType.FEATURE_COLLECTION
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns whether the type is a geometry type according to the <em>GeoJSON</em> specification.
	 *
	 * @return <code>true</code> if the type is a geometry, <code>false</code> otherwise
	 */
	public boolean isGeometryType() {
		return geometryType;
	}
}
