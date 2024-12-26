package cc.allio.uno.core.util.template.internal;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.template.Tokenizer;

/**
 * 通用Token解析器，指定Token解析成指定的内容，比如说：#{token}替换成指定的内容
 *
 * @author j.x
 * @see TokenHandler
 * @since 1.0
 */
public class GenericTokenParser implements TokenParser {
    private final Tokenizer tokenizer;
    private final String openToken;
    private final String closeToken;

    public GenericTokenParser(Tokenizer tokenizer) {
        Tokenizer.TokenSymbol symbol = tokenizer.getSymbol();
        openToken = symbol.getOpen();
        closeToken = symbol.getClose();
        this.tokenizer = tokenizer;
    }

    @Override
    public String parse(String text, TokenHandler handler) {
        if (text == null || text.isEmpty()) {
            return StringPool.EMPTY;
        }
        // search open token
        int start = text.indexOf(openToken);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        do {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    builder.append(handler.handleToken(expression.toString()));
                    offset = end + closeToken.length();
                }
            }
            start = text.indexOf(openToken, offset);
        } while (start > -1);
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }

    @Override
    public Tokenizer getTokenizer() {
        return this.tokenizer;
    }

}
