package cc.allio.uno.data.orm.executor.handler;

/**
 * 定义{@link cc.allio.uno.data.orm.executor.CommandExecutor}结果处理器集合
 *
 * @author j.x
 * @date 2024/2/14 16:02
 * @since 1.1.7
 */
public interface ExecutorResultHandlerSet {

    /**
     * 获取{@code boolean}结果处理器
     *
     * @return BoolResultHandler
     */
    BoolResultHandler obtainBoolResultHandler();

    /**
     * 设置{@link BoolResultHandler}处理器
     *
     * @param boolResultHandler boolResultHandler
     */
    default void setBoolResultHandler(BoolResultHandler boolResultHandler) {
        customizeResultHandler(BoolResultHandler.class, boolResultHandler);
    }

    /**
     * 获取实体结果处理器
     *
     * @param beanClass beanClass 实体class对象
     * @param <R>       实体类型
     * @return BeanResultSetHandler
     */
    <R> BeanResultSetHandler<R> obtainBeanResultSetHandler(Class<R> beanClass);

    /**
     * 设置{@link BeanResultSetHandler}处理器
     *
     * @param beanClass            bean type
     * @param beanResultSetHandler beanResultSetHandler
     * @param <R>                  实体类型
     */
    default <R> void setBeanResultSetHandler(Class<R> beanClass, BeanResultSetHandler<R> beanResultSetHandler) {
        customizeBeanResultHandler(beanClass, BeanResultSetHandler.class, beanResultSetHandler);
    }

    /**
     * 获取map结果处理器
     *
     * @return MapResultSetHandler
     */
    MapResultSetHandler obtainMapResultSetHandler();

    /**
     * 设置{@link MapResultSetHandler}
     *
     * @param mapResultSetHandler mapResultSetHandler
     */
    default void setMapResultSetHandler(MapResultSetHandler mapResultSetHandler) {
        customizeResultHandler(MapResultSetHandler.class, mapResultSetHandler);
    }

    /**
     * 获取默认结果处理器
     *
     * @return DefaultResultSetHandler
     */
    DefaultResultSetHandler obtainDefaultResultSetHandler();

    /**
     * 设置{@link DefaultResultSetHandler}
     *
     * @param defaultResultSetHandler defaultResultSetHandler
     */
    default void setDefaultResultSetHandler(DefaultResultSetHandler defaultResultSetHandler) {
        customizeResultHandler(DefaultResultSetHandler.class, defaultResultSetHandler);
    }

    /**
     * 获取list实体结果处理器
     *
     * @param beanClass beanClass 实体class对象
     * @param <R>       实体类型
     * @return ListBeanResultSetHandler
     */
    <R> ListBeanResultSetHandler<R> obtainListBeanResultSetHandler(Class<R> beanClass);

    /**
     * 设置{@link ListBeanResultSetHandler}
     *
     * @param <R>                      实体类型
     * @param beanClass                beanClass
     * @param listBeanResultSetHandler listBeanResultSetHandler
     */
    default <R> void setListBeanResultSetHandler(Class<R> beanClass, ListBeanResultSetHandler<R> listBeanResultSetHandler) {
        customizeBeanResultHandler(beanClass, ListBeanResultSetHandler.class, listBeanResultSetHandler);
    }

    /**
     * 获取默认list结果处理器
     *
     * @return DefaultListResultSetHandler
     */
    DefaultListResultSetHandler obtainDefaultListResultSetHandler();

    /**
     * 设置{@link DefaultListResultSetHandler}
     *
     * @param defaultListResultSetHandler defaultListResultSetHandler
     */
    default void setDefaultListResultSetHandler(DefaultListResultSetHandler defaultListResultSetHandler) {
        customizeResultHandler(DefaultListResultSetHandler.class, defaultListResultSetHandler);
    }

    /**
     * 获取list map结果处理器
     *
     * @return ListMapResultHandler
     */
    ListMapResultHandler obtainListMapResultHandler();

    /**
     * 设置{@link ListMapResultHandler}
     *
     * @param listMapResultHandler listMapResultHandler
     */
    default void setListMapResultHandler(ListMapResultHandler listMapResultHandler) {
        customizeResultHandler(ListMapResultHandler.class, listMapResultHandler);
    }

    /**
     * 获取列定义结果处理器
     *
     * @return ColumnDefListResultSetHandler
     */
    ColumnDefListResultSetHandler obtainColumnDefListResultSetHandler();

    /**
     * 设置{@link ColumnDefListResultSetHandler}
     *
     * @param columnDefListResultSetHandler columnDefListResultSetHandler
     */
    default void setColumnDefListResultSetHandler(ColumnDefListResultSetHandler columnDefListResultSetHandler) {
        customizeResultHandler(ColumnDefListResultSetHandler.class, columnDefListResultSetHandler);
    }

    /**
     * 获取表定义结果处理器
     *
     * @return TableListResultSetHandler
     */
    TableListResultSetHandler obtainTableListResultSetHandler();

    /**
     * 设置{@link TableListResultSetHandler}
     *
     * @param tableListResultSetHandler tableListResultSetHandler
     */
    default void setTableListResultSetHandler(TableListResultSetHandler tableListResultSetHandler) {
        customizeResultHandler(TableListResultSetHandler.class, tableListResultSetHandler);
    }

    /**
     * 自定义{@link ResultHandler}
     *
     * @param handlerType handler 类型
     * @param handler     handler 实例
     * @param <H>         ResultHandler类型
     */
    <H extends ResultHandler> void customizeResultHandler(Class<H> handlerType, H handler);

    /**
     * 自定义{@link BeanResultSetHandler}
     *
     * @param beanClass   bean 类型
     * @param handlerType handlerType
     * @param handler     handler
     * @param <B>         bean 类型
     * @param <H>         handler 类型
     */
    <B, H extends BeanResultHandler<B>> void customizeBeanResultHandler(Class<B> beanClass, Class<H> handlerType, H handler);
}
