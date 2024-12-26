package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Float Comparator
 *
 * @author j.x
 * @since 1.0
 */
public class FloatComparator implements Comparator<Float> {
    @Override
    public int compare(Float o1, Float o2) {
        return Float.compare(o1, o2);
    }
}
