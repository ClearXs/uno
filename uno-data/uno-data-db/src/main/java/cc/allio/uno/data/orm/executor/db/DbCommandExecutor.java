package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.api.Adapter;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.InnerCommandExecutorManager;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.data.orm.executor.options.ExecutorOptionsImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.omg.CORBA.UNKNOWN;

import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于MybatisSQL执行器
 *
 * @author j.x
 * @date 2023/4/14 13:45
 * @see CommandExecutorFactory
 * @since 1.1.4
 */
@Slf4j
public class DbCommandExecutor extends AbstractCommandExecutor implements AggregateCommandExecutor {

    private final Executor executor;
    private final LanguageDriver languageDriver;
    private final DbMybatisConfiguration configuration;
    private final MybatisSQLCommandAdapter sqlCommandAdapter;
    private final OperatorGroup operatorGroup;
    private static final String PACKAGE_NAME = DbCommandExecutor.class.getPackage().getName();

    public DbCommandExecutor(DbMybatisConfiguration configuration) {
        this(new ExecutorOptionsImpl(
                DataSourceHelper.getDbType(configuration.getEnvironment().getDataSource()),
                ExecutorKey.DB,
                OperatorKey.SQL), configuration);
    }

    public DbCommandExecutor(ExecutorOptions options, DbMybatisConfiguration configuration) {
        super(options);
        if (configuration == null) {
            throw new NullPointerException(String.format("expect %s but not found", Configuration.class.getName()));
        }
        this.configuration = configuration;
        Environment environment = configuration.getEnvironment();
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        Transaction tx = transactionFactory.newTransaction(environment.getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
        this.executor = new SimpleExecutor(configuration, tx);
        this.languageDriver = new RawLanguageDriver();
        this.sqlCommandAdapter = new MybatisSQLCommandAdapter();
        this.operatorGroup = OperatorGroup.getOperatorGroup(OperatorKey.SQL, options);
    }

    @Override
    protected boolean doBool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler) {
        if (commandType == null) {
            throw new IllegalArgumentException("support correct sql command , but it null");
        }
        SqlCommandType sqlCommandType = sqlCommandAdapter.adapt(commandType);
        SqlSource sqlSource;
        Object parameter = null;
        String printSQL;
        if (operator instanceof PrepareOperator) {
            printSQL = ((PrepareOperator<?>) operator).getPrepareDSL();
            sqlSource = languageDriver.createSqlSource(configuration, printSQL, null);
            parameter = ((PrepareOperator<?>) operator).toMapValue();
        } else {
            printSQL = operator.getDSL();
            sqlSource = languageDriver.createSqlSource(configuration, printSQL, null);
        }
        ParameterMap parameterMap = getParameterMap(operator);
        String cacheId = getCacheId();
        MappedStatement.Builder statementBuilder =
                new MappedStatement
                        .Builder(configuration, cacheId, sqlSource, sqlCommandType)
                        .parameterMap(parameterMap);
        // 验证连接是否正常，如果异常则重新建立连接
        checkAndReset();
        try {
            if (CommandType.EXIST_TABLE == commandType || CommandType.SELECT == commandType) {
                ResultMap resultMap =
                        new ResultMap.Builder(
                                configuration,
                                IdGenerator.defaultGenerator().getNextIdAsString(),
                                ResultGroup.class,
                                Collections.emptyList())
                                .build();
                MappedStatement statement = statementBuilder.resultMaps(Collections.singletonList(resultMap)).build();
                List<ResultGroup> resultGroups = executor.query(statement, parameter, RowBounds.DEFAULT, null);
                return resultGroups.stream().anyMatch(resultSetHandler::apply);
            } else {
                MappedStatement statement = statementBuilder.build();
                ResultGroup resultGroup = new ResultGroup();
                int result = executor.update(statement, parameter);
                resultGroup.addRow(
                        ResultRow.builder()
                                .index(0)
                                .column(DSLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                                .javaType(new IntegerJavaType())
                                .value(result)
                                .build());
                return resultSetHandler.apply(resultGroup);
            }

        } catch (SQLException ex) {
            // rollback
            if (log.isWarnEnabled()) {
                log.warn("Operate ['bool'] executor failed, now rollback", ex);
            }
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
    protected <R> List<R> doQueryList(Operator<?> operator, CommandType commandType, ListResultSetHandler<R> resultSetHandler) {
        QueryOperator<?> queryOperator;
        if (commandType == CommandType.SELECT) {
            queryOperator = (QueryOperator<?>) operator;
        } else if (commandType == CommandType.SHOW_TABLES) {
            queryOperator = ((ShowTablesOperator<?>) operator).toQueryOperator();
        } else if (commandType == CommandType.SHOW_COLUMNS) {
            queryOperator = ((ShowColumnsOperator<?>) operator).toQueryOperator();
        } else {
            throw new DSLException(String.format("un accept command to Db query list %s", commandType));
        }

        String querySQL = queryOperator.getPrepareDSL();
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, querySQL, null);
        // 构建ResultMap对象
        ResultMap resultMap =
                new ResultMap.Builder(
                        configuration,
                        IdGenerator.defaultGenerator().getNextIdAsString(),
                        ResultGroup.class,
                        Collections.emptyList()).build();
        ParameterMap parameterMap = getParameterMap(queryOperator);
        String cacheId = getCacheId();
        MappedStatement statement =
                new MappedStatement.Builder(configuration, cacheId, sqlSource, SqlCommandType.SELECT)
                        .resultMaps(Collections.singletonList(resultMap))
                        .parameterMap(parameterMap)
                        .lang(languageDriver)
                        .useCache(false)
                        .resultSetType(ResultSetType.DEFAULT)
                        .build();
        // 验证连接是否正常，如果异常则重新建立连接
        checkAndReset();
        try {
            List<ResultGroup> resultGroups = executor.query(statement, queryOperator.toMapValue(), RowBounds.DEFAULT, null);
            ResultSet resultSet = new ResultSet();
            resultSet.setResultGroups(resultGroups);
            return resultSetHandler.apply(resultSet);
        } catch (Throwable ex) {
            if (log.isWarnEnabled()) {
                log.warn("Execute query failure {}", ex.getMessage());
            }
            throw new DSLException(ex);
        }
    }

    @Override
    protected InnerCommandExecutorManager getInnerCommandExecutorManager() {
        return null;
    }

    @Override
    public boolean check() throws SocketTimeoutException {
        try {
            Connection connection = executor.getTransaction().getConnection();
            return !connection.isClosed();
        } catch (SQLException ex) {
            throw new SocketTimeoutException(ex.getMessage());
        }
    }

    @Override
    public ExecutorKey getKey() {
        return ExecutorKey.DB;
    }

    @Override
    public OperatorGroup getOperatorGroup() {
        return operatorGroup;
    }

    @Override
    public void destroy() {
        this.executor.close(true);
    }

    /**
     * getParameterMap
     *
     * @param sqlOperator sqlOperator
     * @return ParameterMap
     */
    private ParameterMap getParameterMap(Operator<?> sqlOperator) {
        if (sqlOperator instanceof PrepareOperator<?>) {
            List<PrepareValue> prepareValues = ((PrepareOperator<?>) sqlOperator).getPrepareValues();
            List<ParameterMapping> parameterMappings = prepareValues.stream()
                    .map(prepareValue -> {
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
    private void checkAndReset() {
        try {
            boolean check = check();
            if (!check) {
                resetConnection();
            }
        } catch (SocketTimeoutException ex) {
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

    public static class MybatisSQLCommandAdapter implements Adapter<SqlCommandType, CommandType> {

        @Override
        public SqlCommandType adapt(CommandType sqlCommand) {
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
                default:
                    return null;
            }
        }

        @Override
        public CommandType reverse(SqlCommandType sqlCommandType) {
            switch (sqlCommandType) {
                case SELECT:
                    return CommandType.SELECT;
                case INSERT:
                    return CommandType.INSERT;
                case FLUSH:
                    return CommandType.FLUSH;
                case DELETE:
                    return CommandType.DELETE;
                case UPDATE:
                    return CommandType.UPDATE;
                case UNKNOWN:
                    return CommandType.UNKNOWN;
                default:
                    return null;
            }
        }
    }

    private String getCacheId() {
        return PACKAGE_NAME + StringPool.SLASH + getOptions().getKey() + StringPool.SLASH + IdGenerator.defaultGenerator().getNextIdAsString();
    }
}
