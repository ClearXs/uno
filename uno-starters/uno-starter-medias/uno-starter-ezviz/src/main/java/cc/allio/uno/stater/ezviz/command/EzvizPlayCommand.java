package cc.allio.uno.stater.ezviz.command;

import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaCache;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.PlayCommand;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.core.util.type.TypeOperatorFactory;
import cc.allio.uno.core.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * @author heitianzhen
 * @date 2022/4/18 13:45
 */
public class EzvizPlayCommand implements PlayCommand {

    private final String bizKey;

    private final DevicePlayProtocol protocol;

    /**
     * 实例Ezviz播放指令
     *
     * @param bizKey 设备的业务主键，如设别编码
     */
    public EzvizPlayCommand(String bizKey) {
        this(bizKey, DevicePlayProtocol.HLS.getCode());
    }

    /**
     * 实例Ezviz播放指令
     *
     * @param bizKey       设备的业务主键，如设别编码
     * @param playProtocol 播放协议{@link DevicePlayProtocol}，如果值为空，取默认值为{@link DevicePlayProtocol#FLV}
     */
    public EzvizPlayCommand(String bizKey, String playProtocol) {
        this.bizKey = bizKey;
        if (StringUtils.isEmpty(playProtocol)) {
            this.protocol = DevicePlayProtocol.HLS;
        } else {
            this.protocol = (DevicePlayProtocol) TypeOperatorFactory.translator(Enum.class).convert(playProtocol, DevicePlayProtocol.class);
        }
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        MediaCache mediaCache = context.getOrThrows(CommandContext.MEDIA_CACHE, MediaCache.class);
        Media mediaInstance = mediaCache.getByKey(Media.class, "bizKey", this.bizKey);
        if (StringUtils.isNotEmpty(protocol.toUrl(mediaInstance))) {
            context.putAttribute(PLAY_DATA, mediaInstance);
            return Mono.empty();
        }
        // TODO 当没有这个设备的时候向萤石云发请求，获取设备，如果获取不到，就是没有
        return Mono.empty();
    }
}
