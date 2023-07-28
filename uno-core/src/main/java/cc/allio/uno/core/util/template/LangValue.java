package cc.allio.uno.core.util.template;

import cc.allio.uno.core.util.template.expression.BaseInterchange;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 语言值。当它被解析的时候按照{@link #langsym}的true、fasle来判断是否把String 32 = "32"
 *
 * @author jiangwei
 * @date 2023/4/23 20:25
 * @see BaseInterchange
 * @since 1.1.4
 */
@Data
@Accessors(chain = true)
public class LangValue {

    Object value;
    boolean langsym = false;
}
