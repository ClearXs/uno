package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.api.Key;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.executor.elasticsearch.EsSQLCommandExecutor;
import cc.allio.uno.data.orm.executor.mybatis.MybatisSQLCommandExecutor;
import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.ddl.SQLCreateTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLDropTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLExistTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;
import cc.allio.uno.data.orm.sql.dml.SQLDeleteOperator;
import cc.allio.uno.data.orm.sql.dml.SQLInsertOperator;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.SQLUpdateOperator;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * SQL执行器定义
 * TODO 应该考虑采用拦截器做些特殊的事项（如自动赋值id）
 *
 * @author jiangwei
 * @date 2023/4/14 13:43
 * @since 1.1.4
 */
public interface SQLCommandExecutor {

    // sql executor
    String SQL_EXECUTOR_TYPE_KEY = "allio.uno.data.orm.executor.type";
    ExecutorKey MYBATIS_SQL_COMMAND_EXECUTOR_KEY = () -> "mybatis";
    ExecutorKey ELASTICSEARCH_SQL_COMMAND_EXECUTOR_KEY = () -> "elasticsearch";
    ExecutorKey REDIS_SQL_COMMAND_KEY = () -> "redis";
    ExecutorKey MONGODB_SQL_COMMAND_KEY = () -> "mongodb";
    ExecutorKey INFLUXDB_SQL_COMMAND_KEY = () -> "influxdb";
    ExecutorKey NEO4J_SQL_COMMAND_KEY = () -> "neo4j";

    interface ExecutorKey extends Key {
        @Override
        default String getProperties() {
            return SQL_EXECUTOR_TYPE_KEY;
        }
    }

    /**
     * 获取系统默认executor key
     *
     * @return ExecutorKey
     */
    static ExecutorKey getSystemExecutorKey() {
        String executorKey = Envs.getProperty(SQL_EXECUTOR_TYPE_KEY);
        if (MYBATIS_SQL_COMMAND_EXECUTOR_KEY.getKey().equals(executorKey)) {
            return MYBATIS_SQL_COMMAND_EXECUTOR_KEY;
        } else if (ELASTICSEARCH_SQL_COMMAND_EXECUTOR_KEY.getKey().equals(executorKey)) {
            return ELASTICSEARCH_SQL_COMMAND_EXECUTOR_KEY;
        } else if (REDIS_SQL_COMMAND_KEY.getKey().equals(executorKey)) {
            return REDIS_SQL_COMMAND_KEY;
        } else if (MONGODB_SQL_COMMAND_KEY.getKey().equals(executorKey)) {
            return MONGODB_SQL_COMMAND_KEY;
        } else if (INFLUXDB_SQL_COMMAND_KEY.getKey().equals(executorKey)) {
            return INFLUXDB_SQL_COMMAND_KEY;
        } else if (NEO4J_SQL_COMMAND_KEY.getKey().equals(executorKey)) {
            return NEO4J_SQL_COMMAND_KEY;
        }
        return MYBATIS_SQL_COMMAND_EXECUTOR_KEY;
    }

    /**
     * 根据pojo class创建表
     *
     * @param pojoClass the pojoClass
     * @return true 成功 false 失败
     */
    default <P> boolean createTable(Class<P> pojoClass) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojoClass);
        return createTable(o -> o.from(pojoWrapper.getTable()).columns(pojoWrapper.getSQLColumnDef()));
    }

    /**
     * 创表
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean createTable(UnaryOperator<SQLCreateTableOperator> func) {
        return createTable(func.apply(getOperatorMetadata().createTable()));
    }

    /**
     * 创表
     *
     * @param createTableOperator SQLCreateTableOperator
     * @return true 成功 false 失败
     */
    default boolean createTable(SQLCreateTableOperator createTableOperator) {
        return bool(createTableOperator, SQLCommandType.CREATE_TABLE);
    }

    /**
     * 创表
     *
     * @param createTableOperator SQLCreateTableOperator
     * @param resultSetHandler    resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean createTable(SQLCreateTableOperator createTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(createTableOperator, SQLCommandType.CREATE_TABLE, resultSetHandler);
    }

    /**
     * 根据pojo class删除表
     *
     * @param tableName the tableName
     * @return true 成功 false 失败
     */
    default boolean dropTable(String tableName) {
        return dropTable(o -> o.from(tableName));
    }

    /**
     * 根据pojo class删除表
     *
     * @param table the table
     * @return true 成功 false 失败
     */
    default boolean dropTable(Table table) {
        return dropTable(o -> o.from(table));
    }

    /**
     * 根据pojo class删除表
     *
     * @param pojoClass the pojoClass
     * @return true 成功 false 失败
     */
    default <P> boolean dropTable(Class<P> pojoClass) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojoClass);
        return dropTable(o -> o.from(pojoWrapper.getTable()));
    }

    /**
     * 删表
     *
     * @param func func
     * @return true 成功 false 失败
     */
    default boolean dropTable(UnaryOperator<SQLDropTableOperator> func) {
        return dropTable(func.apply(getOperatorMetadata().dropTable()));
    }

    /**
     * 删表
     *
     * @param dropTableOperator dropTableOperator
     * @return true 成功 false 失败
     */
    default boolean dropTable(SQLDropTableOperator dropTableOperator) {
        return bool(dropTableOperator, SQLCommandType.DELETE_TABLE);
    }

    /**
     * 删表
     *
     * @param dropTableOperator dropTableOperator
     * @param resultSetHandler  resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean dropTable(SQLDropTableOperator dropTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(dropTableOperator, SQLCommandType.DELETE, resultSetHandler);
    }

    /**
     * 判断表是否存在
     *
     * @param tableName the tableName
     * @return true 成功 false 失败
     */
    default boolean existTable(String tableName) {
        return existTable(o -> o.from(tableName));
    }

    /**
     * 判断表是否存在
     *
     * @param table the table
     * @return true 成功 false 失败
     */
    default boolean existTable(Table table) {
        return existTable(o -> o.from(table));
    }

    /**
     * 根据pojoClass判断是否存在
     *
     * @param pojoClass pojoClass
     * @return true 成功 false 失败
     */
    default <P> boolean existTable(Class<P> pojoClass) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojoClass);
        return existTable(o -> o.from(pojoWrapper.getTable()));
    }

    /**
     * 判断表是否存在
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean existTable(UnaryOperator<SQLExistTableOperator> func) {
        return existTable(func.apply(getOperatorMetadata().existTable()));
    }

    /**
     * 判断表是否存在
     *
     * @param existTableOperator SQLExistTableOperator
     * @return true 成功 false 失败
     */
    default boolean existTable(SQLExistTableOperator existTableOperator) {
        return bool(existTableOperator, SQLCommandType.EXIST_TABLE);
    }

    /**
     * 判断表是否存在
     *
     * @param existTableOperator SQLExistTableOperator
     * @param resultSetHandler   resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean existTable(SQLExistTableOperator existTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(existTableOperator, SQLCommandType.EXIST_TABLE, resultSetHandler);
    }

    /**
     * 插入数据
     *
     * @param pojo pojo
     * @return true 成功 false 失败
     */
    default <P> boolean insertPojo(P pojo) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
        return insert(o -> o.from(pojoWrapper.getTable()).insertPojo(pojoWrapper.getPojoValue()));
    }

    /**
     * 插入数据（认定为同一个表数据）
     *
     * @param pojos pojos
     * @return true 成功 false 失败
     */
    default <P> boolean batchInsertPojos(List<P> pojos) {
        if (CollectionUtils.isEmpty(pojos)) {
            return false;
        }
        P thePojo = pojos.get(0);
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(thePojo);
        // values
        List<P> pojoValues = pojos.stream().map(pojo -> new PojoWrapper<>(pojo).getPojoValue()).collect(Collectors.toList());
        return insert(o -> o.from(pojoWrapper.getTable()).batchInsertPojos(pojoValues));
    }

    /**
     * 插入数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean insert(UnaryOperator<SQLInsertOperator> func) {
        return insert(func.apply(getOperatorMetadata().insert()));
    }

    /**
     * 插入数据
     *
     * @param sqlInsertOperator SQLInsertOperator
     * @return true 成功 false 失败
     */
    default boolean insert(SQLInsertOperator sqlInsertOperator) {
        return bool(sqlInsertOperator, SQLCommandType.INSERT);
    }

    /**
     * 插入数据
     *
     * @param sqlInsertOperator SQLInsertOperator
     * @param resultSetHandler  resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean insert(SQLInsertOperator sqlInsertOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(sqlInsertOperator, SQLCommandType.INSERT, resultSetHandler);
    }

    /**
     * 更新数据
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean updatePojo(P pojo) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
        return update(o -> o.from(pojoWrapper.getTable()).updatePojo(pojo));
    }

    /**
     * 更新数据
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean updatePojoById(P pojo, Object id) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
        SQLColumnDef theId = pojoWrapper.findByIdColumn();
        return updatePojoByCondition(pojo, condition -> condition.eq(theId.getSqlName(), id));
    }

    /**
     * 更新数据根据条件
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean updatePojoByCondition(P pojo, UnaryOperator<SQLWhereOperator<SQLUpdateOperator>> condition) {
        return update(o -> {
            PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
            SQLUpdateOperator updateOperator = o.from(pojoWrapper.getTable()).updatePojo(pojo);
            condition.apply(updateOperator);
            return updateOperator;
        });
    }

    /**
     * 更新数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean update(UnaryOperator<SQLUpdateOperator> func) {
        return update(func.apply(getOperatorMetadata().update()));
    }

    /**
     * 更新数据
     *
     * @param updateOperator SQLUpdateOperator
     * @return true 成功 false 失败
     */
    default boolean update(SQLUpdateOperator updateOperator) {
        return bool(updateOperator, SQLCommandType.UPDATE);
    }

    /**
     * 更新数据
     *
     * @param updateOperator   SQLUpdateOperator
     * @param resultSetHandler resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean update(SQLUpdateOperator updateOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(updateOperator, SQLCommandType.UPDATE, resultSetHandler);
    }

    /**
     * 删除数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean delete(UnaryOperator<SQLDeleteOperator> func) {
        return delete(func.apply(getOperatorMetadata().delete()));
    }

    /**
     * 删除数据
     *
     * @param deleteOperator SQLUpdateOperator
     * @return true 成功 false 失败
     */
    default boolean delete(SQLDeleteOperator deleteOperator) {
        return bool(deleteOperator, SQLCommandType.DELETE);
    }

    /**
     * 删除数据
     *
     * @param deleteOperator   SQLUpdateOperator
     * @param resultSetHandler resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean delete(SQLDeleteOperator deleteOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(deleteOperator, SQLCommandType.DELETE, resultSetHandler);
    }

    /**
     * bool操作，包含创建、更新、删除、插入表、删除表...
     *
     * @param operator   SQLOperator操作
     * @param sqlCommand sql命令
     * @return true 成功 false 失败
     */
    default boolean bool(SQLOperator<?> operator, SQLCommandType sqlCommand) {
        return bool(operator, sqlCommand, new BoolResultHandler());
    }

    /**
     * bool操作，包含创建、更新、删除、插入表、删除表...
     *
     * @param operator   SQLOperator操作
     * @param sqlCommand sql命令
     * @return true 成功 false 失败
     */
    boolean bool(SQLOperator<?> operator, SQLCommandType sqlCommand, ResultSetHandler<Boolean> resultSetHandler);

    /**
     * save or update。根据pojo的id查询是否存在 存在则更新，否则查询
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean saveOrUpdate(P pojo) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
        SQLColumnDef id = pojoWrapper.findByIdColumn();
        return saveOrUpdate(pojo, f -> f.select(id.getSqlName()).from(pojoWrapper.getTable()).eq(id.getSqlName(), pojoWrapper.findByIdValue()));
    }

    /**
     * save or update
     *
     * @param pojo the pojo
     * @param func the func
     * @return true 成功 false 失败
     */
    default <P> boolean saveOrUpdate(P pojo, UnaryOperator<SQLQueryOperator> func) {
        return saveOrUpdate(pojo, func.apply(getOperatorMetadata().query()), new IdMethodReferenceColumn<P>(pojo));
    }

    /**
     * save or update
     *
     * @param pojo     the pojo
     * @param func     the func
     * @param eqUpdate 用于比较更新字段
     * @return true 成功 false 失败
     */
    default <P> boolean saveOrUpdate(P pojo, UnaryOperator<SQLQueryOperator> func, MethodReferenceColumn<P> eqUpdate) {
        return saveOrUpdate(pojo, func.apply(getOperatorMetadata().query()), eqUpdate);
    }

    /**
     * save or update
     *
     * @param pojo      the pojo
     * @param dataExist the dataExist
     * @param eqUpdate  用于比较更新字段
     * @return true 成功 false 失败
     */
    default <P> boolean saveOrUpdate(P pojo, SQLQueryOperator dataExist, MethodReferenceColumn<P> eqUpdate) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
        return saveOrUpdate(
                dataExist,
                () -> {
                    SQLUpdateOperator updateOperator = getOperatorMetadata().update();
                    updateOperator.from(pojoWrapper.getTable());
                    String column = eqUpdate.getColumn();
                    // update for eq
                    Object eqValue = pojoWrapper.findByColumnValue(column);
                    if (eqValue != null) {
                        updateOperator.eq(column, eqValue);
                    }
                    // 不调用pojoWrapper.getPojoValue()，原因可能id会被赋值上，违反唯一约束，应该采用拦截器实现
                    updateOperator.updatePojo(pojo);
                    return updateOperator;
                },
                () -> {
                    SQLInsertOperator insertOperator = getOperatorMetadata().insert();
                    insertOperator.from(pojoWrapper.getTable());
                    insertOperator.insertPojo(pojoWrapper.getPojoValue());
                    return insertOperator;
                });
    }

    /**
     * save or update
     *
     * @param f1 判断数据是否存在的query操作
     * @param f2 如果数据存在则update
     * @param f3 如果数据不存在则insert
     * @return true 成功 false 失败
     */
    default boolean saveOrUpdate(UnaryOperator<SQLQueryOperator> f1, UnaryOperator<SQLUpdateOperator> f2, UnaryOperator<SQLInsertOperator> f3) {
        return saveOrUpdate(f1.apply(getOperatorMetadata().query()), () -> f2.apply(getOperatorMetadata().update()), () -> f3.apply(getOperatorMetadata().insert()));
    }

    /**
     * save or update
     *
     * @param dataExist      判断数据是否存在的query操作
     * @param updateOperator 如果数据存在则update
     * @param insertOperator 如果数据不存在则insert
     * @return true 成功 false 失败
     */
    default boolean saveOrUpdate(SQLQueryOperator dataExist, SQLUpdateOperator updateOperator, SQLInsertOperator insertOperator) {
        return saveOrUpdate(dataExist, () -> updateOperator, () -> insertOperator);
    }

    /**
     * save or update
     * <b>延迟模式</b>
     *
     * @param dataExist      判断数据是否存在的query操作
     * @param updateOperator 如果数据存在则update
     * @param insertOperator 如果数据不存在则insert
     * @return true 成功 false 失败
     */
    default boolean saveOrUpdate(SQLQueryOperator dataExist, Supplier<SQLUpdateOperator> updateOperator, Supplier<SQLInsertOperator> insertOperator) {
        boolean isExist = bool(dataExist, SQLCommandType.SELECT);
        if (!isExist) {
            return insert(insertOperator.get());
        }
        return update(updateOperator.get());
    }

    /**
     * 查询一个数据
     *
     * @param entityClass entityClass
     * @param <T>         实体类型
     * @return 实体 or null
     */
    default <T> T queryOneById(Class<T> entityClass, Object id) {
        PojoWrapper<T> pojoWrapper = new PojoWrapper<>(entityClass);
        return queryOne(o -> o.from(pojoWrapper.getTable()).eq(pojoWrapper.findByIdColumn().getSqlName(), id), entityClass);
    }

    /**
     * 查询一个数据
     *
     * @param func        the func
     * @param entityClass entityClass
     * @param <T>         实体类型
     * @return 实体 or null
     */
    default <T> T queryOne(UnaryOperator<SQLQueryOperator> func, Class<T> entityClass) {
        return queryOne(func.apply(getOperatorMetadata().query()), new BeanResultSetHandler<>(entityClass));
    }

    /**
     * 查询一个数据
     *
     * @param queryOperator queryOperator
     * @param entityClass   entityClass
     * @param <T>           实体类型
     * @return 实体 or null
     */
    default <T> T queryOne(SQLQueryOperator queryOperator, Class<T> entityClass) {
        return queryOne(queryOperator, new BeanResultSetHandler<>(entityClass));
    }

    /**
     * 查询结果Map
     *
     * @param func the func
     * @return map
     */
    default Map<String, Object> queryMap(UnaryOperator<SQLQueryOperator> func) {
        return queryMap(func.apply(getOperatorMetadata().query()));
    }

    /**
     * 查询结果Map
     *
     * @param queryOperator queryOperator
     * @return map
     */
    default Map<String, Object> queryMap(SQLQueryOperator queryOperator) {
        return queryOne(queryOperator, new MapResultSetHandler());
    }

    /**
     * 查询一个结果
     *
     * @param func the func
     * @return ResultGroup
     */
    default ResultGroup queryOne(UnaryOperator<SQLQueryOperator> func) {
        return queryOne(func.apply(getOperatorMetadata().query()));
    }

    /**
     * 查询一个结果
     *
     * @param queryOperator SQLQueryOperator
     * @return ResultGroup
     */
    default ResultGroup queryOne(SQLQueryOperator queryOperator) {
        return queryOne(queryOperator, new DefaultResultSetHandler());
    }

    /**
     * 查询一个结果
     *
     * @param func the func
     * @param <R>  结果集对象
     * @return ResultGroup
     */
    default <R> R queryOne(UnaryOperator<SQLQueryOperator> func, ResultSetHandler<R> resultSetHandler) {
        return queryOne(func.apply(getOperatorMetadata().query()), resultSetHandler);
    }

    /**
     * 查询一个结果
     *
     * @param queryOperator SQLQueryOperator
     * @param <R>           结果集对象
     * @return ResultGroup
     */
    default <R> R queryOne(SQLQueryOperator queryOperator, ResultSetHandler<R> resultSetHandler) {
        List<ResultGroup> resultGroups = queryList(queryOperator);
        if (CollectionUtils.isNotEmpty(resultGroups)) {
            int size = resultGroups.size();
            if (size == 1) {
                return resultSetHandler.apply(resultGroups.get(0));
            } else if (size > 1) {
                throw new SQLException("Expected one result (or null) to be returned by queryOne(), but found: " + size);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 查询list实体
     *
     * @param func        the func
     * @param entityClass entityClass
     * @param <T>         类型
     * @return list
     */
    default <T> List<T> queryList(UnaryOperator<SQLQueryOperator> func, Class<T> entityClass) {
        return queryList(func.apply(getOperatorMetadata().query()), entityClass);
    }

    /**
     * 查询list实体
     *
     * @param queryOperator SQLQueryOperator
     * @param entityClass   entityClass
     * @param <T>           类型
     * @return list
     */
    default <T> List<T> queryList(SQLQueryOperator queryOperator, Class<T> entityClass) {
        return queryList(queryOperator, new ListBeanResultSetHandler<>(entityClass));
    }

    /**
     * 查询list-Map
     *
     * @param func the func
     * @return list map
     */
    default List<Map<String, Object>> queryListMap(UnaryOperator<SQLQueryOperator> func) {
        return queryListMap(func.apply(getOperatorMetadata().query()));
    }

    /**
     * 查询list-Map
     *
     * @param queryOperator SQLQueryOperator
     * @return list map
     */
    default List<Map<String, Object>> queryListMap(SQLQueryOperator queryOperator) {
        return queryList(queryOperator, new ListMapResultHandler());
    }

    /**
     * 查询list
     *
     * @param func the func
     * @return List
     * @throws SQLException query failed throw
     */
    default List<ResultGroup> queryList(UnaryOperator<SQLQueryOperator> func) {
        return queryList(func.apply(getOperatorMetadata().query()));
    }

    /**
     * 查询list
     *
     * @param queryOperator SQLQueryOperator
     * @return List
     * @throws SQLException query failed throw
     */
    default List<ResultGroup> queryList(SQLQueryOperator queryOperator) {
        return queryList(queryOperator, new DefaultListResultSetHandler());
    }

    /**
     * 查询list
     *
     * @param func             the func
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws SQLException query failed throw
     */
    default <R> List<R> queryList(UnaryOperator<SQLQueryOperator> func, ListResultSetHandler<R> resultSetHandler) {
        return queryList(func.apply(getOperatorMetadata().query()), resultSetHandler);
    }

    /**
     * 查询list
     *
     * @param queryOperator    SQLQueryOperator
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws SQLException query failed throw
     */
    <R> List<R> queryList(SQLQueryOperator queryOperator, ListResultSetHandler<R> resultSetHandler);

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @param <R>  返回结果类型
     * @return List
     * @throws SQLException query failed throw
     */
    default <R> IPage<R> queryPage(IPage<?> page, UnaryOperator<SQLQueryOperator> func, Class<R> entityClass) {
        return queryPage(page, func.apply(getOperatorMetadata().query()), entityClass);
    }

    /**
     * 查询分页
     *
     * @param page          page
     * @param queryOperator SQLQueryOperator
     * @param <R>           返回结果类型
     * @return List
     * @throws SQLException query failed throw
     */
    default <R> IPage<R> queryPage(IPage<?> page, SQLQueryOperator queryOperator, Class<R> entityClass) {
        return queryPage(page, queryOperator, new ListBeanResultSetHandler<>(entityClass));
    }

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @return List
     * @throws SQLException query failed throw
     */
    default IPage<Map<String, Object>> queryPageMap(IPage<?> page, UnaryOperator<SQLQueryOperator> func) {
        return queryPageMap(page, func.apply(getOperatorMetadata().query()));
    }

    /**
     * 查询分页
     *
     * @param page          page
     * @param queryOperator SQLQueryOperator
     * @return List
     * @throws SQLException query failed throw
     */
    default IPage<Map<String, Object>> queryPageMap(IPage<?> page, SQLQueryOperator queryOperator) {
        return queryPage(page, queryOperator, new ListMapResultHandler());
    }

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @return List
     * @throws SQLException query failed throw
     */
    default IPage<ResultGroup> queryPage(IPage<?> page, UnaryOperator<SQLQueryOperator> func) {
        return queryPage(page, func.apply(getOperatorMetadata().query()));
    }

    /**
     * 查询分页
     *
     * @param page          page
     * @param queryOperator SQLQueryOperator
     * @return List
     * @throws SQLException query failed throw
     */
    default IPage<ResultGroup> queryPage(IPage<?> page, SQLQueryOperator queryOperator) {
        return queryPage(page, queryOperator, new DefaultListResultSetHandler());
    }

    /**
     * 查询分页
     *
     * @param page             page
     * @param func             the func
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws SQLException query failed throw
     * @see #queryList(SQLQueryOperator, ListResultSetHandler)
     */
    default <R> IPage<R> queryPage(IPage<?> page, UnaryOperator<SQLQueryOperator> func, ListResultSetHandler<R> resultSetHandler) {
        return queryPage(page, func.apply(getOperatorMetadata().query()), resultSetHandler);
    }

    /**
     * 查询分页
     *
     * @param page             page
     * @param queryOperator    SQLQueryOperator
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws SQLException query failed throw
     * @see #queryList(SQLQueryOperator, ListResultSetHandler)
     */
    default <R> IPage<R> queryPage(IPage<?> page, SQLQueryOperator queryOperator, ListResultSetHandler<R> resultSetHandler) {
        queryOperator.page(page.getCurrent(), page.getSize());
        List<R> r = queryList(queryOperator, resultSetHandler);
        Page<R> rPage = new Page<>(page);
        rPage.setTotal(r.size());
        rPage.setRecords(r);
        return rPage;
    }

    /**
     * 获取某个表的字段
     *
     * @param pojoClass pojoClass
     * @return SQLColumnDef
     */
    default <T> List<SQLColumnDef> showColumns(Class<T> pojoClass) {
        PojoWrapper<T> pojoWrapper = new PojoWrapper<>(pojoClass);
        return showColumns(pojoWrapper.getTable());
    }

    /**
     * 获取某个表的字段
     *
     * @param tableName tableName
     * @return SQLColumnDef
     */
    default List<SQLColumnDef> showColumns(String tableName) {
        return showColumns(o -> o.from(tableName));
    }

    /**
     * 获取某个表的字段
     *
     * @param table table
     * @return SQLColumnDef
     */
    default List<SQLColumnDef> showColumns(Table table) {
        return showColumns(o -> o.from(table));
    }

    /**
     * 获取某个表的字段
     *
     * @param func the func
     * @return SQLColumnDef
     */
    default List<SQLColumnDef> showColumns(UnaryOperator<SQLShowColumnsOperator> func) {
        return showColumns(func.apply(getOperatorMetadata().showColumns()));
    }

    /**
     * 获取某个表的字段
     *
     * @param sqlShowColumnsOperator sqlShowColumnsOperator
     * @return SQLColumnDef
     */
    default List<SQLColumnDef> showColumns(SQLShowColumnsOperator sqlShowColumnsOperator) {
        return queryList(sqlShowColumnsOperator.toQueryOperator(), new SQLColumnDefListResultSetHandler());
    }

    /**
     * 获取执行器key
     *
     * @return ExecutorKey
     */
    ExecutorKey getKey();

    /**
     * 获取SQL操作元数据。获取的将会是默认的值（也可以指定，建议使用默认）比如；
     * <li>
     *     <ul>{@link MybatisSQLCommandExecutor} -> {@link DruidOperatorMetadata}</ul>
     *     <ul>{@link EsSQLCommandExecutor} -> {@link ElasticSearchOperatorMetadata}</ul>
     *     <ul>...（待实现）</ul>
     * </li>
     *
     * @return OperatorMetadata实例
     */
    OperatorMetadata getOperatorMetadata();
}