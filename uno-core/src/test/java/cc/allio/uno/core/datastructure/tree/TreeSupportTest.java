package cc.allio.uno.core.datastructure.tree;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TreeSupportTest extends BaseTestCase {

    static List<Expand> testData = Lists.newArrayList(
            new DefaultExpand(1, 0),
            new DefaultExpand(2, 0),
            new DefaultExpand(3, 0),
            new DefaultExpand(4, 1),
            new DefaultExpand(5, 2)
    );

    @Test
    void testTreeifyAndExpand() {
        List<Element> treeify = TreeSupport.treeify(testData);

        assertEquals(3, treeify.size());

        List<Expand> expands = TreeSupport.expand(treeify);

        assertEquals(testData.size(), expands.size());
    }
}
