package cc.allio.uno.rule.drools;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.rule.api.Action;
import cc.allio.uno.rule.api.Fact;
import cc.allio.uno.rule.api.event.RuleContext;
import com.google.common.collect.Lists;
import org.drools.drl.ast.descr.GlobalDescr;
import org.drools.drl.ast.descr.ImportDescr;
import org.drools.drl.ast.descr.PackageDescr;
import org.drools.drl.ast.descr.RuleDescr;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.*;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.util.Map;

public class RuleIndexTest {

    @Test
    void testExecute() {
        KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kBuilder.add(ResourceFactory.newClassPathResource("cc/allio/uno/rule/drools/ruleindex.drl"), ResourceType.DRL);

        if (kBuilder.hasErrors()) {
            KnowledgeBuilderErrors errors = kBuilder.getErrors();
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error);
                throw new NullPointerException(error.getMessage());
            }
        }
        KiePackage kiePackage = Lists.newArrayList(kBuilder.getKnowledgePackages()).get(1);
        Rule rule = Lists.newArrayList(kiePackage.getRules()).get(0);
        KieBase kieBase = kBuilder.newKieBase();
        KieSession kieSession = kieBase.newKieSession();
        Fact ruleValue = Fact.from(null);
        ruleValue.put("a", 6);
        ruleValue.put("b", 6);
        ruleValue.put("c", 6);
        kieSession.insert(ruleValue);
        kieSession.addEventListener(new AgendaEventListener() {
            @Override
            public void matchCreated(MatchCreatedEvent event) {
                System.out.println(event);
            }

            @Override
            public void matchCancelled(MatchCancelledEvent event) {
                System.out.println(event);
            }

            @Override
            public void beforeMatchFired(BeforeMatchFiredEvent event) {
                System.out.println(event);
            }

            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                System.out.println(event);
            }

            @Override
            public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
                System.out.println(event);
            }

            @Override
            public void agendaGroupPushed(AgendaGroupPushedEvent event) {
                System.out.println(event);
            }

            @Override
            public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
                System.out.println(event);
            }

            @Override
            public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
                System.out.println(event);
            }

            @Override
            public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
                System.out.println(event);
            }

            @Override
            public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
                System.out.println(event);
            }
        });

        Action action = new Action() {
            @Override
            public void onTrigger(RuleContext context) {
                System.out.println("test");
            }
        };
        kieSession.setGlobal("action", action);
        int i = kieSession.fireAllRules();
        System.out.println(kieSession);
    }

    void buildSystemRule() {
        String pkgName = "cc.allio.uno.rule.drools";
        PackageDescr pkgDescr = new PackageDescr(pkgName);
        ImportDescr importMapDescr = new ImportDescr(Map.class.getName());
        ImportDescr importRuleValueDescr = new ImportDescr(Fact.class.getName());
        ImportDescr importActionValueDescr = new ImportDescr(Action.class.getName());
        ImportDescr importDroolsValueDescr = new ImportDescr(OptionalContext.class.getName());
        pkgDescr.addAllImports(Lists.newArrayList(importMapDescr, importRuleValueDescr, importActionValueDescr, importDroolsValueDescr));
        GlobalDescr globalDescr = new GlobalDescr("action", Action.class.getName());
        pkgDescr.addGlobal(globalDescr);
        final RuleDescr ruleDescr = new RuleDescr("rule a");
        ruleDescr.setConsequence(
                "       DroolsContext context = new DroolsContext();\n" +
                        "        context.putAttribute(\"drools\", drools);\n" +
                        "        action.onTrigger(context);");
    }

}
