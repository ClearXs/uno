package cc.allio.uno.http.metadata.body;

import cc.allio.uno.http.metadata.HttpRequestMetadata;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * HTTP请求Body，包装{@link BodyInserters}
 *
 * @author j.x
 * @date 2022/10/19 15:25
 * @since 1.1.0
 */
public interface HttpRequestBody {

    /**
     * byte buffers创建工厂
     */
    DataBufferFactory BUFFER_FACTORY = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));

    /**
     * 获取Body
     *
     * @return {@link BodyInserters}实例
     */
    BodyInserter<?, ? super ClientHttpRequest> getBody(HttpRequestMetadata requestMetadata);

    /**
     * 获取Body对应的MediaType
     *
     * @return
     */
    MediaType getMediaType();
}
