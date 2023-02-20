package cc.allio.uno.starter.automic.command;

import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.component.media.command.Command;
import cc.allio.uno.component.media.command.CommandController;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Map;

/**
 * Automic视频指令控制器
 *
 * @author jiangwei
 * @date 2022/6/16 16:29
 * @since 1.0
 */
@Slf4j
public class AutomicCommandController implements CommandController {

    private final MediaProperty mediaProperty;

    public AutomicCommandController(MediaProperty mediaProperty) {
        this.mediaProperty = mediaProperty;
    }

    @Override
    public Flux<CommandContext> touch(Command... commands) throws MediaException {
        Map<String, Object> storage = Maps.newHashMap();
        storage.put(CommandContext.MEDIA_PROPERTY, mediaProperty);
        return Flux.fromIterable(Arrays.asList(commands))
                .flatMap(command -> command.execute(new CommandContext(this, storage)))
                .onErrorContinue((ex, obj) -> log.error("Touch command {} failed", obj, ex));
    }
}
