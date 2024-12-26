package cc.allio.uno.gis.jackson.geojson.parser;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * 〈功能简述〉<br>
 * 〈〉
 */
public interface GeoJsonBaseParser<T> {

	T deserialize(JsonParser jsonParser) throws IOException;

}
