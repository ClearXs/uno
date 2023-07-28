package cc.allio.uno.core.type;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeValueTest extends BaseTestCase {

    @Test
    void testArray() throws NoSuchFieldException {
        Field enums = User.class.getDeclaredField("enums");

        Object o1 = new TypeValue(enums.getType(), "A").tryTransfer();
        assertTrue(o1.getClass().isArray());

        Object o2 = new TypeValue(enums.getType(), "['A','B']").tryTransfer();
        assertTrue(o2.getClass().isArray());

        Object o3 = new TypeValue(enums.getType(), Lists.newArrayList(ENUM.A, ENUM.B)).tryTransfer();
        assertTrue(o3.getClass().isArray());
    }

    @Test
    void testList() throws NoSuchFieldException {
        Field enums = User.class.getDeclaredField("enumsList");
        Object o1 = new TypeValue(enums.getType(), Lists.newArrayList(ENUM.A, ENUM.B)).tryTransfer();
        assertTrue(List.class.isAssignableFrom(o1.getClass()));

        Field sets = User.class.getDeclaredField("enumsSets");
        Object o2 = new TypeValue(sets.getType(), Lists.newArrayList(ENUM.A, ENUM.B)).tryTransfer();
        assertTrue(Set.class.isAssignableFrom(o2.getClass()));
    }

    @Test
    void testMap() throws NoSuchFieldException {
        Field enums = User.class.getDeclaredField("map");
        Object o1 = new TypeValue(enums.getType(), "{'k':'A'}").tryTransfer();
        assertTrue(Map.class.isAssignableFrom(o1.getClass()));
    }

    public class User {
        ENUM[] enums;
        List<ENUM> enumsList;
        Set<ENUM> enumsSets;

        Map<String, ENUM> map;
    }


    public enum ENUM {
        A, B
    }
}
