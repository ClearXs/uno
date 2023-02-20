package cc.allio.uno.stater.ezviz;

import cc.allio.uno.component.media.MediaBuilder;
import cc.allio.uno.component.media.MediaCache;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.component.media.event.Connect;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import com.google.auto.service.AutoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author heitianzhen
 * @date 2022/4/15 14:24
 */
@AutoService(MediaBuilder.class)
public class EzvizMediaBuilder implements MediaBuilder {

    @Autowired
    private MediaCache mediaCache;

    @Override
    public Media buildByConnect(Connect connect, MediaProperty mediaProperty) {
        return null;
    }

    @Override
    public Media buildByJsonNode(JsonNodeEnhancer clientNode, MediaProperty mediaProperty) {
        Media media = new Media();
        media.setBizKey(clientNode.asString("deviceSerial"))
                .setHlsUrl(clientNode.asString("hdAddress"))
                .setRtmpUrl(clientNode.asString("rtmpHd"));
        return media;
    }
}
