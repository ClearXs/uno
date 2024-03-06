package cc.allio.uno.data.orm.executor.options;

import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.data.orm.executor.handler.*;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * 基于SPI机制，获取每一个类型处理器的实例，如果没有值则采用默认处理器
 *
 * @author jiangwei
 * @date 2024/2/14 16:12
 * @since 1.1.7
 */
public abstract class SPIExecutorOptionsResultHandlerSet implements ExecutorOptions {

    private final Map<Class<? extends ResultHandler>, ResultHandler> handlerSets;
    private final ClassLoader classLoader;

    protected SPIExecutorOptionsResultHandlerSet() {
        this.handlerSets = Maps.newConcurrentMap();
        this.classLoader = this.getClass().getClassLoader();
    }

    @Override
    public BoolResultHandler obtainBoolResultHandler() {
        return obtainSPIHandler(
                BoolResultHandler.class,
                () -> {
                    BoolResultHandler boolResultHandler = new BoolResultHandler();
                    boolResultHandler.setExecutorOptions(this);
                    return boolResultHandler;
                });
    }

    @Override
    public <B> BeanResultSetHandler<B> obtainBeanResultSetHandler(Class<B> beanClass) {
        BeanResultHandlerDelegate beanResultHandler = obtainSPIHandler(BeanResultHandlerDelegate.class, () -> new BeanResultHandlerDelegate(this));
        return beanResultHandler.obtainBeanResultSetHandler(beanClass, BeanResultSetHandler.class, () -> new BeanResultSetHandler<>(beanClass));
    }

    @Override
    public MapResultSetHandler obtainMapResultSetHandler() {
        return obtainSPIHandler(
                MapResultSetHandler.class,
                () -> {
                    MapResultSetHandler mapResultSetHandler = new MapResultSetHandler();
                    mapResultSetHandler.setExecutorOptions(this);
                    return mapResultSetHandler;
                });
    }

    @Override
    public DefaultResultSetHandler obtainDefaultResultSetHandler() {
        return obtainSPIHandler(
                DefaultResultSetHandler.class,
                () -> {
                    DefaultResultSetHandler defaultResultSetHandler = new DefaultResultSetHandler();
                    defaultResultSetHandler.setExecutorOptions(this);
                    return defaultResultSetHandler;
                });
    }

    @Override
    public <R> ListBeanResultSetHandler<R> obtainListBeanResultSetHandler(Class<R> beanClass) {
        ListBeanResultHandlerDelegate listBeanResultHandlerDelegate = obtainSPIHandler(ListBeanResultHandlerDelegate.class, () -> new ListBeanResultHandlerDelegate(this));
        return listBeanResultHandlerDelegate.obtainListBeanResultSetHandler(beanClass, ListBeanResultSetHandler.class, () -> new ListBeanResultSetHandler<>(beanClass));
    }

    @Override
    public DefaultListResultSetHandler obtainDefaultListResultSetHandler() {
        return obtainSPIHandler(
                DefaultListResultSetHandler.class,
                () -> {
                    DefaultListResultSetHandler defaultListResultSetHandler = new DefaultListResultSetHandler();
                    defaultListResultSetHandler.setExecutorOptions(this);
                    return defaultListResultSetHandler;
                });

    }

    @Override
    public ListMapResultHandler obtainListMapResultHandler() {
        return obtainSPIHandler(
                ListMapResultHandler.class,
                () -> {
                    ListMapResultHandler listMapResultHandler = new ListMapResultHandler();
                    listMapResultHandler.setExecutorOptions(this);
                    return listMapResultHandler;
                });

    }

    @Override
    public ColumnDefListResultSetHandler obtainColumnDefListResultSetHandler() {
        return obtainSPIHandler(
                ColumnDefListResultSetHandler.class,
                () -> {
                    ColumnDefListResultSetHandler columnDefListResultSetHandler = new ColumnDefListResultSetHandler();
                    columnDefListResultSetHandler.setExecutorOptions(this);
                    return columnDefListResultSetHandler;
                });
    }

    @Override
    public TableListResultSetHandler obtainTableListResultSetHandler() {
        return obtainSPIHandler(
                TableListResultSetHandler.class,
                () -> {
                    TableListResultSetHandler tableListResultSetHandler = new TableListResultSetHandler();
                    tableListResultSetHandler.setExecutorOptions(this);
                    return tableListResultSetHandler;
                });
    }

    @Override
    public <H extends ResultHandler> void customizeResultHandler(Class<H> handlerType, H handler) {
        forcePut(handlerType, handler);
    }

    @Override
    public <B, H extends BeanResultHandler<B>> void customizeBeanResultHandler(Class<B> beanClass, Class<H> handlerType, H handler) {
        BeanResultHandlerDelegate beanResultHandler = obtainSPIHandler(BeanResultHandlerDelegate.class, () -> new BeanResultHandlerDelegate(this));
        beanResultHandler.forcePut(beanClass, handler);
    }

    /**
     * 从SPI中加载Handler，如果为null则返回默认值
     *
     * @param handlerClass   handlerClass
     * @param defaultHandler defaultHandler
     * @param <H>            处理器类型
     * @return Handler 实例
     */
    private <H extends ResultHandler> H obtainSPIHandler(Class<H> handlerClass, Supplier<H> defaultHandler) {
        return (H) handlerSets.computeIfAbsent(
                handlerClass,
                k -> {
                    ServiceLoader<H> boolResultHandlers = ServiceLoader.load(handlerClass, classLoader);
                    H handler = boolResultHandlers.stream()
                            .findFirst()
                            .map(ServiceLoader.Provider::get)
                            .orElse(null);
                    if (handler != null) {
                        handler.setExecutorOptions(this);
                        return handler;
                    }
                    return defaultHandler.get();
                });
    }

    /**
     * 基于{@link Class#getConstructors()}创建handler
     *
     * @param handlerClass   handlerClass
     * @param defaultHandler defaultHandler
     * @param <H>            处理器类型
     * @return Handler 实例
     */
    private <H extends ResultHandler> H obtainConstructorHandler(Class<H> handlerClass, Supplier<H> defaultHandler, Object[] params) {
        return (H) handlerSets.computeIfAbsent(
                handlerClass,
                k -> {
                    H handler = ClassUtils.newInstance(handlerClass, params);
                    if (handler != null) {
                        handler.setExecutorOptions(this);
                        return handler;
                    }
                    return defaultHandler.get();
                });
    }

    /**
     * 强制添加指定类型的handler
     *
     * @param handlerClass handlerClass
     * @param handler      handler
     * @param <H>          处理器类型
     */
    private <H extends ResultHandler> void forcePut(Class<H> handlerClass, H handler) {
        handlerSets.put(handlerClass, handler);
    }

    /**
     * bean 结果集处理器代理。其内部用于维护bean class与对应handler的关系
     */
    public static class BeanResultHandlerDelegate extends ExecutorOptionsAwareImpl implements BeanResultHandler {

        private final SPIExecutorOptionsResultHandlerSet executorResultHandlerSet;
        private final Map<Class<?>, BeanResultHandler<?>> beanHandlers;

        public BeanResultHandlerDelegate(SPIExecutorOptionsResultHandlerSet executorResultHandlerSet) {
            this.executorResultHandlerSet = executorResultHandlerSet;
            this.beanHandlers = Maps.newConcurrentMap();
        }

        /**
         * 获取bean result handler
         *
         * @param beanClass    bean class
         * @param defaultValue 默认值
         * @param <B>          实体类型
         * @param <H>          bean handler type
         * @return BeanResultSetHandler
         */
        <B, H extends BeanResultHandler<B>> H obtainBeanResultSetHandler(Class<B> beanClass, Class<H> beanResultHandlerClass, Supplier<H> defaultValue) {
            return (H) beanHandlers.computeIfAbsent(
                    beanClass,
                    k -> {
                        H handler = executorResultHandlerSet.obtainConstructorHandler(beanResultHandlerClass, defaultValue, new Object[]{beanClass});
                        handler.setExecutorOptions(executorResultHandlerSet);
                        return handler;
                    });
        }

        /**
         * 强制put bean class handler
         *
         * @param beanClass beanClass
         * @param handler   handler
         * @param <B>       实体类型
         * @param <H>       bean handler type
         */
        public <B, H extends BeanResultHandler<B>> void forcePut(Class<B> beanClass, H handler) {
            beanHandlers.put(beanClass, handler);
        }
    }

    public static class ListBeanResultHandlerDelegate extends ExecutorOptionsAwareImpl implements ListBeanResultHandler {

        private final SPIExecutorOptionsResultHandlerSet executorResultHandlerSet;
        private final Map<Class<?>, BeanResultHandler<?>> beanHandlers;

        public ListBeanResultHandlerDelegate(SPIExecutorOptionsResultHandlerSet executorResultHandlerSet) {
            this.executorResultHandlerSet = executorResultHandlerSet;
            this.beanHandlers = Maps.newConcurrentMap();
        }

        /**
         * 获取list bean result handler
         *
         * @param beanClass    bean class
         * @param defaultValue 默认值
         * @param <B>          实体类型
         * @param <H>          bean handler type
         * @return BeanResultSetHandler
         */
        <B, H extends ListBeanResultHandler<B>> H obtainListBeanResultSetHandler(Class<B> beanClass, Class<H> beanResultHandlerClass, Supplier<H> defaultValue) {
            return (H) beanHandlers.computeIfAbsent(
                    beanClass,
                    k -> {
                        H handler = executorResultHandlerSet.obtainConstructorHandler(beanResultHandlerClass, defaultValue, new Object[]{beanClass});
                        handler.setExecutorOptions(executorResultHandlerSet);
                        return handler;
                    });
        }
    }
}
