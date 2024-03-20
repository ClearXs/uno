package cc.allio.uno.core.metadata.endpoint;

import cc.allio.uno.core.metadata.endpoint.source.*;
import cc.allio.uno.core.metadata.Metadata;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.function.Consumer;

/**
 * <b>数据接入的端点。</b>
 * <p>
 * 使其称为Spring-Bean，在Bean初始化时调用{@link #open()}方法，对{@link AggregationSource#registerSources(Source[])} 注册的数据源进行注册
 * </p>
 *
 * @author j.x
 * @date 2022/9/27 15:14
 * @since 1.1.0
 */
public interface Endpoint<T extends Metadata> extends AggregationSource<T, JsonSource>, InitializingBean, DisposableBean, ApplicationContextAware {

    /**
     * 设置数据源搜集器
     *
     * @param collector 搜集器实例
     */
    void setCollector(SourceCollector<T> collector);

    /**
     * 获取收集器
     *
     * @return SourceCollector
     */
    SourceCollector<T> getCollector();

    /**
     * 设置数据源转换器
     *
     * @param converter 转换器实例
     */
    void setConverter(SourceConverter<T> converter);

    /**
     * 获取转换器
     *
     * @return SourceConverter
     */
    SourceConverter<T> getConverter();

    /**
     * 端点初始化时进行调用
     */
    void open() throws Exception;

    /**
     * 端点关闭时进行调用
     */
    void close() throws Exception;

    @Override
    default void afterPropertiesSet() throws Exception {
        open();
    }

    @Override
    default void destroy() throws Exception {
        close();
    }

    @Override
    default void subscribe(Consumer<T> next) {
        throw new UnsupportedOperationException("Endpoint un support operation");
    }

    @Override
    default void register(ApplicationContext context) {
        throw new UnsupportedOperationException("Endpoint un support operation");
    }
}
