package cc.allio.uno.core.util.calculate;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import com.ql.util.express.ExpressRunner;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Optional;

/**
 * 计算器<br/>
 * 计算内部是一个无状态的过程，不能依赖于其他的数据来源
 *
 * @author jiangwei
 * @date 2022/1/11 09:41
 * @since 1.0
 */
@FunctionalInterface
public interface Calculator<C> extends Serializable {

    /**
     * 触发计算
     *
     * @param context Spring应用上下文
     * @param obj     计算实体
     * @return 返回计算结果
     * @see ExpressionTemplate
     * @see ExpressRunner
     */
    default Mono<C> calculate(Optional<ApplicationContext> context, Object obj) {
        return Mono.justOrEmpty(calculation(context, obj));
    }

    C calculation(Optional<ApplicationContext> context, Object obj);
}
