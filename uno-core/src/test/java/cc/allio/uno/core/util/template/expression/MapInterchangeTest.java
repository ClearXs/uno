package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MapInterchangeTest extends BaseTestCase {

    @Test
    void testCheckFailed() {
        MapInterchange interchange = new MapInterchange();
        assertThrows(IllegalArgumentException.class, () -> interchange.change("", "", false));

        Map map = Maps.newHashMap();
        assertThrows(IllegalArgumentException.class, () -> interchange.change("", map, false));
        // 验证 key
        Map<String, String> gMap = Maps.newHashMap();
        assertThrows(IllegalArgumentException.class, () -> interchange.change("", gMap, false));
    }

    @Test
    void testChangeValue() throws Throwable {
        MapInterchange interchange = new MapInterchange();
        Map<String, String> gMap = Maps.newHashMap();
        gMap.put("s", "s");
        Object s = interchange.change("s", gMap, false);
        assertEquals("s", s);
    }

}
