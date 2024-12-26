package cc.allio.uno.core.metadata.endpoint.source;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

/**
 * 可批量订阅数据源
 *
 * @author j.x
 * @since 1.1.4
 */
public interface AggregationSource<T, C extends Source<?>> extends Source<T> {

    /**
     * 注册数据源对象
     *
     * @param source source
     */
    void registerSource(C source);

    /**
     * 注册数据源对象
     *
     * @param sources 数据源对象实例
     */
    default void registerSources(C[] sources) {
        registerSources(Lists.newArrayList(sources));
    }

    /**
     * 注册数据源对象
     *
     * @param sources 数据源对象实例
     */
    void registerSources(List<C> sources);

    /**
     * 获取已经注册的所有数据源
     *
     * @return 数据源集合
     */
    List<C> getSources();

    /**
     * 根据指定类型的获取对应的数据源
     *
     * @return 数据源
     */
    <S extends Source<?>> Optional<S> getSource(Class<S> sourceClass);
}
