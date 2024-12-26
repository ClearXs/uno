package cc.allio.uno.gis.jackson.geojson.deserializer;

import cc.allio.uno.gis.jackson.geojson.parser.FeatureCollectionGeoJsonParser;
import cc.allio.uno.gis.jackson.geojson.parser.FeatureGeoJsonParser;
import cc.allio.uno.gis.jackson.geojson.parser.GeoJsonBaseParser;
import cc.allio.uno.gis.jackson.geojson.parser.GeometryCollectionGeoJsonParser;
import cc.allio.uno.gis.jackson.geojson.GeoJson;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;


/**
 * 〈功能简述〉<br>
 * 〈〉
 */
public class GeoJsonDeserializer<T> extends JsonDeserializer<T> implements ContextualDeserializer {


	private Map<String, GeoJsonBaseParser<T>> parsers;

	public GeoJsonDeserializer() {

	}

	@Override
	public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
		ObjectNode nodeObject = mapper.readTree(jsonParser);
		JsonNode jsonNode = nodeObject.get(GeoJson.TYPE);
		if (nodeObject.has(GeoJson.TYPE)) {
			String typeName = jsonNode.asText();
			GeoJsonBaseParser<T> parser = parsers.get(typeName);
			if (parser != null) {
				jsonParser = mapper.treeAsTokens(nodeObject);
				return parser.deserialize(jsonParser);
			} else {
				throw new JsonMappingException("Invalid Feature type: " + typeName);
			}
		} else {
			return null;
		}
	}


	@Override
	public JsonDeserializer<T> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
		/**
		 * 获取反序列化泛型类型
		 */
		JavaType valueType = ctxt.getContextualType() != null
			? ctxt.getContextualType()
			: property.getMember().getType();
		GeoJsonDeserializer deserializer = createDeserializer(valueType);
		return deserializer;
	}

	/**
	 * 构建序反列化器
	 *
	 * @param valueType
	 * @return
	 */
	private GeoJsonDeserializer<?> createDeserializer(JavaType valueType) {
		GeoJsonDeserializer deserializer = new GeoJsonDeserializer();
		Class<? extends GeoJsonDeserializer> deserializeClazz = deserializer.getClass();
		parsers = Maps.newHashMapWithExpectedSize(10);
		parsers.put(GeoJson.FEATURE, new FeatureGeoJsonParser<>(valueType, deserializeClazz));
		parsers.put(GeoJson.FEATURE_COLLECTION, new FeatureCollectionGeoJsonParser<>(valueType, deserializeClazz));
		parsers.put(GeoJson.GEOMETRY_COLLECTION, new GeometryCollectionGeoJsonParser<>(valueType, deserializeClazz));
		deserializer.parsers = parsers;
		return deserializer;
	}

	@Override
	public Object deserializeWithType(JsonParser p, DeserializationContext deserializationContext, TypeDeserializer typeDeserializer) throws IOException {
		return this.deserialize(p, deserializationContext);
	}
}
