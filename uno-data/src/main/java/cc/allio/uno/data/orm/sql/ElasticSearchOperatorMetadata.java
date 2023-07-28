package cc.allio.uno.data.orm.sql;

import cc.allio.uno.data.orm.sql.ddl.SQLCreateTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLDropTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLExistTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchCreateIndexOperator;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchDropIndexOperator;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchExistIndexOperator;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchShowColumnsOperator;
import cc.allio.uno.data.orm.sql.dml.SQLDeleteOperator;
import cc.allio.uno.data.orm.sql.dml.SQLInsertOperator;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.SQLUpdateOperator;
import cc.allio.uno.data.orm.sql.dml.elasticsearch.ElasticSearchDeleteOperator;
import cc.allio.uno.data.orm.sql.dml.elasticsearch.ElasticSearchInsertOperator;
import cc.allio.uno.data.orm.sql.dml.elasticsearch.ElasticSearchQueryOperator;
import cc.allio.uno.data.orm.sql.dml.elasticsearch.ElasticSearchUpdateOperator;
import cc.allio.uno.data.orm.type.DBType;

/**
 * elasticsearch实现
 *
 * @author jiangwei
 * @date 2023/5/28 15:32
 * @since 1.1.4
 */
public class ElasticSearchOperatorMetadata implements OperatorMetadata {
    @Override
    public SQLQueryOperator query(DBType dbType) {
        return new ElasticSearchQueryOperator();
    }

    @Override
    public SQLInsertOperator insert(DBType dbType) {
        return new ElasticSearchInsertOperator();
    }

    @Override
    public SQLUpdateOperator update(DBType dbType) {
        return new ElasticSearchUpdateOperator();
    }

    @Override
    public SQLDeleteOperator delete(DBType dbType) {
        return new ElasticSearchDeleteOperator();
    }

    @Override
    public SQLCreateTableOperator createTable(DBType dbType) {
        return new ElasticSearchCreateIndexOperator();
    }

    @Override
    public SQLDropTableOperator dropTable(DBType dbType) {
        return new ElasticSearchDropIndexOperator();
    }

    @Override
    public SQLExistTableOperator existTable(DBType dbType) {
        return new ElasticSearchExistIndexOperator();
    }

    @Override
    public SQLShowColumnsOperator showColumns(DBType dbType) {
        return new ElasticSearchShowColumnsOperator();
    }

    @Override
    public OperatorMetadataKey getKey() {
        return ELASTIC_SEARCH_KEY;
    }
}
