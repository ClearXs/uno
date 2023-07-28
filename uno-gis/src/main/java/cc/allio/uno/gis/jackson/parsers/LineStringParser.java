package cc.allio.uno.gis.jackson.parsers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import static cc.allio.uno.gis.jackson.geojson.GeoJson.COORDINATES;


/**
 * Created by mihaildoronin on 11/11/15.
 */
public class LineStringParser extends BaseParser implements GeometryParser<LineString> {

	public LineStringParser(GeometryFactory geometryFactory) {
		super(geometryFactory);
	}

	public LineString lineStringFromJson(JsonNode root) {
		return geometryFactory.createLineString(PointParser.coordinatesFromJson(root.get(COORDINATES)));
	}

	@Override
	public LineString geometryFromJson(JsonNode node) throws JsonMappingException {
		return lineStringFromJson(node);
	}
}
