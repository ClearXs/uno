package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.function.lambda.MethodReferenceColumn;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;

import java.util.Optional;

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
        this.wrapper = PojoWrapper.getInstance(pojo);
    }

    @Override
    public String getColumn() {
        return Optional.ofNullable(wrapper.getPkColumn())
                .map(ColumnDef::getDslName)
                .map(DSLName::getName)
                .orElse(null);
    }

    @Override
    public Object apply(P p) {
        return p;
    }
}
