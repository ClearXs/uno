package cc.allio.uno.gis.jackson.geojson.collection;

import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonGeometries;
import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Geometry;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonType;
import cc.allio.uno.gis.jackson.geojson.serializer.FeatureType;
import cc.allio.uno.gis.jackson.geojson.serializer.GeoJsonSerializer;

import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈封装Geometry集合〉
 *
 * @author Mr.QL
 * @ClassName GeometryCollection
 * @Date 2022-03-13 0:13
 * @Version 1.0
 */
@GeoJsonType(type = FeatureType.GEOMETRY_COLLECTION)
@JsonSerialize(using = GeoJsonSerializer.class)
@JsonDeserialize(using = GeoJsonDeserializer.class)
public class GeometryCollection<T extends Geometry> {

	public GeometryCollection() {
	}

	private List<T> list;

	@GeoJsonGeometries
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public GeometryCollection build(List<T> list) {
		this.list = list;
		return this;
	}
}

