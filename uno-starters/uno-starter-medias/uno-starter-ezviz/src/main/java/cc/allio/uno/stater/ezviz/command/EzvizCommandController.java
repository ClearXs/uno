package cc.allio.uno.stater.ezviz.command;

import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaCache;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.command.Command;
import cc.allio.uno.component.media.command.CommandController;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @author heitianzhen
 * @date 2022/4/12 15:13
 */
@Slf4j
public class EzvizCommandController implements CommandController, ApplicationContextAware {

    private final MediaProperty mediaProperty;

    private ApplicationContext applicationContext;

    public EzvizCommandController(MediaProperty mediaProperty) {
        this.mediaProperty = mediaProperty;
    }

    @Override
    public Flux<CommandContext> touch(Command... commands) throws MediaException {
        Map<String, Object> storage = Maps.newHashMap();
        storage.put("MEDIA_PROPERTY", this.mediaProperty);
        storage.put("APPLICATION_CONTEXT", this.applicationContext);
        storage.put("MEDIA_CACHE", this.applicationContext.getBean(MediaCache.class));
        return Flux.fromArray(commands)
                .flatMap(command -> command.execute(new CommandContext(this, storage)))
                .onErrorContinue((ex, obj) -> log.error("Touch command {} failed", obj, ex));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
