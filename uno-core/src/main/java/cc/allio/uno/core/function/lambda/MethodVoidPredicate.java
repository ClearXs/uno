package cc.allio.uno.core.function.lambda;

/**
 * 基于函数式接口的Lambda实现
 * <p>该接口包含能够获取Lambda方法名、字段名、Lambda方法的序列化信息等。</p>
 *
 * @author j.x
 * @since 1.1.7
 */
@FunctionalInterface
public interface MethodVoidPredicate extends LambdaMethod {

    boolean test();
}
