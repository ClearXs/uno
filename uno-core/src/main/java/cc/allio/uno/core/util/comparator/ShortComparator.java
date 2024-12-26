package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Short Comparator
 *
 * @author j.x
 * @since 1.0
 */
public class ShortComparator implements Comparator<Short> {
    @Override
    public int compare(Short o1, Short o2) {
        return Short.compare(o1, o2);
    }
}
