package cc.allio.uno.core.metadata.endpoint.source;

import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Consumer;

/**
 * 数据源封装为Json数据
 *
 * @author jiangwei
 * @date 2022/9/27 16:52
 * @since 1.1.0
 */
public abstract class JsonSource implements Source<JsonNode> {

    /**
     * 消费数据源数据实例
     */
    private Consumer<JsonNode> consumer;

    @Override
    public void subscribe(Consumer<JsonNode> next) {
        this.consumer = next;
    }

    /**
     * 子类调用，数据进行发布
     *
     * @see #next(JsonNode)
     */
    public void next(String jsonValue) {
        next(JsonUtils.readTree(jsonValue));
    }

    /**
     * 子类调用，数据进行发布
     *
     * @param jsonValue json数据
     * @throws RuntimeException 不能进行解析为json数据时抛出的异常
     */
    public void next(JsonNode jsonValue) {
        if (consumer != null) {
            consumer.accept(jsonValue);
        }
    }
}
