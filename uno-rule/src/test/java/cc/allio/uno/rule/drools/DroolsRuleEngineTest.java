package cc.allio.uno.rule.drools;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.rule.RuleEngineFactory;
import cc.allio.uno.rule.api.*;
import cc.allio.uno.rule.api.event.Listener;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.Set;

public class DroolsRuleEngineTest extends BaseTestCase {

    DroolsRuleEngine droolsRuleEngine = RuleEngineFactory.get();

    @Test
    void testNoMatch() {
        Rule rule = RuleBuilder.get().buildRuleName("test")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .build();
        Fact fact = Fact.from(rule, "a", '1');
        fact.put("a", "0");
        RuleResult ruleResult = droolsRuleEngine.fire(rule, fact);

        ruleResult.addLister(new Listener() {
            @Override
            public void onTrigger(Rule rule, Set<MatchIndex> matchIndices) {

            }

            @Override
            public void onNoMatch(Rule rule) {
                System.out.println("1");
            }

            @Override
            public void onError(Throwable ex) {

            }
        });
        ruleResult.addLister(new Listener() {
            @Override
            public void onTrigger(Rule rule, Set<MatchIndex> matchIndices) {

            }

            @Override
            public void onNoMatch(Rule rule) {
                System.out.println("2");
            }

            @Override
            public void onError(Throwable ex) {

            }
        });
        Set<MatchIndex> matchs = ruleResult.get();
        assertEquals(0, matchs.size());
    }

    @Test
    void testOneRule() throws InterruptedException {
        Rule rule = RuleBuilder.get().buildRuleName("test")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .build();
        Fact fact = Fact.from(rule, "a", '1');
        fact.put("a", "1");
        RuleResult ruleResult = droolsRuleEngine.fire(rule, fact);

        Set<MatchIndex> matchIndices = ruleResult.get();

        assertEquals(1, matchIndices.size());
    }

    @Test
    void testDiffFactType() {
        Rule rule = RuleBuilder.get().buildRuleName("test")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("accr").buildOp(OP.GREATER_THAN).buildTriggerValue("123").build())
                .build();
        Fact fact = Fact.from(rule, "accr", "asd");
        RuleResult ruleResult = droolsRuleEngine.fire(rule, fact);
        boolean matched = ruleResult.isMatchedOnSync();
        assertTrue(matched);

    }

    @Test
    void testTwoRuleAttr() {
        Rule rule = RuleBuilder.get().buildRuleName("test")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .addRuleAttr(RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build())
                .build();

        Fact fact1 = Fact.from(rule, "a", '1');
        RuleResult ruleResult = droolsRuleEngine.fire(rule, fact1);
        Set<MatchIndex> matchIndices = ruleResult.get();
        assertEquals(0, matchIndices.size());


        Fact fact2 = Fact.from(rule, "a", '1', "b", "1");
        Set<MatchIndex> matchs = droolsRuleEngine.fire(rule, fact2).get();
        assertEquals(2, matchs.size());
    }

    @Test
    void testMultiRule() {
        Rule rule1 = RuleBuilder.get().buildRuleName("test")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .build();
        Fact fact1 = Fact.from(rule1, "a", '1');
        Set<MatchIndex> matchs = droolsRuleEngine.fire(rule1, fact1).get();
        assertEquals(1, matchs.size());

        Rule rule2 = RuleBuilder.get().buildRuleName("test2")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .addRuleAttr(RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build())
                .build();
        Fact fact2 = Fact.from(rule2, "a", "1", "b", "1");
        Set<MatchIndex> matchs1 = droolsRuleEngine.fire(rule2, fact2).get();
        assertEquals(2, matchs1.size());

        Fact fact3 = Fact.from(rule2, "a", "1");
        RuleResult result = droolsRuleEngine.fire(rule2, fact3);
        Set<MatchIndex> matchs3 = result.get();
        assertFalse(result.isMatched());

    }

    @Test
    void testComplexCondition() {
        Rule rule = RuleBuilder.get().buildRuleName("test")
                // a > 1 && b == 1 || c < 5
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.GREATER_THAN).buildTriggerValue("1").build())
                .addRuleAttr(RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .addRuleAttr(RuleAttrBuilder.get().buildKey("c").buildOp(OP.LESS_THAN).buildLogic(LogicPredicate.OR).buildTriggerValue("5").build())
                .build();
        // match
        Fact factMatch = Fact.from(rule, "a", "2", "b", "1", "c", "3");
        RuleResult result = droolsRuleEngine.fire(rule, factMatch);
        Set<MatchIndex> matchs = result.get();
        assertEquals(3, matchs.size());
    }

    @Test
    void testReactiveError() {
        Rule rule = RuleBuilder.get().buildRuleName("test")
                // a > 1
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.GREATER_THAN).buildTriggerValue(new Date()).build())
                .build();

        Fact factMatch = Fact.from(rule, "a", "2");
        RuleResult result = droolsRuleEngine.fire(rule, factMatch);
        result.isMatchedOnReactive()
                .as(StepVerifier::create)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void testCompareDate() {
        Date cv = DateUtil.parse("2023-05-19 17:00:00");
        Date fv = DateUtil.parse("2023-05-19 18:00:00");
        Rule rule = RuleBuilder.get().buildRuleName("test")
                // a <= 2023-05-19 18:00:00
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.LESS_THAN_EQUAL).buildTriggerValue(fv).build())
                .build();
        Fact factMatch = Fact.from(rule, "a", cv);
        RuleResult result = droolsRuleEngine.fire(rule, factMatch);
        Set<MatchIndex> matchIndices = result.get();
        assertEquals(1, matchIndices.size());
    }

    @Test
    void testCompareRangeDate() {
        Date sv = DateUtil.parse("2023-05-19 17:00:00");
        Date ev = DateUtil.parse("2023-05-19 18:00:00");
        Date cv = DateUtil.parse("2023-05-19 17:30:00");
        Date tv = DateUtil.parse("2023-05-19 19:30:00");
        Rule rule = RuleBuilder.get().buildRuleName("test")
                // a <= 2023-05-19 18:00:00
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.LESS_THAN_EQUAL).buildTriggerValue(ev).build())
                // a >= 2023-05-19 17:00:00
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.GREATER_THAN_EQUAL).buildTriggerValue(sv).build())
                .build();
        Fact f1 = Fact.from(rule, "a", cv);
        RuleResult r1 = droolsRuleEngine.fire(rule, f1);
        Set<MatchIndex> m1 = r1.get();
        assertEquals(2, m1.size());
        Fact f2 = Fact.from(rule, "a", tv);
        RuleResult r2 = droolsRuleEngine.fire(rule, f2);
        Set<MatchIndex> m2 = r2.get();
        assertEquals(0, m2.size());
    }

    @Test
    void testPerformance() {
        // 10000
        Rule rule = RuleBuilder.get().buildRuleName("test")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .build();
        Fact fact = Fact.from(rule, "a", "1");
        for (int i = 0; i < 100000; i++) {
            RuleResult ruleResult = droolsRuleEngine.fire(rule, fact);
//            ruleResult.addLister(new Listener() {
//                @Override
//                public void onTrigger(Rule rule, Set<MatchIndex> matchIndices) {
//                    assertEquals(1, matchIndices.size());
//                }
//
//                @Override
//                public void onNoMatch(Rule rule) {
//
//                }
//
//                @Override
//                public void onError(Throwable ex) {
//
//                }
//            });
            Set<MatchIndex> matchIndices = ruleResult.get();
            assertEquals(1, matchIndices.size());
        }
    }

    /**
     * Test Case: 多规则触发
     */
    @Test
    void testMultiTrigger() {
        Rule ruleA = RuleBuilder.get().buildRuleName("ruleA")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .build();

        Fact factA = Fact.from(ruleA, "b", "1");
        RuleResult fireA = droolsRuleEngine.fire(ruleA, factA);

        Rule ruleB = RuleBuilder.get().buildRuleName("ruleB")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .build();
        Fact factB = Fact.from(ruleA, "b", "1");
//        RuleResult fireB = droolsRuleEngine.fire(ruleB, factB);

        Rule ruleC = RuleBuilder.get().buildRuleName("ruleC")
                .addRuleAttr(RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildTriggerValue("1").build())
                .addRuleAttr(RuleAttrBuilder.get().buildKey("a").buildOp(OP.GREATER_THAN).buildTriggerValue("1").buildLogic(LogicPredicate.OR).build())
                .build();
        Fact factC = Fact.from(ruleA, "b", "1", "a", "2");
        RuleResult fireC = droolsRuleEngine.fire(ruleC, factC);

//        RuleResult fireD = droolsRuleEngine.fire(ruleB, factB);

        Flux.merge(fireA.getOnReactive(),  fireC.getOnReactive())
                .subscribe();
//        Set<MatchIndex> matchIndices = fireD.getValue();
//        System.out.println(matchIndices);
//        Set<MatchIndex> matchIndices1 = fireC.getValue();

    }
}
