package cc.allio.uno.core.util.template.mvel;

import cc.allio.uno.core.util.template.TemplateContext;
import org.mvel2.UnresolveablePropertyException;
import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.impl.MapVariableResolver;
import org.mvel2.integration.impl.MapVariableResolverFactory;

/**
 * {@link TemplateContext}
 *
 * @author j.x
 * @date 2024/5/4 14:11
 * @since 1.1.9
 */
public class TemplateContextVariableResolverFactory extends MapVariableResolverFactory {

    final TemplateContext templateContext;

    public TemplateContextVariableResolverFactory(TemplateContext templateContext) {
        super(templateContext.getAll());
        this.templateContext = templateContext;
    }

    public VariableResolver createVariable(String name, Object value) {
        try {
            VariableResolver vr;
            (vr = this.getVariableResolver(name)).setValue(value);
            return vr;
        } catch (UnresolveablePropertyException var5) {
            MapVariableResolver vr;
            this.addResolver(name, vr = new TypeVariableResolver(this.variables, name, templateContext)).setValue(value);
            return vr;
        }
    }

    public VariableResolver createVariable(String name, Object value, Class<?> type) {
        VariableResolver vr;
        try {
            vr = this.getVariableResolver(name);
        } catch (UnresolveablePropertyException var6) {
            vr = null;
        }

        if (vr != null && vr.getType() != null) {
            throw new RuntimeException("variable already defined within scope: " + vr.getType() + " " + name);
        } else {
            this.addResolver(name, vr = new TypeVariableResolver(this.variables, name, type, templateContext)).setValue(value);
            return vr;
        }
    }

    public VariableResolver getVariableResolver(String name) {
        VariableResolver vr = this.variableResolvers.get(name);
        if (vr != null) {
            return vr;
        } else if (this.variables.containsKey(name)) {
            this.variableResolvers.put(name, vr = new TypeVariableResolver(this.variables, name, templateContext));
            return vr;
        } else if (this.nextFactory != null) {
            return this.nextFactory.getVariableResolver(name);
        } else {
            throw new UnresolveablePropertyException("unable to resolve variable '" + name + "'");
        }
    }
}