package cc.allio.uno.core.chain;

import java.util.Map;

/**
 * 链中上下文数据
 *
 * @param <IN> 进入链中元素的范型
 * @author jiangwei
 * @date 2022/8/24 16:25
 * @since 1.0
 */
public interface ChainContext<IN> {

    /**
     * 获取入链的数据
     *
     * @return 入链实例对象
     */
    IN getIN();

    /**
     * 获取链中传递的属性
     *
     * @return Map实例数据
     */
    Map<String, Object> getAttribute();
}

