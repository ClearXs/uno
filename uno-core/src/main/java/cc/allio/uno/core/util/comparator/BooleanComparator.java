package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Boolean Comparator
 *
 * @author j.x
 * @since 1.0
 */
public class BooleanComparator implements Comparator<Boolean> {
    @Override
    public int compare(Boolean o1, Boolean o2) {
        return Boolean.compare(o1, o2);
    }
}
