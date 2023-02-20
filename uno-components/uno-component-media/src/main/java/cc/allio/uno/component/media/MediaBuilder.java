package cc.allio.uno.component.media;

import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.component.media.event.Connect;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Media对象生产者，实现类基于SPI机制产生对象
 *
 * @author jiangwei
 * @date 2022/4/4 14:33
 * @since 1.0.6
 */
public interface MediaBuilder {

    /**
     * 根据{@link Connect}对象构建Media对象
     *
     * @param connect       connect对象实例
     * @param mediaProperty Media属性对象
     * @return media对象实例
     */
    Media buildByConnect(Connect connect, MediaProperty mediaProperty);

    /**
     * 根据{@link JsonNode}对象构建Media对象
     *
     * @param clientNode    某个客户端构建对象
     * @param mediaProperty Media属性对象
     * @return Media对象实例
     */
    Media buildByJsonNode(JsonNodeEnhancer clientNode, MediaProperty mediaProperty);
}
