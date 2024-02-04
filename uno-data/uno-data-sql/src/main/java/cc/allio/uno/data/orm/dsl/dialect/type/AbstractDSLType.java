package cc.allio.uno.data.orm.dsl.dialect.type;

import cc.allio.uno.data.orm.dsl.type.DSLType;

import java.util.List;

/**
 * 实现通用的方法
 *
 * @author jiangwei
 * @date 2024/2/1 19:56
 * @since 1.1.6
 */
public abstract class AbstractDSLType implements DSLType {

    protected final DSLType sqlType;

    protected AbstractDSLType(DSLType sqlType) {
        this(sqlType, null, null);
    }

    protected AbstractDSLType(DSLType sqlType, Integer precision, Integer scale) {
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
        this.sqlType = DSLTypeImpl.createBy(dslType, precision, scale);
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

    protected abstract DSLLinkType[] getDSLLinkValues();
}
