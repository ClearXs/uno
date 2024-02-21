package cc.allio.uno.core.util.comparator;

import cc.allio.uno.core.function.lambda.MethodBiFunction;

import java.util.Collection;
import java.util.Comparator;

/**
 * Collection Comparator
 * <p>该方法将比较如下事项：</p>
 * <ul>
 *     <li>两个集合元素数量是否相等，不相等则返回</li>
 *     <li>以集合o1为基准，遍历它，通过{@link #finder}寻找在集合o2中的元素，如果o2元素存在则进行下一步，否则返回-1（说明两者不一致）</li>
 *     <li>基于{@link ObjectComparator}进行比较获取最后结果</li>
 * </ul>
 *
 * @author jiangwei
 * @date 2022/7/10 16:01
 * @since 1.0
 */
public class CollectionComparator<T> implements Comparator<Collection<T>> {

    private final MethodBiFunction<T, Collection<T>, T> finder;

    public CollectionComparator() {
        this.finder = (bench, comparable) -> comparable.stream().filter(bench::equals).findFirst().orElse(null);
    }

    /**
     * 参数包含三个范型参数：
     * <ol>
     *     <li>用于比较的基准元素</li>
     *     <li>用于比较的集合</li>
     *     <li>从比较集合中查找出来的元素</li>
     * </ol>
     *
     * @param finder 元素基准记录寻找器
     */
    public CollectionComparator(MethodBiFunction<T, Collection<T>, T> finder) {
        this.finder = finder;
    }


    @Override
    public int compare(Collection<T> c1, Collection<T> c2) {
        if (c1.size() != c2.size()) {
            return -1;
        }
        boolean compared = c1.stream()
                .anyMatch(benchElement -> {
                    T testElement = finder.apply(benchElement, c2);
                    if (testElement != null) {
                        return false;
                    }
                    int compare = Comparators.objects().compare(c1, c2);
                    return compare >= 0;
                });
        return compared ? 0 : -1;
    }
}
