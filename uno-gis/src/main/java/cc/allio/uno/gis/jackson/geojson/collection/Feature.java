package cc.allio.uno.gis.jackson.geojson.collection;

import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonGeometry;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonType;
import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import cc.allio.uno.gis.jackson.geojson.serializer.FeatureType;
import cc.allio.uno.gis.jackson.geojson.serializer.GeoJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Geometry;

/**
 * 〈功能简述〉<br>
 * 〈〉
 */
@GeoJsonType(type = FeatureType.FEATURE)
@JsonSerialize(using = GeoJsonSerializer.class)
@JsonDeserialize(using = GeoJsonDeserializer.class)
public class Feature<T extends Geometry> {

	public Feature() {
	}

	public Feature(T geom) {
		this.geom = geom;
	}

	private T geom;

	@GeoJsonGeometry
	public T getGeom() {
		return geom;
	}

	public void setGeom(T geom) {
		this.geom = geom;
	}

	@Override
	public String toString() {
		return "Feature{" +
			"geom=" + geom +
			'}';
	}
}
