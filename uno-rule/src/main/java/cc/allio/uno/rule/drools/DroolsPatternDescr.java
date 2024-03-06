package cc.allio.uno.rule.drools;

import lombok.Getter;
import lombok.Setter;
import org.drools.base.rule.Declaration;
import org.drools.compiler.rule.builder.RuleBuildContext;
import org.drools.drl.ast.descr.*;
import org.kie.internal.definition.GenericTypeDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DroolsPatternDescr extends PatternDescr {

    private String objectType;
    private String identifier;
    private boolean unification;
    @Getter
    @Setter
    private ConditionalElementDescr constraint = new AndDescr();
    private int leftParentCharacter = -1;
    private int rightParentCharacter = -1;
    @Setter
    private PatternSourceDescr source;
    @Setter
    private List<BehaviorDescr> behaviors;
    private boolean query;
    private Declaration xpathStartDeclaration;
    private GenericTypeDefinition genericType;

    public DroolsPatternDescr() {
        this(null,
                null);
    }

    public DroolsPatternDescr(final String objectType) {
        this(objectType,
                null);
    }

    public DroolsPatternDescr(final String objectType,
                              final String identifier) {
        this.objectType = objectType;
        this.identifier = identifier;
    }

    public DroolsPatternDescr(final String objectType,
                              final String identifier,
                              final boolean isQuery) {
        this.objectType = objectType;
        this.identifier = identifier;
        this.query = isQuery;
    }

    @Override
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    @Override
    public void setQuery(boolean query) {
        this.query = query;
    }

    @Override
    public String getObjectType() {
        return this.objectType;
    }

    public boolean resolveObjectType(Function<String, String> resolver) {
        if (genericType == null) {
            genericType = GenericTypeDefinition.parseType(objectType, resolver);
        }
        return genericType != null;
    }

    @Override
    public GenericTypeDefinition getGenericType() {
        return genericType == null ? new GenericTypeDefinition(objectType) : genericType;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public List<String> getAllBoundIdentifiers() {
        List<String> identifiers = new ArrayList<>();
        if (this.identifier != null) {
            identifiers.add(this.identifier);
        }
        for (BaseDescr descr : getDescrs()) {
            String descrText = descr.getText();
            int colonPos = descrText.indexOf(':');
            if (colonPos > 0) {
                identifiers.add(descrText.substring(0, colonPos).trim());
            }
        }
        return identifiers;
    }

    @Override
    public boolean isQuery() {
        return query;
    }

    public boolean isPassive(RuleBuildContext context) {
        // when the source is a FromDesc also check that it isn't the datasource of a ruleunit
        return query || (source instanceof FromDescr && !context.getEntryPointId(((FromDescr) source).getDataSource().getText()).isPresent());
    }

    @Override
    public List<? extends BaseDescr> getDescrs() {
        return this.constraint.getDescrs();
    }

    @Override
    public void addConstraint(BaseDescr base) {
        this.constraint.addDescr(base);
    }

    @Override
    public void removeAllConstraint() {
        constraint = new AndDescr();
    }

    @Override
    public boolean removeConstraint(BaseDescr base) {
        return this.constraint.removeDescr(base);
    }

    @Override
    public DroolsPatternDescr negateConstraint() {
        this.constraint = (ConditionalElementDescr) ((BaseDescr) this.constraint).negate();
        return this;
    }

    @Override
    public List<? extends BaseDescr> getPositionalConstraints() {
        return this.doGetConstraints(ExprConstraintDescr.Type.POSITIONAL);
    }

    @Override
    public List<? extends BaseDescr> getSlottedConstraints() {
        return this.doGetConstraints(ExprConstraintDescr.Type.NAMED);
    }

    private List<? extends BaseDescr> doGetConstraints(ExprConstraintDescr.Type type) {
        List<BaseDescr> returnList = new ArrayList<BaseDescr>();
        for (BaseDescr descr : this.constraint.getDescrs()) {

            // if it is a ExprConstraintDescr - check the type
            if (descr instanceof ExprConstraintDescr desc) {
                if (desc.getType().equals(type)) {
                    returnList.add(desc);
                }
            } else {
                // otherwise, assume 'NAMED'
                if (type.equals(ExprConstraintDescr.Type.NAMED)) {
                    returnList.add(descr);
                }
            }
        }

        return returnList;
    }

    public boolean isInternalFact(RuleBuildContext context) {
        return !(source == null || source instanceof EntryPointDescr ||
                (source instanceof FromDescr fromDescr) && context.getEntryPointId(fromDescr.getExpression()).isPresent());
    }

    @Override
    public String toString() {
        return "[Pattern: id=" + this.identifier + "; objectType=" + this.objectType + "]";
    }

    /**
     * @return the leftParentCharacter
     */
    @Override
    public int getLeftParentCharacter() {
        return this.leftParentCharacter;
    }

    /**
     * @param leftParentCharacter the leftParentCharacter to setValue
     */
    @Override
    public void setLeftParentCharacter(final int leftParentCharacter) {
        this.leftParentCharacter = leftParentCharacter;
    }

    /**
     * @return the rightParentCharacter
     */
    @Override
    public int getRightParentCharacter() {
        return this.rightParentCharacter;
    }

    /**
     * @param rightParentCharacter the rightParentCharacter to setValue
     */
    @Override
    public void setRightParentCharacter(final int rightParentCharacter) {
        this.rightParentCharacter = rightParentCharacter;
    }

    @Override
    public PatternSourceDescr getSource() {
        return source;
    }

    @Override
    public void setResource(org.kie.api.io.Resource resource) {
        super.setResource(resource);
        ((BaseDescr) this.constraint).setResource(resource);
    }

    /**
     * @return the behaviors
     */
    @Override
    public List<BehaviorDescr> getBehaviors() {
        if (behaviors == null) {
            return Collections.emptyList();
        }
        return behaviors;
    }

    @Override
    public void addBehavior(BehaviorDescr behavior) {
        if (this.behaviors == null) {
            this.behaviors = new ArrayList<>();
        }
        this.behaviors.add(behavior);
    }

    /**
     * @return the unification
     */
    @Override
    public boolean isUnification() {
        return unification;
    }

    /**
     * @param unification the unification to setValue
     */
    @Override
    public void setUnification(boolean unification) {
        this.unification = unification;
    }

    public Declaration getXpathStartDeclaration() {
        return xpathStartDeclaration;
    }

    public void setXpathStartDeclaration(Declaration xpathStartDeclaration) {
        this.xpathStartDeclaration = xpathStartDeclaration;
    }

    @Override
    public DroolsPatternDescr clone() {
        DroolsPatternDescr clone = new DroolsPatternDescr(this.objectType,
                this.identifier);
        clone.setQuery(this.query);
        clone.setUnification(unification);
        clone.setLeftParentCharacter(this.leftParentCharacter);
        clone.setRightParentCharacter(this.rightParentCharacter);
        clone.setSource(this.source);
        clone.setStartCharacter(this.getStartCharacter());
        clone.setEndCharacter(this.getEndCharacter());
        clone.setLocation(this.getLine(),
                this.getColumn());
        clone.setEndLocation(this.getEndLine(),
                this.getEndColumn());
        clone.setText(this.getText());
        for (BaseDescr constraint : this.getDescrs()) {
            clone.addConstraint(constraint);
        }
        if (behaviors != null) {
            for (BehaviorDescr behavior : behaviors) {
                clone.addBehavior(behavior);
            }
        }
        clone.setXpathStartDeclaration(xpathStartDeclaration);
        return clone;
    }

    @Override
    public void accept(DescrVisitor visitor) {
        visitor.visit(this);
    }
}
