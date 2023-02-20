package cc.allio.uno.component.media;

import cc.allio.uno.component.media.command.CommandController;
import cc.allio.uno.core.OptionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 命令执行上下文
 *
 * @author jiangwei
 * @date 2022/3/30 14:05
 * @since 1.0.6
 */
@Slf4j
public class CommandContext implements OptionContext {

    /**
     * Spring应用上下文标识
     */
    public static final String APPLICATION_CONTEXT = "APPLICATION_CONTEXT";
    /**
     * Media配置标识
     */
    public static final String MEDIA_PROPERTY = "MEDIA_PROPERTY";
    /**
     * Media缓存标识
     */
    public static final String MEDIA_CACHE = "MEDIA_CACHE";

    private final Map<String, Object> storage = new HashMap<>();

    /**
     * 指令控制器
     */
    private final CommandController commandController;

    public CommandContext(CommandController commandController, Map<String, Object> other) {
        this.commandController = commandController;
        storage.putAll(other);
    }

    public CommandController getCommandController() {
        return commandController;
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(storage.get(key));
    }

    @Override
    public void putAttribute(String key, Object obj) {
        storage.put(key, obj);
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        try {
            ApplicationContext applicationContext = (ApplicationContext) storage.get(APPLICATION_CONTEXT);
            return Optional.ofNullable(applicationContext);
        } catch (ClassCastException | NullPointerException e) {
            log.warn("[{}] fetch ApplicationContext failed", this.getClass().getSimpleName(), e);
            return Optional.empty();
        }
    }
}
