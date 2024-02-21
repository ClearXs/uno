package cc.allio.uno.core.datastructure.tree;

import java.io.Serializable;
import java.util.Comparator;

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
            getChildren().sort((o1, o2) -> comparator.compare((T) o1, (T) o2));
        }
    }
}
