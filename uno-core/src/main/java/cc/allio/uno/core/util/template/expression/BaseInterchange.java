package cc.allio.uno.core.util.template.expression;

/**
 * 抽象的交换
 *
 * @author jiangwei
 * @date 2022/12/3 20:19
 * @since 1.1.2
 */
public abstract class BaseInterchange implements Interchange {

    @Override
    public Object change(String text, Object value) throws Throwable {
        boolean check = onCheck(value);
        // 受检查失败，抛出异常
        if (!check) {
            throw new IllegalArgumentException(String.format("%s check specify 'value type[%s]' failed", getClass().getSimpleName(), value.getClass().getSimpleName()));
        }
        return onChange(text, value);
    }

    /**
     * 子类实现，触发根据表达式文本进行改变值
     *
     * @param text  表但是文本
     * @param value 给定的当前交换的值
     * @return 根据表达式文本改变后的值
     */
    protected abstract Object onChange(String text, Object value);

    /**
     * 检查给定的值是否符合需求
     *
     * @param value 给定的值
     * @return true check on false
     */
    protected abstract boolean onCheck(Object value);
}
