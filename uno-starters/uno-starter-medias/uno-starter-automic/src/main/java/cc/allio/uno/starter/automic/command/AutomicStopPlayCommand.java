package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.StopPlayCommand;
import cc.allio.uno.component.media.entity.GB28181;
import cc.allio.uno.starter.automic.AutomicHttpResponseAdapter;
import cc.allio.uno.starter.automic.AutomicMediaProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

/**
 * Automic视频平台停止播放接口:/v1/play/videoRemoveKeys
 *
 * @author jiangwei
 * @date 2022/6/16 17:32
 * @since 1.0
 */
public class AutomicStopPlayCommand implements StopPlayCommand {

    private final GB28181 gb28181;

    public AutomicStopPlayCommand(GB28181 gb28181) {
        this.gb28181 = gb28181;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        AutomicMediaProperties properties = context.getOrThrows(CommandContext.MEDIA_PROPERTY, AutomicMediaProperties.class);
        return new AutomicHttpResponseAdapter(
                HttpSwapper.build(properties.getUrl() + "/v1/play/videoRemoveKeys", HttpMethod.GET)
                        .addParameter("guid", gb28181.getBizKey())
                        .swap()
        )
                .repsInterceptor(() -> "code", code -> code == HttpStatus.OK.value())
                .flatMap(json -> Mono.empty());
    }
}
