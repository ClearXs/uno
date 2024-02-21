package cc.allio.uno.core.datastructure.tree;

import cc.allio.uno.core.BaseTestCase;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class TreeSupportTest extends BaseTestCase {

    List<Expand> testData = Lists.newArrayList(
            new DefaultExpand(1, 0),
            new DefaultExpand(3, 0),
            new DefaultExpand(2, 0),
            new DefaultExpand(4, 1),
            new DefaultExpand(6, 2),
            new DefaultExpand(5, 2)
    );

    @Test
    void testTreeifyAndExpand() {
        List<DefaultElement> treeify = TreeSupport.treeify(testData);

        assertEquals(3, treeify.size());

        List<DefaultExpand> expands = TreeSupport.expand(Lists.newArrayList(treeify));

        assertEquals(testData.size(), expands.size());
    }

    @Test
    void testComparatorTreeify() {
        List<Role> treeify =
                TreeSupport.treeify(
                        testData,
                        expand -> new Role(expand.getId(), Comparator.comparingInt(Role::getSort))
                );
        assertEquals(3, treeify.size());
        Role element = treeify.get(1);
        assertNotNull(element);
        Role sort = element.getChildren().get(0);
        assertNotNull(sort);
        assertEquals(5, sort.getId());
    }


    @Getter
    public static class Role extends ComparableElement<Role> {

        private final int sort;

        public Role(@NonNull Serializable id, Comparator<Role> comparator) {
            super(id, comparator);
            this.sort = (int) id;
        }
    }
}
