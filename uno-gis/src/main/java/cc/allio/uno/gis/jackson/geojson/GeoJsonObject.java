package cc.allio.uno.gis.jackson.geojson;

import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonBbox;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonCrs;
import cc.allio.uno.gis.jackson.geojson.crs.Crs;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Arrays;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GeoJsonObject implements Serializable {

	private Crs crs;
	private double[] bbox;

	@GeoJsonCrs
	public Crs getCrs() {
		return crs;
	}

	public void setCrs(Crs crs) {
		this.crs = crs;
	}

	@GeoJsonBbox
	public double[] getBbox() {
		return bbox;
	}

	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GeoJsonObject that = (GeoJsonObject) o;
		if (crs != null ? !crs.equals(that.crs) : that.crs != null) {
			return false;
		}
		return Arrays.equals(bbox, that.bbox);
	}

	@Override
	public int hashCode() {
		int result = crs != null ? crs.hashCode() : 0;
		result = 31 * result + (bbox != null ? Arrays.hashCode(bbox) : 0);
		return result;
	}

	@Override
	public String toString() {
		return "GeoJsonObject{" +
			"crs=" + crs +
			", bbox=" + Arrays.toString(bbox) +
			'}';
	}
}
