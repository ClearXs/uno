package cc.allio.uno.data.orm.executor;

/**
 * a marked interface, collect to DDL and DML command operator
 *
 * @author j.x
 * @see DDLCommandExecutor
 * @see DMLCommandExecutor
 * @since 1.1.7
 */
public interface AggregateCommandExecutor extends DDLCommandExecutor, DMLCommandExecutor {
}
