package cc.allio.uno.data.query.param;

import cc.allio.uno.data.query.QueryWrapper;

/**
 * 数据抽稀动作
 *
 * @author jiangwei
 * @date 2022/10/10 16:20
 * @since 1.1.0
 */
public interface DiluteAction {

    /**
     * 执行数据抽稀动作
     *
     * @param queryWrapper 查询定义
     * @param o            原始的数据，根据不同的动作把不同的数据进行赋值
     * @param t            待抽稀的数据
     */
    void trigger(QueryWrapper queryWrapper, Object o, Object t);
}
