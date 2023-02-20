package cc.allio.uno.core.util.comparator;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;

/**
 * Object Comparator
 *
 * @author jiangwei
 * @date 2022/7/10 16:02
 * @since 1.0
 */
@Slf4j
public class ObjectComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        // 两者类型不一致，返回-1
        if (!o1.getClass().isAssignableFrom(o2.getClass())) {
            return -1;
        }
        return new EqualsComparator().compare(o1, o2);
    }
}
