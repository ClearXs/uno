package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.elasticsearch.dml.EsQueryOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.type.JavaType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.QOInnerCommandExecutor;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * elasticsearch query command executor
 *
 * @author j.x
 * @since 1.1.7
 */
public class EsQueryCommandExecutor<R> implements QOInnerCommandExecutor<R, EsQueryOperator> {

    private final ElasticsearchClient elasticsearchClient;

    public EsQueryCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public List<R> doExec(EsQueryOperator operator, ListResultSetHandler<R> handler) throws Throwable {
        SearchResponse<Map> response;
        try {
            SearchRequest searchRequest = operator.getSearchRequest();
            response = elasticsearchClient.search(searchRequest, Map.class);
        } catch (Throwable ex) {
            throw new DSLException(String.format("elasticsearch exec dsl: %s has error", operator.getDSL()), ex);
        }
        if (response == null) {
            throw new DSLException(String.format("elasticsearch request has err the msg is: %s", ""));
        }
        EsResultSet resultSet = explainSearchRes(response);
        return handler.apply(resultSet);
    }

    /**
     * 从查询search返回结果组
     *
     * @param response 查询响应
     * @return EsResultGroup
     */
    EsResultSet explainSearchRes(SearchResponse<Map> response) {
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
}
