package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Equals Comparator。<b>基于{@link Object#equals(Object)}</b>
 *
 * @author j.x
 * @since 1.0
 */
public class EqualsComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return -1;
        }
        return o1.equals(o2) ? 0 : -1;
    }
}
