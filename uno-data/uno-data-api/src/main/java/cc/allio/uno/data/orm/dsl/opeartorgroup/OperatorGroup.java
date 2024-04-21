package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.MetaAcceptorSet;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.SPIOperatorHelper;
import cc.allio.uno.data.orm.dsl.ddl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;

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
     * base on {@link DBType#getSystemDbType()} get {@link QueryOperator}
     *
     * @return the {@link QueryOperator} instance
     */
    default QueryOperator<?> query() {
        return query(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link QueryOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link QueryOperator} instance
     */
    QueryOperator<?> query(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends QueryOperator<T>> T getQueryOperator(Class<T> queryOperatorClass, OperatorKey operatorKey) {
        return getOperator(queryOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends QueryOperator<T>> T getQueryOperator(Class<T> queryOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(queryOperatorClass, operatorKey, dbType);
    }

    /**
     * base on {@link DBType#getSystemDbType()} get {@link InsertOperator}
     *
     * @return the {@link InsertOperator} instance
     */
    default InsertOperator<?> insert() {
        return insert(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link InsertOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link InsertOperator} instance
     */
    InsertOperator<?> insert(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends InsertOperator<T>> T getInsertOperator(Class<T> insertOperatorClass, OperatorKey operatorKey) {
        return getOperator(insertOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends InsertOperator<T>> T getInsertOperator(Class<T> insertOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(insertOperatorClass, operatorKey, dbType);
    }

    /**
     * base on {@link DBType#getSystemDbType()} get {@link UpdateOperator}
     *
     * @return the {@link UpdateOperator} instance
     */
    default UpdateOperator<?> update() {
        return update(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link UpdateOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link UpdateOperator} instance
     */
    UpdateOperator<?> update(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends UpdateOperator<T>> T getUpdateOperator(Class<T> updateOperatorClass, OperatorKey operatorKey) {
        return getOperator(updateOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends UpdateOperator<T>> T getUpdateOperator(Class<T> updateOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(updateOperatorClass, operatorKey, dbType);
    }

    /**
     * base on {@link DBType#getSystemDbType()} get {@link DeleteOperator}
     *
     * @return the {@link DeleteOperator} instance
     */
    default DeleteOperator<?> delete() {
        return delete(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link DeleteOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link DeleteOperator} instance
     */
    DeleteOperator<?> delete(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends DeleteOperator<T>> T getDeleteOperator(Class<T> deleteOperatorClass, OperatorKey operatorKey) {
        return getOperator(deleteOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends DeleteOperator<T>> T getDeleteOperator(Class<T> deleteOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(deleteOperatorClass, operatorKey, dbType);
    }

    // ======================== DDL ========================

    /**
     * base on {@link DBType#getSystemDbType()} get {@link CreateTableOperator}
     *
     * @return the {@link CreateTableOperator} instance
     */
    default CreateTableOperator<?> createTable() {
        return createTable(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link CreateTableOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link CreateTableOperator} instance
     */
    CreateTableOperator<?> createTable(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends CreateTableOperator<T>> T getCreateTableOperator(Class<T> createTableOperatorClass, OperatorKey operatorKey) {
        return getOperator(createTableOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends CreateTableOperator<T>> T getCreateTableOperator(Class<T> createTableOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(createTableOperatorClass, operatorKey, dbType);
    }

    /**
     * base on {@link DBType#getSystemDbType()} get {@link DropTableOperator}
     *
     * @return the {@link DropTableOperator} instance
     */
    default DropTableOperator<?> dropTable() {
        return dropTable(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link DropTableOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link DropTableOperator} instance
     */
    DropTableOperator<?> dropTable(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends DropTableOperator<T>> T getDropTableOperator(Class<T> dropTableOperatorClass, OperatorKey operatorKey) {
        return getOperator(dropTableOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends DropTableOperator<T>> T getDropTableOperator(Class<T> dropTableOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(dropTableOperatorClass, operatorKey, dbType);
    }

    /**
     * base on {@link DBType#getSystemDbType()} get {@link ExistTableOperator}
     *
     * @return the {@link ExistTableOperator} instance
     */
    default ExistTableOperator<?> existTable() {
        return existTable(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link ExistTableOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link ExistTableOperator} instance
     */
    ExistTableOperator<?> existTable(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ExistTableOperator<T>> T getExistTableOperator(Class<T> existTableOperatorClass, OperatorKey operatorKey) {
        return getOperator(existTableOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ExistTableOperator<T>> T getExistTableOperator(Class<T> existTableOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(existTableOperatorClass, operatorKey, dbType);
    }

    /**
     * base on {@link DBType#getSystemDbType()} get {@link ShowColumnsOperator}
     *
     * @return the {@link ShowColumnsOperator} instance
     */
    default ShowColumnsOperator<?> showColumns() {
        return showColumns(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link ShowColumnsOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link ShowColumnsOperator} instance
     */
    ShowColumnsOperator<?> showColumns(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ShowColumnsOperator<T>> T getShowColumnsOperator(Class<T> showColumnsOperatorClass, OperatorKey operatorKey) {
        return getOperator(showColumnsOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ShowColumnsOperator<T>> T getShowColumnsOperator(Class<T> showColumnsOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(showColumnsOperatorClass, operatorKey, dbType);
    }

    /**
     * base on {@link DBType#getSystemDbType()} get {@link ShowTablesOperator}
     *
     * @return the {@link ShowTablesOperator} instance
     */
    default ShowTablesOperator<?> showTables() {
        return showTables(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link ShowTablesOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link ShowTablesOperator} instance
     */
    ShowTablesOperator<?> showTables(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ShowTablesOperator<T>> T getShowTablesOperator(Class<T> showTablesOperatorClass, OperatorKey operatorKey) {
        return getOperator(showTablesOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends ShowTablesOperator<T>> T getShowTablesOperator(Class<T> showTablesOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(showTablesOperatorClass, operatorKey, dbType);
    }

    /**
     * base on {@link DBType#getSystemDbType()} get {@link AlterTableOperator}
     *
     * @return the {@link AlterTableOperator} instance
     */
    default AlterTableOperator<?> alterTables() {
        return alterTables(DBType.getSystemDbType());
    }

    /**
     * base on {@link DBType} get {@link AlterTableOperator}
     *
     * @param dbType the {@link DBType} instance
     * @return the {@link AlterTableOperator} instance
     */
    AlterTableOperator<?> alterTables(DBType dbType);

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends AlterTableOperator<T>> T getAlterTableOperator(Class<T> alterTableOperatorClass, OperatorKey operatorKey) {
        return getOperator(alterTableOperatorClass, operatorKey, null);
    }

    /**
     * @see #getOperator(Class, OperatorKey, DBType)
     */
    static <T extends AlterTableOperator<T>> T getAlterTableOperator(Class<T> alterTableOperatorClass, OperatorKey operatorKey, DBType dbType) {
        return getOperator(alterTableOperatorClass, operatorKey, dbType);
    }

    /**
     * base on operator key gain {@link OperatorGroup}
     *
     * @return {@link OperatorGroup} or null
     */
    static OperatorGroup getOperatorGroup(OperatorKey operatorKey, MetaAcceptorSet metaAcceptorSet) {
        if (operatorKey != null) {
            return new MetaAcceptorSetOperatorGroup(operatorKey, metaAcceptorSet);
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
}
