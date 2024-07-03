package cc.allio.uno.core.util.template;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.*;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

/**
 * parse template for context
 * <p>system practical utility tools</p>
 * <ul>
 *     <li>date: refer to {@link DateUtil}</li>
 *     <li>json: refer to {@link JsonUtils}</li>
 *     <li>bean: refer to {@link BeanUtils}</li>
 *     <li>class: refer to {@link ClassUtils}</li>
 * </ul>
 *
 * @author j.x
 * @date 2024/5/3 20:12
 * @since 1.1.9
 */
public class TemplateContext implements OptionalContext {

    private final Map<String, Object> vars;
    @Getter
    private final Map<String, Class> inputs;
    @Getter
    private final Map<String, Class> imports;
    @Getter
    private final Map<Class<?>, VariableResolve<?, ?>> variableResolveMap;

    private static final String DATE_UTILITY_NAME = "date";
    private static final String JSON_UTILITY_NAME = "json";
    private static final String BEAN_UTILITY_NAME = "bean";
    private static final String CLASS_UTILITY_NAME = "class";
    private static final String STRING_UTILITY_NAME = "string";

    public TemplateContext() {
        this.vars = Maps.newConcurrentMap();
        this.inputs = Maps.newConcurrentMap();
        this.imports = Maps.newConcurrentMap();
        this.variableResolveMap = Maps.newConcurrentMap();
        initial();
    }

    /**
     * initial system practical utility
     */
    void initial() {
        addImport(DATE_UTILITY_NAME, DateUtil.class);
        addImport(JSON_UTILITY_NAME, JsonUtils.class);
        addImport(BEAN_UTILITY_NAME, BeanUtils.class);
        addImport(CLASS_UTILITY_NAME, ClassUtils.class);
        addImport(STRING_UTILITY_NAME, StringUtils.class);
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(vars.get(key));
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getAll() {
        return vars;
    }

    @Override
    public void putAttribute(String key, Object obj) {
        if (obj != null) {
            Class<?> valueClass = obj.getClass();
            vars.put(key, obj);
            if (Types.isBean(valueClass)) {
                inputs.put(key, valueClass);
            }
        }
    }

    /**
     * add key and input class use for parse template
     *
     * @param key        the key
     * @param inputClass the input class
     */
    public void addInput(String key, Class inputClass) {
        this.inputs.put(key, inputClass);
    }

    /**
     * add import class to mvel imports
     *
     * @param importClass the import class
     */
    public void addImport(Class importClass) {
        this.imports.put(importClass.getSimpleName(), importClass);
    }

    /**
     * add import key and class to mvel imports
     *
     * @param key         the import key
     * @param importClass the import class
     */
    public void addImport(String key, Class importClass) {
        this.imports.put(key, importClass);
    }

    /**
     * add {@link VariableResolve} instance
     *
     * @param variableType    the {@link T} class type
     * @param variableResolve the {@link VariableResolve} instance
     * @param <T>             variable type
     * @param <R>             translate type
     */
    public <T, R> void addVariableResolve(Class<T> variableType, VariableResolve<T, R> variableResolve) {
        this.variableResolveMap.put(variableType, variableResolve);
    }
}