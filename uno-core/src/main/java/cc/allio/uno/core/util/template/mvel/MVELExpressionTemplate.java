package cc.allio.uno.core.util.template.mvel;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.TemplateContext;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.ParserContext;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * according to mvel {@link TemplateRuntime} as parser
 *
 * @author j.x
 * @date 2024/5/3 20:07
 * @since 1.1.9
 */
@Slf4j
public class MVELExpressionTemplate implements ExpressionTemplate {

    @Override
    public String parseTemplate(String template, TemplateContext context) {
        // 1. compile the template
        ParserContext parserContext = new ParserContext();

        // add inputs
        Map<String, Class> inputs = context.getInputs();
        parserContext.addInputs(inputs);

        // add import
        Map<String, Class> imports = context.getImports();
        for (Map.Entry<String, Class> importEntry : imports.entrySet()) {
            parserContext.addImport(importEntry.getKey(), importEntry.getValue());
        }

        CompiledTemplate compiledTemplate = TemplateCompiler.compileTemplate(template, parserContext);

        // 2. execute parse template
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

        // use customize VariableResolverFactory
        TemplateContextVariableResolverFactory variableResolverFactory = new TemplateContextVariableResolverFactory(context);
        try {
            TemplateRuntime.execute(compiledTemplate, null, variableResolverFactory, null, new CharsetOutputStream(writer));
        } catch (Throwable ex) {
            log.error("Failed to mvel parse template {}", template, ex);
        }
        try {
            writer.flush();
        } catch (IOException ex) {
            log.error("Failed to mvel parse template {}", template, ex);
        }
        try {
            return out.toString(String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
