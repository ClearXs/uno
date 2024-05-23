package cc.allio.uno.core.util.template.mvel;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.TemplateContext;
import org.mvel2.ParserContext;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

/**
 * according to mvel {@link TemplateRuntime} as parser
 *
 * @author j.x
 * @date 2024/5/3 20:07
 * @since 1.1.9
 */
public class MVELExpressionTemplate implements ExpressionTemplate {

    @Override
    public String parseTemplate(String template, TemplateContext context) {
        // 1. compile the template
        ParserContext parserContext = new ParserContext();
        Map<String, Class> inputs = context.getInputs();
        parserContext.addInputs(inputs);
        CompiledTemplate compiledTemplate = TemplateCompiler.compileTemplate(template, parserContext);
        Object execute = TemplateRuntime.execute(compiledTemplate, context.getAll());
        return execute.toString();
    }
}
