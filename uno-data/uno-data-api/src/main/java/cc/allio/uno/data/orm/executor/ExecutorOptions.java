package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.dsl.type.DBType;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 执行器相关参数集合
 *
 * @author jiangwei
 * @date 2024/1/8 10:30
 * @since 1.1.6
 */
@Getter
public final class ExecutorOptions {

    private final List<Interceptor> interceptors = Lists.newArrayList();

    @Setter
    private DBType dbType = DBType.H2;

    @Setter
    private ExecutorKey executorKey;

    @Setter
    private OperatorKey operatorKey;

    /**
     * 添加拦截器
     *
     * @param interceptor interceptor
     * @return ExecutorOptions
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
}
