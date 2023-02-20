package cc.allio.uno.stater.srs;

import cc.allio.uno.component.http.metadata.HttpResponseAdapter;
import cc.allio.uno.component.http.metadata.HttpResponseMetadata;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.core.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 适配Srs响应
 *
 * @author jiangwei
 * @date 2022/3/31 11:11
 * @since 1.0.6
 */
public class SrsHttpResponseAdapter extends HttpResponseAdapter<Integer> {

    public SrsHttpResponseAdapter(Mono<HttpResponseMetadata> response) {
        super(response);
    }

    @Override
    public Mono<JsonNodeEnhancer> repsInterceptor(Supplier<String> fetchCode, Predicate<Integer> testing) {
        return getResponse().flatMap(HttpResponseMetadata::expectString)
                .flatMap(resToString -> {
                    try {
                        JsonNode node = JsonUtil.readTree(resToString);
                        JsonNodeEnhancer jsonNodeEnhancer = new JsonNodeEnhancer(node);
                        Integer code = jsonNodeEnhancer.asInteger(fetchCode.get());
                        boolean testOk = testing.test(code);
                        if (!testOk) {
                            return Mono.error(new MediaException(String.format("Request Can't Get Data, res: %s", resToString)));
                        }
                        return Mono.just(jsonNodeEnhancer);
                    } catch (Throwable e) {
                        return Mono.error(new MediaException(e));
                    }
                })
                .onErrorStop()
                .cast(JsonNodeEnhancer.class);
    }
}
