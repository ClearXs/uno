package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Long Comparator
 *
 * @author jiangwei
 * @date 2022/7/10 15:58
 * @since 1.0
 */
public class LongComparator implements Comparator<Long> {
    @Override
    public int compare(Long o1, Long o2) {
        return Long.compare(o1, o2);
    }
}
