package cc.allio.uno.rule.api.vistor;

import java.util.List;

/**
 * group node. tag for element
 *
 * @author jiangwei
 * @date 2023/4/26 12:04
 * @since 1.1.4
 */
public interface GroupElement<T extends Element> extends Element {

    /**
     * 添加子节点元素
     *
     * @param element element
     * @return true 添加成功 false 添加失败
     */
    boolean addElement(T element);

    /**
     * 添加子节点元素
     *
     * @param elements element
     * @return true 添加成功 false 添加失败
     */
    boolean addElements(List<T> elements);

    /**
     * 移除该element
     *
     * @param element element
     * @return true 成功 false 失败
     */
    boolean removeElement(T element);

    /**
     * 获取所有子节点中的{@link GroupElement}
     *
     * @return group elements
     */
    List<T> getGroupElement();

    /**
     * 获取所有子节点中的attr elements
     *
     * @return elements
     */
    List<T> getAttrElement();

    /**
     * 清除子节点中的attr elements
     */
    void clearAttrElement();
}
