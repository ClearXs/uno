package cc.allio.uno.component.http.metadata.body;

import cc.allio.uno.component.http.metadata.HttpRequestMetadata;
import cc.allio.uno.core.serializer.Serializer;
import cc.allio.uno.core.serializer.SerializerHolder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

/**
 * MediaType = application/json时
 *
 * @author jiangwei
 * @date 2022/10/19 15:28
 * @since 1.1.0
 */
public class JsonHttpRequestBody implements HttpRequestBody {

    @Override
    public BodyInserter<?, ? super ClientHttpRequest> getBody(HttpRequestMetadata requestMetadata) {
        Object body = requestMetadata.getBody();
        DataBuffer dataBuffer;
        if (body instanceof String) {
            dataBuffer = BUFFER_FACTORY.wrap(((String) body).getBytes());
        } else {
            Serializer serializer = SerializerHolder.holder().get();
            dataBuffer = BUFFER_FACTORY.wrap(serializer.serialize(body));
        }
        return BodyInserters.fromDataBuffers(Mono.justOrEmpty(dataBuffer));
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.APPLICATION_JSON;
    }
}
