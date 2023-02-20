package cc.allio.uno.stater.srs;

import cc.allio.uno.component.media.MediaBuilder;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.component.media.event.Connect;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.serializer.JsonNodeEnhancer;
import com.google.auto.service.AutoService;

import java.util.Map;

/**
 * Srs-MediaBuilder
 *
 * @author jiangwei
 * @date 2022/4/4 14:45
 * @since 1.0.6
 */
@AutoService(MediaBuilder.class)
public class SrsMediaBuilder implements MediaBuilder {


    @Override
    public Media buildByConnect(Connect connect, MediaProperty mediaProperty) {
        Media media = new Media();
        media.setId(connect.getClientId());
        media.setHost(connect.getHost());
        media.setRtmpUrl(connect.getPullUrl());
        media.setPublish(true);
        Map<String, Object> attributes = connect.getAttributes();
        String bizKey = (String) attributes.get("stream");
        media.setStream(bizKey);
        media.setBizKey(bizKey);
        SrsMediaProperty srsMediaProperty = (SrsMediaProperty) mediaProperty;
        media.setFlvUrl(
                srsMediaProperty.getMediaUrl()
                        .concat(StringPool.SLASH)
                        .concat(connect.getApp())
                        .concat(StringPool.SLASH)
                        .concat(media.getBizKey())
                        .concat(StringPool.FileType.Media.FLV));
        return media;
    }

    @Override
    public Media buildByJsonNode(JsonNodeEnhancer clientNode, MediaProperty mediaProperty) {
        Media media = new Media();
        media.setId(clientNode.asString("id"));
        media.setType(clientNode.asString("type"));
        media.setPublish(clientNode.asBoolean("publish"));
        media.setHost(clientNode.asString("vhost"));
        // /live/livestream2
        String url = clientNode.asString("url");
        String[] split = url.split(StringPool.SLASH);
        media.setBizKey(split[1]);
        media.setPublish(clientNode.asBoolean("publish"));
        SrsMediaProperty srsMediaProperty = (SrsMediaProperty) mediaProperty;
        if (media.isPublish()) {
            media.setRtmpUrl(
                    clientNode.asString("swfUrl")
                            .concat(StringPool.SLASH)
                            .concat(media.getBizKey()));
            media.setFlvUrl(
                    srsMediaProperty.getMediaUrl()
                            .concat(StringPool.SLASH)
                            .concat(url)
                            .concat(StringPool.FileType.Media.FLV));
        }
        media.setStream(clientNode.asString("stream"));
        return media;
    }
}
