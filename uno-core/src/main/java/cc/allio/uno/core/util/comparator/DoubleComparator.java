package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Double Comparator
 *
 * @author jiangwei
 * @date 2022/7/10 14:59
 * @since 1.0
 */
public class DoubleComparator implements Comparator<Double> {

    @Override
    public int compare(Double o1, Double o2) {
        return o1.compareTo(o2);
    }
}
