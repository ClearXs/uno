package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Integer Comparator
 *
 * @author j.x
 * @since 1.0
 */
public class IntegerComparator implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        return Integer.compare(o1, o2);
    }
}
