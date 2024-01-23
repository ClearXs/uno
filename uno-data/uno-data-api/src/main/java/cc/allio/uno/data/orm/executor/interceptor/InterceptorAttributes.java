package cc.allio.uno.data.orm.executor.interceptor;

import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandType;
import cc.allio.uno.data.orm.dsl.Operator;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterceptorAttributes {

    private CommandExecutor commandExecutor;
    private Operator<?> operator;
    private CommandType commandType;

    private Object result;

    public InterceptorAttributes(CommandExecutor commandExecutor, Operator<?> operator, CommandType commandType) {
        this.commandExecutor = commandExecutor;
        this.operator = operator;
        this.commandType = commandType;
    }

    public InterceptorAttributes(CommandExecutor commandExecutor, Operator<?> operator, CommandType commandType, Object result) {
        this.commandExecutor = commandExecutor;
        this.operator = operator;
        this.commandType = commandType;
        this.result = result;
    }

    public InterceptorAttributes(InterceptorAttributes attributes, Object result) {
        this.commandExecutor = attributes.commandExecutor;
        this.operator = attributes.operator;
        this.commandType = attributes.commandType;
        this.result = result;
    }
}
