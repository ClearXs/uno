package cc.allio.uno.rule.drools;

import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.compiler.PackageBuilderErrors;
import org.drools.drl.ast.descr.*;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilderError;

public class RuleDrlAstTest {
    @Test
    void testMultiCondition() {
        KnowledgeBuilderImpl kBuilder = new KnowledgeBuilderImpl();
        createRule(kBuilder);

        if (kBuilder.hasErrors()) {
            PackageBuilderErrors errors = kBuilder.getErrors();
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error);
            }
        }

        KieBase kieBase = kBuilder.newKieBase();
        Rule thereIsAnAlarm = kieBase.getRule("cc.allio.uno.rule.drools.logical", "ThereIsAnAlarm");

        KieSession kieSession = kieBase.newKieSession();


        Alarm alarm = new Alarm("12");
        kieSession.insert(alarm);

//
//        kieSession.fireAllRules(new AgendaFilter() {
//            @Override
//            public boolean accept(Match match) {
//                Rule rule = match.getRule();
//                return rule.getName().equals("ThereIsAnAlarm");
//            }
//        });
        kieSession.fireAllRules();
    }


    /**
     * 创建和一致的结构
     * <p>
     * rule ThereIsAnAlarm when
     * Alarm( name == "12")
     * then
     * System.out.println( "there is an Alarm " );
     * end
     *
     * @return
     */
    void createRule(KnowledgeBuilderImpl kBuilder) {
        String pkgName = "cc.allio.uno.rule.drools.logical";
        PackageDescr pkgDescr = new PackageDescr(pkgName);
        kBuilder.addPackage(pkgDescr);

        final RuleDescr ruleDescr = new RuleDescr("ThereIsAnAlarm");
        ruleDescr.setConsequence("System.out.println( \"there is an Alarm \" );");
        final AndDescr lhs = new AndDescr();
        ruleDescr.setLhs(lhs);
        final PatternDescr pattern = new PatternDescr(Alarm.class.getName(), "0");
        pattern.addConstraint(new ExprConstraintDescr("name == 12"));
        lhs.addDescr(pattern);

        pkgDescr.addRule(ruleDescr);
        kBuilder.addPackage(pkgDescr);

//        RuleImpl rule = kBuilder.getPackage(pkgName).getRule("ThereIsAnAlarm");
//
//        InternalKnowledgePackage pkg = kBuilder.getPackageRegistry(pkgName).getPackage();
//        DialectCompiletimeRegistry dialectRegistry = kBuilder.getPackageRegistry(pkg.getName()).getDialectCompiletimeRegistry();
//        JavaDialect javaDialect = (JavaDialect) dialectRegistry.getDialect("java");
//        final RuleBuildContext context =
//                new RuleBuildContext(kBuilder,
//                        ruleDescr,
//                        dialectRegistry,
//                        pkg,
//                        javaDialect);
//        RuleBuilder.preProcess(context);
//        RuleBuilder.buildRule(context);
//
//        pkg.addRule(context.getRule());
    }
}
