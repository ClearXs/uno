package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchPropertyAdapter;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchShowColumnsOperator;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.elasticsearch.ElasticSearchQueryOperator;
import cc.allio.uno.data.orm.type.DataType;
import cc.allio.uno.data.orm.type.JavaType;
import cc.allio.uno.data.orm.type.TypeRegistry;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.GetMappingRequest;
import co.elastic.clients.elasticsearch.indices.GetMappingResponse;
import co.elastic.clients.elasticsearch.indices.get_mapping.IndexMappingRecord;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;
import java.sql.JDBCType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 基于es的SQL执行器
 *
 * @author jiangwei
 * @date 2023/4/19 11:45
 * @since 1.1.4
 */
@Slf4j
public class EsSQLCommandExecutor implements SQLCommandExecutor {

    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchIndicesClient indicesClient;
    private final ElasticSearchOperatorMetadata elasticSearchOperatorMetadata;

    public EsSQLCommandExecutor(Object[] values) {
        RestClientBuilder restClientBuilder = CollectionUtils.findValueOfType(Lists.newArrayList(values), RestClientBuilder.class);
        if (restClientBuilder == null) {
            throw new NullPointerException(String.format("expect %s but not found", RestClientBuilder.class.getName()));
        }
        RestClient restClient = restClientBuilder.build();
        JsonpMapper mapper = new JacksonJsonpMapper();
        RestClientTransport transport = new RestClientTransport(restClient, mapper);
        this.indicesClient = new ElasticsearchIndicesClient(transport);
        this.elasticsearchClient = new ElasticsearchClient(transport);
        this.elasticSearchOperatorMetadata = new ElasticSearchOperatorMetadata();
    }

    @Override
    public boolean bool(SQLOperator<?> operator, SQLCommandType sqlCommandType, ResultSetHandler<Boolean> resultSetHandler) {
        try {
            switch (sqlCommandType) {
                case CREATE_TABLE:
                    return new EsCreateTableCommandExecutor(indicesClient).exec(operator, resultSetHandler);
                case DELETE_TABLE:
                    return new EsDeleteTableCommandExecutor(indicesClient).exec(operator, resultSetHandler);
                case EXIST_TABLE:
                    return new EsExistTableCommandExecutor(elasticsearchClient).exec(operator, resultSetHandler);
                case INSERT:
                    return new EsInsertCommandExecutor(elasticsearchClient).exec(operator, resultSetHandler);
                case UPDATE:
                    return new EsUpdateCommandExecutor(elasticsearchClient).exec(operator, resultSetHandler);
                case DELETE:
                    return new EsDeleteCommandExecutor(elasticsearchClient).exec(operator, resultSetHandler);
                default:
                    return false;
            }
        } catch (Throwable ex) {
            throw new SQLException(String.format("exec operator %s has err", operator.getClass().getName()), ex);
        }
    }

    @Override
    public List<SQLColumnDef> showColumns(SQLShowColumnsOperator sqlShowColumnsOperator) {
        if (!ElasticSearchShowColumnsOperator.class.isAssignableFrom(sqlShowColumnsOperator.getClass())) {
            throw new IllegalArgumentException(String.format("the show index operator not ElasticSearchShowColumnsOperator, this is %s", sqlShowColumnsOperator.getClass().getName()));
        }
        GetMappingRequest mappingRequest = ((ElasticSearchShowColumnsOperator) sqlShowColumnsOperator).getMappingRequest();
        try {
            GetMappingResponse res = indicesClient.getMapping(mappingRequest);
            // key 为索引名称 value 为该索引下mapping的记录
            // 在下面解析时按照只能查询一个来做处理（有且仅有一个）
            Map<String, IndexMappingRecord> result = res.result();
            ElasticSearchPropertyAdapter propertyAdapter = new ElasticSearchPropertyAdapter();
            List<ResultGroup> resultGroups = result.values()
                    .stream()
                    .flatMap(record -> {
                        TypeMapping mappings = record.mappings();
                        Map<String, Property> properties = mappings.properties();
                        return properties.entrySet()
                                .stream()
                                .map(field -> {
                                    ResultGroup resultGroup = new ResultGroup();
                                    ResultRow.ResultRowBuilder builder = ResultRow.builder();
                                    builder.column(SQLName.of(SQLColumnDefListResultSetHandler.ROW_FIELD_NAME));
                                    Property fieldProperty = field.getValue();
                                    DataType dataType = propertyAdapter.reversal(fieldProperty);
                                    builder.jdbcType(JDBCType.valueOf(dataType.getSqlType().getJdbcType()));
                                    String fieldKey = field.getKey();
                                    builder.value(fieldKey);
                                    resultGroup.addRow(builder.build());
                                    return resultGroup;
                                });
                    })
                    .collect(Collectors.toList());
            ResultSet resultSet = new ResultSet();
            resultSet.setResultGroups(resultGroups);
            return new SQLColumnDefListResultSetHandler().apply(resultSet);
        } catch (IOException ex) {
            log.error("show columns has err", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public <R> List<R> queryList(SQLQueryOperator queryOperator, ListResultSetHandler<R> resultSetHandler) {
        ElasticSearchQueryOperator elasticSearchQueryOperator = null;
        if (ElasticSearchQueryOperator.class.isAssignableFrom(queryOperator.getClass())) {
            elasticSearchQueryOperator = ((ElasticSearchQueryOperator) queryOperator);
        }
        // 加这层判断是因为传递进行的可能是BaseQueryFilter类型
        if (elasticSearchQueryOperator == null && (ElasticSearchQueryOperator.class.isAssignableFrom(queryOperator.self().getClass()))) {
            elasticSearchQueryOperator = ((ElasticSearchQueryOperator) queryOperator.self());
        }
        if (elasticSearchQueryOperator != null) {
            SearchResponse<Map> response;
            try {
                SearchRequest searchRequest = elasticSearchQueryOperator.getSearchRequest();
                response = elasticsearchClient.search(searchRequest, Map.class);
            } catch (Throwable ex) {
                throw new SQLException(String.format("elasticsearch exec dsl: %s has error", queryOperator.getSQL()), ex);
            }
            if (response == null) {
                throw new SQLException(String.format("elasticsearch request has err the msg is: %s", ""));
            }
            EsResultSet resultSet = explainSearchRes(response);
            return resultSetHandler.apply(resultSet);
        } else {
            throw new SQLException("queryOperator argument not SearchRequest instance");
        }
    }

    /**
     * 从查询search返回结果组
     *
     * @param response 查询响应
     * @return EsResultGroup
     */
    private EsResultSet explainSearchRes(SearchResponse<Map> response) {
        // 聚合处理
        Map<String, Aggregate> keyAggr = response.aggregations();
        AggrResultSet aggrResultSet = new AggrResultSet(keyAggr);
        // 结果集处理
        HitsMetadata<?> hits = response.hits();
        TotalHits totalHits = hits.total();
        EsResultSet resultSet =
                new EsResultSet(
                        Optional.ofNullable(hits.maxScore()).orElse(0d),
                        Optional.ofNullable(totalHits.value()).orElse(0L),
                        Optional.ofNullable(totalHits.relation()).map(TotalHitsRelation::jsonValue).orElse(StringPool.EMPTY));
        List<? extends Hit<?>> hitList = hits.hits();
        List<ResultGroup> resultGroups = Lists.newArrayListWithCapacity(hitList.size());
        for (Hit<?> hit : hitList) {
            EsResultGroup resultGroup = new EsResultGroup();
            // 设置文档基础信息
            resultGroup.setId(hit.id());
            resultGroup.setIndex(hit.index());
            resultGroup.setType(Optional.ofNullable(hit.type()).orElse(StringPool.EMPTY));
            resultGroup.setScore(Optional.ofNullable(hit.score()).orElse(0D));
            // 设置分片信息
            resultGroup.setNodeId(hit.node());
            resultGroup.setShardId(hit.shard());
            ResultRow.ResultRowBuilder builder = ResultRow.builder();
            // 设置数据信息
            Map<String, Object> source = (Map<String, Object>) hit.source();
            for (Map.Entry<String, Object> df : source.entrySet()) {
                String columnName = df.getKey();
                Object value = df.getValue();
                builder.column(SQLName.of(columnName, SQLName.HUMP_FEATURE));
                JavaType<?> javaType = TypeRegistry.getInstance().guessJavaType(value);
                builder.javaType(javaType);
                builder.value(value);
                ResultRow row = builder.build();
                resultGroup.addRow(row);
            }
            resultGroups.add(resultGroup);
        }
        resultSet.setResultGroups(resultGroups);
        resultSet.setAggrResultSet(aggrResultSet);
        return resultSet;
    }

    @Override
    public ExecutorKey getKey() {
        return MYBATIS_SQL_COMMAND_EXECUTOR_KEY;
    }

    @Override
    public OperatorMetadata getOperatorMetadata() {
        return elasticSearchOperatorMetadata;
    }
}
