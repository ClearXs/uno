package cc.allio.uno.component.sequential.context;

import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.component.sequential.process.Processor;
import cc.allio.uno.core.bean.BeanInfoWrapper;
import cc.allio.uno.core.bus.DefaultMessageContext;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.core.bus.MessageBus;
import cc.allio.uno.component.sequential.process.handle.ProcessHandler;
import cc.allio.uno.core.util.CoreBeanUtil;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * 消息总线数据上下文
 *
 * @author jw
 * @date 2021/12/17 15:47
 */
public class DefaultSequentialContext extends DefaultMessageContext implements SequentialContext {

    /**
     * 时序数据
     */
    private Sequential sequential;
    /**
     * 当前执行上下文全局唯一id
     */
    private Long contextId;

    public static final String HANDLER = "HANDLER";

    public DefaultSequentialContext(Sequential sequential, Map<String, Object> attributes) {
        super(attributes);
        this.contextId = IdGenerator.defaultGenerator().getNextId();
        this.sequential = sequential;
    }

    public DefaultSequentialContext(Sequential sequential, ApplicationContext applicationContext, MessageBus<SequentialContext> messageBus) {
        super(Collections.emptyMap(), messageBus, applicationContext);
        this.contextId = IdGenerator.defaultGenerator().getNextId();
        this.sequential = sequential;
    }


    /**
     * 获取时序数据执行处理器
     *
     * @return 返回执行处理器实例，一次执行过程中所使用到的所有处理器
     * @throws NullPointerException 获取不到时抛出
     * @see Processor#process(SequentialContext)
     */
    @Override
    public Optional<List<ProcessHandler>> getProcessHandler() {
        if (containsKey(HANDLER)) {
            List<ProcessHandler> handler = (List<ProcessHandler>) attributes.get(HANDLER);
            return Optional.ofNullable(handler);
        }
        return Optional.empty();
    }

    @Override
    public SequentialContext newContext() {
        return new DefaultSequentialContext(sequential, attributes);
    }

    @Override
    public Long getContextId() {
        return contextId;
    }

    @Override
    public Sequential getSequential() {
        return CoreBeanUtil.copy(sequential, sequential.getClass());
    }

    @Override
    public Sequential getRealSequential() {
        return sequential;
    }

    /**
     * {@link BeanInfoWrapper}方式进行set
     *
     * @param sequential 时序数据对象
     * @see Processor#process(SequentialContext)
     */
    public void setSequential(Sequential sequential) {
        this.sequential = sequential;
    }

    /**
     * {@link BeanInfoWrapper}方式进行set
     *
     * @param contextId 上下文id
     * @see SequentialContext#newContext()
     */
    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }
}
