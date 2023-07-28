package cc.allio.uno.rule.drools;

import org.drools.compiler.compiler.AnalysisResult;
import org.drools.compiler.compiler.BoundIdentifiers;
import org.drools.compiler.lang.descr.OperatorDescr;
import org.drools.compiler.lang.descr.PredicateDescr;
import org.drools.compiler.rule.builder.PatternBuilder;
import org.drools.compiler.rule.builder.PredicateBuilder;
import org.drools.compiler.rule.builder.RuleBuildContext;
import org.drools.core.reteoo.RuleTerminalNode;
import org.drools.core.rule.Declaration;
import org.drools.core.rule.Pattern;
import org.drools.core.rule.PredicateConstraint;
import org.drools.core.spi.Constraint;

import java.util.Arrays;
import java.util.Map;

public class DroolsPatternBuilder extends PatternBuilder {

    protected Constraint buildEval(final RuleBuildContext context,
                                   final Pattern pattern,
                                   final PredicateDescr predicateDescr,
                                   final Map<String, OperatorDescr> aliases,
                                   final String expr,
                                   final boolean isEvalExpression) {

        AnalysisResult analysis = buildAnalysis(context, pattern, predicateDescr, aliases);

        if (analysis == null) {
            // something bad happened
            return null;
        }

        Declaration[][] usedDeclarations = getUsedDeclarations(context, pattern, analysis);
        Declaration[] previousDeclarations = usedDeclarations[0];
        Declaration[] localDeclarations = usedDeclarations[1];

        BoundIdentifiers usedIdentifiers = analysis.getBoundIdentifiers();

        Arrays.sort(previousDeclarations, RuleTerminalNode.SortDeclarations.instance);
        Arrays.sort(localDeclarations, RuleTerminalNode.SortDeclarations.instance);

        boolean isJavaEval = isEvalExpression && context.getDialect().isJava();

        if (isJavaEval) {
            final PredicateConstraint predicateConstraint = new PredicateConstraint(null,
                    previousDeclarations,
                    localDeclarations);

            final PredicateBuilder builder = context.getDialect().getPredicateBuilder();

            builder.build(context,
                    usedIdentifiers,
                    previousDeclarations,
                    localDeclarations,
                    predicateConstraint,
                    predicateDescr,
                    analysis);

            return predicateConstraint;
        }

        String[] requiredGlobals = usedIdentifiers.getGlobals().keySet().toArray(new String[0]);
        Declaration[] mvelDeclarations = new Declaration[previousDeclarations.length + localDeclarations.length + requiredGlobals.length];
        int i = 0;
        for (Declaration d : previousDeclarations) {
            mvelDeclarations[i++] = d;
        }
        for (Declaration d : localDeclarations) {
            mvelDeclarations[i++] = d;
        }
        for (String global : requiredGlobals) {
            mvelDeclarations[i++] = context.getDeclarationResolver().getDeclaration(global);
        }

        boolean isDynamic =
                !pattern.getObjectType().getClassType().isArray() &&
                        !context.getKnowledgeBuilder().getTypeDeclaration(pattern.getObjectType().getClassType()).isTypesafe();

        return DroolsConstraintBuilder.DROOLS_CONSTRAINT_BUILDER
                .buildMvelConstraint(context.getPkg().getName(), expr, mvelDeclarations, getOperators(usedIdentifiers.getOperators()),
                        context, previousDeclarations, localDeclarations, predicateDescr, analysis, isDynamic);
    }
}
