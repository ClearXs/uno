package cc.allio.uno.gis.config;

import cc.allio.uno.gis.jackson.JtsModule3D;
import cc.allio.uno.gis.jackson.JacksonProperties;
import cc.allio.uno.gis.jackson.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties(JacksonProperties.class)
public class UnoGisJacksonAutoConfiguration implements InitializingBean {

    private final ObjectMapper objectMapper;
    private final JacksonProperties jacksonProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册geo-json序列化、反序列化
        JtsModule jtsModule = new JtsModule(geometryFactory());
        objectMapper.registerModule(jtsModule);
        JtsModule3D jtsModule3D = new JtsModule3D(geometryFactory());
        objectMapper.registerModule(jtsModule3D);
    }

    public GeometryFactory geometryFactory() {
        return new GeometryFactory(precisionModel(), jacksonProperties.getDefaultSRID());
    }

    public PrecisionModel precisionModel() {
        return new PrecisionModel();
    }

}
