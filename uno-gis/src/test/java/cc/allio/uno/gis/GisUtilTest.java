package cc.allio.uno.gis;

import cc.allio.uno.core.util.ResourceUtil;
import cc.allio.uno.gis.GisUtil;
import cc.allio.uno.gis.SRID;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

import java.io.File;
import java.io.FileNotFoundException;

public class GisUtilTest extends BaseTestCase {

    @Test
    void testElevation() throws FileNotFoundException {
        File file = ResourceUtil.getFile("classpath:高平市.tif");
        String path = file.getAbsoluteFile().getPath();
        GisUtil.elevation(GisUtil.createPoint(2, 2), path);

    }

    @Test
    void testTransform() {
        Point point = GisUtil.createPoint(115.974611, 36.36626944);
        Point toPoint = GisUtil.transform(point, SRID.CGCS2000_4548);
        assertNotNull(toPoint);

    }
}
