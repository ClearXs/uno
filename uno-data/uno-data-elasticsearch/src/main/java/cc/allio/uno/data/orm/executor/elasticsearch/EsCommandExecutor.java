package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.ddl.elasticsearch.ElasticSearchPropertyAdapter;
import cc.allio.uno.data.orm.dsl.ddl.elasticsearch.ElasticSearchShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.elasticsearch.ElasticSearchQueryOperator;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.JavaType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
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

/**
 * 基于es的SQL执行器
 *
 * @author jiangwei
 * @date 2023/4/19 11:45
 * @since 1.1.4
 */
@Slf4j
public class EsCommandExecutor extends AbstractCommandExecutor implements CommandExecutor {

    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchIndicesClient indicesClient;
    private final OperatorGroup operatorGroup;

    public EsCommandExecutor(RestClientBuilder restClientBuilder) {
        this(new ExecutorOptions(), restClientBuilder);
    }

    public EsCommandExecutor(ExecutorOptions options, RestClientBuilder restClientBuilder) {
        super(options);
        RestClient restClient = restClientBuilder.build();
        JsonpMapper mapper = new JacksonJsonpMapper();
        RestClientTransport transport = new RestClientTransport(restClient, mapper);
        this.indicesClient = new ElasticsearchIndicesClient(transport);
        this.elasticsearchClient = new ElasticsearchClient(transport);
        this.operatorGroup = OperatorGroup.getOperatorGroup(OperatorKey.ELASTICSEARCH);
    }

    @Override
    protected boolean doBool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler) {
        try {
            return switch (commandType) {
                case CREATE_TABLE -> new EsCreateTableCommandExecutor(indicesClient).exec(operator, resultSetHandler);
                case DELETE_TABLE -> new EsDeleteTableCommandExecutor(indicesClient).exec(operator, resultSetHandler);
                case EXIST_TABLE ->
                        new EsExistTableCommandExecutor(elasticsearchClient).exec(operator, resultSetHandler);
                case INSERT -> new EsInsertCommandExecutor(elasticsearchClient).exec(operator, resultSetHandler);
                case UPDATE -> new EsUpdateCommandExecutor(elasticsearchClient).exec(operator, resultSetHandler);
                case DELETE -> new EsDeleteCommandExecutor(elasticsearchClient).exec(operator, resultSetHandler);
                default -> false;
            };
        } catch (Throwable ex) {
            throw new DSLException(String.format("exec operator %s has err", operator.getClass().getName()), ex);
        }
    }

    @Override
    protected <R> List<R> doQueryList(QueryOperator queryOperator, CommandType commandType, ListResultSetHandler<R> resultSetHandler) {
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
                throw new DSLException(String.format("elasticsearch exec dsl: %s has error", queryOperator.getDSL()), ex);
            }
            if (response == null) {
                throw new DSLException(String.format("elasticsearch request has err the msg is: %s", ""));
            }
            EsResultSet resultSet = explainSearchRes(response);
            return resultSetHandler.apply(resultSet);
        } else {
            throw new DSLException("queryOperator argument not SearchRequest instance");
        }
    }

    @Override
    public List<ColumnDef> showColumns(ShowColumnsOperator sqlShowColumnsOperator) {
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
            List<ResultGroup> resultGroups =
                    result.values()
                            .stream()
                            .flatMap(record -> {
                                TypeMapping mappings = record.mappings();
                                Map<String, Property> properties = mappings.properties();
                                return properties.entrySet()
                                        .stream()
                                        .map(field -> {
                                            ResultGroup resultGroup = new ResultGroup();
                                            ResultRow.ResultRowBuilder builder = ResultRow.builder();
                                            builder.column(DSLName.of(SQLColumnDefListResultSetHandler.ROW_FIELD_NAME));
                                            Property fieldProperty = field.getValue();
                                            DataType dataType = propertyAdapter.reverse(fieldProperty);
                                            builder.jdbcType(JDBCType.valueOf(dataType.getSqlType().getJdbcType()));
                                            String fieldKey = field.getKey();
                                            builder.value(fieldKey);
                                            resultGroup.addRow(builder.build());
                                            return resultGroup;
                                        });
                            })
                            .toList();
            ResultSet resultSet = new ResultSet();
            resultSet.setResultGroups(resultGroups);
            return new SQLColumnDefListResultSetHandler().apply(resultSet);
        } catch (IOException ex) {
            log.error("Show columns has err", ex);
            return Collections.emptyList();
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
                builder.column(DSLName.of(columnName, DSLName.HUMP_FEATURE));
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
        return ExecutorKey.ELASTICSEARCH;
    }

    @Override
    public OperatorGroup getOperatorGroup() {
        return operatorGroup;
    }
}
