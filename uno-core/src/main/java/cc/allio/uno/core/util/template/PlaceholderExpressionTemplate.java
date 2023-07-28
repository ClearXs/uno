package cc.allio.uno.core.util.template;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.template.expression.Engine;
import cc.allio.uno.core.util.template.expression.SymbolEngine;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用{@link TokenParser}完成模板解析
 * <p>定义：模板解析方式</p>
 * <ul>
 *     <li>对象：按照对象的字段名称（忽略大小写）、如id进行替换
 *             <ul>
 *               <li>如果字段包含map，那么按照map的规则，如map.id（假设字段是叫map的话）</li>
 *                <li>如果字段包含list，那么按照list的规则，如list[0].id（假设字段是叫list的话）</li>
 *           </ul>
 *     </li>
 *
 *     <li>Map：按照给定的Key值来选择，如果包含对象的话，则按照对象的方式进行替换。如key，user.id（要求Map的key一定是String）
 *          <ul>
 *              <li>如果字段包含对象，那么按照对象的规则，如user.id（如果Map的Key叫user的话）</li>
 *              <li>如果字段包含List，那么按照List的规则，如list[0].id（如果Map的Key叫list的话）</li>
 *          </ul>
 *     </li>
 *     <li>List：按照给定的index获取值，如果指定的index是map或者对象则按照对应的方式进行解析 id[0]，user[0].id。
 *          <ul>
 *              <li>如果list给定了某个泛型，如果该泛型是POJO，那么按照user[0].id方式进行解析。如果该泛型是Map，以map为标识，如map[0].id解析</li>
 *              <li>如果list没有给定泛型并且没有包含上述情况则以list[0]方式解析</li>
 *          </ul>
 *     </li>
 * </ul>
 *
 * @author jiangwei
 * @date 2021/12/25 16:46
 * @see Tokenizer
 * @since 1.0
 */
@Slf4j
public class PlaceholderExpressionTemplate implements ExpressionTemplate {

    private final TokenParser tokenParser;
    private final Engine engine;
    private final boolean langsym;

    PlaceholderExpressionTemplate(Tokenizer tokenizer) {
        this(tokenizer, false);
    }

    PlaceholderExpressionTemplate(Tokenizer tokenizer, boolean langsym) {
        this.tokenParser = new GenericTokenParser(tokenizer);
        this.engine = new SymbolEngine(StringPool.DOT);
        this.langsym = langsym;
    }

    @Override
    public String parseTemplate(@NonNull String template, @NonNull Object target) {
        return tokenParser.parse(template, expression -> {
            try {
                return engine.run(expression, target, langsym);
            } catch (Throwable e) {
                return expression;
            }
        });
    }

}
