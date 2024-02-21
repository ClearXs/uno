package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLNameTest extends BaseTestCase {

    @Test
    void testTurnHumpInUppercase() {
        String mot = "MOT";
        DSLName motFormat = DSLName.of(mot, DSLName.HUMP_FEATURE);
        assertEquals("mot", motFormat.format());

        String motTest = "MOT_TEST";
        DSLName motTestFormat = DSLName.of(motTest, DSLName.HUMP_FEATURE);
        assertEquals("motTest", motTestFormat.format());
    }
}
