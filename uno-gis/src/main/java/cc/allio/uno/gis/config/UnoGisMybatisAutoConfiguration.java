package cc.allio.uno.gis.config;

import cc.allio.uno.gis.mybatis.type.*;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import lombok.AllArgsConstructor;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties(UnoGisProperties.class)
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class UnoGisMybatisAutoConfiguration implements InitializingBean {
    private final MybatisPlusProperties mybatisPlusProperties;
    private final UnoGisProperties gisProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册Mybatis空间数据Type-Handler
        MybatisConfiguration configuration = mybatisPlusProperties.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(LinearRing.class, new LinearRingTypeHandler(gisProperties));
        typeHandlerRegistry.register(LineString.class, new LineStringTypeHandler(gisProperties));
        typeHandlerRegistry.register(MultiLineString.class, new MultiLineStringTypeHandler(gisProperties));
        typeHandlerRegistry.register(MultiPoint.class, new MultiPointTypeHandler(gisProperties));
        typeHandlerRegistry.register(MultiPolygon.class, new MultiPolygonTypeHandler(gisProperties));
        typeHandlerRegistry.register(Point.class, new PointTypeHandler(gisProperties));
        typeHandlerRegistry.register(Polygon.class, new PolygonTypeHandler(gisProperties));
    }
}
