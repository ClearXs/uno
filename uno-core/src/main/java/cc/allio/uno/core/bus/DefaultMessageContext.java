package cc.allio.uno.core.bus;

import com.google.common.collect.Maps;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

/**
 * MessageBus上下文对象
 *
 * @author jiangwei
 * @date 2022/12/12 16:27
 * @since 1.1.2
 */
public class DefaultMessageContext implements MessageContext {

    protected final Map<String, Object> attributes;

    // ========================= CONSTANT =========================
    public static final String MESSAGE_BUS = "MESSAGE_BUS";
    public static final String APPLICATION_CONTEXT = "APPLICATION_CONTEXT";

    public DefaultMessageContext(Map<String, Object> attributes) {
        this(attributes, null);
    }

    public DefaultMessageContext(@NonNull Map<String, Object> attributes, MessageBus<? extends MessageContext> messageBus) {
        this(attributes, messageBus, null);
    }

    public DefaultMessageContext(@NonNull Map<String, Object> attributes, MessageBus<? extends MessageContext> messageBus, ApplicationContext applicationContext) {
        this.attributes = Maps.newConcurrentMap();
        this.attributes.putAll(attributes);
        if (messageBus != null) {
            this.attributes.put(MESSAGE_BUS, messageBus);
        }
        if (applicationContext != null) {
            this.attributes.put(APPLICATION_CONTEXT, applicationContext);
        }
    }

    public DefaultMessageContext(DefaultMessageContext messageContext) {
        this.attributes = messageContext.attributes;
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    @Override
    public void putAttribute(String key, Object obj) {
        attributes.put(key, obj);
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return get(APPLICATION_CONTEXT, ApplicationContext.class);
    }

    /**
     * 获取消息总线
     *
     * @return
     */
    @Override
    public Optional<MessageBus<MessageContext>> getMessageBus() {
        if (attributes.containsKey(MESSAGE_BUS)) {
            MessageBus<MessageContext> messageBus = (MessageBus<MessageContext>) attributes.get(MESSAGE_BUS);
            return Optional.ofNullable(messageBus);
        }
        return Optional.empty();
    }
}
