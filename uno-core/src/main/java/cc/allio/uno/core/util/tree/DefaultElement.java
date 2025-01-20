package cc.allio.uno.core.util.tree;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 默认树结点
 *
 * @author j.x
 * @since 1.1.5
 */
@Getter
public class DefaultElement<T extends DefaultElement<T>> extends TraversalElement<T> {

    @Setter
    private Serializable id;

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

    /**
     * 根据指定的id获取某个子结点
     *
     * @param id id
     * @return element or null
     */
    @Override
    public T findChildren(Serializable id) {
        for (T child : children) {
            if (id.equals(child.getId())) {
                return child;
            }
            T findChild = child.findChildren(id);
            if (findChild != null) {
                return findChild;
            }
        }
        return null;
    }

    /**
     * 根据指定的id移除某个子结点
     *
     * @param id id
     * @return true if success
     */
    @Override
    public boolean removeChildren(Serializable id) {
        boolean removed = children.removeIf(child -> id.equals(child.getId()));
        if (removed) {
            return true;
        }
        // 没有移除尝试在从字节中再次移除
        for (T child : children) {
            removed = child.removeChildren(id);
            if (removed) {
                return true;
            }
        }
        return false;
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
