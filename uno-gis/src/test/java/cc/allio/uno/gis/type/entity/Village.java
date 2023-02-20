package cc.allio.uno.gis.type.entity;

import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
public class Village {

    private String code;

    private String name;

    private Point geom;

}
