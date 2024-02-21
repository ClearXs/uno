package cc.allio.uno.core.util.comparator;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;

/**
 * Object Comparator
 *
 * @author jiangwei
 * @date 2022/7/10 16:02
 * @since 1.0
 */
@Slf4j
public class ObjectComparator implements Comparator<Object> {

    private static final Comparator<Object> EQUALS_COMPARATOR = new EqualsComparator();

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return -1;
        }
        // 两者类型不一致，返回-1
        if (!o1.getClass().isAssignableFrom(o2.getClass())) {
            return -1;
        }
        return EQUALS_COMPARATOR.compare(o1, o2);
    }
}
