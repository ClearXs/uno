package cc.allio.uno.rule;

import cc.allio.uno.core.api.Key;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.rule.api.RuleEngine;
import cc.allio.uno.rule.drools.DroolsRuleEngine;
import cc.allio.uno.rule.easyrules.EasyRulesEngine;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * rule engine factory
 *
 * @author j.x
 * @date 2023/4/25 15:42
 * @since 1.1.4
 */
public class RuleEngineFactory {

    public static final String RULE_ENGINE_KEY = "allio.uno.rule.engine.type";
    private static final RuleEngine DEFAULT_RULE_ENGINE = new DroolsRuleEngine();

    public static final RuleEngineKey DROOLS_KEY = () -> "drools";
    public static final RuleEngineKey EASY_RULES_KEY = () -> "easy-rules";

    public static final Map<RuleEngineKey, RuleEngine> CACHES = Maps.newHashMap();

    /**
     * 根据系统参数获取RuleEngine实例
     *
     * @return rule engine instance
     */
    public static synchronized <T extends RuleEngine> T get() {
        String ruleEngineKey = Envs.getProperty(RULE_ENGINE_KEY);
        if (StringUtils.isEmpty(ruleEngineKey)) {
            Envs.setProperty(RULE_ENGINE_KEY, DROOLS_KEY.key());
            ruleEngineKey = DROOLS_KEY.key();
        }
        if (DROOLS_KEY.key().equals(ruleEngineKey)) {
            return get(DROOLS_KEY);
        } else if (EASY_RULES_KEY.key().equals(ruleEngineKey)) {
            return get(EASY_RULES_KEY);
        }
        // 默认为drools
        return get(DROOLS_KEY);
    }

    /**
     * 根据执行的{@link RuleEngineKey}获取RuleEngine实例
     *
     * @param key key
     * @return RuleEngine instance or DroolsRuleEngine
     */
    public static synchronized <T extends RuleEngine> T get(RuleEngineKey key) {
        if (key == DROOLS_KEY) {
            return (T) CACHES.computeIfAbsent(key, k -> DEFAULT_RULE_ENGINE);
        } else if (key == EASY_RULES_KEY) {
            return (T) CACHES.computeIfAbsent(key, k -> new EasyRulesEngine());
        }
        return (T) DEFAULT_RULE_ENGINE;
    }

    /**
     * rule engine key
     */
    @FunctionalInterface
    interface RuleEngineKey extends Key {

        @Override
        default String getProperties() {
            return RULE_ENGINE_KEY;
        }
    }
}
