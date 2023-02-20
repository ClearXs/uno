package cc.allio.uno.gis.jackson.parsers;

import org.locationtech.jts.geom.GeometryFactory;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class BaseParser {

	protected GeometryFactory geometryFactory;

	public BaseParser(GeometryFactory geometryFactory) {
		this.geometryFactory = geometryFactory;
	}

}
