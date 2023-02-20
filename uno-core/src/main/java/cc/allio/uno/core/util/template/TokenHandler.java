package cc.allio.uno.core.util.template;

import java.util.function.Function;

/**
 * Token处理器
 *
 * @author jiangwei
 * @date 2022/1/29 16:39
 * @see GenericTokenParser
 * @since 1.0
 */
public interface TokenHandler extends Function<String, String> {

    @Override
    default String apply(String s) {
        return handleToken(s);
    }

    /**
     * 处理Token
     *
     * @param content 处理完成token后的内容
     * @return 自定义内容
     */
    String handleToken(String content);
}
