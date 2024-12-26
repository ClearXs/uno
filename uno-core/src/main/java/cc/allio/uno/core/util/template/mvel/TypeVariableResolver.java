package cc.allio.uno.core.util.template.mvel;

import cc.allio.uno.core.util.template.TemplateContext;
import cc.allio.uno.core.util.template.VariableResolve;
import org.mvel2.integration.impl.MapVariableResolver;

import java.util.Map;
import java.util.Optional;

/**
 * ues {@link VariableResolve} for specifies type parse, otherwise use {@link MapVariableResolver}
 *
 * @author j.x
 * @since 1.1.9
 */
public class TypeVariableResolver extends MapVariableResolver {

    final TemplateContext templateContext;

    public TypeVariableResolver(Map<String, Object> variableMap, String name, TemplateContext templateContext) {
        super(variableMap, name);
        this.templateContext = templateContext;
    }

    public TypeVariableResolver(Map<String, Object> variableMap, String name, Class knownType, TemplateContext templateContext) {
        super(variableMap, name, knownType);
        this.templateContext = templateContext;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Class getType() {
        return super.getType();
    }

    @Override
    public void setStaticType(Class aClass) {
        super.setStaticType(aClass);
    }

    @Override
    public int getFlags() {
        return 0;
    }

    @Override
    public Object getValue() {
        Object value = super.getValue();
        return Optional.ofNullable(value)
                .flatMap(v -> {
                    Class<?> valueType = v.getClass();
                    Map<Class<?>, VariableResolve<?, ?>> variableResolveMap = templateContext.getVariableResolveMap();
                    VariableResolve variableResolve = variableResolveMap.get(valueType);
                    return Optional.ofNullable(variableResolve)
                            .map(resolve -> resolve.apply(v));
                })
                .orElse(value);
    }

    @Override
    public void setValue(Object o) {
        super.setValue(o);
    }
}
