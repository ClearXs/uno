package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.MediaBuilderFactory;
import cc.allio.uno.component.media.command.Command;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import cc.allio.uno.starter.automic.AutomicMediaProperties;
import cc.allio.uno.starter.automic.AutomicHttpResponseAdapter;
import com.google.common.collect.Lists;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Auotmic相关数据接口
 *
 * @author jiangwei
 * @date 2022/11/25 15:00
 * @since 1.1.2
 */
public interface AutomicDataCommand extends Command {

    /**
     * 执行请求数据动作
     *
     * @param properties 配置参数句
     * @param swapper    http请求
     * @return media数据流
     */
    default Flux<Media> query(AutomicMediaProperties properties, HttpSwapper swapper) {
        AutomicBaseRequest request = new AutomicBaseRequest(properties);
        // 加上token
        return new AutomicHttpResponseAdapter(request.authSwapper(swapper).flatMap(HttpSwapper::swap))
                .repsInterceptor(() -> "status", code -> code == 0)
                .flatMapMany(json -> {
                    JsonNodeEnhancer data = json.getNode("data");
                    JsonNodeEnhancer list = data.getNode("list");
                    List<Media> medias = Lists.newArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        JsonNodeEnhancer deviceInfo = list.getNode(i);
                        Media media = MediaBuilderFactory.createMediaBuilder().buildByJsonNode(deviceInfo, properties);
                        medias.add(media);
                    }
                    return Flux.fromIterable(medias);
                });
    }
}
