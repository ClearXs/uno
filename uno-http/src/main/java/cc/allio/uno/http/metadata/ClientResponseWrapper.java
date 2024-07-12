package cc.allio.uno.http.metadata;

import cc.allio.uno.core.serializer.SerializerHolder;
import io.netty.buffer.Unpooled;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * WebFlux{@link ClientResponse}的包装对象
 *
 * @author jw
 * @date 2021/12/7 14:29
 */
@Slf4j
public class ClientResponseWrapper implements HttpResponseMetadata {

    private final ClientResponse response;

    private final Supplier<HttpRequestMetadata> supplier;

    public ClientResponseWrapper(ClientResponse response, Supplier<HttpRequestMetadata> supplier) {
        this.response = response;
        this.supplier = supplier;
    }

    @Override
    public HttpMethod getMethod() {
        return supplier.get().getMethod();
    }

    @Override
    public String getUrl() {
        return supplier.get().getUrl();
    }

    @Override
    public HttpHeaderMetadata getHttpHeaderMetadata() {
        HttpHeaderMetadata httpHeaderMetadata = new HttpHeaderMetadata();
        httpHeaderMetadata.addHeaders(
                response
                        .headers()
                        .asHttpHeaders()
                        .entrySet()
                        .stream()
                        .map(header -> {
                            HttpHeader httpHeader = new HttpHeader();
                            httpHeader.setName(header.getKey());
                            httpHeader.setValues(header.getValue().toArray(new String[0]));
                            return httpHeader;
                        })
                        .collect(Collectors.toList()));
        return httpHeaderMetadata;
    }

    @Override
    public MediaType getMediaType() {
        return response.headers().contentType().orElseGet(null);
    }

    @Override
    public HttpConfigurationMetadata getConfiguration() {
        return supplier.get().getConfiguration();
    }

    @Override
    public Class<?> expectType() {
        return supplier.get().expectType();
    }

    @Override
    public HttpStatus getStatus() {
        return response.statusCode();
    }

    @Override
    public <T> Mono<T> toExpect(Class<T> expect) {
        if (getStatus().value() != HttpStatus.OK.value()) {
            throw WebClientResponseException.create(
                    getStatus().value(),
                    getStatus().name(),
                    response.headers().asHttpHeaders(),
                    new byte[0],
                    null,
                    new HttpRequest() {
                        @Override
                        public String getMethodValue() {
                            return ClientResponseWrapper.this.getMethod().name();
                        }

                        @Override
                        public URI getURI() {
                            try {
                                return new URI(ClientResponseWrapper.this.getUrl());
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public HttpHeaders getHeaders() {
                            return ClientResponseWrapper.this.response.headers().asHttpHeaders();
                        }
                    }
            );
        }
        return response
                .bodyToMono(ByteBuffer.class)
                .map(Unpooled::wrappedBuffer)
                .defaultIfEmpty(Unpooled.EMPTY_BUFFER)
                .map(payload -> {
                    byte[] bytes = new byte[payload.readableBytes()];
                    payload.readBytes(bytes);
                    T res = SerializerHolder.holder().get().deserialize(bytes, expect);
                    // 日志记录
                    log.info("\n Request url: {}\n Request param: {}\n Request Headers: {}\nRequest Body {}\n Response data: {}",
                            getUrl(),
                            supplier.get().getParameters(),
                            supplier.get().getHttpHeaderMetadata(),
                            supplier.get().getBody(),
                            res.toString());
                    return res;
                });
    }
}
