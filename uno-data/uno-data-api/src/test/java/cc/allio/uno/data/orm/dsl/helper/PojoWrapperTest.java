package cc.allio.uno.data.orm.dsl.helper;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.test.BaseTestCase;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PojoWrapperTest extends BaseTestCase {

    @Test
    void testInconsistentColumnDefAndValue() {
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.setId("1");
        simplePojo.setUserName("1");
        PojoWrapper<SimplePojo> instance = PojoWrapper.getInstance(simplePojo);

        Object idValue = instance.getValueByColumn("id");
        assertEquals(1L, idValue);
        Object userName = instance.getValueByColumn("user_name");
        assertEquals("1", userName);
    }

    @Test
    void testNullValue() {
        SimplePojo simplePojo = new SimplePojo();

        PojoWrapper<SimplePojo> instance = PojoWrapper.getInstance(simplePojo);

        Long age = instance.getForce("age", Long.class);
        assertNull(age);
    }

    @Data
    public static class SimplePojo implements PojoResolver {

        private String id;

        private String userName;

        private Long age;

        @Override
        public ColumnDefListResolver obtainColumnDefListResolver() {
            return new SimplePojoColumnDefListResolver();
        }
    }

    public static class SimplePojoColumnDefListResolver implements ColumnDefListResolver {

        ColumnDef id = ColumnDef.builder()
                .dslName(DSLName.of("id"))
                .dataType(DataType.create(DSLType.BIGINT))
                .build();


        ColumnDef userName = ColumnDef.builder()
                .dslName(DSLName.of("userName"))
                .dataType(DataType.create(DSLType.VARCHAR))
                .build();


        @Override
        public List<ColumnDef> resolve(Class<?> pojoClass) {
            return List.of(id, userName);
        }
    }

}


