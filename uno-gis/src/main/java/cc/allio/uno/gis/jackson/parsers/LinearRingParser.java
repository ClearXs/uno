package cc.allio.uno.gis.jackson.parsers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;

import static cc.allio.uno.gis.jackson.geojson.GeoJson.COORDINATES;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class LinearRingParser extends BaseParser implements GeometryParser<LinearRing> {

	public LinearRingParser(GeometryFactory geometryFactory) {
		super(geometryFactory);
	}

	public LinearRing linearRingFromJson(JsonNode root) {
		return geometryFactory.createLinearRing(PointParser.coordinatesFromJson(root.get(COORDINATES)));
	}

	@Override
	public LinearRing geometryFromJson(JsonNode node) throws JsonMappingException {
		return linearRingFromJson(node);
	}
}
