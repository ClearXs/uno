package cc.allio.uno.data.orm.executor.interceptor;

import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandType;
import cc.allio.uno.data.orm.dsl.Operator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.OrderUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.AnnotatedElement;

/**
 * {@link Interceptor}装饰器，确定调用事件方法
 *
 * @author j.x
 * @date 2024/1/8 10:51
 * @since 1.1.7
 */
@Slf4j
public abstract class InternalInterceptor implements Interceptor, Ordered {

    protected final Interceptor interceptor;

    protected InternalInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public Mono<InterceptorAttributes> execute(Chain<InterceptorAttributes, InterceptorAttributes> chain, ChainContext<InterceptorAttributes> context) throws Throwable {
        InterceptorAttributes attributes = context.getIN();
        CommandExecutor commandExecutor = attributes.getCommandExecutor();
        Operator<?> operator = attributes.getOperator();
        CommandType commandType = attributes.getCommandType();
        Object result = attributes.getResult();
        if (log.isDebugEnabled()) {
            log.debug("Internal Interceptor [{}] intercept actual Interceptor [{}] the command [{}]", getClass().getSimpleName(), interceptor.getClass().getSimpleName(), commandType);
        }
        switch (commandType) {
            case INSERT -> this.onSave(commandExecutor, operator, result);
            case UPDATE -> this.onUpdate(commandExecutor, operator, result);
            case DELETE -> this.onDelete(commandExecutor, operator, result);
            case SELECT -> this.onQuery(commandExecutor, operator, result);
            default -> interceptor.onUnknownCommand(commandExecutor, commandType, operator);
        }
        return chain.proceed(context);
    }

    @Override
    public int getOrder() {
        AnnotatedElement element = (interceptor instanceof AnnotatedElement ae ? ae : interceptor.getClass());
        Integer order = OrderUtils.getOrder(element);
        if (order == null) {
            return 0;
        }
        return order;
    }

    protected abstract void onSave(CommandExecutor commandExecutor, Operator<?> operator, Object result);

    protected abstract void onUpdate(CommandExecutor commandExecutor, Operator<?> operator, Object result);

    protected abstract void onDelete(CommandExecutor commandExecutor, Operator<?> operator, Object result);

    protected abstract void onQuery(CommandExecutor commandExecutor, Operator<?> operator, Object result);
}
