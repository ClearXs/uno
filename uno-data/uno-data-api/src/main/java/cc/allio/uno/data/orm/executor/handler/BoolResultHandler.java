package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * bool 转换
 *
 * @author j.x
 * @date 2023/4/18 13:38
 * @since 1.1.4
 */
public class BoolResultHandler extends ExecutorOptionsAwareImpl implements ResultSetHandler<Boolean> {

    // guess list column name
    public static final DSLName GUESS_UPDATE_OR_UPDATE = DSLName.of("UPDATE", DSLName.HUMP_FEATURE);
    public static final DSLName GUESS_COUNT = DSLName.of("COUNT", DSLName.HUMP_FEATURE);
    private static final List<DSLName> GUESS_LIST = Lists.newArrayList(GUESS_UPDATE_OR_UPDATE, GUESS_COUNT);

    @Override
    public Boolean apply(ResultGroup resultGroup) {
        for (DSLName guess : GUESS_LIST) {
            ResultRow row = resultGroup.getRow(guess);
            if (row != null) {
                try {
                    return Types.parseBoolean(row.getValue());
                } catch (ClassCastException | NullPointerException ex) {
                    // ignore
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.FALSE;
    }
}
