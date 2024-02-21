package cc.allio.uno.data.orm.executor.options;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 执行器相关参数集合
 *
 * @author jiangwei
 * @date 2024/1/8 10:30
 * @since 1.1.6
 */
public final class ExecutorOptionsImpl extends SPIExecutorOptionsResultHandlerSet implements ExecutorOptions {

    private final List<Interceptor> interceptors = Lists.newCopyOnWriteArrayList();
    private final Map<String, Object> properties = Maps.newConcurrentMap();

    public ExecutorOptionsImpl(@NotNull DBType dbType, @NotNull ExecutorKey executorKey, @NotNull OperatorKey operatorKey) {
        this(IdGenerator.defaultGenerator().getNextIdAsString(), dbType, executorKey, operatorKey);
    }

    public ExecutorOptionsImpl(@NotNull String key, @NotNull DBType dbType, @NotNull ExecutorKey executorKey, @NotNull OperatorKey operatorKey) {
        super();
        setKey(key);
        putAttribute(DB_TYPE_MARK, dbType);
        putAttribute(EXECUTOR_KEY_MARK, executorKey);
        putAttribute(OPERATOR_KEY_MARK, operatorKey);
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
    public void putAttribute(String key, Object obj) {
        this.properties.put(key, obj);
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        throw Exceptions.unOperate("getApplicationContext");
    }

    @Override
    public Map<String, Object> getAll() {
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
}
