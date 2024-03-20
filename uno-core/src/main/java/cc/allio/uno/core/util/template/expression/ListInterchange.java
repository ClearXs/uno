package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.util.template.GenericTokenParser;
import cc.allio.uno.core.util.template.TokenParser;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.core.type.Types;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link java.util.List}进行替换
 *
 * @author j.x
 * @date 2022/12/3 19:38
 * @since 1.1.2
 */
public class ListInterchange extends BaseInterchange implements ListableInterchange {

    private final TokenParser tokenParser = new GenericTokenParser(Tokenizer.SMALL_BRACKETS);

    @Override
    protected Object onChange(String text, Object value, boolean langsym) {
        List<?> listable = (List<?>) value;
        // getValue index
        AtomicReference<Integer> indexRef = new AtomicReference<>();
        tokenParser.parse(text, content -> {
            indexRef.set(Integer.valueOf(content));
            return content;
        });
        Integer index = indexRef.get();
        return reValue(listable.get(index), langsym);
    }

    @Override
    protected boolean onCheck(Object value) {
        return Types.isList(value.getClass());
    }
}
