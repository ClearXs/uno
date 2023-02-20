package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Equals Comparator
 *
 * @author jiangwei
 * @date 2022/7/10 16:16
 * @since 1.0
 */
public class EqualsComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        return o1.equals(o2) ? 0 : -1;
    }
}
