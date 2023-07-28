package cc.allio.uno.data.orm.executor.mybatis;

import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.SQLAdapter;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.type.IntegerJavaType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于MybatisSQL执行器
 *
 * @author jiangwei
 * @date 2023/4/14 13:45
 * @see SQLCommandExecutorFactory
 * @since 1.1.4
 */
@Slf4j
public class MybatisSQLCommandExecutor implements SQLCommandExecutor {

    private final Executor executor;
    private final LanguageDriver languageDriver;
    private final UnoMybatisConfiguration configuration;
    private final MybatisSQLCommandAdapter sqlCommandAdapter;
    private final DruidOperatorMetadata operatorMetadata;
    private static final String PACKAGE_NAME = MybatisSQLCommandExecutor.class.getPackage().getName();

    public MybatisSQLCommandExecutor() {
        this(new Object[]{new Configuration()});
    }

    public MybatisSQLCommandExecutor(Object[] values) {
        Configuration configuration = CollectionUtils.findValueOfType(Lists.newArrayList(values), Configuration.class);
        if (configuration == null) {
            throw new NullPointerException(String.format("expect %s but not found", Configuration.class.getName()));
        }
        this.configuration = new UnoMybatisConfiguration(configuration);
        Environment environment = configuration.getEnvironment();
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        Transaction tx = transactionFactory.newTransaction(environment.getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
        this.executor = configuration.newExecutor(tx);
        this.languageDriver = new RawLanguageDriver();
        this.sqlCommandAdapter = new MybatisSQLCommandAdapter();
        this.operatorMetadata = new DruidOperatorMetadata();
    }

    @Override
    public boolean bool(SQLOperator<?> operator, SQLCommandType sqlCommand, ResultSetHandler<Boolean> resultSetHandler) {
        if (sqlCommand == null) {
            throw new IllegalArgumentException("support correct sql command , but it null");
        }
        SqlCommandType sqlCommandType = sqlCommandAdapter.get(sqlCommand);
        SqlSource sqlSource;
        Object parameter = null;
        if (operator instanceof SQLPrepareOperator) {
            sqlSource = languageDriver.createSqlSource(configuration, ((SQLPrepareOperator<?>) operator).getPrepareSQL(), null);
            parameter = ((SQLPrepareOperator<?>) operator).toMapValue();
        } else {
            sqlSource = languageDriver.createSqlSource(configuration, operator.getSQL(), null);
        }
        MappedStatement.Builder statementBuilder =
                new MappedStatement
                        .Builder(configuration, PACKAGE_NAME + IdGenerator.defaultGenerator().getNextIdAsString(), sqlSource, sqlCommandType)
                        .parameterMap(getParameterMap(operator));
        // 验证连接是否正常，如果异常则重新建立连接
        checkConnection();
        try {
            if (SQLCommandType.EXIST_TABLE == sqlCommand || SQLCommandType.SELECT == sqlCommand) {
                ResultMap resultMap =
                        new ResultMap.Builder(configuration, IdGenerator.defaultGenerator().getNextIdAsString(), ResultGroup.class, Collections.emptyList()).build();
                MappedStatement statement = statementBuilder.resultMaps(Collections.singletonList(resultMap)).build();
                List<ResultGroup> resultGroups = executor.query(statement, parameter, RowBounds.DEFAULT, null);
                return resultGroups.stream().anyMatch(resultSetHandler::apply);
            } else {
                MappedStatement statement = statementBuilder.build();
                ResultGroup resultGroup = new ResultGroup();
                resultGroup.addRow(
                        ResultRow.builder()
                                .index(0)
                                .column(SQLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                                .javaType(new IntegerJavaType())
                                .value(executor.update(statement, parameter))
                                .build());
                return resultSetHandler.apply(resultGroup);
            }
        } catch (SQLException ex) {
            // rollback
            log.error("SQL: {} executor failed, now rollback", sqlSource, ex);
            try {
                executor.rollback(true);
            } catch (SQLException ex2) {
                // ignore
            }
        } finally {
            try {
                executor.commit(true);
            } catch (SQLException ex) {
                // ignore
            }
        }
        return false;
    }

    @Override
    public <R> List<R> queryList(SQLQueryOperator queryOperator, ListResultSetHandler<R> resultSetHandler) {
        String querySQL = queryOperator.getPrepareSQL();
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, querySQL, null);
        // 构建ResultMap对象
        ResultMap resultMap =
                new ResultMap.Builder(configuration, IdGenerator.defaultGenerator().getNextIdAsString(), ResultGroup.class, Collections.emptyList())
                        .build();
        MappedStatement statement =
                new MappedStatement.Builder(configuration, PACKAGE_NAME + IdGenerator.defaultGenerator().getNextIdAsString(), sqlSource, SqlCommandType.SELECT)
                        .resultMaps(Collections.singletonList(resultMap))
                        .parameterMap(getParameterMap(queryOperator))
                        .lang(languageDriver)
                        .useCache(false)
                        .resultSetType(ResultSetType.DEFAULT)
                        .build();
        // 验证连接是否正常，如果异常则重新建立连接
        checkConnection();
        try {
            List<ResultGroup> resultGroups = executor.query(statement, queryOperator.toMapValue(), RowBounds.DEFAULT, null);
            ResultSet resultSet = new ResultSet();
            resultSet.setResultGroups(resultGroups);
            return resultSetHandler.apply(resultSet);
        } catch (Throwable ex) {
            log.error("Execute query SQL: {} failure", querySQL, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    public ExecutorKey getKey() {
        return SQLCommandExecutor.MYBATIS_SQL_COMMAND_EXECUTOR_KEY;
    }

    @Override
    public OperatorMetadata getOperatorMetadata() {
        return operatorMetadata;
    }

    /**
     * 获取mybatis ParameterMap
     *
     * @param sqlOperator sqlOperator
     * @return ParameterMap
     */
    private ParameterMap getParameterMap(SQLOperator<?> sqlOperator) {
        if (sqlOperator instanceof SQLPrepareOperator) {
            List<PrepareValue> prepareValues = ((SQLPrepareOperator<?>) sqlOperator).getPrepareValues();
            List<ParameterMapping> parameterMappings = prepareValues.stream().map(prepareValue -> {
                        Class<?> javaType;
                        try {
                            javaType = prepareValue.getJavaType().getJavaType();
                        } catch (UnsupportedOperationException ex) {
                            // ignore maybe unknown java type
                            javaType = Object.class;
                        }
                        return new ParameterMapping
                                .Builder(configuration, prepareValue.getColumn(), javaType)
                                .build();
                    })
                    .collect(Collectors.toList());
            return new ParameterMap
                    .Builder(configuration, IdGenerator.defaultGenerator().getNextIdAsString(), null, parameterMappings)
                    .build();
        }
        return new ParameterMap.Builder(configuration, "defaultParameterMap", null, new ArrayList<>()).build();
    }

    /**
     * 检查当前{@link Executor}的Connection.检查关闭后，重置连接
     */
    private void checkConnection() {
        try {
            Connection connection = executor.getTransaction().getConnection();
            if (connection.isClosed()) {
                resetConnection();
            }
        } catch (SQLException ex) {
            // ignore
        }
    }

    /**
     * 重置{@link Executor}的Connection
     */
    private void resetConnection() {
        Transaction transaction = executor.getTransaction();
        try {
            Field connectionField = transaction.getClass().getDeclaredField("connection");
            ClassUtils.setAccessible(connectionField);
            connectionField.set(transaction, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // ignore
        }
    }

    public static class MybatisSQLCommandAdapter implements SQLAdapter<SqlCommandType, SQLCommandType> {

        @Override
        public SqlCommandType get(SQLCommandType sqlCommand) {
            switch (sqlCommand) {
                case UNKNOWN:
                    return SqlCommandType.UNKNOWN;
                case FLUSH:
                    return SqlCommandType.FLUSH;
                case DELETE:
                    return SqlCommandType.DELETE;
                case INSERT:
                    return SqlCommandType.INSERT;
                case SELECT:
                    return SqlCommandType.SELECT;
                case UPDATE:
                    return SqlCommandType.UPDATE;
            }
            return null;
        }

        @Override
        public SQLCommandType reversal(SqlCommandType sqlCommandType) {
            return null;
        }
    }
}
