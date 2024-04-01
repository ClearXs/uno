package cc.allio.uno.core.datastructure.tree;

import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 提供树相关操作
 *
 * @author j.x
 * @date 2023/11/9 11:34
 * @since 1.1.5
 */
public final class TreeSupport {

    private TreeSupport() {
    }

    /**
     * @see #treeify(List, Function)
     */
    public static <T extends Expand> List<DefaultElement> treeify(List<T> expandTrees) {
        return treeify(expandTrees, e -> new DefaultElement<>(e.getId()));
    }

    /**
     * 把平展的树转换为具有层次性的树。
     *
     * @param expandTrees 平展的树
     * @param treeFunc    平展结构转换为树结构
     * @param <T>         继承于{@link Expand}的泛型
     * @param <R>         继承于{@link Element}的泛型
     * @return hierarchy filter expand tree depth == 0的结点
     */
    public static synchronized <T extends Expand, R extends Element<R>> List<R> treeify(List<T> expandTrees, Function<T, R> treeFunc) {
        if (CollectionUtils.isEmpty(expandTrees)) {
            return Collections.emptyList();
        }
        List<R> expands =
                expandTrees.stream()
                        .map(expand -> {
                            R element = treeFunc.apply(expand);
                            if (element.getParentId() == null) {
                                element.setParentId(expand.getParentId());
                            }
                            if (element.getId() == null) {
                                element.setId(expand.getId());
                            }
                            return element;
                        })
                        .toList();
        return adjust(expands);
    }

    /**
     * 对给定的Tree Element进行调整，使得可以进行树化
     * <p>算法明细如下：</p>
     * <ol>
     *     <li>构建已{@link Element#getId()}为key的散列表结构</li>
     *     <li>循环{@link Element}列表,每一次循环根据散列表结构找到对应父与当前{@link Element}的结构,然后进行添加</li>
     * </ol>
     * <p>时间复杂度将会是O(n)</p>
     *
     * @param elements elements
     * @param <R>      Tree Element
     * @return 调整完成的树
     */
    public static <R extends Element<R>> List<R> adjust(List<R> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return elements;
        }
        // transfer expand id must not null
        Map<Serializable, R> idElement = elements.stream().collect(Collectors.toMap(Element::getId, e -> e));

        // 已散列表为基础循环设置添加子结点
        for (R e : elements) {
            Serializable parentId = e.getParentId();
            R parent = idElement.get(parentId);
            if (parent != null) {
                Serializable id = e.getId();
                R children = idElement.get(id);
                children.setDepth(parent.getDepth() + 1);
                parent.addChildren(children);
            }
        }

        Element sentinel = Element.getRootSentinel();
        for (R virtual : idElement.values()) {
            if (virtual.getDepth() == Element.ROOT_NODE) {
                // 触发Element添加结点的特性，如排序
                sentinel.addChildren(virtual);
            }
        }

        return sentinel.getChildren();
    }

    /**
     * @see #expand(List, Function, Comparator)
     */
    public static <R extends Element<R>> List<DefaultExpand> expand(List<R> forest) {
        return expand(
                forest,
                r -> new DefaultExpand(r.getId(), r.getParent() != null ? r.getParent().getId() : null),
                null);
    }

    /**
     * 把树结构进行平展化。
     * <p>采取树访问者模式</p>
     *
     * @param forest     树
     * @param expandFunc 树结构展缓为平展结构
     * @param comparator 用于平展结构的排序，可以为null
     * @param <T>        继承于{@link Expand}的泛型
     * @param <R>        继承于{@link Element}的泛型
     * @return expand
     */
    public static synchronized <T extends Expand, R extends Element<R>> List<T> expand(List<R> forest, Function<R, T> expandFunc, Comparator<T> comparator) {
        List<T> expands = Lists.newArrayList();
        try {
            Element.ROOT_SENTINEL.setChildren(Lists.newArrayList(forest));
            Element.ROOT_SENTINEL.accept(e -> {
                // 忽略哨兵结点
                if (!Element.ROOT_SENTINEL.equals(e)) {
                    expands.add(expandFunc.apply((R) e));
                }
            });
        } finally {
            Element.ROOT_SENTINEL.clearChildren();
        }
        if (comparator != null) {
            expands.sort(comparator);
        }
        return expands;
    }
}
