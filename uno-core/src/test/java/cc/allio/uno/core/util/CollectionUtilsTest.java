package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtilsTest extends BaseTestCase {

    @Test
    void testUnion() {
        ArrayList<String> c1 = Lists.newArrayList("1", "2", "3");
        ArrayList<String> c2 = Lists.newArrayList("1", "4");
        Collection<String> union = CollectionUtils.union(c1, c2);

        assertEquals(4, union.size());
    }

    @Test
    void testIntersection() {
        ArrayList<String> c1 = Lists.newArrayList("1", "2", "3");
        ArrayList<String> c2 = Lists.newArrayList("1", "4");

        Collection<String> intersection = CollectionUtils.intersection(c1, c2);

        assertEquals(1, intersection.size());
    }

    @Test
    void testComplement() {
        ArrayList<String> c1 = Lists.newArrayList("1", "2", "3");
        ArrayList<String> c2 = Lists.newArrayList("1", "4");

        Collection<String> complement = CollectionUtils.complement(c1, c2);

        assertEquals(2, complement.size());
    }
}
