package cc.allio.uno.core.metadata.convert;

import cc.allio.uno.core.bean.BeanWrapper;
import cc.allio.uno.core.metadata.Metadata;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * 转换器工厂
 *
 * @author j.x
 * @date 2022/9/27 23:07
 * @since 1.1.0
 */
public class ConverterFactory {

    private ConverterFactory() {
    }

    /**
     * 创建Converter实例对象根据某个具体的Sequential类型
     *
     * @return Converter实例
     */
    public static <T extends Metadata> DefaultConverter<T> createConverter() {
        return new DefaultConverter<>();
    }

    /**
     * 创建Converter实例对象根据某个具体的Sequential类型
     *
     * @param type Metadata类型
     * @return Converter实例
     */
    public static <T extends Metadata> DefaultConverter<T> createConverter(Class<T> type) {
        return new DefaultConverter<>(type);
    }

    /**
     * 根据Sequential类型与增加使用方操作赋值动作
     *
     * @param type     Metadata类型
     * @param consumer Function数据
     * @return Converter实例
     */
    public static <T extends Metadata> FunctionConverter<T> createConverter(Class<T> type, Consumer<BeanWrapper> consumer) {
        return new FunctionConverter<>(type, consumer);
    }

    /**
     * 进行赋值动作时增加使用方进行操作
     */
    public static class FunctionConverter<T extends Metadata> extends AbstractJsonConverter<T> {
        private final Consumer<BeanWrapper> consumer;

        public FunctionConverter(Class<? extends T> convertType, Consumer<BeanWrapper> consumer) {
            super(convertType);
            this.consumer = consumer;
        }

        @Override
        protected Mono<Void> executeAssignmentDefaultAction(T metadata, BeanWrapper wrapper) {
            consumer.accept(wrapper);
            return Mono.empty();
        }
    }

    public static class DefaultConverter<T extends Metadata> extends AbstractJsonConverter<T> {

        public DefaultConverter() {
        }

        public DefaultConverter(Class<? extends T> convertType) {
            super(convertType);
        }

        @Override
        protected Mono<Void> executeAssignmentDefaultAction(T metadata, BeanWrapper wrapper) {
            // TODO NOTHING
            return Mono.empty();
        }

    }
}
