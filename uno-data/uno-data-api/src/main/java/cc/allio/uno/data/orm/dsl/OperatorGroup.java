package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.data.orm.dsl.ddl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 操作管理接口
 *
 * @author j.x
 * @date 2023/4/13 18:52
 * @see Operator
 * @since 1.1.4
 */
public interface OperatorGroup {

    // ======================== DML ========================

    /**
     * 获取查询操作
     *
     * @return QueryOperator
     */
    default QueryOperator query() {
        return query(DBType.getSystemDbType());
    }

    /**
     * 获取查询操作
     *
     * @param dbType dbType
     * @return QueryOperator
     */
    QueryOperator query(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends QueryOperator> T getQueryOperator(Class<T> queryOperatorClass, OperatorKey operatorKey) {
        return getOperator(queryOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends QueryOperator> T getQueryOperator(Class<T> queryOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(queryOperatorClass, operatorKey, dbType);
    }

    /**
     * 获取insert操作
     *
     * @return InsertOperator
     */
    default InsertOperator insert() {
        return insert(DBType.getSystemDbType());
    }

    /**
     * 获取insert操作
     *
     * @param dbType dbType
     * @return InsertOperator
     */
    InsertOperator insert(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends InsertOperator> T getInsertOperator(Class<T> insertOperatorClass, OperatorKey operatorKey) {
        return getOperator(insertOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends InsertOperator> T getInsertOperator(Class<T> insertOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(insertOperatorClass, operatorKey, dbType);
    }

    /**
     * 获取update操作
     *
     * @return SQLUpdateOperator
     */
    default UpdateOperator update() {
        return update(DBType.getSystemDbType());
    }

    /**
     * 获取update操作
     *
     * @param dbType dbType
     * @return SQLUpdateOperator
     */
    UpdateOperator update(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends UpdateOperator> T getUpdateOperator(Class<T> updateOperatorClass, OperatorKey operatorKey) {
        return getOperator(updateOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends UpdateOperator> T getUpdateOperator(Class<T> updateOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(updateOperatorClass, operatorKey, dbType);
    }

    /**
     * 获取delete操作
     *
     * @return SQLDeleteOperator
     */
    default DeleteOperator delete() {
        return delete(DBType.getSystemDbType());
    }

    /**
     * 获取delete操作
     *
     * @return SQLDeleteOperator
     */
    DeleteOperator delete(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends DeleteOperator> T getDeleteOperator(Class<T> deleteOperatorClass, OperatorKey operatorKey) {
        return getOperator(deleteOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends DeleteOperator> T getDeleteOperator(Class<T> deleteOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(deleteOperatorClass, operatorKey, dbType);
    }

    // ======================== DDL ========================

    /**
     * registry table操作
     *
     * @return CreateTableOperator
     */
    default CreateTableOperator createTable() {
        return createTable(DBType.getSystemDbType());
    }

    /**
     * registry table操作
     *
     * @param dbType dbType
     * @return CreateTableOperator
     */
    CreateTableOperator createTable(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends CreateTableOperator> T getCreateTableOperator(Class<T> createTableOperatorClass, OperatorKey operatorKey) {
        return getOperator(createTableOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends CreateTableOperator> T getCreateTableOperator(Class<T> createTableOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(createTableOperatorClass, operatorKey, dbType);
    }

    /**
     * drop xxxx
     *
     * @return DropTableOperator
     */
    default DropTableOperator dropTable() {
        return dropTable(DBType.getSystemDbType());
    }

    /**
     * drop xxxx
     *
     * @param dbType dbType
     * @return DropTableOperator
     */
    DropTableOperator dropTable(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends DropTableOperator> T getDropTableOperator(Class<T> dropTableOperatorClass, OperatorKey operatorKey) {
        return getOperator(dropTableOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends DropTableOperator> T getDropTableOperator(Class<T> dropTableOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(dropTableOperatorClass, operatorKey, dbType);
    }

    /**
     * exist xxxx
     *
     * @return ExistTableOperator
     */
    default ExistTableOperator existTable() {
        return existTable(DBType.getSystemDbType());
    }

    /**
     * exist xxxx
     *
     * @param dbType dbType
     * @return ExistTableOperator
     */
    ExistTableOperator existTable(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ExistTableOperator> T getExistTableOperator(Class<T> existTableOperatorClass, OperatorKey operatorKey) {
        return getOperator(existTableOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ExistTableOperator> T getExistTableOperator(Class<T> existTableOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(existTableOperatorClass, operatorKey, dbType);
    }

    /**
     * show columns for xxxx
     *
     * @return ShowColumnsOperator
     */
    default ShowColumnsOperator showColumns() {
        return showColumns(DBType.getSystemDbType());
    }

    /**
     * show columns for xxxx
     *
     * @param dbType dbType
     * @return ShowColumnsOperator
     */
    ShowColumnsOperator showColumns(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ShowColumnsOperator> T getShowColumnsOperator(Class<T> showColumnsOperatorClass, OperatorKey operatorKey) {
        return getOperator(showColumnsOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ShowColumnsOperator> T getShowColumnsOperator(Class<T> showColumnsOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(showColumnsOperatorClass, operatorKey, dbType);
    }

    /**
     * show tables
     *
     * @return ShowTablesOperator
     */
    default ShowTablesOperator showTables() {
        return showTables(DBType.getSystemDbType());
    }

    /**
     * show tables
     *
     * @param dbType dbType
     * @return ShowTablesOperator
     */
    ShowTablesOperator showTables(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ShowTablesOperator> T getShowTablesOperator(Class<T> showTablesOperatorClass, OperatorKey operatorKey) {
        return getOperator(showTablesOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ShowTablesOperator> T getShowTablesOperator(Class<T> showTablesOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(showTablesOperatorClass, operatorKey, dbType);
    }

    /**
     * @see #alterTables(DBType)
     */
    default AlterTableOperator alterTables() {
        return alterTables(DBType.getSystemDbType());
    }

    /**
     * alert tables
     *
     * @param dbType dbType
     * @return AlterTableOperator
     */
    AlterTableOperator alterTables(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends AlterTableOperator> T getAlterTableOperator(Class<T> alterTableOperatorClass, OperatorKey operatorKey) {
        return getOperator(alterTableOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends AlterTableOperator> T getAlterTableOperator(Class<T> alterTableOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(alterTableOperatorClass, operatorKey, dbType);
    }

    /**
     * 获取当前系统的OperatorGroup
     *
     * @see #getOperatorGroup(OperatorKey)
     */
    static OperatorGroup getSystemOperatorGroup() {
        OperatorKey systemOperatorKey = OperatorKey.getSystemOperatorKey();
        if (systemOperatorKey == null) {
            return null;
        }
        return getOperatorGroup(systemOperatorKey);
    }

    /**
     * base on operator key gain {@link OperatorGroup}
     *
     * @return {@link OperatorGroup} or null
     */
    static OperatorGroup getOperatorGroup(OperatorKey operatorKey) {
        if (operatorKey != null) {
            return new OperatorGroupImpl(operatorKey);
        }
        return null;
    }

    /**
     * 根据operator的class获取指定operator实例.
     * <ul>
     *     <li>operator key = {@link OperatorKey#getSystemOperatorKey()}</li>
     *     <li>dbtype = {@link DBType#getSystemDbType()}</li>
     * </ul>
     *
     * @param operatorClass operatorClass
     * @param <T>           SQLOperator
     * @return SQLOperator
     * @throws IllegalArgumentException operatorClass is null
     * @throws NullPointerException     An operation that does not exist
     */
    static <T extends Operator<T>> T getOperator(Class<T> operatorClass) {
        return getOperator(operatorClass, OperatorKey.getSystemOperatorKey(), DBType.getSystemDbType());
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends Operator<?>> T getOperator(Class<T> operatorClass, OperatorKey operatorKey) {
        return getOperator(operatorClass, operatorKey, DBType.getSystemDbType());
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends Operator<?>> T getOperator(Class<T> operatorClass, DBType dbType) {
        return getOperator(operatorClass, OperatorKey.getSystemOperatorKey(), dbType);
    }

    /**
     * 根据operator的class获取指定operator实例
     *
     * @param operatorClass operatorClass
     * @param operatorKey   operatorKey
     * @param dbType        dbtype
     * @param <T>           generic of Operator type
     * @return Operator
     * @throws IllegalArgumentException operatorClass is null
     * @throws NullPointerException     An operation that does not exist
     * @see OperatorKey
     * @see SPIOperatorHelper#lazyGet(Class, OperatorKey, DBType)
     */
    static <T extends Operator<?>> T getOperator(Class<T> operatorClass, OperatorKey operatorKey, DBType dbType) {
        return SPIOperatorHelper.lazyGet(operatorClass, operatorKey, dbType);
    }

    @Data
    @AllArgsConstructor
    class OperatorGroupImpl implements OperatorGroup {

        private final OperatorKey key;

        @Override
        public QueryOperator query(DBType dbType) {
            return SPIOperatorHelper.lazyGet(QueryOperator.class, key, dbType);
        }

        @Override
        public InsertOperator insert(DBType dbType) {
            return SPIOperatorHelper.lazyGet(InsertOperator.class, key, dbType);
        }

        @Override
        public UpdateOperator update(DBType dbType) {
            return SPIOperatorHelper.lazyGet(UpdateOperator.class, key, dbType);
        }

        @Override
        public DeleteOperator delete(DBType dbType) {
            return SPIOperatorHelper.lazyGet(DeleteOperator.class, key, dbType);
        }

        @Override
        public CreateTableOperator createTable(DBType dbType) {
            return SPIOperatorHelper.lazyGet(CreateTableOperator.class, key, dbType);
        }

        @Override
        public DropTableOperator dropTable(DBType dbType) {
            return SPIOperatorHelper.lazyGet(DropTableOperator.class, key, dbType);
        }

        @Override
        public ExistTableOperator existTable(DBType dbType) {
            return SPIOperatorHelper.lazyGet(ExistTableOperator.class, key, dbType);
        }

        @Override
        public ShowColumnsOperator showColumns(DBType dbType) {
            return SPIOperatorHelper.lazyGet(ShowColumnsOperator.class, key, dbType);
        }

        @Override
        public ShowTablesOperator showTables(DBType dbType) {
            return SPIOperatorHelper.lazyGet(ShowTablesOperator.class, key, dbType);
        }

        @Override
        public AlterTableOperator alterTables(DBType dbType) {
            return SPIOperatorHelper.lazyGet(AlterTableOperator.class, key, dbType);
        }
    }
}
