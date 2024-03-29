package cc.allio.uno.data.orm.dsl.ddl.elasticsearch;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.type.DBType;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;

/**
 * 创建索引
 *
 * @author j.x
 * @date 2023/5/29 14:00
 * @see <a href="https://blog.csdn.net/C1041067258/article/details/127311699">索引的settings</a>
 * @since 1.1.4
 */
@AutoService(CreateTableOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class EsCreateIndexOperator implements CreateTableOperator {
    private final EsPropertyAdapter elasticSearchPropertyAdapter;
    private final IndexSettings.Builder settingsBuilder;
    private final TypeMapping.Builder mappingBuilder;
    private CreateIndexRequest request;
    private CreateIndexRequest.Builder builder;
    private Table table;
    private DBType dbType;

    private static final String ERROR_MSG = "elasticsearch registry operator not support that operator";

    public EsCreateIndexOperator() {
        this.dbType = DBType.ELASTIC_SEARCH;
        this.builder = new CreateIndexRequest.Builder();
        this.elasticSearchPropertyAdapter = new EsPropertyAdapter();
        this.settingsBuilder = new IndexSettings.Builder();
        // 一些settings的默认值
        maxResultWindow(Integer.MAX_VALUE);
        numberOfShards("1");
        numberOfReplicas("1");
        codec("best_compression");
        maxScriptFields(10000);
        this.mappingBuilder = new TypeMapping.Builder();
    }

    @Override
    public String getDSL() {
        CreateIndexRequest createIndexRequest = getCreateIndexRequest();
        String dsl = JsonpUtils.toString(createIndexRequest);
        return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
    }

    @Override
    public EsCreateIndexOperator parse(String dsl) {
        throw new DSLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public void reset() {
        request = null;
        this.builder = new CreateIndexRequest.Builder();
    }

    @Override
    public void setDBType(DBType dbType) {
        throw Exceptions.unOperate("setDBType");
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public EsCreateIndexOperator from(Table table) {
        this.table = table;
        builder.index(table.getName().format());
        return (EsCreateIndexOperator) self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public EsCreateIndexOperator column(ColumnDef columnDef) {
        Property property = elasticSearchPropertyAdapter.adapt(columnDef.getDataType());
        mappingBuilder.properties(columnDef.getDslName().format(), property);
        return (EsCreateIndexOperator) self();
    }

    @Override
    public EsCreateIndexOperator comment(String comment) {
        throw new DSLException(ERROR_MSG);
    }

    // =================== settings ===================

    /**
     * 查询最大结果数量（官方默认为）
     */
    public EsCreateIndexOperator maxResultWindow(Integer value) {
        settingsBuilder.maxResultWindow(value);
        return (EsCreateIndexOperator) self();
    }

    /**
     * 主分片数量设置，默认为1
     * API name: number_of_shards
     */
    public EsCreateIndexOperator numberOfShards(String value) {
        settingsBuilder.numberOfShards(value);
        return (EsCreateIndexOperator) self();
    }

    /**
     * 副本分配设置
     * API name: number_of_replicas
     */
    public EsCreateIndexOperator numberOfReplicas(String value) {
        settingsBuilder.numberOfReplicas(value);
        return (EsCreateIndexOperator) self();
    }

    /**
     * 数据压缩算法
     * <ul>
     *     <li>default</li>
     *     <li>best_compression</li>
     * </ul>
     */
    public EsCreateIndexOperator codec(String value) {
        settingsBuilder.codec(value);
        return (EsCreateIndexOperator) self();
    }

    /**
     * 在查询中script_fields的最大数量，默认值10000
     */
    public EsCreateIndexOperator maxScriptFields(Integer value) {
        settingsBuilder.maxScriptFields(value);
        return (EsCreateIndexOperator) self();
    }

    /**
     * 获取创建索引request
     *
     * @return CreateIndexRequest
     */
    public CreateIndexRequest getCreateIndexRequest() {
        if (request == null) {
            IndexSettings settings = settingsBuilder.build();
            TypeMapping mappings = mappingBuilder.build();
            builder.settings(settings);
            builder.mappings(mappings);
            this.request = builder.build();
        }
        return request;
    }
}
