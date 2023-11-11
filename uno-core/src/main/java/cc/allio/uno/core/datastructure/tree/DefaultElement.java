package cc.allio.uno.core.datastructure.tree;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 默认树结点
 *
 * @author j.x
 * @date 2023/11/9 11:36
 * @since 1.1.5
 */
@Getter
public class DefaultElement extends TraversalElement {

    private final Serializable id;
    @Setter
    private int depth;

    @Setter
    private Element parent;

    private final List<Element> children;

    public DefaultElement(@NonNull Serializable id) {
        this.id = id;
        this.children = Lists.newArrayList();
    }

    public DefaultElement(@NonNull Serializable id, int depth) {
        this.id = id;
        this.depth = depth;
        this.children = Lists.newArrayList();
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public <T extends Element> void setChildren(List<T> children) {
        clearChildren();
        this.children.addAll(children);
    }

    @Override
    public void addChildren(Element element) {
        children.add(element);
    }

    @Override
    public void clearChildren() {
        children.clear();
    }

}
