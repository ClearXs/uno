package cc.allio.uno.gis.jackson;

import cc.allio.uno.gis.jackson.geojson.annotation.*;
import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import cc.allio.uno.gis.jackson.geojson.serializer.FeatureType;
import cc.allio.uno.gis.jackson.geojson.serializer.GeoJsonSerializer;
import cc.allio.uno.gis.SRID;
import cc.allio.uno.gis.transform.FromTo;
import cc.allio.uno.test.BaseTestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Map;

/**
 * <code>
 * {
 * "type":"Feature",
 * "id":1,
 * "geometry":{
 * "type":"Point",
 * "coordinates":[
 * 0,
 * 0
 * ]
 * },
 * "properties":"name"
 * }
 * </code>
 * geojson序列化实体定义
 *
 * @author j.x
 * @GeoJsonType 注解该geojson对象的类型
 * @JsonSerialize(using = {@link GeoJsonSerializer}) 注释该实体使用{@link GeoJsonSerializer}进行序列化
 * @JsonDeserialize(using = GeoJsonDeserializer.class) 注释该实体使用{@link GeoJsonDeserializer}进行反序列化
 * @GeoJsonId、@GeoJsonGeometry适用于字段上或者getxxx()方法，表明该字段进行geojson数据的何种形式
 * @GeoJsonProperties、@GeoJsonProperty是互为互斥的两个注解，都是构建geojson-properties属性，其中@GeoJsonProperties只能出现一次，@GeoJsonProperty在同一个对象中可以多次出现
 * @date 2022/9/26 14:43
 * @since 1.0
 */
class JacksonTest extends BaseTestCase {

    private static ObjectMapper objectMapper;
    private static GeometryFactory factory;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());
        factory = new GeometryFactory();

    }

    /**
     * Test Case: {"type":"Feature","id":1,"geometry":{"type":"Point","coordinates":[0.0,0.0]},"properties":"name"}
     */
    @Test
    void testSingleSerializer() throws JsonProcessingException {
        FeaturePropertiesSingle featureGis = new FeaturePropertiesSingle();
        featureGis.setId(1L);
        featureGis.setUser("name");
        featureGis.setPoint(factory.createPoint(new Coordinate(38.022131, 99.238129)));
        String geojson = objectMapper.writeValueAsString(featureGis);
        Assertions.assertEquals("{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"Point\",\"coordinates\":[0.0,0.0]},\"properties\":\"name\"}\n", geojson);
    }

    @Test
    void testSingleDeserializer() throws JsonProcessingException {
        FeaturePropertiesSingle featureGis = new FeaturePropertiesSingle();
        featureGis.setId(1L);
        featureGis.setUser("name");
        featureGis.setPoint(factory.createPoint(new Coordinate(38.022131, 99.238129)));
        FeaturePropertiesSingle featureObjectGis = objectMapper.readValue("{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"Point\",\"coordinates\":[0.0,0.0]},\"properties\":\"name\"}\n", FeaturePropertiesSingle.class);
        Assertions.assertEquals(featureGis, featureObjectGis);
    }

    @Test
    void testObjectSerializer() throws JsonProcessingException {
        FeaturePropertiesObject featureGis = new FeaturePropertiesObject();
        featureGis.setId(1L);
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        featureGis.setUser(user);
        featureGis.setPoint(factory.createPoint(new Coordinate(0, 0)));
        String geojson = objectMapper.writeValueAsString(featureGis);
        Assertions.assertEquals("{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"Point\",\"coordinates\":[0.0,0.0]},\"properties\":{\"username\":\"username\",\"password\":\"password\"}}", geojson);
    }

    @Test
    void testObjectDeserializer() throws JsonProcessingException {
        FeaturePropertiesObject featureGis = new FeaturePropertiesObject();
        featureGis.setId(1L);
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        featureGis.setUser(user);
        featureGis.setPoint(factory.createPoint(new Coordinate(0, 0)));
        FeaturePropertiesObject featureObjectGis = objectMapper.readValue("{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"Point\",\"coordinates\":[0.0,0.0]},\"properties\":{\"username\":\"username\",\"password\":\"password\"}}", FeaturePropertiesObject.class);
        Assertions.assertEquals(featureGis, featureObjectGis);
    }

    @Test
    void testMultiPropertiesSerializer() throws JsonProcessingException {
        FeatureMultiProperties properties = new FeatureMultiProperties();
        properties.setId(1L);
        properties.setName("name");
        properties.setPassword("password");
        properties.setPoint(factory.createPoint(new Coordinate(0, 0)));
        String geoJson = objectMapper.writeValueAsString(properties);
        Assertions.assertEquals("{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"Point\",\"coordinates\":[0.0,0.0]},\"properties\":{\"name\":\"name\",\"password\":\"password\"}}", geoJson);
    }

    @Test
    void testMultiPropertiesDeserializer() throws JsonProcessingException {
        FeatureMultiProperties properties = new FeatureMultiProperties();
        properties.setId(1L);
        properties.setName("name");
        properties.setPassword("password");
        properties.setPoint(factory.createPoint(new Coordinate(99.238129, 38.022131)));
        FeatureMultiProperties featureMultiProperties = objectMapper.readValue("{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"Point\",\"coordinates\":[99.238129,0.0]},\"properties\":{\"name\":\"name\",\"password\":\"password\"}}", FeatureMultiProperties.class);
        Assertions.assertEquals(featureMultiProperties, properties);
    }

    @Data
    @GeoJsonType(type = FeatureType.FEATURE)
    @GeoJsonTransform(serializer = @FromTo(fromCrs = SRID.WGS84_4326, toCrs = SRID.CGCS2000_4548))
    @JsonSerialize(using = GeoJsonSerializer.class)
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    public static class FeaturePropertiesSingle {

        @GeoJsonId
        private Long id;

        @GeoJsonProperties
        private String user;

        @GeoJsonGeometry
        private Point point;
    }

    @Data
    @GeoJsonType(type = FeatureType.FEATURE)
    @GeoJsonTransform(serializer = @FromTo(fromCrs = SRID.WGS84_4326, toCrs = SRID.CGCS2000_4548), deserializer = @FromTo(fromCrs = SRID.CGCS2000_4548, toCrs = SRID.WGS84_4326))
    @JsonSerialize(using = GeoJsonSerializer.class)
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    public static class FeaturePropertiesObject {

        @GeoJsonId
        private Long id;

        @GeoJsonProperties
        private User user;

        @GeoJsonGeometry
        private Point point;
    }

    @Data
    @GeoJsonType(type = FeatureType.FEATURE)
    @JsonSerialize(using = GeoJsonSerializer.class)
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    public static class FeatureListProperties {

        @GeoJsonId
        private Long id;

        @GeoJsonProperties
        private List<User> user;

        @GeoJsonGeometry
        private Point point;
    }

    @Data
    @GeoJsonType(type = FeatureType.FEATURE)
    @JsonSerialize(using = GeoJsonSerializer.class)
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    public static class FeatureMapProperties {

        @GeoJsonId
        private Long id;

        @GeoJsonProperties
        private Map<String, User> user;

        @GeoJsonGeometry
        private Point point;
    }

    @Data
    public static class User {
        private String username;
        private String password;
    }

    @Data
    @GeoJsonType(type = FeatureType.FEATURE)
    @JsonSerialize(using = GeoJsonSerializer.class)
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    public static class FeatureMultiProperties {

        @GeoJsonId
        private Long id;

        @GeoJsonProperty
        private String name;

        @GeoJsonProperty
        private String password;

        @GeoJsonGeometry
        private Point point;
    }
}
