package cc.allio.uno.gis.jackson.geojson.parser;

import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.gis.jackson.geojson.annotation.*;
import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ListMultimap;
import cc.allio.uno.gis.jackson.geojson.GeoJson;
import cc.allio.uno.core.annotation.document.DocumentFactoryException;
import cc.allio.uno.core.annotation.Annotated;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *
 * @author Mr.QL
 * @ClassName FeatureGeoJsonParser
 * @Date 2022-03-12 22:38
 * @Version 1.0
 */
public class FeatureGeoJsonParser<T> extends BaseIgnoreDeserializerObjectMapper<T> implements GeoJsonBaseParser<T> {

    public FeatureGeoJsonParser(JavaType valueType, Class<? extends GeoJsonDeserializer> deserializeClazz) {
        super(valueType, deserializeClazz);
    }

    @Override
    public T deserialize(JsonParser jsonParser) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode nodeObject = mapper.readTree(jsonParser);
        // 获取对象空间数据字段反序列化名称
        String geometryFieldName = null;
        String idFieldName = null;
        String propertiesName = null;
        List<Annotated> propertyAnnotated = null;
        try {
            ListMultimap<Class<? extends Annotation>, Annotated> index = GeoJsonBeanAnnotated.getInstance().index(valueType.getRawClass());
            Annotated geometryAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonGeometry.class);
            Annotated idAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonId.class);
            Annotated propertiesAnnotation = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonProperties.class);
            propertyAnnotated = index.get(GeoJsonProperty.class);
            if (null != geometryAnnotated) {
                geometryFieldName = geometryAnnotated.getName();
            }
            if (null != idAnnotated) {
                idFieldName = idAnnotated.getName();
            }
            if (null != propertiesAnnotation) {
                propertiesName = propertiesAnnotation.getName();
            }
        } catch (DocumentFactoryException e) {
            e.printStackTrace();
        }
        // 获取数据
        JsonNode id = nodeObject.get(GeoJson.ID);
        JsonNode geometry = nodeObject.get(GeoJson.GEOMETRY);
        JsonNode properties = nodeObject.get(GeoJson.PROPERTIES);
        // 组装
        nodeObject.remove(GeoJson.ID);
        nodeObject.remove(GeoJson.TYPE);
        nodeObject.remove(GeoJson.GEOMETRY);
        nodeObject.remove(GeoJson.PROPERTIES);
        if (null != idFieldName && null != id) {
            nodeObject.replace(idFieldName, id);
        }
        if (null != geometryFieldName && null != geometry) {
            nodeObject.replace(geometryFieldName, geometry);
        }
        // @GeoJsonProperties
        if (null != properties && StringUtils.isNotBlank(propertiesName)) {
            nodeObject.replace(propertiesName, properties);
            // @GeoJsonProperty
        } else if (null != properties && !ObjectUtils.isEmpty(propertyAnnotated)) {
            if (propertyAnnotated.size() == 1) {
                nodeObject.replace(propertyAnnotated.get(0).getName(), properties);
            } else {
                nodeObject.setAll((ObjectNode) properties);
            }
        }
        ObjectMapper copyMapper = getIgnoreDeserializerAnnotationMapper(mapper);
        return copyMapper.convertValue(nodeObject, valueType);
    }
}
