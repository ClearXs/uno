package cc.allio.uno.rule.api;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;

import java.io.Serializable;

/**
 * 规则属性，包含属性key，关系符号（>、<、==..），逻辑关系，以及生成的表达式 a > 5..
 *
 * @author jiangwei
 * @date 2023/4/23 09:31
 * @see RuleAttrBuilder
 * @since 1.1.4
 */
public interface RuleAttr extends Serializable {

    String EXPRESS_TEMPLATE = "get(#{key}) #{op} #{value}";
    String SIMPLE_EXPRESS_TEMPLATE = "#{key} #{op} #{value}";
    ExpressionTemplate TEMPLATE = ExpressionTemplate.createTemplate(Tokenizer.HASH_BRACE, false);

    /**
     * 指标字段
     *
     * @return 字段标识
     */
    String getKey();

    /**
     * 指标名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 指标单位
     *
     * @return 单位
     */
    String getUnit();

    /**
     * 获取逻辑与、或OP
     *
     * @return OP
     */
    LogicPredicate getLogic();

    /**
     * op
     *
     * @return op
     */
    OP getOp();

    /**
     * 获取当前触发值的类型
     *
     * @return the trigger value type
     */
    Class<?> getValueType();

    /**
     * 获取指标过滤值
     *
     * @return object
     */
    Object getTriggerValue();

    /**
     * 获取rule index 构建的表达式。
     *
     * @return 如 get(a) > xxx
     */
    String getExpr();

    /**
     * 获取rule index构建表达式
     *
     * @return 如 a > xx
     */
    String getIndexExpr();

    /**
     * 获取rule
     *
     * @return rule instance
     */
    Rule getRule();
}
