package cc.allio.uno.core.util.comparator;

import java.util.Comparator;

/**
 * Byte
 *
 * @author j.x
 * @since 1.0
 */
public class ByteComparator implements Comparator<Byte> {
    @Override
    public int compare(Byte o1, Byte o2) {
        return Byte.compare(o1, o2);
    }
}
