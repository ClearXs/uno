package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.UnrecognizedOperator;
import cc.allio.uno.data.orm.dsl.ddl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 执行命令
 *
 * @author j.x
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
public enum CommandType {
    UNKNOWN(UnrecognizedOperator.class),
    ALERT_TABLE(AlterTableOperator.class),
    CREATE_TABLE(CreateTableOperator.class),
    DELETE_TABLE(DropTableOperator.class),
    EXIST_TABLE(ExistTableOperator.class),
    INSERT(InsertOperator.class),
    UPDATE(UpdateOperator.class),
    DELETE(DeleteOperator.class),
    SELECT(QueryOperator.class),
    FLUSH(null),
    SHOW_COLUMNS(ShowColumnsOperator.class),
    SHOW_TABLES(ShowTablesOperator.class);

    private final Class<? extends Operator> operatorClass;

    /**
     * base on operator class get command type, if {@link CommandType} self operatorClass is null, return null
     *
     * @param o   the operatorClass
     * @param <O> the operator type
     * @return CommandType or {@link #UNKNOWN}
     */
    public static <O extends Operator<?>> CommandType getByOperatorClass(Class<O> o) {
        if (o == null) {
            return null;
        }
        // try get hirachical
        Class<? extends Operator<?>> hireachialType = cc.allio.uno.data.orm.dsl.Operator.getHierarchicalType(o);
        for (CommandType commandType : values()) {
            if (commandType == UNKNOWN || commandType == FLUSH) {
                continue;
            }
            if (commandType.operatorClass.isAssignableFrom(hireachialType)) {
                return commandType;
            }
        }
        return UNKNOWN;
    }
}
