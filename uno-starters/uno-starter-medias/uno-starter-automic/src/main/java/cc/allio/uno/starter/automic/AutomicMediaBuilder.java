package cc.allio.uno.starter.automic;

import cc.allio.uno.component.media.MediaBuilder;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.component.media.event.Connect;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import com.google.auto.service.AutoService;

@AutoService(MediaBuilder.class)
public class AutomicMediaBuilder implements MediaBuilder {

    @Override
    public Media buildByConnect(Connect connect, MediaProperty mediaProperty) {
        // TODO Automic平台没有Connect支持
        return null;
    }

    @Override
    public Media buildByJsonNode(JsonNodeEnhancer jsonNodeEnhancer, MediaProperty mediaProperty) {
        Media media = new Media();
        // 设备编码
        media.setId(jsonNodeEnhancer.asString("guid"));
        // 设备名称
        media.setName(jsonNodeEnhancer.asString("mopoName"));
        // 经度
        media.getLocation().setLongitude(jsonNodeEnhancer.asBigDecimal("mopoLat", () -> null));
        // 纬度
        media.getLocation().setLatitude(jsonNodeEnhancer.asBigDecimal("mopoLong", () -> null));
        // flv流地址
        media.setFlvUrl(jsonNodeEnhancer.asString("detlLoc"));
        // rtspUrl流地址
        media.setRtmpUrl(jsonNodeEnhancer.asString("rtspUrl"));
        // 业务关联
        media.setBizKey(jsonNodeEnhancer.asString("stcd"));
        // 设备状态
        media.setIden(jsonNodeEnhancer.asInteger("iden"));
        return media;
    }
}
