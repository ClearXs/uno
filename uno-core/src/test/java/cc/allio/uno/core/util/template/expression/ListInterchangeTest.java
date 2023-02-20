package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ListInterchangeTest extends BaseTestCase {

    @Test
    void testChangeValue() throws Throwable {
        List list = Lists.newArrayList();
        list.add("1");

        ListInterchange interchange = new ListInterchange();
        Object change = interchange.change("list[0]", list);
        assertEquals("1", change);
    }
}
