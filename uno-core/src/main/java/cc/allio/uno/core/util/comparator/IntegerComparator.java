package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Integer Comparator
 *
 * @author j.x
 * @date 2022/7/10 14:57
 * @since 1.0
 */
public class IntegerComparator implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        return Integer.compare(o1, o2);
    }
}
