package cc.allio.uno.gis.mybatis;

import cc.allio.uno.gis.SRID;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("cc.allio.uno.uno.gis.mybatis")
public class MybatisProperties {

    /**
     * 数据库默认的srid值
     */
    private int dbSRID = SRID.WGS84_4326.getCode();
}
