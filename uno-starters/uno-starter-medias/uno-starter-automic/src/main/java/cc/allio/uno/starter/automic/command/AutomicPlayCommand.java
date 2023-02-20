package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaBuilderFactory;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.PlayCommand;
import cc.allio.uno.component.media.entity.GB28181;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.starter.automic.AutomicHttpResponseAdapter;
import cc.allio.uno.starter.automic.AutomicMediaProperties;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * Automic视频播放：/v1/play/video_play
 *
 * @author jiangwei
 * @date 2022/6/16 17:30
 * @since 1.0
 */
public class AutomicPlayCommand implements PlayCommand {

    /**
     * 调用Automic视频平台的业务key，即是设备的code
     */
    private final GB28181 gb28181;

    public AutomicPlayCommand(GB28181 gb28181) {
        this.gb28181 = gb28181;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        AutomicMediaProperties properties = context.getOrThrows(CommandContext.MEDIA_PROPERTY, AutomicMediaProperties.class);
        HttpSwapper swapper = HttpSwapper.build(properties.getUrl() + "/v1/play/video_play", HttpMethod.GET)
                .addParameter("guid", gb28181.getBizKey());
        AutomicBaseRequest baseRequest = new AutomicBaseRequest(properties);
        return new AutomicHttpResponseAdapter(
                baseRequest.authSwapper(swapper)
                        .flatMap(HttpSwapper::swap)
        )
                .repsInterceptor(() -> "status", code -> code == 1)
                .flatMap(json -> {
                    JsonNodeEnhancer data = json.getNode("data");
                    Media media = MediaBuilderFactory.createMediaBuilder().buildByJsonNode(data, properties);
                    context.putAttribute(PLAY_DATA, media);
                    return Mono.empty();
                });
    }
}
