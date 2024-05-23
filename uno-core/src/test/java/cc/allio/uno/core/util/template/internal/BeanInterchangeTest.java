package cc.allio.uno.core.util.template.internal;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.User;
import org.junit.jupiter.api.Test;

public class BeanInterchangeTest extends BaseTestCase {

    @Test
    void testChangeValue() throws Throwable {
        User user = new User();
        user.setName("name");
        BeanInterchange beanInterchange = new BeanInterchange();
        Object name = beanInterchange.change("name", user, false);
        assertEquals("name", name);
    }

    @Test
    void testLangValue() throws Throwable {
        User user = new User();
        user.setName("name");
        BeanInterchange beanInterchange = new BeanInterchange();
        Object name = beanInterchange.change("name", user, true);
        assertEquals("\"name\"", name);
    }
}
