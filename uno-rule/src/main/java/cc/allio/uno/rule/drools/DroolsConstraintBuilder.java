package cc.allio.uno.rule.drools;

import cc.allio.uno.rule.api.Rule;
import cc.allio.uno.rule.api.RuleAttr;
import org.drools.base.base.ClassObjectType;
import org.drools.base.rule.Declaration;
import org.drools.base.rule.Pattern;
import org.drools.base.rule.constraint.Constraint;
import org.drools.compiler.compiler.AnalysisResult;
import org.drools.compiler.compiler.DescrBuildError;
import org.drools.compiler.rule.builder.EvaluatorWrapper;
import org.drools.compiler.rule.builder.RuleBuildContext;
import org.drools.core.rule.consequence.KnowledgeHelper;
import org.drools.drl.ast.descr.PredicateDescr;
import org.drools.mvel.MVELConstraintBuilder;
import org.drools.mvel.builder.MVELAnalysisResult;
import org.drools.mvel.builder.MVELDialect;
import org.drools.mvel.expr.MVELCompilationUnit;

import java.util.Map;

import static org.drools.mvel.asm.AsmUtil.copyErrorLocation;

public class DroolsConstraintBuilder extends MVELConstraintBuilder {

    static final DroolsConstraintBuilder DROOLS_CONSTRAINT_BUILDER = new DroolsConstraintBuilder();

    @Override
    public Constraint buildMvelConstraint(String packageName,
                                          String expression,
                                          Declaration[] declarations,
                                          EvaluatorWrapper[] operators,
                                          RuleBuildContext context,
                                          Declaration[] previousDeclarations,
                                          Declaration[] localDeclarations,
                                          PredicateDescr predicateDescr,
                                          AnalysisResult analysis,
                                          boolean isDynamic) {

        MVELCompilationUnit compilationUnit = buildCompilationUnit(context, previousDeclarations, localDeclarations, predicateDescr, analysis);
        DroolsRuleDescr ruleDescr = (DroolsRuleDescr) context.getRuleDescr();
        // 获取指标
        Rule rule = ruleDescr.getRule();
        RuleAttr ruleIndex = rule.getIndexByExpr(predicateDescr.getContent().toString());
        DroolsMVELConstraint droolsMVELConstraint = new DroolsMVELConstraint(packageName, expression, declarations, operators, compilationUnit, isDynamic);
        droolsMVELConstraint.setRuleAttr(ruleIndex);
        return droolsMVELConstraint;
    }

    private MVELCompilationUnit buildCompilationUnit(final RuleBuildContext context,
                                                     final Declaration[] previousDeclarations,
                                                     final Declaration[] localDeclarations,
                                                     final PredicateDescr predicateDescr,
                                                     final AnalysisResult analysis) {
        if (context.isTypesafe() && analysis instanceof MVELAnalysisResult) {
            Class<?> returnClass = analysis.getReturnType();
            if (returnClass != Boolean.class && returnClass != Boolean.TYPE) {
                context.addError(new DescrBuildError(context.getParentDescr(),
                        predicateDescr,
                        null,
                        "Predicate '" + predicateDescr.getContent() + "' must be a Boolean expression\n" + predicateDescr.positionAsString()));
            }
        }

        MVELDialect dialect = (MVELDialect) context.getDialect("mvel");

        MVELCompilationUnit unit = null;

        try {
            Map<String, Class<?>> declIds = context.getDeclarationResolver().getDeclarationClasses(context.getRule());

            Pattern p = (Pattern) context.getDeclarationResolver().peekBuildStack();
            if (p.getObjectType() instanceof ClassObjectType c) {
                declIds.put("this",
                        c.getClassType());
            }

            unit = MVELDialect.getMVELCompilationUnit((String) predicateDescr.getContent(),
                    analysis,
                    previousDeclarations,
                    localDeclarations,
                    null,
                    context,
                    "drools",
                    KnowledgeHelper.class,
                    context.isInXpath(),
                    MVELCompilationUnit.Scope.CONSTRAINT);
        } catch (final Exception e) {
            copyErrorLocation(e, predicateDescr);
            context.addError(new DescrBuildError(context.getParentDescr(),
                    predicateDescr,
                    e,
                    "Unable to build expression for 'inline-eval' : " + e.getMessage() + "'" + predicateDescr.getContent() + "'\n" + e.getMessage()));
        }

        return unit;
    }
}
