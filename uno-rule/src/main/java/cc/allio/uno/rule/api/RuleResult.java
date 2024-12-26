package cc.allio.uno.rule.api;

import cc.allio.uno.rule.api.event.Listener;
import cc.allio.uno.rule.exception.RuleResultRuntimeException;
import cc.allio.uno.rule.exception.RuleResultTimeoutException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 定义Rule Result
 *
 * @author j.x
 * @since 1.1.4
 */
public interface RuleResult {

    Long DEFAULT_TIME_OUT = 30000L;
    TimeUnit DEFAULT_TIME_OUT_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 获取运行时关联的Rule实例
     *
     * @return rule 实例
     */
    Rule getRule();

    /**
     * 获取运行的Fact对象
     *
     * @return fact 实例
     */
    Fact getFact();

    /**
     * 获取标识
     *
     * @return Long
     */
    Long getId();

    /**
     * <pre>同步模式</pre><b>获取结果匹配集</b>，默认30s超时
     *
     * @return when match list is empty -- no match or -- match
     * @throws RuleResultTimeoutException 当获取超时时抛出
     * @throws RuleResultRuntimeException 当执行规则时有异常时抛出
     */
    default Set<MatchIndex> get() throws RuleResultTimeoutException, RuleResultRuntimeException {
        return get(DEFAULT_TIME_OUT, DEFAULT_TIME_OUT_UNIT);
    }

    /**
     * <pre>同步模式</pre><b>获取结果匹配集</b>
     *
     * @param timeout  超时事件
     * @param timeUnit time unit
     * @return when match list is empty -- no match or -- match
     * @throws RuleResultTimeoutException 当获取超时时抛出
     * @throws RuleResultRuntimeException 当执行规则时有异常时抛出
     */
    Set<MatchIndex> get(long timeout, TimeUnit timeUnit) throws RuleResultTimeoutException, RuleResultRuntimeException;

    /**
     * <pre>异步模式</pre>
     * 根据响应式方式获取匹配集
     *
     * @return MatchIndex for flux
     */
    Flux<MatchIndex> getOnReactive();

    /**
     * <pre>异步模式</pre>是否匹配，异步返回，直到调用{@link #get()}、{@link #addLister(Listener)}才会获取到结果
     *
     * @return true match false no match
     */
    boolean isMatched();

    /**
     * <pre>同步模式</pre>是否匹配，同步返回
     *
     * @return true match false no match
     */
    boolean isMatchedOnSync();

    /**
     * <pre>异步模式</pre>
     * 是否匹配
     *
     * @return true match false no match
     */
    Mono<Boolean> isMatchedOnReactive();

    /**
     * <pre>异步模式</pre>添加监听器
     *
     * @param listener listener
     */
    void addLister(Listener listener);

    /**
     * 获取结果集生成结果时间
     *
     * @return the Date
     */
    Date getResultTime();

    /**
     * 获取结果异常信息
     *
     * @return err
     */
    Throwable getErr();

    /**
     * 释放资源
     */
    void release();
}
