package cc.allio.uno.data.orm.type;

import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class TypeRegistryTest extends BaseTestCase {

    @Test
    void testGuessType() {
        Collection<JdbcType> jdbcTypes = TypeRegistry.getInstance().guessJdbcType(TestEnum.class);
        assertNotNull(jdbcTypes);
        JdbcType jdbcType = Lists.newArrayList(jdbcTypes).get(0);

    }

    public enum TestEnum {

    }
}
