package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.MediaControlCommand;
import cc.allio.uno.component.media.entity.GB28181;
import cc.allio.uno.component.media.entity.MediaControl;
import cc.allio.uno.core.util.ObjectUtil;
import cc.allio.uno.starter.automic.AutomicMediaProperties;
import cc.allio.uno.starter.automic.AutomicHttpResponseAdapter;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * Automic视频控制指令
 *
 * @author jiangwei
 * @date 2022/7/16 10:44
 * @since 1.0
 */
public class AutomicMediaControlCommand implements MediaControlCommand {

    /**
     * 控制实体
     */
    private final MediaControl mediaControl;

    public AutomicMediaControlCommand(MediaControl mediaControl) {
        this.mediaControl = mediaControl;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        GB28181 gb28181 = mediaControl.getGb28181();
        if (ObjectUtil.isEmpty(gb28181)) {
            return Mono.error(new IllegalArgumentException(String.format("wrong automic play arg: %s, needs 'channelId@coding'", mediaControl)));
        }
        AutomicMediaProperties properties = context.getOrThrows(CommandContext.MEDIA_PROPERTY, AutomicMediaProperties.class);
        HttpSwapper httpSwapper = HttpSwapper.build(properties.getUrl() + "/v1/ptz/ptzSrsMove", HttpMethod.GET)
                .addParameter("guid", gb28181.getBizKey())
                .addParameter("ptzcmd", mediaControl.getCommand().getName())
                .addParameter("speed", mediaControl.getSpeed());
        AutomicBaseRequest baseRequest = new AutomicBaseRequest(properties);
        return new AutomicHttpResponseAdapter(
                // 加上token
                baseRequest.authSwapper(httpSwapper)
                        .flatMap(HttpSwapper::swap)
        )
                .repsInterceptor(() -> "status", code -> code == 0)
                .flatMap(res -> Mono.just(context));
    }
}
