package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class SymbolEngineTest extends BaseTestCase {

    Engine engine = new SymbolEngine(StringPool.DOT);

    @Test
    void testOnlyObject() throws Throwable {
        User user = new User();
        user.setName("name");
        String name = engine.run("name", user);
        assertEquals("name", name);
    }

    /**
     * Test Case: users[0].name ...
     */
    @Test
    void testBean() throws Throwable {
        List<User> users = Lists.newArrayList();
        User user = new User();
        user.setName("0");
        users.add(user);
        User user1 = new User();
        user1.setName("1");
        users.add(user1);
        String e1 = engine.run("users[0].name", users);
        assertEquals("0", e1);
        String e2 = engine.run("users[1].name", users);
        assertEquals("1", e2);
        // throws
        assertThrows(IndexOutOfBoundsException.class, () -> engine.run("users[22].name", users));
    }

    /**
     * Test Case:
     */
    @Test
    void testMap() throws Throwable {
        Map<Object, Object> throwMap = Maps.newHashMap();
        String re = engine.run("", throwMap);
        assertEquals("", re);

        Map<String, Object> map = Maps.newHashMap();
        // map 直接取值
        map.put("name", "name");
        re = engine.run("name", map);
        assertEquals("name", re);
        // map-object取值
        User user = new User();
        user.setName("name");
        map.put("user", user);
        re = engine.run("user.name", map);
        assertEquals("name", re);
        // map-list-object取值
        List<User> users = Lists.newArrayList(user);
        map.put("users", users);
        re = engine.run("users[0].name", map);
        assertEquals("name", re);
    }

    @Test
    void testComplex() throws Throwable {
        Person person = new Person();
        person.setId("1");
        User user = new User();
        user.setName("name");
        person.setUser(user);
        // 从对象中取值
        String re = engine.run("id", person);
        assertEquals("1", re);

        re = engine.run("user.name", person);
        assertEquals("name", re);

        Level level = new Level();
        level.setPerson(person);
        re = engine.run("person.user.name", level);
        assertEquals("name", re);

        Map<String, Object> map = Maps.newHashMap();
        map.put("person", person);
        level.setMap(map);
        // path: object->map->object->object->properties
        re = engine.run("map.person.user.name", level);
        assertEquals("name", re);

        List list = Lists.newArrayList();
        list.add(map);
        level.setList(list);

        // path: object->list-map-object-object-properties
        re = engine.run("list[0].person.user.name", level);
        assertEquals("name", re);
    }

    @Data
    public static class Person {
        private String id;
        private User user;
        private List list;
    }

    @Data
    public static class Level {
        private Person person;
        private Map<String, Object> map;
        private List list;
    }

}
