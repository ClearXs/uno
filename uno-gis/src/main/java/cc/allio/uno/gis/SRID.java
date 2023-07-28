package cc.allio.uno.gis;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Spatial Reference System Identifier <a href="https://epsg.io/">check</a>
 *
 * @author jiangwei
 * @date 2022/12/8 15:07
 * @since 1.1.2
 */
@Getter
@AllArgsConstructor
public enum SRID {

    WGS84_4326(4326, "WGS 84", "World", ""),
    CGCS2000_4548(4548, "CGCS2000 / 3-degree Gauss-Kruger CM 117E", "China - onshore between 115°30'E and 118°30'E", ""),
    CGCS2000_4490(4490, "CGCS2000 / 3-degree Gauss-Kruger CM 117E", "China - onshore between 115°30'E and 118°30'E", "");

    /**
     * srid
     */
    private final int code;

    /**
     * name
     */
    private final String name;

    /**
     * 使用区域
     */
    private final String area;

    /**
     * 精准度
     */
    private final String accuracy;

    /**
     * 根据srid code获取SRID对象
     *
     * @param code
     * @return
     */
    public static SRID from(int code) {
        for (SRID srid : values()) {
            if (srid.getCode() == code) {
                return srid;
            }
        }
        return null;
    }
}
