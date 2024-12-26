package cc.allio.uno.data.query.param;

import cc.allio.uno.data.query.QueryWrapper;

/**
 * 默认抽稀动作，不实现任何动作
 *
 * @author j.x
 * @since 1.1.0
 */
public class DefaultDiluteAction implements DiluteAction {

    @Override
    public void trigger(QueryWrapper queryWrapper, Object o, Object t) {
        // TODO NOTHING
    }
}
