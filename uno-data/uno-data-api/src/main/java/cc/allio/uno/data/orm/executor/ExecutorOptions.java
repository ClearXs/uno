package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 执行器相关参数集合
 *
 * @author jiangwei
 * @date 2024/1/8 10:30
 * @since 1.1.6
 */
@Getter
public final class ExecutorOptions {

    // 唯一标识，用来在CommandExecutorRegistry中作为标识的
    @Setter
    private final String key;

    private final List<Interceptor> interceptors = Lists.newArrayList();
    private final DBType dbType;
    private final ExecutorKey executorKey;
    private final OperatorKey operatorKey;

    @Setter
    private String address;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String database;

    /**
     * 当前给定的executor options是否是系统默认的
     */
    @Setter
    private boolean systemDefault = false;

    @Setter
    private Map<String, Object> properties;

    public ExecutorOptions(@NotNull DBType dbType, @NotNull ExecutorKey executorKey, @NotNull OperatorKey operatorKey) {
        this(IdGenerator.defaultGenerator().getNextIdAsString(), dbType, executorKey, operatorKey);
    }

    public ExecutorOptions(@NotNull String key, @NotNull DBType dbType, @NotNull ExecutorKey executorKey, @NotNull OperatorKey operatorKey) {
        this.key = key;
        this.dbType = dbType;
        this.executorKey = executorKey;
        this.operatorKey = operatorKey;
    }

    /**
     * 添加拦截器
     *
     * @param interceptor interceptor
     */
    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    /**
     * 添加拦截器
     *
     * @param interceptors interceptors
     */
    public void addInterceptors(List<Interceptor> interceptors) {
        this.interceptors.addAll(interceptors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutorOptions that = (ExecutorOptions) o;
        return Objects.equal(dbType, that.dbType) && Objects.equal(executorKey, that.executorKey) && Objects.equal(operatorKey, that.operatorKey);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dbType, executorKey, operatorKey);
    }
}
