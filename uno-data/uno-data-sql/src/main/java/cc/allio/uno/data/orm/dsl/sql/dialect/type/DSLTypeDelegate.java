package cc.allio.uno.data.orm.dsl.sql.dialect.type;

import cc.allio.uno.data.orm.dsl.type.DSLType;

import java.util.List;

/**
 * 内置DSLType基类，实现通用的方法
 *
 * @author j.x
 * @date 2024/2/1 19:56
 * @since 1.1.7
 */
public abstract class DSLTypeDelegate implements DSLType {

    protected final DSLType sqlType;

    protected DSLTypeDelegate(DSLType sqlType) {
        this(sqlType, null, null);
    }

    protected DSLTypeDelegate(DSLType sqlType, Integer precision, Integer scale) {
        DSLType dslType = null;
        for (DSLLinkType linkType : getDSLLinkValues()) {
            List<DSLType> parent = linkType.getParent();
            if (parent.stream().anyMatch(p -> p.getName().equals(sqlType.getName()))) {
                dslType = linkType;
                // 优先取link的定义
                precision = dslType.getPrecision();
                scale = dslType.getScale();
                break;
            }
        }
        if (dslType == null) {
            dslType = sqlType;
        }
        this.sqlType = DSLType.create(dslType, precision, scale);
    }

    @Override
    public String getName() {
        return sqlType.getName();
    }

    @Override
    public int getJdbcType() {
        return sqlType.getJdbcType();
    }

    @Override
    public Integer getPrecision() {
        return sqlType.getPrecision();
    }

    @Override
    public Integer getScale() {
        return sqlType.getScale();
    }

    /**
     * 子类实现，获取{@link DSLLinkType}数组，得到某个{@link DefaultDSLType}关联到不同数据实例的数据类型
     *
     * @return values
     */
    protected abstract DSLLinkType[] getDSLLinkValues();
}
