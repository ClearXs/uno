package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.BaseMediaCommand;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.starter.automic.AutomicMediaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * Automic获取单个多媒体数据
 *
 * @author jiangwei
 * @date 2022/11/25 13:33
 * @since 1.1.2
 */
@Slf4j
public class AutomicMediaCommand extends BaseMediaCommand implements AutomicDataCommand {

    public AutomicMediaCommand(String key) {
        super(key);
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        AutomicMediaProperties properties = context.getOrThrows(CommandContext.MEDIA_PROPERTY, AutomicMediaProperties.class);
        if (StringUtils.isEmpty(getKey())) {
            throw new IllegalArgumentException("query single media data error, media 'code (key)' is empty");
        }
        // 分页传入整形最大值使其变为不分页接口
        HttpSwapper httpSwapper = HttpSwapper.build(properties.getUrl() + "/v1/base/list", HttpMethod.GET)
                .addParameter("pageNum", String.valueOf(1))
                .addParameter("pageSize", String.valueOf(Integer.MAX_VALUE))
                .addParameter("videoCode", getKey());
        return query(properties, httpSwapper)
                .single()
                .flatMap(media -> {
                    context.putAttribute(MEDIA_TAG, media);
                    return Mono.just(context);
                });
    }
}
