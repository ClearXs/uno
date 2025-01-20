package cc.allio.uno.core.bus;

import cc.allio.uno.core.util.id.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订阅信息
 *
 * @author j.x
 */
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Subscription implements Serializable {

    /**
     * 唯一id
     */
    private Long id;

    /**
     * 订阅的主题
     */
    private String path;


    public static Subscription of() {
        return of(IdGenerator.defaultGenerator().getNextId(), "");
    }

    public static Subscription of(String topic) {
        return of(IdGenerator.defaultGenerator().getNextId(), topic);
    }

    public static Subscription of(Long id) {
        return of(id, "");
    }

    public static Subscription of(Long id, String topic) {
        return new Subscription(id, topic);
    }

    /**
     * topics转换为{@link Subscription}实例List对象
     *
     * @param topics 主题
     * @return 实例对象
     */
    public static List<Subscription> ofList(List<String> topics) {
        if (ObjectUtils.isEmpty(topics)) {
            return Collections.emptyList();
        }
        return topics.stream()
                .map(topic -> of((long) topic.hashCode(), topic))
                .toList();
    }

    /**
     * 构建订阅信息
     *
     * @param customize 多值Map，从配置文件中获取
     * @param except    期望获取的订阅分组key
     * @param affix     附加于Topic上
     * @return empty或者存在的订阅信息
     */
    public static List<Subscription> buildSubscriptionByCustomizeProperties(MultiValueMap<String, String> customize, @NonNull String except, @NonNull String affix) {
        return Optional.ofNullable(customize.get(except))
                .orElse(Collections.emptyList())
                .stream()
                .map(topic -> of((long) topic.concat(affix).hashCode(), topic.concat(affix)))
                .toList();
    }
}
