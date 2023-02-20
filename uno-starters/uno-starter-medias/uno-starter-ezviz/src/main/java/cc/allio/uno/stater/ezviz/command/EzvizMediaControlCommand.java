package cc.allio.uno.stater.ezviz.command;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.command.MediaControlCommand;
import cc.allio.uno.stater.ezviz.EzvizAccessToken;
import cc.allio.uno.stater.ezviz.EzvizMediaProperties;
import cc.allio.uno.stater.ezviz.EzvizMediaProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;



/**
 * 云台控制
 * @author heitianzhen
 * @date 2022/4/15 11:07
 */
public class EzvizMediaControlCommand implements MediaControlCommand {

    private String deviceSerial;

    private int direction;
    @Autowired
    private EzvizAccessToken ezvizAccessToken;

    public EzvizMediaControlCommand(String deviceSerial,int direction){
        this.deviceSerial = deviceSerial;
        this.direction = direction;
    }

    @Override
    public Mono<CommandContext> execute(CommandContext context) throws NullPointerException, MediaException {
        EzvizMediaProperty property = (EzvizMediaProperties) context.getOrThrows(CommandContext.MEDIA_PROPERTY, MediaProperty.class);
        return new EzvizHttpResponseAdapter(
                HttpSwapper.build(property.getDeviceControlUrl(), HttpMethod.POST)
                        .addParameter("deviceSerial",deviceSerial)
                        .addParameter("accessToken",ezvizAccessToken.getAccessTokenFromRedis(context))
                        .addParameter("channelNo","1")
                        .addParameter("direction",String.valueOf(this.direction))
                        .addParameter("speed",String.valueOf(1))
                        .swap()
        ).testResponse().flatMap(node -> {
            context.putAttribute(MediaControlCommand.MEDIA_CONTROL_TAG,node.get("msg").textValue());
           return Mono.empty();
        });
    }
}
