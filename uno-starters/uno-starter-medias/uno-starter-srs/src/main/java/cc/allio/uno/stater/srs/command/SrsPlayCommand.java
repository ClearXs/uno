package cc.allio.uno.stater.srs.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.stater.srs.SrsHttpResponseAdapter;
import cc.allio.uno.stater.srs.SrsMediaProperty;
import cc.allio.uno.component.media.*;
import cc.allio.uno.component.media.command.CommandFactory;
import cc.allio.uno.component.media.command.PlayCommand;
import cc.allio.uno.component.media.command.StopPlayCommand;
import cc.allio.uno.component.media.entity.GB28181;
import cc.allio.uno.core.util.StringUtils;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * Srs流媒体服务器点播指令
 *
 * @author jiangwei
 * @date 2022/3/30 16:07
 * @since 1.0.6
 */
public class SrsPlayCommand implements PlayCommand {

    private final GB28181 gb28181;

    /**
     * 实例Srs播放指令
     *
     * @param gb28181 设备的业务主键，14058100001320000013@34020000001320000001
     */
    public SrsPlayCommand(GB28181 gb28181) {
        this.gb28181 = gb28181;
    }

    /**
     * 数据存放于上下属性key = 'media'中
     * <ol>
     *     <li>从缓存中获取Media对象，如果获取不到则抛出{@link MediaException}</li>
     *     <li>如果Media中播放地址为空，则远程调用Srs流媒体服务器接口获取播放地址</li>
     * </ol>
     *
     * @return 单流Void
     * @see #execute(CommandContext)
     */
    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        if (StringUtils.isBlank(gb28181.getId()) || StringUtils.isBlank(gb28181.getChid())) {
            return Mono.error(new IllegalArgumentException(String.format("wrong srs play arg: %s, needs 'channelId@coding'", gb28181)));
        }
        MediaProperty property = context.getOrThrows(CommandContext.MEDIA_PROPERTY, MediaProperty.class);
        SrsMediaProperty srsProperty = (SrsMediaProperty) property;
        return new SrsHttpResponseAdapter(
                HttpSwapper.build(srsProperty.getMediaUrl().concat("/api/v1/gb28181"), HttpMethod.GET)
                        .addParameter("action", "sip_invite")
                        .addParameter("id", gb28181.getId())
                        .addParameter("chid", gb28181.getChid())
                        .swap()
        )
                .repsInterceptor(() -> "code", code -> code == 0 || code == 6001 || code == 6010)
                .flatMap(back -> {
                    Integer code = back.asInteger("code");
                    // 需要重新拉流
                    if (code == 6010) {
                        return context.getCommandController()
                                .stopPlay((StopPlayCommand) CommandFactory.createCommand(StopPlayCommand.class, gb28181))
                                .then(
                                        context.getCommandController()
                                                .play((PlayCommand) CommandFactory.createCommand(PlayCommand.class, gb28181))
                                                .then()
                                )
                                .map(empty -> context);
                    } else {
                        context.putAttribute(PLAY_DATA, code);
                        return Mono.empty();
                    }
                });
    }
}
