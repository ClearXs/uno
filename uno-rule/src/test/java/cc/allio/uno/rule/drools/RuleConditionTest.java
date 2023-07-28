package cc.allio.uno.rule.drools;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.util.Collection;

public class RuleConditionTest {

    @Test
    void testCollect() {
        KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kBuilder.add(ResourceFactory.newClassPathResource("cc/allio/uno/rule/drools/collect.drl"), ResourceType.DRL);

        Collection<KiePackage> knowledgePackages = kBuilder.getKnowledgePackages();

        KiePackage kiePackage = Lists.newArrayList(knowledgePackages).get(2);
        Collection<Rule> rules = kiePackage.getRules();

        Rule rule = Lists.newArrayList(rules).get(0);

        KieBase kieBase = kBuilder.newKieBase();

        KieSession kieSession = kieBase.newKieSession();

        kieSession.insert(6);
        kieSession.insert(7);

        kieSession.fireAllRules();
    }

    @Test
    void testForall() {
        KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kBuilder.add(ResourceFactory.newClassPathResource("cc/allio/uno/rule/drools/forall.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kBuilder.getErrors();
        if (errors != null) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error);
            }
        }
        Collection<KiePackage> knowledgePackages = kBuilder.getKnowledgePackages();
        KiePackage kiePackage = Lists.newArrayList(knowledgePackages).get(1);
        Collection<Rule> rules = kiePackage.getRules();

        Rule rule = Lists.newArrayList(rules).get(0);

        KieBase kieBase = kBuilder.newKieBase();

        KieSession kieSession = kieBase.newKieSession();

//        kieSession.insert(2);
//        kieSession.insert(5);
        kieSession.insert(6);


        kieSession.fireAllRules();
    }
}
