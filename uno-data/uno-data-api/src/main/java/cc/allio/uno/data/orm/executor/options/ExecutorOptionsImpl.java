package cc.allio.uno.data.orm.executor.options;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.internal.SPIInnerCommandScanner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 执行器相关参数集合
 *
 * @author j.x
 * @since 1.1.7
 */
public class ExecutorOptionsImpl extends SPIExecutorOptionsResultHandlerSet implements ExecutorOptions {

    private final List<Interceptor> interceptors = Lists.newCopyOnWriteArrayList();
    private final Map<String, Object> properties = Maps.newConcurrentMap();
    @Getter
    private final SPIInnerCommandScanner scanner;

    private final Map<Class<? extends Meta<?>>, MetaAcceptor<? extends Meta<?>>> acceptorSet;

    public ExecutorOptionsImpl(@NotNull DBType dbType, @NotNull ExecutorKey executorKey, @NotNull OperatorKey operatorKey) {
        this(IdGenerator.defaultGenerator().getNextIdAsString(), dbType, executorKey, operatorKey);
    }

    public ExecutorOptionsImpl(@NotNull String key, @NotNull DBType dbType, @NotNull ExecutorKey executorKey, @NotNull OperatorKey operatorKey) {
        super();
        setKey(key);
        put(DB_TYPE_MARK, dbType);
        put(EXECUTOR_KEY_MARK, executorKey);
        put(OPERATOR_KEY_MARK, operatorKey);
        this.scanner = new SPIInnerCommandScanner(executorKey);
        this.acceptorSet = Maps.newConcurrentMap();
    }

    /**
     * 添加拦截器
     *
     * @param interceptor interceptor
     */
    @Override
    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    /**
     * 添加拦截器
     *
     * @param interceptors interceptors
     */
    @Override
    public void addInterceptors(List<Interceptor> interceptors) {
        this.interceptors.addAll(interceptors);
    }

    @Override
    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(this.properties.get(key));
    }

    @Override
    public void put(String key, Object obj) {
        this.properties.put(key, obj);
    }

    @Override
    public boolean remove(String key) {
        return properties.remove(key) != null;
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        throw Exceptions.unOperate("getApplicationContext");
    }

    @Override
    public Map getAll() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutorOptionsImpl that = (ExecutorOptionsImpl) o;
        return Objects.equal(getDbType(), that.getDbType()) && Objects.equal(getExecutorKey(), that.getExecutorKey()) && Objects.equal(getOperatorKey(), that.getOperatorKey());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDbType(), getExecutorKey(), getOperatorKey());
    }

    @Override
    public <T extends Meta<T>> void customizeMetaAcceptorSetter(Class<T> metaClass, MetaAcceptor<T> metaAcceptor) {
        acceptorSet.put(metaClass, metaAcceptor);
    }

    @Override
    public <T extends Meta<T>> MetaAcceptor<T> customizeMetaAcceptorGetter(Class<T> metaClass) {
        return (MetaAcceptor<T>) acceptorSet.get(metaClass);
    }

    @Override
    public void clear() {
        acceptorSet.clear();
    }
}
