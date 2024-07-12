package cc.allio.uno.core.type;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

class TypeValueTest extends BaseTestCase {

    @Test
    void testArray() throws NoSuchFieldException {
        Field enums = TypeSet.class.getDeclaredField("enums");

        Object o1 = new TypeValue(enums.getType(), "A").tryConvert();
        assertTrue(o1.getClass().isArray());

        Object o2 = new TypeValue(enums.getType(), "['A','B']").tryConvert();
        assertTrue(o2.getClass().isArray());

        Object o3 = new TypeValue(enums.getType(), Lists.newArrayList(ENUM.A, ENUM.B)).tryConvert();
        assertTrue(o3.getClass().isArray());
    }

    @Test
    void testList() throws NoSuchFieldException {
        Field enums = TypeSet.class.getDeclaredField("enumsList");
        Object o1 = new TypeValue(enums.getType(), Lists.newArrayList(ENUM.A, ENUM.B)).tryConvert();
        assertTrue(List.class.isAssignableFrom(o1.getClass()));

        Field sets = TypeSet.class.getDeclaredField("enumsSets");
        Object o2 = new TypeValue(sets.getType(), Lists.newArrayList(ENUM.A, ENUM.B)).tryConvert();
        assertTrue(Set.class.isAssignableFrom(o2.getClass()));

        Field longList = TypeSet.class.getDeclaredField("longList");
        Object o3 = new TypeValue(longList.getType(), Lists.newArrayList("1", "2")).tryConvert();
        assertTrue(List.class.isAssignableFrom(o3.getClass()));
    }

    @Test
    void testMap() throws NoSuchFieldException {
        Field enums = TypeSet.class.getDeclaredField("map");
        Object o1 = new TypeValue(enums.getType(), "{'k':'A'}").tryConvert();
        assertTrue(Map.class.isAssignableFrom(o1.getClass()));
    }

    @Test
    void testCompositeType() {

        Map<String, Map<String, Object>> value = Maps.newHashMap();
        Map<String, Object> kv = Maps.newHashMap();
        kv.put("name", "n");
        value.put("children", kv);

        Object o = new TypeValue(Parent.class, value).tryConvert();
        assertNotNull(o);
        assertEquals(Parent.class, o.getClass());
        Children children = ((Parent) o).getChildren();
        assertNotNull(children);

        assertEquals("n", children.getName());
    }

    @Test
    void testEnumType() {
        Object success = new TypeValue(Status.class, "SUCCESS").tryConvert();

        assertNotNull(success);
        assertEquals(Status.SUCCESS, success);
    }

    public static class TypeSet {
        ENUM[] enums;
        List<ENUM> enumsList;
        Set<ENUM> enumsSets;

        Map<String, ENUM> map;

        List<Long> longList;
    }

    public enum ENUM {
        A, B
    }

    @Data
    public static class Children {
        private String name;
    }

    @Data
    public static class Parent {
        private Children children;
    }


    @Getter
    @AllArgsConstructor
    public enum Status {
        SUCCESS("SUCCESS"),
        FAILED("FAILED");

        private final String value;
    }
}
