package cc.allio.uno.rule.config;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.rule.RuleEngineFactory;
import cc.allio.uno.rule.drools.DroolsRuleEngine;
import cc.allio.uno.rule.easyrules.EasyRulesEngine;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RuleProperties.class)
public class RuleAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = RuleEngineFactory.RULE_ENGINE_KEY, havingValue = "drools")
    @ConditionalOnClass(KieSession.class)
    public DroolsRuleEngine droolsRuleEngine() {
        Envs.setProperty(RuleEngineFactory.RULE_ENGINE_KEY, "drools");
        return RuleEngineFactory.get();

    }

    @Bean
    @ConditionalOnProperty(name = RuleEngineFactory.RULE_ENGINE_KEY, havingValue = "easy-rules")
    @ConditionalOnClass(KieSession.class)
    public EasyRulesEngine easyRulesEngine() {
        Envs.setProperty(RuleEngineFactory.RULE_ENGINE_KEY, "easy-rules");
        return RuleEngineFactory.get();

    }
}
