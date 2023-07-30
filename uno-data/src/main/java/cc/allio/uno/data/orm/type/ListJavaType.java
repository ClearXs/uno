package cc.allio.uno.data.orm.type;

import cc.allio.uno.core.type.Types;

import java.util.List;

/**
 * list
 *
 * @author jiangwei
 * @date 2023/4/16 16:58
 * @since 1.1.4
 */
public class ListJavaType extends JavaTypeImpl<List> {
    @Override
    public Class<List> getJavaType() {
        return Types.LIST;
    }
}