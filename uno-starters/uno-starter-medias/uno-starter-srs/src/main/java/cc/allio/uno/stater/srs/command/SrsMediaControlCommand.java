package cc.allio.uno.stater.srs.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.MediaControlCommand;
import cc.allio.uno.component.media.entity.GB28181;
import cc.allio.uno.component.media.entity.MediaControl;
import cc.allio.uno.core.util.ObjectUtil;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.stater.srs.SrsHttpResponseAdapter;
import cc.allio.uno.stater.srs.SrsMediaProperty;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * Srs流媒体服务器控制指令
 *
 * @author jiangwei
 * @date 2022/7/16 11:28
 * @since 1.0.6
 */
public class SrsMediaControlCommand implements MediaControlCommand {

    private final MediaControl mediaControl;

    public SrsMediaControlCommand(MediaControl mediaControl) {
        this.mediaControl = mediaControl;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        GB28181 gb28181 = mediaControl.getGb28181();
        if (ObjectUtil.isEmpty(gb28181) || StringUtils.isBlank(gb28181.getId()) || StringUtils.isBlank(gb28181.getChid())) {
            return Mono.error(new IllegalArgumentException(String.format("wrong srs play arg: %s, needs 'channelId@coding'", mediaControl)));
        }
        SrsMediaProperty property = context.getOrThrows(CommandContext.MEDIA_PROPERTY, SrsMediaProperty.class);
        return new SrsHttpResponseAdapter(
                HttpSwapper.build(property.getMediaUrl().concat("/api/v1/gb28181"), HttpMethod.GET)
                        .addParameter("action", "sip_ptz")
                        .addParameter("id", gb28181.getId())
                        .addParameter("chid", gb28181.getChid())
                        .addParameter("ptzcmd", mediaControl.getCommand().getName())
                        .addParameter("speed", mediaControl.getSpeed())
                        .swap()
        )
                .repsInterceptor(() -> "code", code -> code == 0)
                .flatMap(media -> Mono.empty());
    }
}
