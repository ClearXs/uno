package cc.allio.uno.rule.api;

import cc.allio.uno.rule.api.vistor.CompilationRule;
import cc.allio.uno.rule.api.vistor.GroupElement;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * default
 *
 * @author j.x
 * @date 2023/4/23 19:22
 * @see RuleBuilder#get()
 * @since 1.1.4
 */
@Getter
@ToString(of = {"id", "name"})
public class RuleImpl implements Rule {

    private final Long id;
    private final String name;
    private final List<RuleAttr> attrs;
    private final GroupElement<?> root;

    RuleImpl(Long id, String name, List<RuleAttr> attrs) {
        this.id = id;
        this.name = name;
        this.attrs = attrs;
        this.root = CompilationRule.DEFAULT.treeifyBin(attrs);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<RuleAttr> getRuleAttr() {
        return attrs;
    }

    @Override
    public RuleAttr getIndexByExpr(String expr) {
        return attrs.stream()
                .filter(i -> expr.equals(i.getExpr()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getLiteralExpr() {
        return root.getLiteral();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleImpl rule = (RuleImpl) o;
        return Objects.equals(name, rule.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
