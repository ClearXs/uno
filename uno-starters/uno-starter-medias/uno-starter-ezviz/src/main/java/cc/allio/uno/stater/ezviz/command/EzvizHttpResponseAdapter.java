package cc.allio.uno.stater.ezviz.command;

import cc.allio.uno.component.http.metadata.HttpResponseMetadata;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.core.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

/**
 * @author heitianzhen
 * @date 2022/4/15 10:47
 */
public class EzvizHttpResponseAdapter {

    private Mono<HttpResponseMetadata> response;

    public EzvizHttpResponseAdapter(Mono<HttpResponseMetadata> response) {
        this.response = response;
    }

    /**
     * 验证响应是否成功
     *
     * @return 解析ezviz的json数据
     */
    public Mono<JsonNode> testResponse() {
        return response.flatMap(HttpResponseMetadata::expectString)
                .flatMap(resToString -> {
                    try {
                        JsonNode node = JsonUtil.readTree(resToString);
                        int code = node.get("code").intValue();
                        if (code != 200) {
                            return Mono.error(new MediaException(String.format("Request Can't Get Data, res: %s", resToString)));
                        }
                        return Mono.just(node);
                    } catch (Throwable e) {
                        return Mono.error(new MediaException(e));
                    }
                })
                .onErrorStop()
                .cast(JsonNode.class);
    }
}
