package cc.allio.uno.core.chain;

import reactor.core.publisher.Mono;

/**
 * 链中结点标识接口，给定某个优先级注解对结点进行排序
 * <p>
 * Example usage:
 * <pre class="code">
 *     <code class="java">
 *      <b>&#064Order</b>
 *      public class NodeTest implements Node {
 *          ...
 *      }
 *     </code>
 * </pre>
 *
 * @param <IN>  进入链中元素的范型
 * @param <OUT> 出链元素的范型
 * @author j.x
 * @date 2022/8/22 18:46
 * @see org.springframework.core.annotation.Order
 * @see jakarta.annotation.Priority
 * @see org.springframework.core.Ordered
 * @since 1.0
 */
public interface Node<IN, OUT> {

    /**
     * 执行当前结点
     *
     * @param chain   下一链的数据
     * @param context 链上下文实例
     * @return 出链数据
     */
    Mono<OUT> execute(Chain<IN, OUT> chain, ChainContext<IN> context) throws Throwable;

}
