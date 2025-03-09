package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.elasticsearch.ddl.EsPropertyAdapter;
import cc.allio.uno.data.orm.dsl.elasticsearch.ddl.EsShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.result.ResultSet;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.SCOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.GetMappingRequest;
import co.elastic.clients.elasticsearch.indices.GetMappingResponse;
import co.elastic.clients.elasticsearch.indices.get_mapping.IndexMappingRecord;
import lombok.extern.slf4j.Slf4j;

import java.sql.JDBCType;
import java.util.List;
import java.util.Map;

/**
 * elasticsearch show columns command executor
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
@AutoService(SCOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.ELASTICSEARCH_LITERAL)
public class EsShowColumnCommandExecutor implements SCOInnerCommandExecutor<ColumnDef, EsShowColumnsOperator> {

    private final ElasticsearchIndicesClient indicesClient;

    public EsShowColumnCommandExecutor(ElasticsearchIndicesClient indicesClient) {
        this.indicesClient = indicesClient;
    }

    @Override
    public List<ColumnDef> doExec(EsShowColumnsOperator operator, ListResultSetHandler<ColumnDef> listResultSetHandler) throws Throwable {
        GetMappingRequest mappingRequest = operator.getMappingRequest();
        GetMappingResponse res = indicesClient.getMapping(mappingRequest);
        // key 为索引名称 value 为该索引下mapping的记录
        // 在下面解析时按照只能查询一个来做处理（有且仅有一个）
        Map<String, IndexMappingRecord> result = res.result();
        EsPropertyAdapter propertyAdapter = new EsPropertyAdapter();
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
//                                            builder.column(DSLName.of(ColumnDefListResultSetHandler.ROW_FIELD_NAME));
                                        Property fieldProperty = field.getValue();
                                        DataType dataType = propertyAdapter.reverse(fieldProperty);
                                        builder.jdbcType(JDBCType.valueOf(dataType.getDslType().getJdbcType()));
                                        String fieldKey = field.getKey();
                                        builder.value(fieldKey);
                                        resultGroup.addRow(builder.build());
                                        return resultGroup;
                                    });
                        })
                        .toList();
        ResultSet resultSet = new ResultSet();
        resultSet.setResultGroups(resultGroups);
        return listResultSetHandler.apply(resultSet);
    }
}
