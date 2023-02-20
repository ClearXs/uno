package cc.allio.uno.gis.jackson.parsers;

import cc.allio.uno.gis.jackson.geojson.GeoJson;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;


/**
 * Created by mihaildoronin on 11/11/15.
 */
public class LineStringParser extends BaseParser implements GeometryParser<LineString> {

	public LineStringParser(GeometryFactory geometryFactory) {
		super(geometryFactory);
	}

	public LineString lineStringFromJson(JsonNode root) {
		return geometryFactory.createLineString(PointParser.coordinatesFromJson(root.get(GeoJson.COORDINATES)));
	}

	@Override
	public LineString geometryFromJson(JsonNode node) throws JsonMappingException {
		return lineStringFromJson(node);
	}
}
