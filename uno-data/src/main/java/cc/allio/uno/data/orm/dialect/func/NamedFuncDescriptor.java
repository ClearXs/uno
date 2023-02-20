package cc.allio.uno.data.orm.dialect.func;

import cc.allio.uno.data.orm.dialect.Dialect;
import lombok.Getter;

import java.util.Optional;

/**
 * 指定函数名称name
 *
 * @author jiangwei
 * @date 2023/1/12 15:37
 * @since 1.1.4
 */
public class NamedFuncDescriptor implements FuncDescriptor {

    // 函数名称
    private final String funcName;
    // 函数类型
    private final FuncType funcType;
    // dialect
    @Getter
    private final Dialect dialect;
    private final FuncRendering rendering;

    @Getter
    private final String signature;

    protected NamedFuncDescriptor(String funcName, String signature, FuncType funcType, Dialect dialect, FuncRendering rendering) {
        this.funcName = funcName;
        this.signature = signature;
        this.funcType = funcType;
        this.dialect = dialect;
        this.rendering = rendering;
    }

    @Override
    public String getFuncName() {
        return funcName;
    }

    @Override
    public Func createFunc() {
        return null;
    }

    @Override
    public FuncType getFuncType() {
        return funcType;
    }

    @Override
    public String render(Object[] arguments) {
        return rendering.render(this, Optional.ofNullable(arguments).orElse(new Object[]{}));
    }
}
