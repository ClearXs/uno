package cc.allio.uno.data.orm.dsl;

/**
 * not belong to 'ddl' and 'dml'
 *
 * @author j.x
 * @date 2024/7/25 18:33
 * @since 1.1.9
 */
public interface UnrecognizedOperator<T extends UnrecognizedOperator<T>> extends Operator<T> {
}
