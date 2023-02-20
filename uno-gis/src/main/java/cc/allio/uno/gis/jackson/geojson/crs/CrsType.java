package cc.allio.uno.gis.jackson.geojson.crs;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CrsType {
	@JsonProperty("name")
	name,
	@JsonProperty("link")
	link;
}
