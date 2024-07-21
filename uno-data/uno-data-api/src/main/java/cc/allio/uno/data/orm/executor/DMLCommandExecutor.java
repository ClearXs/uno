package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.WhereOperator;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.CohesionListResultSetHandler;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * contains DML relativity command operator.
 * <ul>
 *     <li>{@link InsertOperator}</li>
 *     <li>{@link UpdateOperator}</li>
 *     <li>{@link DeleteOperator}</li>
 *     <li>{@link QueryOperator}</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/3/15 08:27
 * @since 1.1.7
 */
public interface DMLCommandExecutor extends CommandExecutor {

    /**
     * 插入数据
     *
     * @param pojo pojo
     * @return true 成功 false 失败
     */
    default <P> boolean insertPojo(P pojo) {
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojo);
        return insert(o -> o.from(pojoWrapper.getTable()).insertPojo(pojoWrapper.getPojo()));
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
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(thePojo);
        return insert(o -> o.from(pojoWrapper.getTable()).batchInsertPojos(pojos));
    }

    /**
     * 插入数据
     * <p>根据指定的{@link InsertOperator}插入数据</p>
     *
     * @param pojoClass pojoClass
     * @param func      the func
     * @return true 成功 false 失败
     */
    default <T> boolean insert(Class<T> pojoClass, UnaryOperator<InsertOperator<?>> func) {
        InsertOperator<?> insertOperator = getOperatorGroup().insert(getOptions().getDbType());
        return insert(func.apply(insertOperator.from(pojoClass)));
    }

    /**
     * 插入数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean insert(UnaryOperator<InsertOperator<?>> func) {
        return insert(func.apply(getOperatorGroup().insert(getOptions().getDbType())));
    }

    /**
     * 插入数据
     *
     * @param insertOperator insertOperator
     * @return true 成功 false 失败
     */
    default boolean insert(InsertOperator<?> insertOperator) {
        return bool(insertOperator, CommandType.INSERT);
    }

    /**
     * 插入数据
     *
     * @param insertOperator   insertOperator
     * @param resultSetHandler resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean insert(InsertOperator<?> insertOperator, ResultSetHandler<Boolean> resultSetHandler) {
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
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojo);
        ColumnDef theId = pojoWrapper.getPkColumn();
        return updatePojoByCondition(pojo, condition -> (WhereOperator<?>) condition.eq(theId.getDslName(), id));
    }

    /**
     * 更新数据根据条件
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean updatePojoByCondition(P pojo, UnaryOperator<WhereOperator<?>> condition) {
        return update(o -> {
            PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojo);
            UpdateOperator<?> updateOperator = o.from(pojoWrapper.getTable()).updatePojo(pojo);
            condition.apply(updateOperator);
            return updateOperator;
        });
    }

    /**
     * 更新数据
     * <p>根据给定{@link UpdateOperator}更新数据</p>
     *
     * @param pojoClass pojoClass
     * @param func      the func
     * @return true 成功 false 失败
     */
    default <T> boolean update(Class<T> pojoClass, UnaryOperator<UpdateOperator<?>> func) {
        UpdateOperator<?> updateOperator = getOperatorGroup().update(getOptions().getDbType());
        return update(func.apply(updateOperator.from(pojoClass)));
    }

    /**
     * 更新数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean update(UnaryOperator<UpdateOperator<?>> func) {
        return update(func.apply(getOperatorGroup().update(getOptions().getDbType())));
    }

    /**
     * 更新数据
     *
     * @param updateOperator updateOperator
     * @return true 成功 false 失败
     */
    default boolean update(UpdateOperator<?> updateOperator) {
        return bool(updateOperator, CommandType.UPDATE);
    }

    /**
     * 更新数据
     *
     * @param updateOperator   updateOperator
     * @param resultSetHandler resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean update(UpdateOperator<?> updateOperator, ResultSetHandler<Boolean> resultSetHandler) {
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
        PojoWrapper<T> pojoWrapper = PojoWrapper.getInstance(pojo);
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
        UpdateOperator<?> update = getOperatorGroup().update(getOptions().getDbType());
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
        UpdateOperator<?> update = getOperatorGroup().update(getOptions().getDbType());
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
                            PojoWrapper<T> pojoWrapper = PojoWrapper.getInstance(pojo);
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
        UpdateOperator<?> update = getOperatorGroup().update(getOptions().getDbType());
        return bool(update.from(pojoClass).in(pkColumn.getDslName(), ids), CommandType.DELETE);
    }

    /**
     * 删除数据
     * <p>根据给定的条件删除数据</p>
     *
     * @param pojoClass pojoClass
     * @return true 成功 false 失败
     */
    default <T> boolean delete(Class<T> pojoClass, UnaryOperator<DeleteOperator<?>> func) {
        DeleteOperator<?> deleteOperator = getOperatorGroup().delete(getOptions().getDbType());
        return delete(func.apply(deleteOperator.from(pojoClass)));
    }

    /**
     * 删除数据
     *
     * @param func the func
     * @return true 成功 false 失败
     */
    default boolean delete(UnaryOperator<DeleteOperator<?>> func) {
        return delete(func.apply(getOperatorGroup().delete(getOptions().getDbType())));
    }

    /**
     * 删除数据
     *
     * @param deleteOperator deleteOperator
     * @return true 成功 false 失败
     */
    default boolean delete(DeleteOperator<?> deleteOperator) {
        return bool(deleteOperator, CommandType.DELETE);
    }

    /**
     * 删除数据
     *
     * @param deleteOperator   deleteOperator
     * @param resultSetHandler resultSetHandler
     * @return true 成功 false 失败
     */
    default boolean delete(DeleteOperator<?> deleteOperator, ResultSetHandler<Boolean> resultSetHandler) {
        return bool(deleteOperator, CommandType.DELETE, resultSetHandler);
    }

    /**
     * save or update。根据pojo的id查询是否存在 存在则更新，否则查询
     *
     * @param pojo the pojo
     * @return true 成功 false 失败
     */
    default <P> boolean saveOrUpdate(P pojo) {
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojo);
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
        PojoWrapper<P> pojoWrapper = PojoWrapper.getInstance(pojo);
        return saveOrUpdate(
                dataExist,
                () -> {
                    UpdateOperator<?> updateOperator = getOperatorGroup().update(getOptions().getDbType());
                    updateOperator.from(pojoWrapper.getTable());
                    String column = eqUpdate.getColumn();
                    // update for eq
                    Object eqValue = pojoWrapper.getValueByColumn(column);
                    if (eqValue != null) {
                        updateOperator.eq(column, eqValue);
                    }
                    // 不调用pojoWrapper.getPojo()，原因可能id会被赋值上，违反唯一约束，应该采用拦截器实现
                    updateOperator.updatePojo(pojo);
                    return updateOperator;
                },
                () -> {
                    InsertOperator<?> insertOperator = getOperatorGroup().insert(getOptions().getDbType());
                    insertOperator.from(pojoWrapper.getTable());
                    insertOperator.insertPojo(pojoWrapper.getPojo());
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
    default boolean saveOrUpdate(BooleanSupplier f1, UnaryOperator<UpdateOperator<?>> f2, UnaryOperator<InsertOperator<?>> f3) {
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
    default boolean saveOrUpdate(BooleanSupplier dataExist, UpdateOperator<?> updateOperator, InsertOperator<?> insertOperator) {
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
    default boolean saveOrUpdate(BooleanSupplier dataExist, Supplier<UpdateOperator<?>> updateOperator, Supplier<InsertOperator<?>> insertOperator) {
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
        PojoWrapper<T> pojoWrapper = PojoWrapper.getInstance(entityClass);
        return queryOne(entityClass, o ->
                o.selects(pojoWrapper.getColumnDSLName())
                        .from(pojoWrapper.getTable())
                        .eq(pojoWrapper.getPkColumn().getDslName(), id));
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
    default <T> T queryOne(Class<T> entityClass, UnaryOperator<QueryOperator<?>> func) {
        return queryOne(func.apply(getOperatorGroup().query(getOptions().getDbType())), getOptions().obtainBeanResultSetHandler(entityClass));
    }

    /**
     * 查询一个数据
     *
     * @param queryOperator queryOperator
     * @param entityClass   entityClass
     * @param <T>           实体类型
     * @return 实体 or null
     */
    default <T> T queryOne(Class<T> entityClass, QueryOperator<?> queryOperator) {
        return queryOne(queryOperator, getOptions().obtainBeanResultSetHandler(entityClass));
    }

    /**
     * 查询一个结果
     *
     * @param func the func
     * @return ResultGroup
     */
    default ResultGroup queryOne(UnaryOperator<QueryOperator<?>> func) {
        return queryOne(func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询结果Map
     *
     * @param func the func
     * @return map
     */
    default Map<String, Object> queryMap(UnaryOperator<QueryOperator<?>> func) {
        return queryMap(func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询结果Map
     *
     * @param queryOperator queryOperator
     * @return map
     */
    default Map<String, Object> queryMap(QueryOperator<?> queryOperator) {
        return queryOne(queryOperator, getOptions().obtainMapResultSetHandler());
    }

    /**
     * 查询一个结果
     *
     * @param queryOperator queryOperator
     * @return ResultGroup
     */
    default ResultGroup queryOne(QueryOperator<?> queryOperator) {
        return queryOne(queryOperator, getOptions().obtainDefaultResultSetHandler());
    }

    /**
     * 查询一个结果
     *
     * @param func the func
     * @param <R>  结果集对象
     * @return ResultGroup
     */
    default <R> R queryOne(UnaryOperator<QueryOperator<?>> func, ResultSetHandler<R> resultSetHandler) {
        return queryOne(func.apply(getOperatorGroup().query(getOptions().getDbType())), resultSetHandler);
    }

    /**
     * 查询一个结果
     *
     * @param queryOperator queryOperator
     * @param <R>           结果集对象
     * @return ResultGroup
     * @throws DSLException 当结果集大于1时抛出
     */
    default <R> R queryOne(QueryOperator<?> queryOperator, ResultSetHandler<R> resultSetHandler) {
        List<R> resultGroups = queryList(queryOperator, new CohesionListResultSetHandler<>(resultSetHandler));
        return checkCollectionIsOneAndGet(resultGroups);
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
    default <T> List<T> queryList(Class<T> entityClass, UnaryOperator<QueryOperator<?>> func) {
        return queryList(
                entityClass,
                func.apply(
                        getOperatorGroup()
                                .query(getOptions().getDbType())
                                .selects(PojoWrapper.findColumns(entityClass))
                                .from(entityClass)
                )
        );
    }

    /**
     * 查询list实体
     *
     * @param queryOperator queryOperator
     * @param entityClass   entityClass
     * @param <T>           类型
     * @return list
     */
    default <T> List<T> queryList(Class<T> entityClass, QueryOperator<?> queryOperator) {
        return queryList(queryOperator, getOptions().obtainListBeanResultSetHandler(entityClass));
    }

    /**
     * 查询list-Map
     *
     * @param func the func
     * @return list map
     */
    default List<Map<String, Object>> queryListMap(UnaryOperator<QueryOperator<?>> func) {
        return queryListMap(func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询list-Map
     *
     * @param queryOperator queryOperator
     * @return list map
     */
    default List<Map<String, Object>> queryListMap(QueryOperator<?> queryOperator) {
        return queryList(queryOperator, getOptions().obtainListMapResultHandler());
    }

    /**
     * 查询list
     *
     * @param func the func
     * @return List
     * @throws DSLException query failed throw
     */
    default List<ResultGroup> queryList(UnaryOperator<QueryOperator<?>> func) {
        return queryList(func.apply(getOperatorGroup().query(getOptions().getDbType())));
    }

    /**
     * 查询list
     *
     * @param queryOperator queryOperator
     * @return List
     * @throws DSLException query failed throw
     */
    default List<ResultGroup> queryList(QueryOperator<?> queryOperator) {
        return queryList(queryOperator, getOptions().obtainDefaultListResultSetHandler());
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
    default <R> List<R> queryList(UnaryOperator<QueryOperator<?>> func, ListResultSetHandler<R> resultSetHandler) {
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
    default <R> List<R> queryList(QueryOperator<?> queryOperator, ListResultSetHandler<R> resultSetHandler) {
        return queryList(queryOperator, CommandType.SELECT, resultSetHandler);
    }


    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @param <R>  返回结果类型
     * @return List
     * @throws DSLException query failed throw
     */
    default <R> IPage<R> queryPage(IPage<?> page, UnaryOperator<QueryOperator<?>> func, Class<R> entityClass) {
        PojoWrapper<R> pojoWrapper = PojoWrapper.getInstance(entityClass);
        return queryPage(
                page,
                func.apply(
                        getOperatorGroup()
                                .query(getOptions().getDbType())
                                .selects(pojoWrapper.getColumnDSLName())
                                .from(pojoWrapper.getTable())),
                entityClass);
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
    default <R> IPage<R> queryPage(IPage<?> page, QueryOperator<?> queryOperator, Class<R> entityClass) {
        return queryPage(page, queryOperator, getOptions().obtainListBeanResultSetHandler(entityClass));
    }

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @return List
     * @throws DSLException query failed throw
     */
    default IPage<Map<String, Object>> queryPageMap(IPage<?> page, UnaryOperator<QueryOperator<?>> func) {
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
    default IPage<Map<String, Object>> queryPageMap(IPage<?> page, QueryOperator<?> queryOperator) {
        return queryPage(page, queryOperator, getOptions().obtainListMapResultHandler());
    }

    /**
     * 查询分页
     *
     * @param page page
     * @param func the func
     * @return List
     * @throws DSLException query failed throw
     */
    default IPage<ResultGroup> queryPage(IPage<?> page, UnaryOperator<QueryOperator<?>> func) {
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
    default IPage<ResultGroup> queryPage(IPage<?> page, QueryOperator<?> queryOperator) {
        return queryPage(page, queryOperator, getOptions().obtainDefaultListResultSetHandler());
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
    default <R> IPage<R> queryPage(IPage<?> page, UnaryOperator<QueryOperator<?>> func, ListResultSetHandler<R> resultSetHandler) {
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
    default <R> IPage<R> queryPage(IPage<?> page, QueryOperator<?> queryOperator, ListResultSetHandler<R> resultSetHandler) {
        queryOperator.page(page.getCurrent(), page.getSize());
        List<R> r = queryList(queryOperator, resultSetHandler);
        Long count = queryOne(
                f -> f.count().from(queryOperator.getTable()),
                resultGroup -> resultGroup.getLongValue(BoolResultHandler.GUESS_COUNT));
        Page<R> rPage = new Page<>(page);
        rPage.setCurrent(page.getCurrent());
        rPage.setSize(page.getSize());
        rPage.setTotal(count);
        rPage.setRecords(r);
        return rPage;
    }
}
