package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.Data;

/**
 * base on aspectj implementation {@link Operators}.
 * <p>enhance {@link Operator#obtainMetaAcceptorSet()} load by proxy</p>
 *
 * @author j.x
 * @date 2024/4/21 15:31
 * @since 1.1.8
 */
@Data
public class MetaAcceptorSetOperatorGroup implements Operators {

    private final OperatorKey key;
    private MetaAcceptorSet metaAcceptorSet;

    public MetaAcceptorSetOperatorGroup(OperatorKey key) {
        this(key, null);
    }

    public MetaAcceptorSetOperatorGroup(OperatorKey key, MetaAcceptorSet metaAcceptorSet) {
        this.key = key;
        this.metaAcceptorSet = metaAcceptorSet;
    }

    @Override
    public QueryOperator<?> query(DBType dbType) {
        QueryOperator<?> queryOperator = SPIOperatorHelper.lazyGet(QueryOperator.class, key, dbType);
        return new MetaAcceptorQueryOperator(queryOperator, metaAcceptorSet);
    }

    @Override
    public InsertOperator<?> insert(DBType dbType) {
        InsertOperator<?> insertOperator = SPIOperatorHelper.lazyGet(InsertOperator.class, key, dbType);
        return new MetaAcceptorInsertOperator(insertOperator, metaAcceptorSet);
    }

    @Override
    public UpdateOperator<?> update(DBType dbType) {
        UpdateOperator<?> updateOperator = SPIOperatorHelper.lazyGet(UpdateOperator.class, key, dbType);
        return new MetaAcceptorUpdateOperator(updateOperator, metaAcceptorSet);
    }

    @Override
    public DeleteOperator<?> delete(DBType dbType) {
        DeleteOperator<?> deleteOperator = SPIOperatorHelper.lazyGet(DeleteOperator.class, key, dbType);
        return new MetaAcceptorDeleteOperator(deleteOperator, metaAcceptorSet);
    }

    @Override
    public CreateTableOperator<?> createTable(DBType dbType) {
        CreateTableOperator<?> createTableOperator = SPIOperatorHelper.lazyGet(CreateTableOperator.class, key, dbType);
        return new MetaAcceptorCreateTableOperator(createTableOperator, metaAcceptorSet);
    }

    @Override
    public DropTableOperator<?> dropTable(DBType dbType) {
        DropTableOperator<?> dropTableOperator = SPIOperatorHelper.lazyGet(DropTableOperator.class, key, dbType);
        return new MetaAcceptorDropTableOperator(dropTableOperator, metaAcceptorSet);
    }

    @Override
    public ExistTableOperator<?> existTable(DBType dbType) {
        ExistTableOperator<?> existTableOperator = SPIOperatorHelper.lazyGet(ExistTableOperator.class, key, dbType);
        return new MetaAcceptorExistTableOperator(existTableOperator, metaAcceptorSet);
    }

    @Override
    public ShowColumnsOperator<?> showColumns(DBType dbType) {
        ShowColumnsOperator<?> showColumnsOperator = SPIOperatorHelper.lazyGet(ShowColumnsOperator.class, key, dbType);
        return new MetaAcceptorShowColumnsOperator(showColumnsOperator, metaAcceptorSet);
    }

    @Override
    public ShowTablesOperator<?> showTables(DBType dbType) {
        ShowTablesOperator<?> showTablesOperator = SPIOperatorHelper.lazyGet(ShowTablesOperator.class, key, dbType);
        return new MetaAcceptorShowTablesOperator(showTablesOperator, metaAcceptorSet);
    }

    @Override
    public AlterTableOperator<?> alterTables(DBType dbType) {
        AlterTableOperator<?> alterTableOperator = SPIOperatorHelper.lazyGet(AlterTableOperator.class, key, dbType);
        return new MetaAcceptorAlterTableOperator(alterTableOperator, metaAcceptorSet);
    }

    @Override
    public UnrecognizedOperator<?> unrecognized(DBType dbType) {
        return SPIOperatorHelper.lazyGet(UnrecognizedOperator.class, key, dbType);
    }
}
