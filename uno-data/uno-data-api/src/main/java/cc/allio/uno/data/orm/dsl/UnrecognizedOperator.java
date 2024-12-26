package cc.allio.uno.data.orm.dsl;

/**
 * not belong to 'ddl' and 'dml'
 *
 * @author j.x
 * @since 1.1.9
 */
public interface UnrecognizedOperator<T extends UnrecognizedOperator<T>> extends Operator<T> {
}
