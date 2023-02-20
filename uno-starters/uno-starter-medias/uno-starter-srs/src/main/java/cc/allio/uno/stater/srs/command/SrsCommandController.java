package cc.allio.uno.stater.srs.command;

import cc.allio.uno.component.media.CommandContext;
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

import java.util.Arrays;
import java.util.HashMap;

/**
 * 基于srs流媒体服务器指令控制器
 *
 * @author jiangwei
 * @date 2022/3/30 15:51
 * @since 1.0.6
 */
@Slf4j
public class SrsCommandController implements CommandController, ApplicationContextAware {
    private final MediaProperty mediaProperty;
    private ApplicationContext applicationContext;

    public SrsCommandController(MediaProperty mediaProperty) {
        this.mediaProperty = mediaProperty;
    }

    @Override
    public Flux<CommandContext> touch(Command... commands) throws MediaException {
        HashMap<String, Object> storage = Maps.newHashMap();
        storage.put(CommandContext.MEDIA_PROPERTY, mediaProperty);
        storage.put(CommandContext.APPLICATION_CONTEXT, applicationContext);
        CommandContext commandContext = new CommandContext(this, storage);
        return Flux.fromIterable(Arrays.asList(commands))
                .flatMap(command -> command.execute(commandContext))
                .onErrorContinue((ex, obj) -> log.error("Touch command {} failed", obj, ex));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
