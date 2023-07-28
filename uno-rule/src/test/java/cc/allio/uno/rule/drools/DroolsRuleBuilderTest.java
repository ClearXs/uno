package cc.allio.uno.rule.drools;

import cc.allio.uno.rule.api.*;
import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DroolsRuleBuilderTest extends BaseTestCase {

    ArrayList<RuleAttr> ruleAttrs = Lists.newArrayList(
            RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("c").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.OR).buildTriggerValue("1").build()
    );
    DroolsRuleManager droolsRuleManager = new DroolsRuleManager();

    @Test
    void testSingleIndex() {
        Rule rule = RuleBuilder.get().buildRuleName("test")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build())
                .build();
        droolsRuleManager.buildRule(rule);
    }

    @Test
    void testMultiIndex() {
        Rule rule = RuleBuilder.get().buildRuleName("test")
                .addRuleAttrs(ruleAttrs)
                .build();
        droolsRuleManager.buildRule(rule);
    }

    @Test
    void testSameName() {
        ArrayList<RuleAttr> a = Lists.newArrayList(
                RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build()
        );
        Rule ra = RuleBuilder.get().buildRuleName("test")
                .addRuleAttrs(a)
                .build();
        droolsRuleManager.buildRule(ra);

        Rule rb = RuleBuilder.get().buildRuleName("test")
                .addRuleAttrs(a)
                .build();
        boolean r1 = droolsRuleManager.compareRuleAndRemoveOldRule(rb);

        assertTrue(r1);
        ArrayList<RuleAttr> b = Lists.newArrayList(
                RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build()
        );

        Rule rc = RuleBuilder.get().buildRuleName("test")
                .addRuleAttrs(b)
                .build();
        boolean r2 = droolsRuleManager.compareRuleAndRemoveOldRule(rc);
        assertFalse(r2);

    }
}
