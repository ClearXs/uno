package cc.allio.uno.core.metadata.convert;

import org.springframework.context.ApplicationContext;

/**
 * 时序数据转换器
 *
 * @author jiangwei
 * @date 2022/9/13 11:16
 * @see ConverterFactory
 * @since 1.1.0
 */
public interface Converter<T> {

    /**
     * 执行时序数据转换操作
     *
     * @param context Spring 上下文实例
     * @param value   可能是外部的值
     * @return 给定转换数据实例
     * @throws Throwable 转换执行过程中抛出的异常
     */
    T execute(ApplicationContext context, Object value) throws Throwable;
}
