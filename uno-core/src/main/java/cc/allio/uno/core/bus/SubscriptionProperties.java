package cc.allio.uno.core.bus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于Spring的订阅信息配置文件
 * <ol>
 *     <li>提供消息主题配置</li>
 * </ol>
 *
 * @author jiangwei
 * @date 2022/2/7 16:14
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "allio.uno.bus")
public class SubscriptionProperties {

    /**
     * 时序数据消息主题
     */
    private List<String> sequential = new ArrayList<>();

    /**
     * 自定义消息主题
     *
     * @key 分组key
     * @value 多值的消息主题
     */
    private MultiValueMap<String, String> customize = new LinkedMultiValueMap<>();
}
