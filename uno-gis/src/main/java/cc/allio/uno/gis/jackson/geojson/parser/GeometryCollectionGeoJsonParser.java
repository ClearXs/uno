package cc.allio.uno.gis.jackson.geojson.parser;

import cc.allio.uno.gis.jackson.geojson.GeoJson;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonGeometries;
import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonBeanAnnotated;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ListMultimap;
import cc.allio.uno.core.annotation.document.DocumentFactoryException;
import cc.allio.uno.core.annotation.Annotated;

import java.io.IOException;
import java.lang.annotation.Annotation;


/**
 * 〈功能简述〉<br>
 * 〈〉
 *
 * @author Mr.QL
 * @ClassName FeatureCollectionGeoJsonParser
 * @Date 2022-03-12 23:13
 * @Version 1.0
 */
public class GeometryCollectionGeoJsonParser<T> extends BaseIgnoreDeserializerObjectMapper<T> implements GeoJsonBaseParser<T> {
	public GeometryCollectionGeoJsonParser(JavaType valueType, Class<? extends GeoJsonDeserializer> deserializeClazz) {
		super(valueType, deserializeClazz);
	}

	@Override
	public T deserialize(JsonParser jsonParser) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
		ObjectNode nodeObject = mapper.readTree(jsonParser);
		// 取JSON数组
		ArrayNode geometries = nodeObject.withArray(GeoJson.GEOMETRIES);
		// 创建ObjectNode
		ObjectMapper copyMapper = getIgnoreDeserializerAnnotationMapper(mapper);
		ObjectNode objectNode = copyMapper.createObjectNode();
		//获取对象空间数据字段反序列化名称
		String geometriesFieldName = null;
		try {
			ListMultimap<Class<? extends Annotation>, Annotated> index = GeoJsonBeanAnnotated.getInstance().index(valueType.getRawClass());
			Annotated featuresAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonGeometries.class);
			geometriesFieldName = featuresAnnotated.getName();
		} catch (DocumentFactoryException e) {
			e.printStackTrace();
		}
		if (null != geometriesFieldName && null != geometries) {
			objectNode.set(geometriesFieldName, geometries);
		}
		return copyMapper.convertValue(objectNode, valueType);

	}
}
