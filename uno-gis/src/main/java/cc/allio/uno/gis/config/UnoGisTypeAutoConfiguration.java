package cc.allio.uno.gis.config;

import cc.allio.uno.data.orm.dsl.type.JdbcType;
import cc.allio.uno.data.orm.dsl.type.JdbcTypeImpl;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import cc.allio.uno.gis.GeometryTypes;
import cc.allio.uno.gis.local.type.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(TypeRegistry.class)
public class UnoGisTypeAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        TypeRegistry typeRegistry = TypeRegistry.getInstance();

        // geometry
        GeometryJavaType geometryJavaType = new GeometryJavaType();
        JdbcTypeImpl geometryJdbc = JdbcType.builder()
                .setJdbcCode(GeometryTypes.GEOMETRY_CODE)
                .build();
        typeRegistry.registerJavaType(geometryJavaType);
        typeRegistry.registerJdbcType(geometryJdbc);
        typeRegistry.registerRelation(geometryJdbc, geometryJavaType);

        // liner ring
        LinearRingJavaType linearRingJavaType = new LinearRingJavaType();
        JdbcTypeImpl linearRingJdbc =
                JdbcType.builder()
                        .setJdbcCode(GeometryTypes.LINEAR_RING_CODE)
                        .build();
        typeRegistry.registerJavaType(linearRingJavaType);
        typeRegistry.registerJdbcType(linearRingJdbc);
        typeRegistry.registerRelation(linearRingJdbc, linearRingJavaType);

        // line string
        LineStringJavaType lineStringJavaType = new LineStringJavaType();
        JdbcTypeImpl lineStringJdbc =
                JdbcType.builder()
                        .setJdbcCode(GeometryTypes.LINE_STRING_CODE)
                        .build();
        typeRegistry.registerJavaType(lineStringJavaType);
        typeRegistry.registerJdbcType(lineStringJdbc);
        typeRegistry.registerRelation(lineStringJdbc, lineStringJavaType);

        // multi line string
        MultiLineStringJavaType multiLineStringJavaType = new MultiLineStringJavaType();
        JdbcTypeImpl multiLineStringJdbc =
                JdbcType.builder()
                        .setJdbcCode(GeometryTypes.MULTI_LINE_STRING_CODE)
                        .build();
        typeRegistry.registerJavaType(multiLineStringJavaType);
        typeRegistry.registerJdbcType(multiLineStringJdbc);
        typeRegistry.registerRelation(multiLineStringJdbc, multiLineStringJavaType);

        // multi point
        MultiPointJavaType multiPointJavaType = new MultiPointJavaType();
        JdbcTypeImpl multiPointJdbc =
                JdbcType.builder()
                        .setJdbcCode(GeometryTypes.MULTI_POINT_CODE)
                        .build();
        typeRegistry.registerJavaType(multiPointJavaType);
        typeRegistry.registerJdbcType(multiPointJdbc);
        typeRegistry.registerRelation(multiPointJdbc, multiPointJavaType);

        // multi polygon
        MultiPolygonJavaType multiPolygonJavaType = new MultiPolygonJavaType();
        JdbcTypeImpl MultiPolygonJdbc =
                JdbcType.builder()
                        .setJdbcCode(GeometryTypes.MULTI_POLYGON_CODE)
                        .build();
        typeRegistry.registerJavaType(multiPolygonJavaType);
        typeRegistry.registerJdbcType(MultiPolygonJdbc);
        typeRegistry.registerRelation(MultiPolygonJdbc, multiPolygonJavaType);

        // point
        PointJavaType pointJavaType = new PointJavaType();
        JdbcTypeImpl pointJdbc =
                JdbcType.builder()
                        .setJdbcCode(GeometryTypes.POINT_CODE)
                        .build();
        typeRegistry.registerJavaType(pointJavaType);
        typeRegistry.registerJdbcType(pointJdbc);
        typeRegistry.registerRelation(pointJdbc, pointJavaType);

        // polygon
        PolygonJavaType polygonJavaType = new PolygonJavaType();
        JdbcTypeImpl polygonJdbc =
                JdbcType.builder()
                        .setJdbcCode(GeometryTypes.POLYGON_CODE)
                        .build();
        typeRegistry.registerJavaType(polygonJavaType);
        typeRegistry.registerJdbcType(polygonJdbc);
        typeRegistry.registerRelation(polygonJdbc, polygonJavaType);

        // geometry collection
        GeometryCollectionJavaType geometryCollectionJavaType = new GeometryCollectionJavaType();
        JdbcTypeImpl geometryCollectionJdbc =
                JdbcType.builder()
                        .setJdbcCode(GeometryTypes.GEOMETRY_COLLECTION_CODE)
                        .build();
        typeRegistry.registerJavaType(geometryCollectionJavaType);
        typeRegistry.registerJdbcType(geometryCollectionJdbc);
        typeRegistry.registerRelation(geometryCollectionJdbc, geometryCollectionJavaType);
    }
}
