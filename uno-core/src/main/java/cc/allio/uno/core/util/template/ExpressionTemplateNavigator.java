package cc.allio.uno.core.util.template;

import cc.allio.uno.core.api.OptionalContext;
import cc.allio.uno.core.util.template.internal.PlaceholderExpressionTemplate;
import cc.allio.uno.core.util.template.mvel.MVELExpressionTemplate;
import lombok.Getter;

/**
 * compatible {@link PlaceholderExpressionTemplate} and new mvel {@link MVELExpressionTemplate}
 * <p>if {@link Tokenizer#AT_BRACE} then use {@link MVELExpressionTemplate} otherwise {@link PlaceholderExpressionTemplate}</p>
 *
 * @author j.x
 * @date 2024/5/3 21:32
 * @since 1.1.9
 */
public class ExpressionTemplateNavigator implements ExpressionTemplate {

    private final ExpressionTemplate internal;
    @Getter
    private final Tokenizer tokenizer;

    public ExpressionTemplateNavigator(Tokenizer tokenizer, Object... args) {
        if (Tokenizer.AT_BRACE == tokenizer) {
            this.internal = new MVELExpressionTemplate();
        } else {
            Boolean langsym = OptionalContext.immutable(args).getTypeFirst(Boolean.class).orElse(false);
            this.internal = new PlaceholderExpressionTemplate(tokenizer, langsym);
        }
        this.tokenizer = tokenizer;
    }

    @Override
    public String parseTemplate(String template, TemplateContext context) {
        return internal.parseTemplate(template, context);
    }
}
