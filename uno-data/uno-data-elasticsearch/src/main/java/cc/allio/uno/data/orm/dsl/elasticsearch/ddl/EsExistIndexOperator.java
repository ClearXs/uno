package cc.allio.uno.data.orm.dsl.elasticsearch.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.type.DBType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 判断索引是否存在{@link co.elastic.clients.elasticsearch.indices.ExistsRequest}
 *
 * @author j.x
 * @date 2023/5/29 18:59
 * @since 1.1.4
 */
@AutoService(ExistTableOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class EsExistIndexOperator implements ExistTableOperator<EsExistIndexOperator> {

    private final DBType dbType;
    private SearchRequest searchRequest;
    private Table table;
    SearchRequest.Builder builder;

    public EsExistIndexOperator() {
        this.dbType = DBType.ELASTICSEARCH;
        this.builder = new SearchRequest.Builder();
    }

    @Override
    public String getDSL() {
        return StringPool.EMPTY;
    }

    @Override
    public EsExistIndexOperator parse(String dsl) {
        return self();
    }

    @Override
    public EsExistIndexOperator customize(UnaryOperator<EsExistIndexOperator> operatorFunc) {
        return operatorFunc.apply(new EsExistIndexOperator());
    }

    @Override
    public void reset() {
        this.searchRequest = null;
        this.builder = new SearchRequest.Builder();
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing to do
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public String getPrepareDSL() {
        return getDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return List.of();
    }

    @Override
    public EsExistIndexOperator from(Table table) {
        this.table = table;
        builder.index(table.getName().format());
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    /**
     * 获取存在request
     *
     * @return ExistsRequest
     */
    public SearchRequest getSearchRequest() {
        if (searchRequest == null) {
            searchRequest = builder.build();
        }
        return searchRequest;
    }
}