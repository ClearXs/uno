package cc.allio.uno.core.function.lambda;

/**
 * 继承或实现都需要实现{@link java.io.Serializable}，{@link java.io.NotSerializableException} 当实体没有实现{@link java.io.Serializable}时抛出
 *
 * @author jiangwei
 * @date 2024/1/26 18:26
 * @since 1.1.7
 */
public interface LambdaMethod {

    /**
     * 获取Method名称
     *
     * @return Column名称
     */
    default String getMethodName() {
        return getLambda().getMethodName();
    }

    /**
     * 获取字段名
     */
    default String getFieldName() {
        return getLambda().getFieldName();
    }

    /**
     * 获取 lambda实例
     */
    default SerializedLambda getLambda() {
        return SerializedLambda.of(this);
    }
}
