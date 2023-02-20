package cc.allio.uno.stater.srs.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaBuilderFactory;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.command.MediaCommand;
import cc.allio.uno.stater.srs.SrsHttpResponseAdapter;
import cc.allio.uno.stater.srs.SrsMediaProperty;
import reactor.core.publisher.Mono;

/**
 * rs流媒体Media对象指令
 *
 * @author jiangwei
 * @date 2022/3/30 23:30
 * @since 1.0.6
 */
public class SrsMediaCommand implements MediaCommand {

    private final String clientId;

    /**
     * 实例Srs播放指令
     *
     * @param clientId 请求Srs多媒体API参数
     */
    public SrsMediaCommand(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        MediaProperty property = context.getOrThrows(CommandContext.MEDIA_PROPERTY, MediaProperty.class);
        SrsMediaProperty srsProperty = (SrsMediaProperty) property;
        return new SrsHttpResponseAdapter(
                HttpSwapper.build(srsProperty.getMediaUrl().concat("/api/v1/clients/{clientId}"))
                        .addParameter("clientId", clientId)
                        .swap()
        )
                .repsInterceptor(() -> "code", code -> code == 0)
                .map(node -> MediaBuilderFactory.createMediaBuilder().buildByJsonNode(node, property))
                .flatMap(media -> {
                    context.putAttribute(MediaCommand.MEDIA_TAG, media);
                    return Mono.empty();
                });
    }
}
