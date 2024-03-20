package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;

/**
 * Definition DSL internal command executor
 *
 * @author j.x
 * @date 2023/5/29 20:41
 * @modify 1.1.7
 * @since 1.1.4
 */
public interface InnerCommandExecutor<R, O extends Operator<?>, H> {

    /**
     * 执行类型
     *
     * @param operator 操作器
     * @param handler  结果集处理器
     * @return R
     * @throws Throwable 执行发生异常时抛出
     */
    default R exec(Operator<?> operator, H handler) throws Throwable {
        O o = castTo(operator, getRealityOperatorType());
        return doExec(o, handler);
    }

    /**
     * sub-class reality exec
     *
     * @param operator 操作器
     * @param handler  handler
     * @return R
     * @throws Throwable 执行发生异常时抛出
     */
    R doExec(O operator, H handler) throws Throwable;

    /**
     * take to original {@link Operator} cast to generic type O
     *
     * @param original original operator
     * @param castFor  generic class
     * @param <O>      generic type
     * @return O
     * @throws cc.allio.uno.data.orm.dsl.exception.DSLException cast failed
     */
    static <O extends Operator<?>> O castTo(Operator<?> original, Class<O> castFor) {
        try {
            return castFor.cast(original);
        } catch (ClassCastException ex) {
            throw new DSLException(String.format("original %s cast to %s failed", original.getClass().getName(), castFor.getName()), ex);
        }
    }

    /**
     * sub-class support reality operator type
     *
     * @return the operator type
     */
    Class<O> getRealityOperatorType();
}
