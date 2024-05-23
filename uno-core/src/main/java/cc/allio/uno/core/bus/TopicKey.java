package cc.allio.uno.core.bus;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bean.BeanWrapper;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.StringUtils;
import lombok.Getter;

import java.util.Arrays;

/**
 * topic key
 *
 * @author j.x
 * @date 2023/4/25 13:31
 * @since 1.1.4
 */
public interface TopicKey {

    /**
     * 获取订阅实例
     *
     * @return Subscription
     */
    default Subscription getSubscription() {
        return Subscription.of(getPath());
    }

    /**
     * 获取topic的路径
     *
     * @return /test/xx
     */
    String getPath();

    /**
     * 根据某一个class对象的包名创建cc/allio/xx的主题路径
     *
     * @param clazz clazz
     * @return cc/allio/xx
     */
    static TopicKey create(Class<?> clazz) {
        return create(clazz, new String[]{});
    }

    /**
     * 根据某一个class对象的包名创建cc/allio/xx的主题路径 然后追加于后续给定的值。
     *
     * @param clazz   clazz
     * @param appends appends
     * @return cc/allio/xx appends = ['1', '2'] = cc/allio/xx/1/2
     */
    static TopicKey create(Class<?> clazz, String[] appends) {
        String topicName = clazz.getName();
        // 转小写
        String underlineTopic = StringUtils.camelToUnderline(topicName);
        String classTopicPath = Topic.DotPathwayStrategy.INSTANCE.transform().apply(underlineTopic);
        if (ObjectUtils.isEmpty(appends)) {
            return create(classTopicPath);
        }
        String thenAppender = Arrays.stream(appends).reduce(classTopicPath, (o, n) -> o + StringPool.SLASH + n);
        return create(thenAppender);
    }

    /**
     * 根据前缀以及pojo对象创建{@link TopicKey}
     *
     * @param prefix cc/allio/
     * @param pojo   给定pojo对象，按照其字段顺序取出值，作为追加的值
     * @return TopicKey
     */
    static TopicKey create(String prefix, Object pojo) {
        BeanWrapper wrapper = new BeanWrapper(pojo);
        return create(prefix, wrapper.findMapValuesForce().values().stream().map(Object::toString).toArray(String[]::new));
    }

    /**
     * 根据前缀以及追加值创建主题路径
     *
     * @param prefix  cc/allio/
     * @param appends /1/2
     * @return TopicKey
     */
    static TopicKey create(String prefix, String[] appends) {
        // 去除cc/allio/最后一个'/'符号
        if (prefix.endsWith(StringPool.SLASH)) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        String thenAppender = Arrays.stream(appends).reduce(prefix, (o, n) -> o + StringPool.SLASH + n);
        return create(thenAppender);
    }

    /**
     * 根据指定的路径创建{@link TopicKey}
     *
     * @param path 路径 可以是cc/allio/xx 也可以是 xxx-xxx-xx
     * @return TopicKey
     */
    static TopicKey create(String path) {
        return new DefaultTopicKey(path);
    }

    class DefaultTopicKey implements TopicKey {

        @Getter
        private final String path;

        public DefaultTopicKey(String path) {
            this.path = path;
        }

    }
}
