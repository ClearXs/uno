package cc.allio.uno.core.util.template.expression;

/**
 * 表达式交换定义
 *
 * @author jiangwei
 * @date 2022/12/3 19:35
 * @since 1.1.2
 */
public interface Interchange {

    /**
     * 通过文本的信息替换值
     *
     * @param text  表达式文本
     * @param value 文本对应的对象
     * @return 替换后的值
     */
    Object change(String text, Object value) throws Throwable;
}
