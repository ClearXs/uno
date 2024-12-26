package cc.allio.uno.core.util.comparator;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * BigDecimal Comparator
 *
 * @author j.x
 * @since 1.0
 */
public class BigDecimalComparator implements Comparator<BigDecimal> {
    @Override
    public int compare(BigDecimal o1, BigDecimal o2) {
        return o1.compareTo(o2);
    }
}
