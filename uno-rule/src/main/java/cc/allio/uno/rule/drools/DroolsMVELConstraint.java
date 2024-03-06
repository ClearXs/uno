package cc.allio.uno.rule.drools;

import cc.allio.uno.rule.api.Fact;
import cc.allio.uno.rule.api.RuleAttr;
import cc.allio.uno.rule.api.event.RuleContext;
import cc.allio.uno.rule.api.MatchIndex;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.drools.base.base.ValueResolver;
import org.drools.base.rule.Declaration;
import org.drools.compiler.rule.builder.EvaluatorWrapper;
import org.drools.mvel.MVELConstraint;
import org.drools.mvel.expr.MVELCompilationUnit;
import org.kie.api.runtime.rule.FactHandle;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Setter
@Getter
public class DroolsMVELConstraint extends MVELConstraint {

    private RuleAttr ruleAttr;

    public DroolsMVELConstraint() {
    }

    public DroolsMVELConstraint(String packageName,
                                String expression,
                                Declaration[] declarations,
                                EvaluatorWrapper[] operators,
                                MVELCompilationUnit compilationUnit,
                                boolean isDynamic) {
        super(packageName, expression, declarations, operators, compilationUnit, isDynamic);
    }

    public DroolsMVELConstraint(MVELConstraint constraint, MVELCompilationUnit compilationUnit) {
        super(Lists.newArrayList(constraint.getPackageNames()).get(0),
                constraint.getExpression(),
                constraint.getRequiredDeclarations(),
                constraint.getOperators(),
                compilationUnit,
                constraint.isDynamic());
    }

    @Override
    public boolean isAllowed(FactHandle handle, ValueResolver valueResolver) {
        RuleContext context = (RuleContext) valueResolver.getGlobal(DroolsRuleManager.GLOBAL_CONTEXT_NAME);
        // 匹配结果
        Fact fact = context.getCurrentFact();
        boolean allowed = super.isAllowed(handle, valueResolver);
        // 验证成功并且属性当前rule drools的rate算法会匹配所有节点，避免不属于当前rule的判断放入
        if (allowed && context.getCurrentRule().equals(ruleAttr.getRule())) {
            context.putMatchIndex(new MatchIndex(ruleAttr, fact.get(ruleAttr.getKey())));
        }
        return allowed;

    }

    @Override
    public DroolsMVELConstraint clone() {
        MVELConstraint clone = super.clone();
        DroolsMVELConstraint constraint = new DroolsMVELConstraint(clone, compilationUnit);
        constraint.setRuleAttr(ruleAttr);
        return constraint;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.ruleAttr = (RuleAttr) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(ruleAttr);
    }
}
