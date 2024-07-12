package cc.allio.uno.rule.drools;

import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.compiler.DialectCompiletimeRegistry;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.drools.compiler.rule.builder.RuleBuildContext;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.mvel.builder.MVELDialect;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;

public class RuleDesrTest {

    @Test
    void dynamicCreate() {
        PackageDescr pkgDescr = new PackageDescr("pkg1");
        KnowledgeBuilderImpl kBuilder = new KnowledgeBuilderImpl();
        kBuilder.addPackage(pkgDescr);

        // 添加自定义 rule
        InternalKnowledgePackage pkg = kBuilder.getPackageRegistry("pkg1").getPackage();
        final RuleDescr ruleDescr = new RuleDescr("rule 1");
        ruleDescr.setNamespace("pkg1");
        ruleDescr.setConsequence("modify (cheese) {price = 5 };\nretract (cheese)");
        DialectCompiletimeRegistry dialectRegistry = kBuilder.getPackageRegistry(pkg.getName()).getDialectCompiletimeRegistry();
        MVELDialect mvelDialect = (MVELDialect) dialectRegistry.getDialect("mvel");
        final RuleBuildContext context =
                new RuleBuildContext(kBuilder,
                        ruleDescr,
                        dialectRegistry,
                        pkg,
                        mvelDialect);
        pkg.addRule(context.getRule());

        InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addPackages(kBuilder.getKnowledgePackages());
        KieSession kieSession = kbase.newKieSession();
        int i = kieSession.fireAllRules();
    }
}
