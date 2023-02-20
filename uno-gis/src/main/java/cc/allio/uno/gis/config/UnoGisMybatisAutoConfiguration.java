package cc.allio.uno.gis.config;

import cc.allio.uno.gis.mybatis.MybatisProperties;
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
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class UnoGisMybatisAutoConfiguration implements InitializingBean {
    private final MybatisPlusProperties mybatisPlusProperties;
    private final MybatisProperties mybatisProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册Mybatis空间数据Type-Handler
        MybatisConfiguration configuration = mybatisPlusProperties.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(LinearRing.class, new LinearRingTypeHandler(mybatisProperties));
        typeHandlerRegistry.register(LineString.class, new LineStringTypeHandler(mybatisProperties));
        typeHandlerRegistry.register(MultiLineString.class, new MultiLineStringTypeHandler(mybatisProperties));
        typeHandlerRegistry.register(MultiPoint.class, new MultiPointTypeHandler(mybatisProperties));
        typeHandlerRegistry.register(MultiPolygon.class, new MultiPolygonTypeHandler(mybatisProperties));
        typeHandlerRegistry.register(Point.class, new PointTypeHandler(mybatisProperties));
        typeHandlerRegistry.register(Polygon.class, new PolygonTypeHandler(mybatisProperties));
    }
}
