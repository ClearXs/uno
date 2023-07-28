package cc.allio.uno.rule.api.visitor;

import cc.allio.uno.rule.api.vistor.CompilationRule;
import cc.allio.uno.rule.api.vistor.GroupElement;
import cc.allio.uno.rule.api.vistor.Traversal;
import cc.allio.uno.rule.api.LogicPredicate;
import cc.allio.uno.rule.api.OP;
import cc.allio.uno.rule.api.RuleAttr;
import cc.allio.uno.rule.api.RuleAttrBuilder;
import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DefaultCompilationRuleTest extends BaseTestCase {

    // a && b || c ==> (a && b) || c
    ArrayList<RuleAttr> testA = Lists.newArrayList(
            RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("c").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.OR).buildTriggerValue("1").build()
    );

    // a || b && c ==> a || (b && c)
    ArrayList<RuleAttr> testB = Lists.newArrayList(
            RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.OR).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("c").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build()
    );

    // a && b || c || d ==> (a && b) || c || d
    ArrayList<RuleAttr> testC = Lists.newArrayList(
            RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("c").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.OR).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("d").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.OR).buildTriggerValue("1").build()
    );

    // a && b || c && d ==> (a && b) || (c && d)
    ArrayList<RuleAttr> testD = Lists.newArrayList(
            RuleAttrBuilder.get().buildKey("a").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("b").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("c").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.OR).buildTriggerValue("1").build(),
            RuleAttrBuilder.get().buildKey("d").buildOp(OP.EQUALITY).buildLogic(LogicPredicate.AND).buildTriggerValue("1").build()
    );

    @Test
    void testCompile() {
        CompilationRule compilationRule = CompilationRule.DEFAULT;

        GroupElement<?> groupElement = compilationRule.treeifyBin(testA);
        List<RuleAttr> expand = compilationRule.expand(groupElement);

        assertEquals(testA.size(), expand.size());

        GroupElement<?> b = compilationRule.treeifyBin(testB);
        GroupElement<?> c = compilationRule.treeifyBin(testC);
        GroupElement<?> d = compilationRule.treeifyBin(testD);
    }

    @Test
    void testTraversal() {
        CompilationRule compilationRule = CompilationRule.DEFAULT;

        GroupElement<?> groupElement = compilationRule.treeifyBin(testA);

        groupElement.accept(
                e -> {
                    System.out.println(e);
                });


        groupElement.accept(
                e -> {
                    System.out.println(e);
                },
                Traversal.BREADTH);


        groupElement.accept(
                e -> {
                    System.out.println(e);
                },
                Traversal.DEEP);
    }

    @Test
    void testOutboundLiteralVisitor() {
        CompilationRule compilationRule = CompilationRule.DEFAULT;

        GroupElement<?> ra = compilationRule.treeifyBin(testA);
        String la = ra.getLiteral();
        assertEquals("((\"a\" == \"1\" && \"b\" == \"1\" ) || \"c\" == \"1\" )", la);

        GroupElement<?> rb = compilationRule.treeifyBin(testB);
        String lb = rb.getLiteral();
        assertEquals("(\"a\" == \"1\"  || (\"b\" == \"1\" && \"c\" == \"1\" ) )", lb);
        GroupElement<?> rc = compilationRule.treeifyBin(testC);
        String lc = rc.getLiteral();
        assertEquals("((\"a\" == \"1\" && \"b\" == \"1\" ) || \"c\" == \"1\" || \"d\" == \"1\"  )", lc);
        GroupElement<?> rd = compilationRule.treeifyBin(testD);
        String ld = rd.getLiteral();
        assertEquals("((\"a\" == \"1\" && \"b\" == \"1\" ) || (\"c\" == \"1\" && \"d\" == \"1\" ) )", ld);
    }

}
