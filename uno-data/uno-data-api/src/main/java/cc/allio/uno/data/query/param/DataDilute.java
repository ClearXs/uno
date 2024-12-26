package cc.allio.uno.data.query.param;

import lombok.Data;

/**
 * 抽稀实体
 *
 * @author j.x
 * @since 1.1.0
 */
@Data
public class DataDilute {

    /**
     * 抽稀时间窗口
     */
    private Window window;

    /**
     * 抽稀动作
     */
    private Action action = Action.DEFAULT;
}
