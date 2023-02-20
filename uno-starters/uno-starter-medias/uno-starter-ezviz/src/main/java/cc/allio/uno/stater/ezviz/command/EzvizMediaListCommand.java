package cc.allio.uno.stater.ezviz.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaBuilderFactory;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.command.MediaListCommand;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.stater.ezviz.EzvizMediaProperties;
import cc.allio.uno.stater.ezviz.EzvizAccessToken;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heitianzhen
 * @date 2022/4/19 11:25
 */
public class EzvizMediaListCommand implements MediaListCommand {

    // 分页页码
    private int start;

    // 分页数量
    private int count;

    public EzvizMediaListCommand(Integer start, Integer count) {
        this.start = start;
        this.count = count;
    }

    @Autowired
    private EzvizAccessToken ezvizAccessToken;


    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        return getAllMediaFromEzviz(context)
                .collectList()
                .switchIfEmpty(Mono.just(Collections.emptyList()))
                .flatMap(medias -> {
                    context.putAttribute(MEDIA_LIST_TAG, medias);
                    return Mono.empty();
                });
    }

    /**
     * 获取所有设备的直播流地址
     *
     * @param context
     * @return
     */
    public Flux<Media> getAllMediaFromEzviz(CommandContext context) {
        EzvizMediaProperties property = (EzvizMediaProperties) context.getOrThrows("MEDIA_PROPERTY", MediaProperty.class);
        String accessToken = ezvizAccessToken.getAccessTokenFromRedis(context);
        List<Media> allMedias = new ArrayList<>();
        AtomicInteger total = new AtomicInteger(0);
        // 当总条数大于当前（页码+1）* 页码数量 说明已经获取完所有的数据
        while (total.get() <= (start + 1) * count) {
            new EzvizHttpResponseAdapter(
                    HttpSwapper.build(property.getObtainPlayUrl(), HttpMethod.POST)
                            .addParameter("accessToken", accessToken)
                            .addParameter("pageStart", String.valueOf(start))
                            .addParameter("pageSize", String.valueOf(count))
                            .swap())
                    .testResponse()
                    .flatMapMany(node -> {
                        total.set(node.get("page").get("total").intValue());
                        Iterator<JsonNode> mediaData = node.get("data").elements();
                        while (mediaData.hasNext()) {
                            allMedias.add(MediaBuilderFactory.createMediaBuilder().buildByJsonNode(new JsonNodeEnhancer(mediaData.next()), property));
                        }
                        return Flux.fromIterable(Collections.emptyList());
                    });
        }
        return Flux.fromIterable(allMedias);
    }
}
