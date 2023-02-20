package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaBuilderFactory;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.command.BaseMediaPageCommand;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.component.media.entity.Page;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.starter.automic.AutomicMediaProperties;
import cc.allio.uno.starter.automic.AutomicHttpResponseAdapter;
import com.google.common.collect.Lists;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Automic多美数据分页指令
 *
 * @author jiangwei
 * @date 2022/11/25 14:51
 * @since 1.1.2
 */
public class AutomicMediaPageCommand extends BaseMediaPageCommand implements AutomicDataCommand {

    /**
     * 多媒体名称
     */
    private final String name;

    /**
     * 多媒体code
     */
    private final String key;

    public AutomicMediaPageCommand() {
        this(null, null);
    }

    public AutomicMediaPageCommand(Integer start, Integer count) {
        this(start, count, null, null);
    }

    public AutomicMediaPageCommand(Integer start, Integer count, String name) {
        this(start, count, name, null);
    }

    public AutomicMediaPageCommand(Integer start, Integer count, String name, String key) {
        super(start, count);
        this.name = name;
        this.key = key;
    }


    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        AutomicMediaProperties properties = context.getOrThrows(CommandContext.MEDIA_PROPERTY, AutomicMediaProperties.class);
        // 分页传入整形最大值使其变为不分页接口
        HttpSwapper httpSwapper = HttpSwapper.build(properties.getUrl() + "/v1/base/list", HttpMethod.GET)
                .addParameter("pageNum", String.valueOf(getStart()))
                .addParameter("pageSize", String.valueOf(getCount()));
        if (StringUtils.isNotBlank(name)) {
            httpSwapper.addParameter("nickName", name);
        }
        if (StringUtils.isNotBlank(key)) {
            httpSwapper.addParameter("videoCode", key);
        }
        AutomicBaseRequest request = new AutomicBaseRequest(properties);
        return new AutomicHttpResponseAdapter(request.authSwapper(httpSwapper).flatMap(HttpSwapper::swap))
                .repsInterceptor(() -> "status", code -> code == 0)
                .flatMap(json -> {
                    Page page = new Page();
                    JsonNodeEnhancer data = json.getNode("data");
                    JsonNodeEnhancer list = data.getNode("list");
                    List<Media> medias = Lists.newArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        JsonNodeEnhancer deviceInfo = list.getNode(i);
                        Media media = MediaBuilderFactory.createMediaBuilder().buildByJsonNode(deviceInfo, properties);
                        medias.add(media);
                    }
                    page.setCurrent(getStart());
                    page.setSize(getCount());
                    page.setRecords(medias);
                    page.setTotal(data.asLong("total"));
                    return Mono.just(page);
                })
                .flatMap(page -> {
                    // 放入上下文中
                    context.putAttribute(MEDIA_PAGE_TAG, page);
                    return Mono.just(context);
                });
    }
}
