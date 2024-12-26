package cc.allio.uno.gis.jackson.geojson.collection;

import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonBbox;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonFeatures;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonType;
import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import cc.allio.uno.gis.jackson.geojson.serializer.FeatureType;
import cc.allio.uno.gis.jackson.geojson.serializer.GeoJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈封装Feature集合〉
 */
@GeoJsonType(type = FeatureType.FEATURE_COLLECTION)
@JsonSerialize(using = GeoJsonSerializer.class)
@JsonDeserialize(using = GeoJsonDeserializer.class)
public class FeatureCollection<T> {

	public FeatureCollection() {
	}

	private List<T> list;

	private double[] bbox;

	@GeoJsonFeatures
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@GeoJsonBbox
	public double[] getBbox() {
		return bbox;
	}

	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}

	public FeatureCollection build(List<T> list) {
		this.list = list;
		return this;
	}

	public FeatureCollection build(List<T> list, double[] bbox) {
		this.list = list;
		this.bbox = bbox;
		return this;
	}

	@Override
	public String toString() {
		return "FeatureCollection{" +
			"list=" + list +
			", bbox=" + Arrays.toString(bbox) +
			'}';
	}
}
