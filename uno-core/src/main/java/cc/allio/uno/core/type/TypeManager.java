package cc.allio.uno.core.type;

import org.springframework.core.Ordered;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时序数据的类型管理器，针对不同类型的数据，不同类型的来源管理并查询
 *
 * @author j.x
 * @date 2023/4/11 19:52
 * @see MemoryTypeManager
 * @since 1.1.4
 */
public interface TypeManager {

    /**
     * 添加指定的类型
     *
     * @param type Type
     */
    void add(Type type);

    /**
     * 添加所有的类型
     *
     * @param types 类型
     */
    void addAll(Type... types);

    /**
     * 添加所有的类型
     *
     * @param types 类型
     */
    void addAll(List<Type> types);

    /**
     * 判断给定类型是否存在
     *
     * @param type 给定的类型
     * @return true 存在 false 不存在
     */
    boolean contains(Type type);

    /**
     * 判断给定类型是否存在
     *
     * @param code 标识
     * @return true 存在 false 不存在
     */
    boolean contains(String code);

    /**
     * 获取所有的类型
     *
     * @return list 数据
     */
    Collection<Type> findAll();

    /**
     * 根据指定的标识查找类型实例
     *
     * @param code 标识
     * @return 类型实例
     */
    Optional<Type> findOne(String code);

    /**
     * 比较给定的两个类型是否相同
     *
     * @param t1 类型1
     * @param t2 类型2
     * @return true 相同 false 不同
     */
    default boolean match(Type t1, Type t2) {
        return Matchers.match(t1, t2);
    }

    /**
     * 类型Matchers
     *
     * @author j.x
     * @date 2023/4/12 13:28
     * @since 1.1.4
     */
    class Matchers {

        public static boolean match(Type t1, Type t2) {
            TypeMatcher matcher1 = getMatcher(t1);
            TypeMatcher matcher2 = getMatcher(t2);

            if (matcher1.order() < matcher2.order()) {
                return matcher1.match(t2);
            }
            return matcher2.match(t1);
        }

        /**
         * 根据类型获取匹配器
         *
         * @param type 类型实例
         * @return true 相同 false 不同
         */
        private static TypeMatcher getMatcher(Type type) {
            if (type instanceof RegexType) {
                return new PatternTypeMatcher((RegexType) type);
            }
            return new DefaultTypeMatcher(type);
        }
    }

    /**
     * 类型匹配器定义
     *
     * @author j.x
     * @date 2023/4/12 13:14
     * @since 1.1.4
     */
    interface TypeMatcher {

        /**
         * 匹配其他的类型是否相同
         *
         * @param other 其他类型实例
         * @return true 匹配相同 false 不同
         */
        boolean match(Type other);

        /**
         * 匹配器优先级
         *
         * @return int
         */
        int order();
    }

    // ======================== 定义默认的类型匹配器 ========================

    /**
     * 简单的类型匹配器，通过{@link Object#equals(Object)}进行匹配
     *
     * @author j.x
     * @date 2023/4/12 13:18
     * @since 1.1.4
     */
    class DefaultTypeMatcher implements TypeMatcher {

        private final Type origin;

        public DefaultTypeMatcher(Type origin) {
            this.origin = origin;
        }

        @Override
        public boolean match(Type other) {
            return origin.equals(other);
        }

        @Override
        public int order() {
            return Ordered.LOWEST_PRECEDENCE;
        }
    }

    /**
     * 通配符类型匹配器
     *
     * @author j.x
     * @date 2023/4/12 13:19
     * @since 1.1.4
     */
    class PatternTypeMatcher implements TypeMatcher {

        private final RegexType regexType;

        public PatternTypeMatcher(RegexType regexType) {
            this.regexType = regexType;
        }

        @Override
        public boolean match(Type other) {
            if (other == null) {
                return false;
            }
            Pattern compile = Pattern.compile(regexType.getCode());
            Matcher matcher = compile.matcher(other.getCode());
            return matcher.matches();
        }

        @Override
        public int order() {
            return Ordered.HIGHEST_PRECEDENCE;
        }
    }
}
