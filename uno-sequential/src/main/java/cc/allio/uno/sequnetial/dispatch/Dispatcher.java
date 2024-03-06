package cc.allio.uno.sequnetial.dispatch;

import cc.allio.uno.sequnetial.Sequential;

import java.util.function.Predicate;

/**
 * 时序数据调度器，派发不同类型数据数据该怎么处理。
 * <p>
 * 它将会构建一个责任链
 *
 * @author jiangwei
 * @date 2022/2/26 22:48
 * @see org.springframework.core.Ordered
 * @see javax.annotation.Priority
 * @since 1.0
 */
public interface Dispatcher {

    /**
     * 触发调度的动作，由具体实现类编写具体逻辑
     *
     * @param sequential 时序数据对象
     * @throws Throwable 调度过程中产生异常将抛出
     */
    void dispatch(Sequential sequential) throws Throwable;

    /**
     * 判断当前时序数据是否运行被调度
     *
     * @return 断言实例对象
     */
    Predicate<? extends Sequential> isAssign();
}
