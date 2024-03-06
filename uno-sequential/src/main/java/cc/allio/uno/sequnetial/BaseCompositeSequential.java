package cc.allio.uno.sequnetial;

import cc.allio.uno.core.proxy.ComposableInvocationInterceptor;
import cc.allio.uno.core.proxy.ProxyFactory;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 组合的时序数据，时序数据的类型为组合的时序数据类型
 *
 * @author jiangwei
 * @date 2021/12/20 14:04
 * @since 1.0
 */
@Slf4j
public abstract class BaseCompositeSequential implements CompositeSequential {

    /**
     * 组合的Sequential数据
     */
    private final List<Sequential> composeSequential = new CopyOnWriteArrayList<>();

    protected BaseCompositeSequential() {

    }

    @SafeVarargs
    protected BaseCompositeSequential(Class<? extends Sequential>... composeSequentialClass) {
        addSequential(composeSequentialClass);
    }

    protected BaseCompositeSequential(Sequential... sequential) {
        composeSequential.addAll(Lists.newArrayList(sequential));
    }

    /**
     * 从组合时序数据中根据时序数据类型获取期望的时序数据
     *
     * @param expectedSequentialType 期望时序数据类型
     * @return 时序数据
     * @throws NullPointerException 没有找到时抛出
     */
    public Sequential getExpectSequential(String expectedSequentialType) {
        return composeSequential.stream()
                .filter(s -> s.getType().equals(expectedSequentialType))
                .findFirst()
                .orElseThrow(() -> new NullPointerException(String.format("Not found expect type [%s]", expectedSequentialType)));
    }

    /**
     * 从组合时序数据中根据时序数据类型获取期望的时序数据
     *
     * @param expectedSequentialType 期望时序数据类型
     * @return 时序数据
     * @throws NullPointerException 没有找到时抛出
     */
    public Sequential getExpectSequential(Class<? extends Sequential> expectedSequentialType) {
        return composeSequential.stream()
                .filter(s -> s.getClass().getSuperclass().isAssignableFrom(expectedSequentialType))
                .findFirst()
                .orElseThrow(() -> new NullPointerException(String.format("Not found expect type [%s]", expectedSequentialType)));
    }

    @Override
    public List<Sequential> getCompositeMetadata() {
        return composeSequential;
    }

    /**
     * 向组合时序数据添加时序数据
     *
     * @param sequentialClasses 时序数据Class对象数组
     */
    @SafeVarargs
    public final void addSequential(Class<? extends Sequential>... sequentialClasses) {
        Flux.fromArray(sequentialClasses)
                .map(sequentialClass ->
                        ProxyFactory.proxy()
                                .newProxyInstance(sequentialClass,
                                        new ComposableInvocationInterceptor(BaseCompositeSequential.this)))
                .onErrorContinue((error, o) -> log.error("Proxy compose sequential error", error))
                .cast(Sequential.class)
                .subscribe(composeSequential::add);
    }

    /**
     * 向组合时序数据添加时序数据
     *
     * @param sequential 时序数据实例对象
     */
    public final void addSequential(Sequential... sequential) {
        composeSequential.addAll(Lists.newArrayList(sequential));
    }

    /**
     * 清空当前组合的时序数据
     */
    public void clear() {
        composeSequential.clear();
    }
}
