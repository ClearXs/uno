package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.MediaListCommand;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.starter.automic.AutomicMediaProperties;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * 调用Auotmic视频平台获取多媒体数据接口：/v1/base/list
 *
 * @author jiangwei
 * @date 2022/6/16 17:30
 * @since 1.0
 */
public class AutomicMediaListCommand implements MediaListCommand, AutomicDataCommand {

    /**
     * 多媒体code
     */
    private final String code;

    /**
     * 多媒体名称
     */
    private final String name;

    public AutomicMediaListCommand() {
        this(null, null);
    }

    public AutomicMediaListCommand(String name) {
        this(name, null);
    }

    public AutomicMediaListCommand(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        AutomicMediaProperties properties = context.getOrThrows(CommandContext.MEDIA_PROPERTY, AutomicMediaProperties.class);
        // 分页传入整形最大值使其变为不分页接口
        HttpSwapper httpSwapper = HttpSwapper.build(properties.getUrl() + "/v1/base/list", HttpMethod.GET)
                .addParameter("pageNum", String.valueOf(1))
                .addParameter("pageSize", String.valueOf(Integer.MAX_VALUE));
        if (StringUtils.isNotBlank(name)) {
            httpSwapper.addParameter("nickName", name);
        }
        if (StringUtils.isNotBlank(code)) {
            httpSwapper.addParameter("videoCode", code);
        }
        return query(properties, httpSwapper)
                .collectList()
                .flatMap(medias -> {
                    // 放入上下文中
                    context.putAttribute(MEDIA_LIST_TAG, medias);
                    return Mono.just(context);
                });
    }
}
