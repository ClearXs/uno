package cc.allio.uno.data.orm.dialect;

import cc.allio.uno.data.orm.dialect.func.FuncRegistry;
import cc.allio.uno.data.orm.dialect.func.ReuseFuncFactory;
import cc.allio.uno.data.orm.type.TypeRegistry;

/**
 * dialect抽象层
 *
 * @author jiangwei
 * @date 2023/1/5 18:49
 * @since 1.1.4
 */
public abstract class AbstractDialect implements Dialect {

    protected final TypeRegistry typeRegistry = TypeRegistry.getInstance();
    protected final FuncRegistry funcRegistry;

    protected AbstractDialect() {
        this.funcRegistry = new FuncRegistry();
        // registry general func
        funcRegistry.register(ReuseFuncFactory.count(this));
        funcRegistry.register(ReuseFuncFactory.min(this));
        funcRegistry.register(ReuseFuncFactory.max(this));
        funcRegistry.register(ReuseFuncFactory.avg(this));
        funcRegistry.register(ReuseFuncFactory.sum(this));
        // 触发子类 func
        triggerInitFunc();
        // 初始化type
        triggerInitType();
    }

    @Override
    public FuncRegistry getFuncRegistry() {
        return funcRegistry;
    }

    @Override
    public TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    /**
     * 触发初始化Func实例
     */
    protected abstract void triggerInitFunc();

    /**
     * 触发初始化Type实例
     */
    protected abstract void triggerInitType();
}
