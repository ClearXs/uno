package cc.allio.uno.gis.config;

import cc.allio.uno.gis.SRID;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("cc.allio.uno.gis")
public class UnoGisProperties {

    private Integer defaultSrid = SRID.CGCS2000_4490.getCode();
    private Integer transformSrid = SRID.CGCS2000_4490.getCode();
}
