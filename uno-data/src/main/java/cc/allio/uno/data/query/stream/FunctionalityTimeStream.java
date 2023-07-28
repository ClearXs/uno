package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.query.mybatis.QueryFilter;
import cc.allio.uno.data.query.mybatis.QueryWrapper;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.core.util.StringUtils;
import reactor.core.publisher.Flux;

/**
 * 设计参考自{@link java.io.FilterInputStream}。
 * <p>其里面包含一个{@link DataStream}实例，其作用是把它作为基本的数据源</p>
 *
 * @author jiangwei
 * @date 2022/11/16 13:14
 * @since 1.1.0
 */
public abstract class FunctionalityTimeStream<T> implements CollectionTimeStream<T> {

    protected volatile CollectionTimeStream<T> ds;

    protected FunctionalityTimeStream(CollectionTimeStream<T> ds) {
        this.ds = ds;
    }

    @Override
    public Flux<T> read(QueryFilter queryFilter) throws Throwable {
        checkQuery(queryFilter);
        Flux<T> source = ds.read(queryFilter);
        if (source == null) {
            source = Flux.empty();
        }
        if (onFilter(queryFilter)) {
            return doRead(queryFilter, source);
        }
        return source;
    }

    /**
     * 子类实现，判断是否执行功能性数据
     *
     * @param queryFilter 执行过滤器
     * @return true 执行 false 不执行
     */
    protected abstract boolean onFilter(QueryFilter queryFilter);

    /**
     * 子类实现。构建出不同具备功能性的Stream。
     *
     * @param queryFilter 查询过滤器
     * @param source      由构造参数给定的Stream调用获取origin 数据传递给当前方法作为入参数
     * @return 功能性执行完成后list数据
     */
    protected abstract Flux<T> doRead(QueryFilter queryFilter, Flux<T> source);

    /**
     * 检查给定查询过滤器是否符合要求
     *
     * @param queryFilter 实例对象
     */
    protected void checkQuery(QueryFilter queryFilter) {
        if (queryFilter == null) {
            throw new IllegalArgumentException("QueryFilter Object must not null");
        }
        QueryWrapper queryWrapper = queryFilter.getQueryWrapper();
        // check QueryWrapper
        if (queryWrapper == null) {
            throw new IllegalArgumentException("QueryWrapper Object must not null, Please check");
        }
        // check timeField
        String timeField = queryWrapper.getTimeField();
        if (StringUtils.isEmpty(timeField)) {
            throw new IllegalArgumentException("timeField must not null, Please check");
        }
        // check dataFields
        String[] dataFields = queryWrapper.getDataFields();
        if (ObjectUtils.isEmpty(dataFields)) {
            throw new IllegalArgumentException("dataFields must not null, Please check");
        }
    }
}
