package cc.allio.uno.sequnetial;

import cc.allio.uno.core.type.DefaultType;
import cc.allio.uno.core.type.MemoryTypeManager;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于{@link SubscriptionProperties}内存主题
 *
 * @author j.x
 * @date 2023/4/12 11:45
 * @since 1.1.4
 */
public class SubscriptionPropertiesTypeManager extends MemoryTypeManager {

    public SubscriptionPropertiesTypeManager(SubscriptionProperties subscriptionProperties) {
        // 添加时序数据类型
        List<String> sequentialTopic = subscriptionProperties.getSequential();
        addAll(sequentialTopic.stream().map(DefaultType::of).collect(Collectors.toList()));
        // 添加自定义类型
        MultiValueMap<String, String> customize = subscriptionProperties.getCustomize();
        for (Map.Entry<String, List<String>> topicGroup : customize.entrySet()) {
            List<String> value = topicGroup.getValue();
            addAll(value.stream().map(DefaultType::of).collect(Collectors.toList()));
        }
    }
}
