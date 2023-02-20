package cc.allio.uno.gis.jackson.parsers;

import cc.allio.uno.gis.jackson.geojson.GeoJson;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class MultiPointParser extends BaseParser implements GeometryParser<MultiPoint> {

	public MultiPointParser(GeometryFactory geometryFactory) {
		super(geometryFactory);
	}

	public MultiPoint multiPointFromJson(JsonNode root) {
		return geometryFactory.createMultiPoint(PointParser.coordinatesFromJson(root.get(GeoJson.COORDINATES)));
	}

	@Override
	public MultiPoint geometryFromJson(JsonNode node) throws JsonMappingException {
		return multiPointFromJson(node);
	}
}
