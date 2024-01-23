package cc.allio.uno.data.orm.executor;

/**
 * 执行命令
 *
 * @author jiangwei
 * @date 2023/4/14 13:49
 * @since 1.1.4
 */
public enum CommandType {
    UNKNOWN,
    CREATE_TABLE,
    DELETE_TABLE,
    EXIST_TABLE,
    INSERT,
    UPDATE,
    DELETE,
    SELECT,
    FLUSH,
    SHOW_COLUMNS,
    SHOW_TABLES
}
