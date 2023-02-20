package cc.allio.uno.stater.srs.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaBuilderFactory;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.command.MediaPageCommand;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.stater.srs.SrsHttpResponseAdapter;
import cc.allio.uno.stater.srs.SrsMediaProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于Srs流服务获取多媒体分页数据
 *
 * @author jiangwei
 * @date 2022/11/25 14:53
 * @since 1.1.2
 */
public class SrsMediaPageCommand implements MediaPageCommand {

    /**
     * 分页页码
     */
    private final int start;

    /**
     * 分页数量
     */
    private final int count;

    public SrsMediaPageCommand(Integer start, Integer count) {
        this.start = start;
        this.count = count;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        MediaProperty property = context.getOrThrows(CommandContext.MEDIA_PROPERTY, MediaProperty.class);
        SrsMediaProperty srsProperty = (SrsMediaProperty) property;
        return new SrsHttpResponseAdapter(
                HttpSwapper.build(srsProperty.getMediaUrl().concat("/api/v1/clients/"), HttpMethod.GET)
                        .addParameter("start", String.valueOf(start))
                        .addParameter("count", String.valueOf(count))
                        .swap()
        )
                .repsInterceptor(() -> "code", code -> code == 0)
                .flatMapMany(node -> {
                    List<JsonNode> clients = node.findValues("clients");
                    return Flux.fromIterable(clients)
                            .cast(ArrayNode.class)
                            .flatMap(clientNode -> {
                                Iterator<JsonNode> elements = clientNode.elements();
                                return Flux.fromIterable(Lists.newArrayList(elements).stream().map(JsonNodeEnhancer::new).collect(Collectors.toList()));
                            })
                            .map(clientNode -> MediaBuilderFactory.createMediaBuilder().buildByJsonNode(clientNode, property));
                })
                .collectList()
                .switchIfEmpty(Mono.just(Collections.emptyList()))
                .flatMap(medias -> {
                    context.putAttribute(MEDIA_PAGE_TAG, medias);
                    return Mono.empty();
                });
    }
}
