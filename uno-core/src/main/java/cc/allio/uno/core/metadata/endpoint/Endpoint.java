package cc.allio.uno.core.metadata.endpoint;

import cc.allio.uno.core.metadata.Metadata;
import cc.allio.uno.core.metadata.endpoint.source.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Optional;

/**
 * <b>数据接入的端点。</b>
 * <p>
 * 使其称为Spring-Bean，在Bean初始化时调用{@link #open()}方法，对{@link #registerSource(JsonSource...)}注册的数据源进行注册
 * </p>
 *
 * @author jiangwei
 * @date 2022/9/27 15:14
 * @since 1.1.0
 */
public interface Endpoint<T extends Metadata> extends InitializingBean, DisposableBean {

    /**
     * 注册数据源对象
     *
     * @param source 数据源对象实例
     */
    void registerSource(JsonSource... source);

    /**
     * 获取已经注册的所有数据源
     *
     * @return 数据源集合
     */
    List<JsonSource> getSources();

    /**
     * 根据指定类型的获取对应的数据源
     *
     * @return 数据源
     */
    <S extends JsonSource> Optional<S> getSource(Class<S> sourceClass);

    /**
     * 设置数据源搜集器
     *
     * @param collector 搜集器实例
     */
    void setCollector(SourceCollector<T> collector);

    /**
     * 设置数据源转换器
     *
     * @param converter 转换器实例
     */
    void setConverter(SourceConverter<T> converter);

    /**
     * 端点初始化时进行调用
     */
    void open();

    /**
     * 端点关闭时进行调用
     */
    void close();

    @Override
    default void afterPropertiesSet() throws Exception {
        open();
    }

    @Override
    default void destroy() throws Exception {
        close();
    }
}
