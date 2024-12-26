package cc.allio.uno.core.util.template.internal;

import cc.allio.uno.core.util.template.Tokenizer;

/**
 * 按照指定的Token标识符号（如#{}、{{}}）等等TOKEN来对文本内容进行解析
 *
 * @author j.x
 * @see GenericTokenParser
 */
public interface TokenParser {

    /**
     * 解析动作。
     *
     * @param text    文本内容
     * @param handler 遇到指定的TOKEN时触发调用
     * @return 对包含指定Token解析替换完成后文本
     */
    String parse(String text, TokenHandler handler);

    /**
     * 获取Tokenizer
     *
     * @return Tokenizer实体
     */
    Tokenizer getTokenizer();
}
