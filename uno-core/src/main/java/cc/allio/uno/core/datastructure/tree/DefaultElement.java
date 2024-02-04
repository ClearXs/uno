package cc.allio.uno.core.datastructure.tree;

import com.google.common.collect.Lists;
import lombok.Getter;
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
public class DefaultElement<T extends DefaultElement<T>> extends TraversalElement<T> {

    private final Serializable id;

    @Getter
    private Serializable parentId;
    @Setter
    private int depth;

    @Setter
    private T parent;

    private List<T> children;

    public DefaultElement(Serializable id) {
        this.id = id;
        this.children = Lists.newArrayList();
    }

    public DefaultElement(Serializable id, Integer depth) {
        this.id = id;
        this.depth = depth;
        this.children = Lists.newArrayList();
    }

    public DefaultElement(Serializable id, Serializable parentId) {
        this.id = id;
        this.parentId = parentId;
        this.children = Lists.newArrayList();
    }

    public DefaultElement(Serializable id, Serializable parentId, int depth) {
        this.id = id;
        this.parentId = parentId;
        this.depth = depth;
        this.children = Lists.newArrayList();
    }

    @Override
    public void setParentId(Serializable parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public void setChildren(List<T> children) {
        this.children = children;
    }

    @Override
    public List<T> getChildren() {
        return children;
    }

    @Override
    public void addChildren(T element) {
        this.children.add(element);
    }

    @Override
    public void clearChildren() {
        children.clear();
    }

}
