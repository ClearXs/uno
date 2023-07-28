package cc.allio.uno.component.sequential.transform;

import cc.allio.uno.component.sequential.context.SequentialContext;
import cc.allio.uno.core.bean.BeanInfoWrapper;
import cc.allio.uno.core.type.Type;
import reactor.core.publisher.Mono;

/**
 * 数据转换
 *
 * @author jiangwei
 * @date 2022/5/20 10:24
 * @since 1.0
 */
public interface Transducer {

    /**
     * 获取类型
     *
     * @return Type实例
     */
    Type getType();

    /**
     * 对数据做在改造
     *
     * @param sequentialContext 上下文
     */
    void rebirth(SequentialContext sequentialContext);

    /**
     * 设置数据
     *
     * @param wrapper bean对象
     * @param field   需要设置的字段
     * @param values  设置字段的值
     * @deprecated 1.1.4删除
     */
    default Mono<Object> set(BeanInfoWrapper wrapper, String field, Object... values) {
        return wrapper.set(wrapper, field, values);
    }
}
