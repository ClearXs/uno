package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.ddl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.executor.CommandType;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * manage {@link InnerCommandExecutor} entity
 *
 * @author j.x
 * @date 2024/3/15 04:49
 * @see SPIInnerCommandScanner
 * @since 1.1.7
 */
public class InnerCommandExecutorManager {

    private final Map<Class<?>, InnerCommandExecutor<?, ?, ?>> registry;
    private final Map<CommandType, InnerCommandExecutor<?, ?, ?>> commandExecutorMap;

    InnerCommandExecutorManager() {
        this.registry = Maps.newConcurrentMap();
        // thinking commandExecutorMap how to optimize avoid create
        this.commandExecutorMap = Maps.newConcurrentMap();
    }

    /**
     * set inner command executor into registry
     *
     * @param operatorType the operatorType is not null
     * @param executor     the executor is not null
     * @param <R>          {@link InnerCommandExecutor#exec(Operator, ResultSetHandler)} return type
     * @param <O>          {@link InnerCommandExecutor} operator
     */
    public <R, O extends Operator<?>, H> void set(Class<O> operatorType, InnerCommandExecutor<R, O, H> executor) {
        registry.put(operatorType, executor);
        CommandType commandType = CommandType.getByOperatorClass(operatorType);
        if (commandType != null) {
            commandExecutorMap.put(commandType, executor);
        }
    }

    /**
     * get default inner through operator type
     *
     * @param operatorType the operatorType is not null
     * @param <R>          {@link InnerCommandExecutor#exec(Operator, Object)} return type
     * @param <O>          {@link InnerCommandExecutor} operator
     * @return InnerCommandExecutor or null
     * @see InnerDefaultCommandExecutor
     */
    public <R, O extends Operator<?>> InnerDefaultCommandExecutor<R, O> getDefault(Class<O> operatorType) {
        if (InnerDefaultCommandExecutor.class.isAssignableFrom(operatorType)) {
            return (InnerDefaultCommandExecutor<R, O>) registry.get(operatorType);
        }
        return null;
    }

    /**
     * get list inner through operator type
     *
     * @param operatorType the operatorType is not null
     * @param <R>          {@link InnerCommandExecutor#exec(Operator, Object)} return type
     * @param <O>          {@link InnerCommandExecutor} operator
     * @return InnerCommandExecutor or null
     * @see InnerListCommandExecutor
     */
    public <R, O extends Operator<?>> InnerListCommandExecutor<R, O> getList(Class<O> operatorType) {
        if (InnerListCommandExecutor.class.isAssignableFrom(operatorType)) {
            return (InnerListCommandExecutor<R, O>) registry.get(operatorType);
        }
        return null;
    }

    /**
     * get inner through operator type
     *
     * @param operatorType the operatorType is not null
     * @param <R>          {@link InnerCommandExecutor#exec(Operator, Object)} return type
     * @param <O>          {@link InnerCommandExecutor} operator
     * @return InnerCommandExecutor or null
     */
    public <R, O extends Operator<?>, H> InnerCommandExecutor<R, O, H> get(Class<O> operatorType) {
        return (InnerCommandExecutor<R, O, H>) registry.get(operatorType);
    }

    /**
     * get {@link CTOInnerCommandExecutor} if exist
     *
     * @param <O> sub-type {@link CreateTableOperator}
     * @param <E> sub-type {@link CTOInnerCommandExecutor}
     * @return CTOInnerCommandExecutor instance or null
     */
    public <O extends CreateTableOperator, E extends CTOInnerCommandExecutor<O>> E getCreateTable() {
        if (commandExecutorMap.containsKey(CommandType.CREATE_TABLE)) {
            return (E) commandExecutorMap.get(CommandType.CREATE_TABLE);
        }
        return null;
    }

    /**
     * get {@link DOInnerCommandExecutor} if exist
     *
     * @param <O> sub-type {@link DeleteOperator}
     * @param <E> sub-type {@link DOInnerCommandExecutor}
     * @return DOInnerCommandExecutor instance or null
     */
    public <O extends DeleteOperator, E extends DOInnerCommandExecutor<O>> E getDelete() {
        if (commandExecutorMap.containsKey(CommandType.DELETE)) {
            return (E) commandExecutorMap.get(CommandType.DELETE);
        }
        return null;
    }

    /**
     * get {@link DTOInnerCommandExecutor} if exist
     *
     * @param <O> sub-type {@link DropTableOperator}
     * @param <E> sub-type {@link DTOInnerCommandExecutor}
     * @return DTOInnerCommandExecutor instance or null
     */
    public <O extends DropTableOperator, E extends DTOInnerCommandExecutor<O>> E getDeleteTable() {
        if (commandExecutorMap.containsKey(CommandType.DELETE_TABLE)) {
            return (E) commandExecutorMap.get(CommandType.DELETE_TABLE);
        }
        return null;
    }

    /**
     * get {@link ETOInnerCommandExecutor} if exist
     *
     * @param <O> sub-type {@link ExistTableOperator}
     * @param <E> sub-type {@link ETOInnerCommandExecutor}
     * @return ETOInnerCommandExecutor instance or null
     */
    public <O extends ExistTableOperator, E extends ETOInnerCommandExecutor<O>> E getExistTable() {
        if (commandExecutorMap.containsKey(CommandType.EXIST_TABLE)) {
            return (E) commandExecutorMap.get(CommandType.EXIST_TABLE);
        }
        return null;
    }

    /**
     * get {@link IOInnerCommandExecutor} if exist
     *
     * @param <O> sub-type {@link InsertOperator}
     * @param <E> sub-type {@link IOInnerCommandExecutor}
     * @return IOInnerCommandExecutor instance or null
     */
    public <O extends InsertOperator, E extends IOInnerCommandExecutor<O>> E getInsert() {
        if (commandExecutorMap.containsKey(CommandType.INSERT)) {
            return (E) commandExecutorMap.get(CommandType.INSERT);
        }
        return null;
    }

    /**
     * get {@link QOInnerCommandExecutor} if exist
     *
     * @param <R> return type
     * @param <O> sub-type {@link QueryOperator}
     * @param <E> sub-type {@link QOInnerCommandExecutor}
     * @return QOInnerCommandExecutor instance or null
     */
    public <R, O extends QueryOperator, E extends QOInnerCommandExecutor<R, O>> E getQuery() {
        if (commandExecutorMap.containsKey(CommandType.SELECT)) {
            return (E) commandExecutorMap.get(CommandType.SELECT);
        }
        return null;
    }

    /**
     * get {@link SCOInnerCommandExecutor} if exist
     *
     * @param <O> sub-type {@link ShowColumnsOperator}
     * @param <E> sub-type {@link SCOInnerCommandExecutor}
     * @return SCOInnerCommandExecutor instance or null
     */
    public <O extends ShowColumnsOperator, E extends SCOInnerCommandExecutor<O>> E getShowColumn() {
        if (commandExecutorMap.containsKey(CommandType.SHOW_COLUMNS)) {
            return (E) commandExecutorMap.get(CommandType.SHOW_COLUMNS);
        }
        return null;
    }

    /**
     * get {@link UOInnerCommandExecutor} if exist
     *
     * @param <O> sub-type {@link UpdateOperator}
     * @param <E> sub-type {@link UOInnerCommandExecutor}
     * @return UOInnerCommandExecutor instance or null
     */
    public <O extends UpdateOperator, E extends UOInnerCommandExecutor<O>> E getUpdate() {
        if (commandExecutorMap.containsKey(CommandType.UPDATE)) {
            return (E) commandExecutorMap.get(CommandType.UPDATE);
        }
        return null;
    }

    /**
     * get {@link ATOInnerCommandExecutor} if exist
     *
     * @param <O> sub-type {@link UpdateOperator}
     * @param <E> sub-type {@link ATOInnerCommandExecutor}
     * @return ATOInnerCommandExecutor instance or null
     */
    public <O extends AlterTableOperator, E extends ATOInnerCommandExecutor<O>> E getAlter() {
        if (commandExecutorMap.containsKey(CommandType.ALERT_TABLE)) {
            return (E) commandExecutorMap.get(CommandType.ALERT_TABLE);
        }
        return null;
    }

    /**
     * get inner through operator type
     *
     * @param commandType the commandType is not null
     * @param <R>         {@link InnerCommandExecutor#exec(Operator, Object)} return type
     * @param <O>         {@link InnerCommandExecutor} operator
     * @return InnerCommandExecutor or null
     */
    public <R, O extends Operator<?>, H> InnerCommandExecutor<R, O, H> getByCommandType(CommandType commandType) {
        if (commandExecutorMap.containsKey(commandType)) {
            return (InnerCommandExecutor<R, O, H>) commandExecutorMap.get(commandType);
        }
        return null;
    }
}
