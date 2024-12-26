package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * String Comparator，相等 = 1、不相等 = -1
 *
 * @author j.x
 * @since 1.0
 */
public class StringComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.equals(o2) ? 0 : -1;
    }
}
