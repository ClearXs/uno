package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.Operator;

/**
 * 定义DSL命令类型执行器
 *
 * @author jiangwei
 * @date 2023/5/29 20:41
 * @since 1.1.4
 */
public interface CommandTypeExecutor<R> {

    /**
     * 执行类型
     *
     * @param operator         操作器
     * @param resultSetHandler 结果集处理器
     * @return R
     * @throws Throwable 执行发生异常时抛出
     */
    R exec(Operator<?> operator, ResultSetHandler<R> resultSetHandler) throws Throwable;
}
