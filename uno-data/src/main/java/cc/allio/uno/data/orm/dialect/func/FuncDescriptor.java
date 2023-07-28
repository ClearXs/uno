package cc.allio.uno.data.orm.dialect.func;

import cc.allio.uno.data.orm.dialect.Dialect;

/**
 * 描述某个指定的SQL aggregate
 *
 * @author jiangwei
 * @date 2023/1/12 15:39
 * @since 1.1.4
 */
public interface FuncDescriptor {

    /**
     * 获取Func name
     *
     * @return func name
     */
    String getFuncName();

    /**
     * 创建Func实例
     *
     * @return Func实例
     */
    Func createFunc();

    /**
     * 获取func签名
     *
     * @return 签名
     */
    String getSignature();

    /**
     * 获取func 类型
     *
     * @return 函数类型
     */
    FuncType getFuncType();

    /**
     * 获取数据库方言对象
     *
     * @return dialect instance
     */
    Dialect getDialect();

    /**
     * 渲染当前函数为表达式字符串
     *
     * @param arguments 渲染过程中动态参数
     * @return 表达式
     * @see FuncRendering#render(FuncDescriptor, Object[])
     */
    String render(Object[] arguments);

}
