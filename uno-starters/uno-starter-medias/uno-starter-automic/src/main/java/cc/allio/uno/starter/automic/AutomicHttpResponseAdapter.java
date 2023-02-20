package cc.allio.uno.starter.automic;

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
 * Automic视频平台Http响应适配器
 *
 * @author jiangwei
 * @date 2022/6/16 17:09
 * @since 1.0
 */
public class AutomicHttpResponseAdapter extends HttpResponseAdapter<Integer> {

    public AutomicHttpResponseAdapter(Mono<HttpResponseMetadata> response) {
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
                        boolean testOK = testing.test(code);
                        if (!testOK) {
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
