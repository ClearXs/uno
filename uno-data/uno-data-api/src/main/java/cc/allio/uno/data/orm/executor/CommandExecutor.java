package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Command Executor
 *
 * @author jiangwei
 * @date 2023/4/14 13:43
 * @since 1.1.4
 */
public interface CommandExecutor {

    /**
     * 根据pojo class创建表
     *
     * @param pojoClass the pojoClass
     * @return true 成功 false 失败
     */
    default <P> boolean createTable(Class<P> pojoClass) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojoClass);
        return createTable(o -> o.from(pojoWrapper.getTable()).columns(pojoWrapper.getColumnDef()));
    }

    /**
     * 创表
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean createTable(UnaryOperator<CreateTableOperator> func) {
        return createTable(func.apply(getOperatorGroup().createTable(getOptions().getDbType())));
    }

    /**
     * 创表
     *
     * @param createTableOperator CreateTableOperator
     * @return true 成功 false 失败
     */
    default boolean createTable(CreateTableOperator createTableOperator) {
        return bool(createTableOperator, CommandType.CREATE_TABLE);
    }

    /**
     * 创表
     *
     * @param createTableOperator CreateTableOperator
     * @param resultSetHandler    resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean createTable(CreateTableOperator createTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(createTableOperator, CommandType.CREATE_TABLE, resultSetHandler);
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
     * @param tableName the tableName
     * @return true 成功 false 失败
     */
    default boolean dropTable(DSLName tableName) {
        return dropTable(o -> o.from(tableName));
    }

    /**
     * 根据pojo class删除表
     *
     * @param table the xxxx
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
    default boolean dropTable(UnaryOperator<DropTableOperator> func) {
        return dropTable(func.apply(getOperatorGroup().dropTable(getOptions().getDbType())));
    }

    /**
     * 删表
     *
     * @param dropTableOperator dropTableOperator
     * @return true 成功 false 失败
     */
    default boolean dropTable(DropTableOperator dropTableOperator) {
        return bool(dropTableOperator, CommandType.DELETE_TABLE);
    }

    /**
     * 删表
     *
     * @param dropTableOperator dropTableOperator
     * @param resultSetHandler  resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean dropTable(DropTableOperator dropTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(dropTableOperator, CommandType.DELETE, resultSetHandler);
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
     * @param table the xxxx
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
    default boolean existTable(UnaryOperator<ExistTableOperator> func) {
        return existTable(func.apply(getOperatorGroup().existTable(getOptions().getDbType())));
    }

    /**
     * 判断表是否存在
     *
     * @param existTableOperator ExistTableOperator
     * @return true 成功 false 失败
     */
    default boolean existTable(ExistTableOperator existTableOperator) {
        return bool(existTableOperator, CommandType.EXIST_TABLE);
    }

    /**
     * 判断表是否存在
     *
     * @param existTableOperator ExistTableOperator
     * @param resultSetHandler   resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean existTable(ExistTableOperator existTableOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(existTableOperator, CommandType.EXIST_TABLE, resultSetHandler);
    }

    /**
     * 获取某个表的字段
     *
     * @param pojoClass pojoClass
     * @return ColumnDef
     */
    default <T> List<ColumnDef> showColumns(Class<T> pojoClass) {
        PojoWrapper<T> pojoWrapper = new PojoWrapper<>(pojoClass);
        return showColumns(pojoWrapper.getTable());
    }

    /**
     * 获取某个表的字段
     *
     * @param tableName tableName
     * @return ColumnDef
     */
    default List<ColumnDef> showColumns(String tableName) {
        return showColumns(o -> o.from(tableName));
    }

    /**
     * 获取某个表的字段
     *
     * @param table xxxx
     * @return ColumnDef
     */
    default List<ColumnDef> showColumns(Table table) {
        return showColumns(o -> o.from(table));
    }

    /**
     * 获取某个表的字段
     *
     * @param func the func
     * @return ColumnDef
     */
    default List<ColumnDef> showColumns(UnaryOperator<ShowColumnsOperator> func) {
        return showColumns(func.apply(getOperatorGroup().showColumns(getOptions().getDbType())));
    }

    /**
     * 获取某个表的字段
     *
     * @param showColumnsOperator showColumnsOperator
     * @return ColumnDef
     */
    default List<ColumnDef> showColumns(ShowColumnsOperator showColumnsOperator) {
        return queryList(showColumnsOperator.toQueryOperator(), CommandType.SHOW_COLUMNS, new DSLColumnDefListResultSetHandler());
    }

    /**
     * 基于{@link ExecutorOptions#getDatabase()}获取数据库表
     *
     * @return List<Table>
     */
    default List<Table> showTables() {
        return showTables(o -> o.database(getOptions().getDatabase()));
    }

    /**
     * 获取某个数据库的表
     *
     * @param database database
     * @return List<Table>
     */
    default List<Table> showTables(String database) {
        return showTables(o -> o.database(database));
    }

    /**
     * 获取某个数据库的表
     *
     * @param database database
     * @return List<Table>
     */
    default List<Table> showTables(Database database) {
        return showTables(o -> o.database(database));
    }

    /**
     * 获取某个数据库的表
     *
     * @param func func
     * @return List<Table>
     */
    default List<Table> showTables(UnaryOperator<ShowTablesOperator> func) {
        return showTables(func.apply(getOperatorGroup().showTables(getOptions().getDbType())));
    }

    /**
     * 获取某个数据库的表
     *
     * @param showTablesOperator showTablesOperator
     * @return List<Table>
     */
    default List<Table> showTables(ShowTablesOperator showTablesOperator) {
        return queryList(showTablesOperator.toQueryOperator(), CommandType.SHOW_TABLES, new DSLTableListResultSetHandler());
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
        return insert(o -> o.from(pojoWrapper.getTable()).batchInsertPojos(pojos));
    }

    /**
     * 插入数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean insert(UnaryOperator<InsertOperator> func) {
        return insert(func.apply(getOperatorGroup().insert(getOptions().getDbType())));
    }

    /**
     * 插入数据
     *
     * @param insertOperator insertOperator
     * @return true 成功 false 失败
     */
    default boolean insert(InsertOperator insertOperator) {
        return bool(insertOperator, CommandType.INSERT);
    }

    /**
     * 插入数据
     *
     * @param insertOperator   insertOperator
     * @param resultSetHandler resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean insert(InsertOperator insertOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(insertOperator, CommandType.INSERT, resultSetHandler);
    }

    /**
     * 更新数据
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean updatePojo(P pojo) {
        return update(o -> o.updatePojo(pojo));
    }

    /**
     * 更新数据
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P, ID extends Serializable> boolean updatePojoById(P pojo, ID id) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
        ColumnDef theId = pojoWrapper.getPKColumn();
        return updatePojoByCondition(pojo, condition -> condition.eq(theId.getDslName(), id));
    }

    /**
     * 更新数据根据条件
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean updatePojoByCondition(P pojo, UnaryOperator<WhereOperator<UpdateOperator>> condition) {
        return update(o -> {
            PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
            UpdateOperator updateOperator = o.from(pojoWrapper.getTable()).updatePojo(pojo);
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
    default boolean update(UnaryOperator<UpdateOperator> func) {
        return update(func.apply(getOperatorGroup().update(getOptions().getDbType())));
    }

    /**
     * 更新数据
     *
     * @param updateOperator updateOperator
     * @return true 成功 false 失败
     */
    default boolean update(UpdateOperator updateOperator) {
        return bool(updateOperator, CommandType.UPDATE);
    }

    /**
     * 更新数据
     *
     * @param updateOperator   updateOperator
     * @param resultSetHandler resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean update(UpdateOperator updateOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(updateOperator, CommandType.UPDATE, resultSetHandler);
    }

    /**
     * 基于POJO删除数据
     * <b>需要注意，该API不会直接删除数据，如果存在删除标记，则会把删除标记做更新</b>
     *
     * @param pojo pojo
     * @return true 成功 false 失败
     * @see #deleteById(Class, Serializable)
     */
    default <T> boolean delete(T pojo) {
        PojoWrapper<T> pojoWrapper = new PojoWrapper<>(pojo);
        Object pkValue = ValueWrapper.restore(pojoWrapper.getPKValue());
        return deleteById(pojo.getClass(), (Serializable) pkValue);
    }

    /**
     * 根据id删除数据
     * <b>需要注意，该API不会直接删除数据，如果存在删除标记，则会把删除标记做更新</b>
     *
     * @param pojoClass pojoClass
     * @param id        id
     * @return true 成功 false 失败
     */
    default <T, ID extends Serializable> boolean deleteById(Class<T> pojoClass, ID id) {
        ColumnDef pkColumn = PojoWrapper.findPKColumn(pojoClass);
        if (pkColumn == null) {
            throw new DSLException("Can not find the primary key column");
        }
        UpdateOperator update = getOperatorGroup().update(getOptions().getDbType());
        return bool(update.from(pojoClass).eq(pkColumn.getDslName(), id), CommandType.DELETE);
    }

    /**
     * 删除所有数据
     * <b>需要注意，该API不会直接删除数据，如果存在删除标记，则会把删除标记做更新</b>
     *
     * @param pojoClass pojoClass
     * @return true 成功 false 失败
     */
    default <T> boolean deleteAll(Class<T> pojoClass) {
        ColumnDef pkColumn = PojoWrapper.findPKColumn(pojoClass);
        if (pkColumn == null) {
            throw new DSLException("Can not find the primary key column");
        }
        UpdateOperator update = getOperatorGroup().update(getOptions().getDbType());
        return bool(update.from(pojoClass), CommandType.DELETE);
    }

    /**
     * 删除所有数据
     * <b>需要注意，该API不会直接删除数据，如果存在删除标记，则会把删除标记做更新</b>
     *
     * @param pojoClass pojoClass
     * @return true 成功 false 失败
     */
    default <T> boolean deleteAll(Class<T> pojoClass, Iterable<? extends T> pojos) {
        return deleteAllById(
                pojoClass,
                Lists.newArrayList(pojos)
                        .stream()
                        .map(pojo -> {
                            PojoWrapper<T> pojoWrapper = new PojoWrapper<>(pojo);
                            Object pkValue = ValueWrapper.restore(pojoWrapper.getPKValue());
                            return (Serializable) pkValue;
                        })
                        .toList());
    }

    /**
     * 批量根据id删除数据
     * <b>需要注意，该API不会直接删除数据，如果存在删除标记，则会把删除标记做更新</b>
     *
     * @param pojoClass pojoClass
     * @param ids       ids
     * @return true 成功 false 失败
     */
    default <T, ID extends Serializable> boolean deleteAllById(Class<T> pojoClass, Iterable<ID> ids) {
        ColumnDef pkColumn = PojoWrapper.findPKColumn(pojoClass);
        if (pkColumn == null) {
            throw new DSLException("Can not find the primary key column");
        }
        UpdateOperator update = getOperatorGroup().update(getOptions().getDbType());
        return bool(update.from(pojoClass).in(pkColumn.getDslName(), ids), CommandType.DELETE);
    }

    /**
     * 删除数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean delete(UnaryOperator<DeleteOperator> func) {
        return delete(func.apply(getOperatorGroup().delete(getOptions().getDbType())));
    }

    /**
     * 删除数据
     *
     * @param deleteOperator deleteOperator
     * @return true 成功 false 失败
     */
    default boolean delete(DeleteOperator deleteOperator) {
        return bool(deleteOperator, CommandType.DELETE);
    }

    /**
     * 删除数据
     *
     * @param deleteOperator   deleteOperator
     * @param resultSetHandler resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean delete(DeleteOperator deleteOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(deleteOperator, CommandType.DELETE, resultSetHandler);
    }

    /**
     * bool操作，包含创建、更新、删除、插入表、删除表...
     *
     * @param operator Operator操作
     * @param command  command
     * @return true 成功 false 失败
     */
    default boolean bool(Operator<?> operator, CommandType command) {
        return bool(operator, command, new BoolResultHandler());
    }

    /**
     * bool操作，包含创建、更新、删除、插入表、删除表...
     *
     * @param operator         operator
     * @param commandType      命令类型
     * @param resultSetHandler 结果集处理器
     * @return true 成功 false 失败
     */
    boolean bool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler);

    /**
     * save or update。根据pojo的id查询是否存在 存在则更新，否则查询
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean saveOrUpdate(P pojo) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
        return saveOrUpdate(pojo, () -> {
            Object pkValue = ValueWrapper.restore(pojoWrapper.getPKValue());
            return pkValue != null;
        });
    }

    /**
     * save or update
     *
     * @param pojo the pojo
     * @param func the func
     * @return true 成功 false 失败
     */
    default <P> boolean saveOrUpdate(P pojo, BooleanSupplier func) {
        return saveOrUpdate(pojo, func, new IdMethodReferenceColumn<>(pojo));
    }

    /**
     * save or update
     *
     * @param pojo      the pojo
     * @param dataExist the dataExist
     * @param eqUpdate  用于比较更新字段
     * @return true 成功 false 失败
     */
    default <P> boolean saveOrUpdate(P pojo, BooleanSupplier dataExist, MethodReferenceColumn<P> eqUpdate) {
        PojoWrapper<P> pojoWrapper = new PojoWrapper<>(pojo);
        return saveOrUpdate(
                dataExist,
                () -> {
                    UpdateOperator updateOperator = getOperatorGroup().update(getOptions().getDbType());
                    updateOperator.from(pojoWrapper.getTable());
                    String column = eqUpdate.getColumn();
                    // update for eq
                    Object eqValue = pojoWrapper.getValueByColumn(column);
                    if (eqValue != null) {
                        updateOperator.eq(column, eqValue);
                    }
                    // 不调用pojoWrapper.getPojoValue()，原因可能id会被赋值上，违反唯一约束，应该采用拦截器实现
                    updateOperator.updatePojo(pojo);
                    return updateOperator;
                },
                () -> {
                    InsertOperator insertOperator = getOperatorGroup().insert(getOptions().getDbType());
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
    default boolean saveOrUpdate(BooleanSupplier f1, UnaryOperator<UpdateOperator> f2, UnaryOperator<InsertOperator> f3) {
        return saveOrUpdate(f1, () -> f2.apply(getOperatorGroup().update(getOptions().getDbType())), () -> f3.apply(getOperatorGroup().insert(getOptions().getDbType())));
    }

    /**
     * save or update
     *
     * @param dataExist      判断数据是否存在的query操作
     * @param updateOperator 如果数据存在则update
     * @param insertOperator 如果数据不存在则insert
     * @return true 成功 false 失败
     */
    default boolean saveOrUpdate(BooleanSupplier dataExist, UpdateOperator updateOperator, InsertOperator insertOperator) {
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
    default boolean saveOrUpdate(BooleanSupplier dataExist, Supplier<UpdateOperator> updateOperator, Supplier<InsertOperator> insertOperator) {
        boolean isExist = dataExist.getAsBoolean();
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
    default <T, ID extends Serializable> T queryOneById(Class<T> entityClass, ID id) {
        PojoWrapper<T> pojoWrapper = new PojoWrapper<>(entityClass);
        return queryOne(entityClass, o ->
                o.selects(PojoWrapper.findColumns(entityClass))
                        .from(pojoWrapper.getTable())
                        .eq(pojoWrapper.getPKColumn().getDslName(), id));
    }

    /**
     * 查询一个数据
     *
     * @param entityClass entityClass
     * @param <T>         实体类型
     * @return 实体 or null
     */
    default <T> T queryOne(Class<T> entityClass) {
        return queryOne(entityClass, o -> o.selects(PojoWrapper.findColumns(entityClass)).from(entityClass));
    }

    /**
     * 查询一个数据
     *
     * @param func        the func
     * @param entityClass entityClass
     * @param <T>         实体类型
     * @return 实体 or null
     */
    default <T> T queryOne(Class<T> entityClass, UnaryOperator<QueryOperator> func) {
        return queryOne(func.apply(getOperatorGroup().query(getOptions().getDbType())), new BeanResultSetHandler<>(entityClass));
    }

    /**
     * 查询一个数据
     *
     * @param queryOperator queryOperator
     * @param entityClass   entityClass
     * @param <T>           实体类型
     * @return 实体 or null
     */
    default <T> T queryOne(Class<T> entityClass, QueryOperator queryOperator) {
        return queryOne(queryOperator, new BeanResultSetHandler<>(entityClass));
    }

    /**
     * 查询一个结果
     *
     * @param func the func
     * @return ResultGroup
     */
    default ResultGroup queryOne(UnaryOperator<QueryOperator> func) {
        return queryOne(func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询结果Map
     *
     * @param func the func
     * @return map
     */
    default Map<String, Object> queryMap(UnaryOperator<QueryOperator> func) {
        return queryMap(func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询结果Map
     *
     * @param queryOperator queryOperator
     * @return map
     */
    default Map<String, Object> queryMap(QueryOperator queryOperator) {
        return queryOne(queryOperator, new MapResultSetHandler());
    }

    /**
     * 查询一个结果
     *
     * @param queryOperator queryOperator
     * @return ResultGroup
     */
    default ResultGroup queryOne(QueryOperator queryOperator) {
        return queryOne(queryOperator, new DefaultResultSetHandler());
    }

    /**
     * 查询一个结果
     *
     * @param func the func
     * @param <R>  结果集对象
     * @return ResultGroup
     */
    default <R> R queryOne(UnaryOperator<QueryOperator> func, ResultSetHandler<R> resultSetHandler) {
        return queryOne(func.apply(getOperatorGroup().query(getOptions().getDbType())), resultSetHandler);
    }

    /**
     * 查询一个结果
     *
     * @param queryOperator queryOperator
     * @param <R>           结果集对象
     * @return ResultGroup
     */
    default <R> R queryOne(QueryOperator queryOperator, ResultSetHandler<R> resultSetHandler) {
        List<ResultGroup> resultGroups = queryList(queryOperator);
        if (CollectionUtils.isNotEmpty(resultGroups)) {
            int size = resultGroups.size();
            if (size == 1) {
                return resultSetHandler.apply(resultGroups.get(0));
            } else if (size > 1) {
                throw new DSLException("Expected one result (or null) to be returned by queryOne(), but found: " + size);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 查询list实体
     *
     * @param entityClass entityClass
     * @param <T>         类型
     * @return list
     */
    default <T> List<T> queryList(Class<T> entityClass) {
        return queryList(entityClass, o -> o.selects(PojoWrapper.findColumns(entityClass)).from(entityClass));
    }

    /**
     * 根据ids集合查询list实体
     *
     * @param entityClass entityClass
     * @param ids         ids
     * @param <T>         类型
     * @return list
     */
    default <T, ID extends Serializable> List<T> queryListByIds(Class<T> entityClass, Iterable<ID> ids) {
        ColumnDef pkColumn = PojoWrapper.findPKColumn(entityClass);
        if (pkColumn == null) {
            throw new DSLException("Can not find the primary key column");
        }
        return queryList(entityClass, o -> o.selects(PojoWrapper.findColumns(entityClass)).from(entityClass).in(pkColumn.getDslName(), ids));
    }

    /**
     * 查询list实体
     *
     * @param func        the func
     * @param entityClass entityClass
     * @param <T>         类型
     * @return list
     */
    default <T> List<T> queryList(Class<T> entityClass, UnaryOperator<QueryOperator> func) {
        return queryList(entityClass, func.apply(
                getOperatorGroup()
                        .query(getOptions().getDbType())
                        .selects(PojoWrapper.findColumns(entityClass))
                        .from(entityClass)));
    }

    /**
     * 查询list实体
     *
     * @param queryOperator queryOperator
     * @param entityClass   entityClass
     * @param <T>           类型
     * @return list
     */
    default <T> List<T> queryList(Class<T> entityClass, QueryOperator queryOperator) {
        return queryList(queryOperator, new ListBeanResultSetHandler<>(entityClass));
    }

    /**
     * 查询list-Map
     *
     * @param func the func
     * @return list map
     */
    default List<Map<String, Object>> queryListMap(UnaryOperator<QueryOperator> func) {
        return queryListMap(func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询list-Map
     *
     * @param queryOperator queryOperator
     * @return list map
     */
    default List<Map<String, Object>> queryListMap(QueryOperator queryOperator) {
        return queryList(queryOperator, new ListMapResultHandler());
    }

    /**
     * 查询list
     *
     * @param func the func
     * @return List
     * @throws DSLException query failed throw
     */
    default List<ResultGroup> queryList(UnaryOperator<QueryOperator> func) {
        return queryList(func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询list
     *
     * @param queryOperator queryOperator
     * @return List
     * @throws DSLException query failed throw
     */
    default List<ResultGroup> queryList(QueryOperator queryOperator) {
        return queryList(queryOperator, new DefaultListResultSetHandler());
    }

    /**
     * 查询list
     *
     * @param func             the func
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws DSLException query failed throw
     */
    default <R> List<R> queryList(UnaryOperator<QueryOperator> func, ListResultSetHandler<R> resultSetHandler) {
        return queryList(func.apply(getOperatorGroup().query(getOptions().getDbType())), resultSetHandler);
    }

    /**
     * 查询list
     *
     * @param queryOperator    queryOperator
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws DSLException query failed throw
     */
    default <R> List<R> queryList(QueryOperator queryOperator, ListResultSetHandler<R> resultSetHandler) {
        return queryList(queryOperator, CommandType.SELECT, resultSetHandler);
    }

    /**
     * 查询list
     *
     * @param queryOperator    queryOperator
     * @param commandType      命令类型
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws DSLException query failed throw
     */
    <R> List<R> queryList(QueryOperator queryOperator, CommandType commandType, ListResultSetHandler<R> resultSetHandler);

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @param <R>  返回结果类型
     * @return List
     * @throws DSLException query failed throw
     */
    default <R> IPage<R> queryPage(IPage<?> page, UnaryOperator<QueryOperator> func, Class<R> entityClass) {
        return queryPage(page, func.apply(getOperatorGroup().query(getOptions().getDbType())), entityClass);
    }

    /**
     * 查询分页
     *
     * @param page          page
     * @param queryOperator queryOperator
     * @param <R>           返回结果类型
     * @return List
     * @throws DSLException query failed throw
     */
    default <R> IPage<R> queryPage(IPage<?> page, QueryOperator queryOperator, Class<R> entityClass) {
        return queryPage(page, queryOperator, new ListBeanResultSetHandler<>(entityClass));
    }

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @return List
     * @throws DSLException query failed throw
     */
    default IPage<Map<String, Object>> queryPageMap(IPage<?> page, UnaryOperator<QueryOperator> func) {
        return queryPageMap(page, func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询分页
     *
     * @param page          page
     * @param queryOperator queryOperator
     * @return List
     * @throws DSLException query failed throw
     */
    default IPage<Map<String, Object>> queryPageMap(IPage<?> page, QueryOperator queryOperator) {
        return queryPage(page, queryOperator, new ListMapResultHandler());
    }

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @return List
     * @throws DSLException query failed throw
     */
    default IPage<ResultGroup> queryPage(IPage<?> page, UnaryOperator<QueryOperator> func) {
        return queryPage(page, func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询分页
     *
     * @param page          page
     * @param queryOperator queryOperator
     * @return List
     * @throws DSLException query failed throw
     */
    default IPage<ResultGroup> queryPage(IPage<?> page, QueryOperator queryOperator) {
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
     * @throws DSLException query failed throw
     * @see #queryList(QueryOperator, ListResultSetHandler)
     */
    default <R> IPage<R> queryPage(IPage<?> page, UnaryOperator<QueryOperator> func, ListResultSetHandler<R> resultSetHandler) {
        return queryPage(page, func.apply(getOperatorGroup().query(getOptions().getDbType())), resultSetHandler);
    }

    /**
     * 查询分页
     *
     * @param page             page
     * @param queryOperator    queryOperator
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws DSLException query failed throw
     * @see #queryList(QueryOperator, ListResultSetHandler)
     */
    default <R> IPage<R> queryPage(IPage<?> page, QueryOperator queryOperator, ListResultSetHandler<R> resultSetHandler) {
        queryOperator.page(page.getCurrent(), page.getSize());
        List<R> r = queryList(queryOperator, resultSetHandler);
        Page<R> rPage = new Page<>(page);
        rPage.setTotal(r.size());
        rPage.setRecords(r);
        return rPage;
    }

    /**
     * 检查连接是否正常
     *
     * @return it ture normal connection
     * @throws SocketTimeoutException timeout throws
     */
    boolean check() throws SocketTimeoutException;

    /**
     * 获取执行器key
     *
     * @return ExecutorKey
     */
    ExecutorKey getKey();

    /**
     * 获取DSL操作元数据。
     *
     * @return OperatorMetadata实例
     */
    OperatorGroup getOperatorGroup();

    /**
     * 获取执行器参数
     *
     * @return ExecutorOptions
     */
    ExecutorOptions getOptions();

    /**
     * 销毁，关闭数据连接通道
     */
    default void destroy() {

    }
}