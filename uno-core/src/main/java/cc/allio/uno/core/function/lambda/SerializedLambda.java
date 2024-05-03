package cc.allio.uno.core.function.lambda;

import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 参考自{@link java.lang.invoke.SerializedLambda}
 *
 * @author j.x
 * @date 2023/1/5 15:04
 * @see MethodReference
 * @see StaticMethodReference
 * @since 1.1.4
 */
@Getter
public class SerializedLambda implements Serializable {

    private static final Map<Class<?>, SerializedLambda> cache = Maps.newConcurrentMap();

    private static final long serialVersionUID = 8025925345765570181L;
    private final String capturingClass;
    private final String functionalInterfaceClass;
    private final String functionalInterfaceMethodName;
    private final String functionalInterfaceMethodSignature;
    private final String implClass;
    private final String implMethodName;
    private final String implMethodSignature;
    private final int implMethodKind;
    private final String instantiatedMethodType;
    private Object[] capturedArgs;

    private SerializedLambda(java.lang.invoke.SerializedLambda serializedLambda) {
        this(
                serializedLambda.getCapturingClass(),
                serializedLambda.getFunctionalInterfaceClass(),
                serializedLambda.getFunctionalInterfaceMethodName(),
                serializedLambda.getFunctionalInterfaceMethodSignature(),
                serializedLambda.getImplClass(),
                serializedLambda.getImplMethodName(),
                serializedLambda.getImplMethodSignature(),
                serializedLambda.getImplMethodKind(),
                serializedLambda.getInstantiatedMethodType(),
                null);
        int capturedArgCount = serializedLambda.getCapturedArgCount();
        Object[] capturedArgs = new Object[capturedArgCount];
        for (int i = 0; i < capturedArgCount; i++) {
            capturedArgs[i] = serializedLambda.getCapturedArg(i);
        }
        this.capturedArgs = capturedArgs;
    }

    private SerializedLambda(
            String capturingClass,
            String functionalInterfaceClass,
            String functionalInterfaceMethodName,
            String functionalInterfaceMethodSignature,
            String implClass,
            String implMethodName,
            String implMethodSignature,
            int implMethodKind,
            String instantiatedMethodType,
            Object[] capturedArgs) {
        this.capturingClass = capturingClass;
        this.functionalInterfaceClass = functionalInterfaceClass;
        this.functionalInterfaceMethodName = functionalInterfaceMethodName;
        this.functionalInterfaceMethodSignature = functionalInterfaceMethodSignature;
        this.implClass = implClass;
        this.implMethodName = implMethodName;
        this.implMethodSignature = implMethodSignature;
        this.implMethodKind = implMethodKind;
        this.instantiatedMethodType = instantiatedMethodType;
        this.capturedArgs = capturedArgs;
    }

    /**
     * 获取方法名称
     *
     * @return 方法名称
     */
    public String getMethodName() {
        return this.implMethodName;
    }

    /**
     * 获取POJO方法对应的字段名称，如果不是POJO那么返回方法名
     *
     * @return 字段名 or 方法名
     */
    public String getFieldName() {
        String methodName = getMethodName();
        if (methodName.startsWith("get")) {
            return StringUtils.toLowerCaseFirstOne(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            return StringUtils.toLowerCaseFirstOne(methodName.substring(2));
        }
        return methodName;
    }

    /**
     * 使Lambda表达实例转换为{@link SerializedLambda}对象
     *
     * @param lambda lambda实例
     * @return SerializedLambda实例
     */
    public static SerializedLambda of(Object lambda) {
        return cache.computeIfAbsent(
                lambda.getClass(),
                clazz -> {
                    Method writeReplace = ClassUtils.getMethod(clazz, "writeReplace");
                    SerializedLambda serializedLambda;
                    try {
                        ClassUtils.setAccessible(writeReplace);
                        serializedLambda = new SerializedLambda((java.lang.invoke.SerializedLambda) writeReplace.invoke(lambda));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new UnsupportedOperationException(e);
                    }
                    if (serializedLambda.getMethodName().startsWith("lambda$")) {
                        throw new UnsupportedOperationException("请使用方法引用,例如: UserEntity::getName");
                    }
                    return serializedLambda;
                });
    }
}
