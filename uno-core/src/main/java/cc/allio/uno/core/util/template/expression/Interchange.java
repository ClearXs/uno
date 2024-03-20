package cc.allio.uno.core.util.template.expression;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.Types;

/**
 * 表达式交换定义
 *
 * @author j.x
 * @date 2022/12/3 19:35
 * @since 1.1.2
 */
public interface Interchange {

    /**
     * 通过文本的信息替换值
     *
     * @param text    表达式文本
     * @param value   文本对应的对象
     * @param langsym 语言值
     * @return 替换后的值
     */
    Object change(String text, Object value, boolean langsym) throws Throwable;

    /**
     * 获取语言类型值，如String 2 = "2" char 2 = '2'
     *
     * @param s 原始值
     * @return lang value
     */
    default Object getTypeValue(Object s) {
        if (Types.isString(s.getClass())) {
            return StringPool.QUOTE + s + StringPool.QUOTE;
        } else if (Types.isChar(s.getClass())) {
            return StringPool.SINGLE_QUOTE + s + StringPool.SINGLE_QUOTE;
        }
        return s;
    }
}
