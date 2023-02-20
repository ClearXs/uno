package cc.allio.uno.stater.srs.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.command.StopPlayCommand;
import cc.allio.uno.component.media.entity.GB28181;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.stater.srs.SrsHttpResponseAdapter;
import cc.allio.uno.stater.srs.SrsMediaProperty;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * Srs流媒体服务器停止点播指令
 *
 * @author jiangwei
 * @date 2022/7/16 11:25
 * @since 1.0
 */
public class SrsStopPlayCommand implements StopPlayCommand {

    private final GB28181 gb28181;

    public SrsStopPlayCommand(GB28181 gb28181) {
        this.gb28181 = gb28181;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        try {
            if (StringUtils.isBlank(gb28181.getId()) || StringUtils.isBlank(gb28181.getChid())) {
                return Mono.error(new IllegalArgumentException(String.format("wrong srs play arg: %s, needs 'channelId@coding'", gb28181)));
            }
            MediaProperty property = context.getOrThrows(CommandContext.MEDIA_PROPERTY, MediaProperty.class);
            SrsMediaProperty srsProperty = (SrsMediaProperty) property;
            return new SrsHttpResponseAdapter(
                    HttpSwapper.build(srsProperty.getMediaUrl().concat("/api/v1/gb28181"), HttpMethod.GET)
                            .addParameter("action", "sip_bye")
                            .addParameter("id", gb28181.getId())
                            .addParameter("chid", gb28181.getChid())
                            .swap()
            )
                    .repsInterceptor(() -> "code", code -> code == 0)
                    .flatMap(media -> Mono.empty());
        } catch (Throwable e) {
            return Mono.error(e);
        }
    }
}
