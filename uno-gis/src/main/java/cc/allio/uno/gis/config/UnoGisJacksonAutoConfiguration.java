package cc.allio.uno.gis.config;

import cc.allio.uno.gis.jackson.JtsModule;
import cc.allio.uno.gis.jackson.JtsModule3D;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties(UnoGisProperties.class)
public class UnoGisJacksonAutoConfiguration implements InitializingBean {

    private final ObjectMapper objectMapper;
    private final UnoGisProperties gisProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册geo-json序列化、反序列化
        JtsModule jtsModule = new JtsModule(geometryFactory());
        objectMapper.registerModule(jtsModule);
        JtsModule3D jtsModule3D = new JtsModule3D(geometryFactory());
        objectMapper.registerModule(jtsModule3D);
    }

    public GeometryFactory geometryFactory() {
        return new GeometryFactory(precisionModel(), gisProperties.getDefaultSrid());
    }

    public PrecisionModel precisionModel() {
        return new PrecisionModel();
    }

}
