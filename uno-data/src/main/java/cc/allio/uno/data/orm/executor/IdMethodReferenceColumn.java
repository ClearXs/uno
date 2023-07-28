package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.sql.MethodReferenceColumn;
import cc.allio.uno.data.orm.sql.PojoWrapper;

/**
 * pojo的id方法
 *
 * @author jiangwei
 * @date 2023/7/11 12:34
 * @since 1.1.4
 */
public class IdMethodReferenceColumn<P> implements MethodReferenceColumn<P> {

    private final PojoWrapper<P> wrapper;

    public IdMethodReferenceColumn(P pojo) {
        this.wrapper = new PojoWrapper<>(pojo);
    }

    @Override
    public String getColumn() {
        return wrapper.findByIdColumn().getSqlName().getName();
    }

    @Override
    public Object apply(P p) {
        return null;
    }
}
