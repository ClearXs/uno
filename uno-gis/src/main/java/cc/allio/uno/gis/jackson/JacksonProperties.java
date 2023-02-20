package cc.allio.uno.gis.jackson;

import cc.allio.uno.gis.SRID;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("cc.allio.uno.uno.gis.jackson")
public class JacksonProperties {

    /**
     * 默认SRID值
     */
    private Integer defaultSRID = SRID.WGS84_4326.getCode();

}
