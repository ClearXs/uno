package cc.allio.uno.rule.api;

import cc.allio.uno.rule.api.event.RuleContext;

/**
 * 定义规则触发后执行的动作
 *
 * @author j.x
 * @since 1.1.4
 */
public interface Action {

    /**
     * 当触发具体的规则实例后，进行毁掉
     *
     * @param context context
     */
    void onTrigger(RuleContext context);
}
