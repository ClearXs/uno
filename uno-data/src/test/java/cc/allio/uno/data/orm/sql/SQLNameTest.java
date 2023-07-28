package cc.allio.uno.data.orm.sql;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLNameTest extends BaseTestCase {

    @Test
    void testTurnHumpInUppercase() {
        String mot = "MOT";
        SQLName motFormat = SQLName.of(mot, SQLName.HUMP_FEATURE);
        assertEquals("mot", motFormat.format());

        String motTest = "MOT_TEST";
        SQLName motTestFormat = SQLName.of(motTest, SQLName.HUMP_FEATURE);
        assertEquals("motTest", motTestFormat.format());
    }
}
