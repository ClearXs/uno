package cc.allio.uno.core.util.template.internal;

/**
 * 抽象的交换
 *
 * @author j.x
 * @date 2022/12/3 20:19
 * @since 1.1.2
 */
public abstract class BaseInterchange implements Interchange {

    @Override
    public Object change(String text, Object value, boolean langsym) {
        boolean check = onCheck(value);
        // 受检查失败，抛出异常
        if (!check) {
            throw new IllegalArgumentException(String.format("%s check specify 'value type[%s]' failed", getClass().getSimpleName(), value.getClass().getSimpleName()));
        }
        return onChange(text, value, langsym);
    }

    /**
     * 重新获取值或者语言值的转换
     *
     * @param value   文本对应的对象
     * @param langsym 语言值
     * @return Object
     */
    protected Object reValue(Object value, boolean langsym) {
        // 如果值类型为语言值则其判断的优先级大于参数langsym
        if (value instanceof LangValue langValue) {
            Object or = langValue.getValue();
            if (langValue.isLangsym()) {
                return getTypeValue(or);
            }
            return or;
        }
        if (langsym) {
            return getTypeValue(value);
        }
        return value;
    }

    /**
     * 子类实现，触发根据表达式文本进行改变值
     *
     * @param text    表但是文本
     * @param value   给定的当前交换的值
     * @param langsym langsym
     * @return 根据表达式文本改变后的值
     */
    protected abstract Object onChange(String text, Object value, boolean langsym);

    /**
     * 检查给定的值是否符合需求
     *
     * @param value 给定的值
     * @return true check on false
     */
    protected abstract boolean onCheck(Object value);
}
