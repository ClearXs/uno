package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.type.Types;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * bool 转换
 *
 * @author jiangwei
 * @date 2023/4/18 13:38
 * @since 1.1.4
 */
public class BoolResultHandler implements ResultSetHandler<Boolean> {

    // guess list column name
    public static final String GUESS_UPDATE_OR_UPDATE = "update";
    public static final String GUESS_COUNT = "count";
    private static final List<String> GUESS_LIST = Lists.newArrayList(GUESS_UPDATE_OR_UPDATE, GUESS_COUNT);

    @Override
    public Boolean apply(ResultGroup resultGroup) {
        for (String guess : GUESS_LIST) {
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
