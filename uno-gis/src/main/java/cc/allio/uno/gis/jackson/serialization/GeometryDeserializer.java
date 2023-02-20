package cc.allio.uno.gis.jackson.serialization;

import cc.allio.uno.gis.jackson.parsers.GeometryParser;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonTransform;
import cc.allio.uno.gis.transform.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class GeometryDeserializer<T extends Geometry> extends JsonDeserializer<T> {

    private final GeometryParser<T> geometryParser;

    public GeometryDeserializer(GeometryParser<T> geometryParser) {
        this.geometryParser = geometryParser;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode root = oc.readTree(jsonParser);

        T fromGeometry = geometryParser.geometryFromJson(root);

        // 寻找GeoJsonTransform的注解
        Object o = jsonParser.getCurrentValue();

        AnnoTransform transform = new AnnoTransform(o, GeoJsonTransform.class, ReadWrite.READ);
        return transform.transform(fromGeometry);
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext deserializationContext, TypeDeserializer typeDeserializer) throws IOException {
        return this.deserialize(p, deserializationContext);
    }
}
