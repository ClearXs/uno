package cc.allio.uno.core.util.comparator;

import java.util.Collection;
import java.util.Comparator;

/**
 * Collection Comparator
 *
 * @author jiangwei
 * @date 2022/7/10 16:01
 * @since 1.0
 */
public class CollectionComparator<T> implements Comparator<Collection<T>> {

    @Override
    public int compare(Collection<T> c1, Collection<T> c2) {
        return new ObjectComparator().compare(c1, c2);
    }
}
