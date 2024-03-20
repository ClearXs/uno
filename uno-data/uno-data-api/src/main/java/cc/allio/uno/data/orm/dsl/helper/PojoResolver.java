package cc.allio.uno.data.orm.dsl.helper;

/**
 * pojo相关的Resolver
 *
 * @author j.x
 * @date 2024/2/6 23:32
 * @since 1.1.7
 */
public interface PojoResolver {

    /**
     * 获取TableResolver实例
     *
     * @return TableResolver
     */
    default TableResolver obtainTableResolver() {
        return null;
    }

    /**
     * 获取ColumnDefListResolver实例
     *
     * @return ColumnDefListResolver
     */
    default ColumnDefListResolver obtainColumnDefListResolver() {
        return null;
    }

    /**
     * 获取ColumnDefResolver实例
     *
     * @return ColumnDefResolver
     */
    default ColumnDefResolver obtainColumnDefResolver() {
        return null;
    }
}
