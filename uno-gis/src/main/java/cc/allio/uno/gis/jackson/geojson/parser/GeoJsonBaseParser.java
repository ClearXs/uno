package cc.allio.uno.gis.jackson.geojson.parser;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *
 * @author Mr.QL
 * @ClassName BaseParser
 * @Date 2022-03-12 22:13
 * @Version 1.0
 */
public interface GeoJsonBaseParser<T> {

	T deserialize(JsonParser jsonParser) throws IOException;

}
