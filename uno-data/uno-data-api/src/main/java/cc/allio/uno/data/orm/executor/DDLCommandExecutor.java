package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.*;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * contains DDL relativity command operator.
 * <ul>
 *     <li>{@link AlterTableOperator}</li>
 *     <li>{@link CreateTableOperator}</li>
 *     <li>{@link DropTableOperator}</li>
 *     <li>{@link ExistTableOperator}</li>
 *     <li>{@link ShowColumnsOperator}</li>
 *     <li>{@link ShowTablesOperator}</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/3/15 08:26
 * @since 1.1.7
 */
public interface DDLCommandExecutor extends CommandExecutor {

    /**
     * 基于某个实体作为alter table
     *
     * @see #alertTable(AlterTableOperator)
     */
    default <R> boolean alertTable(Class<R> entityType, UnaryOperator<AlterTableOperator> func) {
        return alertTable(func.apply(getOperatorGroup().alterTables().from(entityType)));
    }

    /**
     * @see #alertTable(AlterTableOperator)
     */
    default boolean alertTable(UnaryOperator<AlterTableOperator> func) {
        return alertTable(func.apply(getOperatorGroup().alterTables()));
    }

    /**
     * @see #bool(Operator, CommandType)
     */
    default boolean alertTable(AlterTableOperator alterTableOperator) {
        return bool(alterTableOperator, CommandType.ALERT_TABLE);
    }

    /**
     * alert table
     *
     * @param alterTableOperator alterTableOperator
     * @param resultSetHandler   resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean alertTable(AlterTableOperator alterTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(alterTableOperator, CommandType.ALERT_TABLE, resultSetHandler);
    }

    /**
     * 根据pojo class创建表
     *
     * @param pojoClass the pojoClass
     * @return true 成功 false 失败
     */
    default <P> boolean createTable(Class<P> pojoClass) {
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojoClass);
        return createTable(o -> o.from(pojoWrapper.getTable()).columns(pojoWrapper.getColumnDefs()));
    }

    /**
     * 创表
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean createTable(UnaryOperator<CreateTableOperator> func) {
        return createTable(func.apply(getOperatorGroup().createTable(getOptions().getDbType())));
    }

    /**
     * 创表
     *
     * @param createTableOperator CreateTableOperator
     * @return true 成功 false 失败
     */
    default boolean createTable(CreateTableOperator createTableOperator) {
        return bool(createTableOperator, CommandType.CREATE_TABLE);
    }

    /**
     * 创表
     *
     * @param createTableOperator CreateTableOperator
     * @param resultSetHandler    resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean createTable(CreateTableOperator createTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(createTableOperator, CommandType.CREATE_TABLE, resultSetHandler);
    }

    /**
     * 根据pojo class删除表
     *
     * @param tableName the tableName
     * @return true 成功 false 失败
     */
    default boolean dropTable(String tableName) {
        return dropTable(o -> o.from(tableName));
    }

    /**
     * 根据pojo class删除表
     *
     * @param tableName the tableName
     * @return true 成功 false 失败
     */
    default boolean dropTable(DSLName tableName) {
        return dropTable(o -> o.from(tableName));
    }

    /**
     * 根据pojo class删除表
     *
     * @param table the xxxx
     * @return true 成功 false 失败
     */
    default boolean dropTable(Table table) {
        return dropTable(o -> o.from(table));
    }

    /**
     * 根据pojo class删除表
     *
     * @param pojoClass the pojoClass
     * @return true 成功 false 失败
     */
    default <P> boolean dropTable(Class<P> pojoClass) {
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojoClass);
        return dropTable(o -> o.from(pojoWrapper.getTable()));
    }

    /**
     * 删表
     *
     * @param func func
     * @return true 成功 false 失败
     */
    default boolean dropTable(UnaryOperator<DropTableOperator> func) {
        return dropTable(func.apply(getOperatorGroup().dropTable(getOptions().getDbType())));
    }

    /**
     * 删表
     *
     * @param dropTableOperator dropTableOperator
     * @return true 成功 false 失败
     */
    default boolean dropTable(DropTableOperator dropTableOperator) {
        return bool(dropTableOperator, CommandType.DELETE_TABLE);
    }

    /**
     * 删表
     *
     * @param dropTableOperator dropTableOperator
     * @param resultSetHandler  resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean dropTable(DropTableOperator dropTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(dropTableOperator, CommandType.DELETE, resultSetHandler);
    }

    /**
     * 判断表是否存在
     *
     * @param tableName the tableName
     * @return true 成功 false 失败
     */
    default boolean existTable(String tableName) {
        return existTable(o -> o.from(tableName));
    }

    /**
     * 判断表是否存在
     *
     * @param table the xxxx
     * @return true 成功 false 失败
     */
    default boolean existTable(Table table) {
        return existTable(o -> o.from(table));
    }

    /**
     * 根据pojoClass判断是否存在
     *
     * @param pojoClass pojoClass
     * @return true 成功 false 失败
     */
    default <P> boolean existTable(Class<P> pojoClass) {
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojoClass);
        return existTable(o -> o.from(pojoWrapper.getTable()));
    }

    /**
     * 判断表是否存在
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean existTable(UnaryOperator<ExistTableOperator> func) {
        return existTable(func.apply(getOperatorGroup().existTable(getOptions().getDbType())));
    }

    /**
     * 判断表是否存在
     *
     * @param existTableOperator ExistTableOperator
     * @return true 成功 false 失败
     */
    default boolean existTable(ExistTableOperator existTableOperator) {
        return bool(existTableOperator, CommandType.EXIST_TABLE);
    }

    /**
     * 判断表是否存在
     *
     * @param existTableOperator ExistTableOperator
     * @param resultSetHandler   resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean existTable(ExistTableOperator existTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(existTableOperator, CommandType.EXIST_TABLE, resultSetHandler);
    }

    /**
     * 获取某个表的字段
     *
     * @param pojoClass pojoClass
     * @return ColumnDef
     */
    default <T> List<ColumnDef> showColumns(Class<T> pojoClass) {
        PojoWrapper<T> pojoWrapper = PojoWrapper.getInstance(pojoClass);
        return showColumns(pojoWrapper.getTable());
    }

    /**
     * 获取某个表的字段
     *
     * @param tableName tableName
     * @return ColumnDef
     */
    default List<ColumnDef> showColumns(String tableName) {
        return showColumns(o -> o.from(tableName));
    }

    /**
     * 获取某个表的字段
     *
     * @param table xxxx
     * @return ColumnDef
     */
    default List<ColumnDef> showColumns(Table table) {
        return showColumns(o -> o.from(table));
    }

    /**
     * 获取某个表的字段
     *
     * @param func the func
     * @return ColumnDef
     */
    default List<ColumnDef> showColumns(UnaryOperator<ShowColumnsOperator> func) {
        return showColumns(func.apply(getOperatorGroup().showColumns(getOptions().getDbType())));
    }

    /**
     * 获取某个表的字段
     *
     * @param showColumnsOperator showColumnsOperator
     * @return ColumnDef
     */
    default List<ColumnDef> showColumns(ShowColumnsOperator showColumnsOperator) {
        return queryList(showColumnsOperator, CommandType.SHOW_COLUMNS, getOptions().obtainColumnDefListResultSetHandler());
    }

    /**
     * 基于{@link ExecutorOptions#getDatabase()}获取数据库表
     *
     * @return List<Table>
     */
    default List<Table> showTables() {
        return showTables(o -> o.database(getOptions().getDatabase()));
    }

    /**
     * 获取某个数据库的表
     *
     * @param database database
     * @return List<Table>
     */
    default List<Table> showTables(String database) {
        return showTables(o -> o.database(database));
    }

    /**
     * 获取某个数据库的表
     *
     * @param database database
     * @return List<Table>
     */
    default List<Table> showTables(Database database) {
        return showTables(o -> o.database(database));
    }

    /**
     * 获取某个数据库的表
     *
     * @param func func
     * @return List<Table>
     */
    default List<Table> showTables(UnaryOperator<ShowTablesOperator> func) {
        return showTables(func.apply(getOperatorGroup().showTables(getOptions().getDbType())));
    }

    /**
     * 获取某个数据库的表
     *
     * @param showTablesOperator showTablesOperator
     * @return List<Table>
     */
    default List<Table> showTables(ShowTablesOperator showTablesOperator) {
        return queryList(showTablesOperator, CommandType.SHOW_TABLES, getOptions().obtainTableListResultSetHandler());
    }

    /**
     * @see #showOneTable(DSLName)
     */
    default Table showOneTable(String tableName) {
        return showOneTable(DSLName.of(tableName));
    }

    /**
     * 基于某个具体的表明获取唯一表的信息
     *
     * @param tableName tableName
     * @return table instance or null
     */
    default Table showOneTable(DSLName tableName) {
        List<Table> tables = showTables(o -> o.from(tableName));
        return checkCollectionIsOneAndGet(tables);
    }
}
