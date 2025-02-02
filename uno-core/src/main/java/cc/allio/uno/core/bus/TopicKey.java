package cc.allio.uno.core.bus;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.bean.BeanWrapper;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;

/**
 * topic key. the of all specific path will be transforms to slash topic path. whatever is dot path or underscore path.
 * <p>
 * examples:
 * <ol>
 *     <li>cc.allio.xx -> cc/allio/xx</li>
 *     <li>cc_allio_xx -> cc/allio/xx</li>
 * </ol>
 *
 * @author j.x
 * @see PathwayStrategy
 * @since 1.1.4
 */
public interface TopicKey extends Self<TopicKey>, Serializable {

    /**
     * 获取topic的路径
     *
     * @return /test/xx
     */
    String getPath();

    /**
     * @see #append(String)
     */
    default TopicKey append(Integer path) {
        return append(String.valueOf(path));
    }

    /**
     * @see #append(String)
     */
    default TopicKey append(Short path) {
        return append(String.valueOf(path));
    }

    /**
     * @see #append(String)
     */
    default TopicKey append(Float path) {
        return append(String.valueOf(path));
    }

    /**
     * @see #append(String)
     */
    default TopicKey append(Double path) {
        return append(String.valueOf(path));
    }

    /**
     * @see #append(String)
     */
    default TopicKey append(Character path) {
        return append(String.valueOf(path));
    }

    /**
     * @see #append(String)
     */
    default TopicKey append(Boolean path) {
        return append(String.valueOf(path));
    }

    /**
     * @see #append(String)
     */
    default TopicKey append(Long path) {
        return append(String.valueOf(path));
    }

    /**
     * append path to current path
     *
     * @param path the other path.
     * @return self of {@link TopicKey}
     */
    default TopicKey append(String path) {
        return append(TopicKey.of(path));
    }

    /**
     * append other topic {@link TopicKey} to current path
     *
     * @param otherTopic the other topic
     * @return self of {@link TopicKey}
     */
    TopicKey append(TopicKey otherTopic);

    /**
     * 主题路径化
     *
     * @return 路径策略实例
     */
    static String pathway(String path) {
        return PathwayStrategy.require(path)
                .transformTo(PathwayStrategy.SLASH)
                .apply(path);
    }

    /**
     * 根据某一个class对象的包名创建cc/allio/xx的主题路径
     *
     * @param clazz clazz
     * @return cc/allio/xx
     */
    static TopicKey of(Class<?> clazz) {
        return of(clazz, new String[]{});
    }

    /**
     * 根据某一个class对象的包名创建cc/allio/xx的主题路径 然后追加于后续给定的值。
     *
     * @param clazz   clazz
     * @param appends appends
     * @return cc/allio/xx appends = ['1', '2'] = cc/allio/xx/1/2
     */
    static TopicKey of(Class<?> clazz, String[] appends) {
        String topicName = clazz.getName();
        // 转小写
        String underlineTopic = StringUtils.camelToUnderline(topicName);
        String classTopicPath = PathwayStrategy.DOT.transform().apply(underlineTopic);
        if (ObjectUtils.isEmpty(appends)) {
            return of(classTopicPath);
        }
        String thenAppender = Arrays.stream(appends).reduce(classTopicPath, (o, n) -> o + StringPool.SLASH + n);
        return of(thenAppender);
    }

    /**
     * 根据前缀以及pojo对象创建{@link TopicKey}
     *
     * @param prefix cc/allio/
     * @param pojo   给定pojo对象，按照其字段顺序取出值，作为追加的值
     * @return TopicKey
     */
    static TopicKey of(String prefix, Object pojo) {
        BeanWrapper wrapper = new BeanWrapper(pojo);
        return of(prefix, wrapper.findMapValuesForce().values().stream().map(Object::toString).toArray(String[]::new));
    }

    /**
     * 根据前缀以及追加值创建主题路径
     *
     * @param prefix  cc/allio/
     * @param appends /1/2
     * @return TopicKey
     */
    static TopicKey of(String prefix, String[] appends) {
        // 去除cc/allio/最后一个'/'符号
        if (prefix.endsWith(StringPool.SLASH)) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        String thenAppend = Arrays.stream(appends).reduce(prefix, (o, n) -> o + StringPool.SLASH + n);
        return of(thenAppend);
    }

    /**
     * 根据指定的路径创建{@link TopicKey}
     *
     * @param path 路径 可以是cc/allio/xx 也可以是 xxx-xxx-xx
     * @return TopicKey
     */
    static TopicKey of(String path) {
        return new DefaultTopicKey(path);
    }

    @Getter
    @ToString(of = "path")
    @EqualsAndHashCode(of = "path")
    class DefaultTopicKey implements TopicKey {

        private String path;

        public DefaultTopicKey(String path) {
            this.path = PathwayStrategy.SLASH.transform(path);
        }

        @Override
        public TopicKey append(TopicKey otherTopic) {
            String newPath = this.path + otherTopic.getPath();
            this.path = PathwayStrategy.SLASH.transform(newPath);
            return self();
        }
    }
}
