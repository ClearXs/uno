package cc.allio.uno.component.sequential.context;

import cc.allio.uno.component.sequential.process.handle.ProcessHandler;
import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.core.bus.EventContext;
import cc.allio.uno.core.bus.EventTracer;
import cc.allio.uno.core.bus.Topic;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 事件总线上下文对象
 *
 * @author jiangwei
 * @date 2021/12/29 14:41
 * @modify 1.0.6
 * @since 1.0
 */
public interface SequentialContext extends EventContext {

    /**
     * 定义一个全局处理使出过程中id，在事件总线路由中这个id唯一保持不变
     *
     * @return 全局唯一的值
     */
    Long getContextId();

    /**
     * 获取执行的上下文数据，返回复制的对象，避免原始对象在执行链过程中被反射赋值。
     *
     * @return 时序数据的实体对象
     */
    Sequential getSequential();

    /**
     * 获取真实存在的对象。
     *
     * @return 时序数据的实体对象
     */
    Sequential getRealSequential();

    /**
     * 获取时序数据执行处理器
     *
     * @return 返回执行处理器实例，一次执行过程中所使用到的所有处理器
     * @throws NullPointerException 获取不到时抛出
     */
    Optional<List<ProcessHandler>> getProcessHandler();

    /**
     * 复制原有Context，改变它的上下文Id值。</br>
     * Note:</br>
     * 在事件总线路由中，如果在不同的{@link Topic}中发布某个{@link Topic}使用的{@link SequentialContext}，这必然是不正确的，</br>
     * 那么结论就是{@link SequentialContext}将会是一个新的（或者说可以做成链路追踪那样，设置一个追踪链？），</br>
     * 所以说就有加上这个方法的意义
     *
     * @return 新的上下文Id值
     * @throws NullPointerException 复制出错时抛出
     */
    SequentialContext newContext();

    /**
     * 获取空的上下文对象
     *
     * @return 实例对象
     */
    static SequentialContext empty() {
        return new SequentialContext() {
            @Override
            public String getTopicPath() {
                return null;
            }

            @Override
            public Topic<?> getTopic() {
                return null;
            }

            @Override
            public EventTracer getEventTracer() {
                return null;
            }

            @Override
            public Long getContextId() {
                return null;
            }

            @Override
            public Sequential getSequential() {
                return null;
            }

            @Override
            public Sequential getRealSequential() {
                return null;
            }

            @Override
            public Optional<Object> get(String key) {
                return Optional.empty();
            }

            @Override
            public void putAttribute(String key, Object obj) {

            }

            @Override
            public Optional<ApplicationContext> getApplicationContext() {
                return Optional.empty();
            }

            @Override
            public Map<String, Object> getAll() {
                return null;
            }

            @Override
            public Optional<List<ProcessHandler>> getProcessHandler() {
                return Optional.empty();
            }

            @Override
            public SequentialContext newContext() {
                return null;
            }
        };
    }

}
