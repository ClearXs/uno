package cc.allio.uno.core.id;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.util.id.IdGenerator;
import org.junit.jupiter.api.Test;

public class InternalDataCenterIdGeneratorTest extends BaseTestCase {

    @Test
    void testNextId() {
        Long nextId = IdGenerator.defaultGenerator().getNextId();
        assertNotNull(nextId);
    }
}
