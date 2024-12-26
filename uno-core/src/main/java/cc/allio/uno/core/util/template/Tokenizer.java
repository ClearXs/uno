package cc.allio.uno.core.util.template;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.template.internal.TokenParser;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用于{@link TokenParser}解析的Token解析
 *
 * @author j.x
 * @since 1.1.1
 */
@FunctionalInterface
public interface Tokenizer {

    // TOKEN = {}
    Tokenizer BRACE = () -> TokenSymbol.BRACE_SYMBOL;
    // TOKEN = {{}}
    Tokenizer DOUBLE_BRACE = () -> TokenSymbol.DOUBLE_BRACE_SYMBOL;
    // TOKEN = ${}
    Tokenizer DOLLAR_BRACE = () -> TokenSymbol.DOLLAR_BRACE_SYMBOL;
    // TOKEN = #{}
    Tokenizer HASH_BRACE = () -> TokenSymbol.HASH_BRACE_SYMBOL;
    // TOKEN = []
    Tokenizer SMALL_BRACKETS = () -> TokenSymbol.SMALL_BRACKETS_SYMBOL;
    // TOKEN = ()
    Tokenizer DOUBLE_BRACKET = () -> TokenSymbol.DOUBLE_BRACKET;
    // TOKEN = @{}
    Tokenizer AT_BRACE = () -> TokenSymbol.AT_BRACE;

    /**
     * 获取Token数组列表
     *
     * @return Token数组列表
     */
    TokenSymbol getSymbol();

    /**
     * 为给定的参数添加当前Token
     *
     * @param unToken 未添加Token数据
     * @return 添加后的
     */
    default String createTokenString(String unToken) {
        TokenSymbol symbol = getSymbol();
        String open = symbol.getOpen();
        String close = symbol.getClose();
        return open + unToken + close;
    }

    /**
     * 判断给定的参数是否包含token symbol
     *
     * @param token 参数
     * @return true 包含 false 不包含
     */
    default boolean contains(String token) {
        TokenSymbol symbol = getSymbol();
        String open = symbol.getOpen();
        String close = symbol.getClose();
        return token.contains(open) && token.contains(close);
    }

    /**
     * 标识当前Token的结构，为left - right结构
     */
    @Getter
    @AllArgsConstructor
    class TokenSymbol {
        public static final TokenSymbol BRACE_SYMBOL = new TokenSymbol(StringPool.LEFT_BRACE, StringPool.RIGHT_BRACE);
        public static final TokenSymbol DOUBLE_BRACE_SYMBOL = new TokenSymbol(StringPool.DOUBLE_LEFT_BRACE, StringPool.DOUBLE_RIGHT_BRACE);
        public static final TokenSymbol DOLLAR_BRACE_SYMBOL = new TokenSymbol(StringPool.DOLLAR_LEFT_BRACE, StringPool.RIGHT_BRACE);
        public static final TokenSymbol HASH_BRACE_SYMBOL = new TokenSymbol(StringPool.HASH_LEFT_BRACE, StringPool.RIGHT_BRACE);
        public static final TokenSymbol SMALL_BRACKETS_SYMBOL = new TokenSymbol(StringPool.LEFT_SMALL_BRACKETS, StringPool.RIGHT_SMALL_BRACKETS);
        public static final TokenSymbol DOUBLE_BRACKET = new TokenSymbol(StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET);
        public static final TokenSymbol AT_BRACE = new TokenSymbol(StringPool.AT + StringPool.LEFT_BRACE, StringPool.RIGHT_BRACE);

        // token left
        private final String open;
        // token right
        private final String close;
    }
}
