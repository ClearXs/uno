package cc.allio.uno.core.bus;

import cc.allio.uno.core.OptionContext;

import java.util.Optional;

/**
 * 消息上下文
 *
 * @author jiangwei
 * @date 2022/12/14 09:17
 * @since 1.1.2
 */
public interface MessageContext extends OptionContext {

    /**
     * 获取消息总线对象
     *
     * @return
     */
    <T extends MessageContext> Optional<MessageBus<T>> getMessageBus();
}
