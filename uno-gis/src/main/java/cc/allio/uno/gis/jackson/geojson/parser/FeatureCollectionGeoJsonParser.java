package cc.allio.uno.gis.jackson.geojson.parser;

import cc.allio.uno.gis.jackson.geojson.GeoJson;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonFeatures;
import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonBeanAnnotated;
import cc.allio.uno.core.annotation.document.DocumentFactoryException;
import cc.allio.uno.core.annotation.Annotated;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ListMultimap;

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
public class FeatureCollectionGeoJsonParser<T> extends BaseIgnoreDeserializerObjectMapper<T> implements GeoJsonBaseParser<T> {

    public FeatureCollectionGeoJsonParser(JavaType valueType, Class<? extends GeoJsonDeserializer> deserializeClazz) {
        super(valueType, deserializeClazz);
    }

    @Override
    public T deserialize(JsonParser jsonParser) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode nodeObject = mapper.readTree(jsonParser);
        // 取JSON数组
        ArrayNode features = nodeObject.withArray(GeoJson.FEATURES);
        JsonNode bbox = nodeObject.get(GeoJson.BBOX);
        // 创建ObjectNode
        ObjectMapper copyMapper = getIgnoreDeserializerAnnotationMapper(mapper);
        ObjectNode objectNode = copyMapper.createObjectNode();
        //获取对象空间数据字段反序列化名称
        String featuresFieldName = null;
        try {
            ListMultimap<Class<? extends Annotation>, Annotated> index = GeoJsonBeanAnnotated.getInstance().index(valueType.getRawClass());
            Annotated featuresAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonFeatures.class);
            featuresFieldName = featuresAnnotated.getName();
        } catch (DocumentFactoryException e) {
            e.printStackTrace();
        }
        if (null != featuresFieldName && null != features) {
            objectNode.replace(featuresFieldName, features);
        }
        if (null != bbox) {
            objectNode.replace(GeoJson.BBOX, bbox);
        }
        return copyMapper.convertValue(objectNode, valueType);
    }
}
