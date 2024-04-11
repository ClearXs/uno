package cc.allio.uno.core.datastructure.tree;

import java.io.Serializable;
import java.util.Comparator;

/**
 * comparable tree element base on {@link Comparator}
 *
 * @author j.x
 * @date 2023/4/11 22:21
 * @since 1.1.4
 */
public class ComparableElement<T extends ComparableElement<T>> extends DefaultElement<T> {

    private final Comparator<T> comparator;

    public ComparableElement(Serializable id, Comparator<T> comparator) {
        super(id);
        this.comparator = comparator;
    }

    @Override
    public void addChildren(T element) {
        super.addChildren(element);
        if (comparator != null) {
            getChildren().sort(comparator);
        }
    }

    @Override
    public Element obtainSentinel() {
        return new ComparableElement(-1, comparator);
    }
}
