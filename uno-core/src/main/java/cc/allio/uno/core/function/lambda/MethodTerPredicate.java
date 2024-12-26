package cc.allio.uno.core.function.lambda;

/**
 * 基于函数式接口的Lambda实现
 * <p>该接口包含能够获取Lambda方法名、字段名、Lambda方法的序列化信息等。</p>
 *
 * @author j.x
 * @since 1.1.7
 */
public interface MethodTerPredicate<T, U, P> extends LambdaMethod {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param p the third input argument
     * @return {@code true} if the input arguments match the predicate,
     * otherwise {@code false}
     */
    boolean test(T t, U u, P p);
}
