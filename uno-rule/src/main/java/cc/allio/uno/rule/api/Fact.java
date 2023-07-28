package cc.allio.uno.rule.api;

import cc.allio.uno.core.bean.ObjectWrapper;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuple6;

import java.io.Serializable;
import java.util.Map;

/**
 * rule 事实对象
 *
 * @author jiangwei
 * @date 2023/4/23 17:21
 * @since 1.1.4
 */
public interface Fact extends Map<String, Object>, Serializable {

    /**
     * 获取Rule实例对象
     *
     * @return Rule
     */
    Rule getRule();

    /**
     * 根据指定的key获取事实值
     *
     * @param key key
     * @return o
     */
    Object getValue(String key);

    /**
     * 获取rule的触发值
     *
     * @return map
     */
    Map<String, Object> ruleValues();

    /**
     * 创建Fact实例
     *
     * @param rule rule
     * @return Fact instance
     */
    static Fact from(Rule rule) {
        return new FactImpl(rule);
    }

    /**
     * 根据pojo对象获取Fact实例
     *
     * @param rule rule
     * @param pojo pojo
     * @return Fact instance
     */
    static Fact from(Rule rule, Object pojo) {
        ObjectWrapper wrapper = new ObjectWrapper(pojo);
        return from(rule, wrapper.findAllValuesForce());
    }

    /**
     * 根据variables创建Fact实例
     *
     * @param rule      rule
     * @param variables variables
     * @return fact instance
     */
    static Fact from(Rule rule, Map<String, Object> variables) {
        return new FactImpl(rule, variables);
    }

    /**
     * 创建Fact实例
     *
     * @param rule rule
     * @param k1   k1
     * @param v1   v1
     * @return fact instance
     */
    static Fact from(Rule rule, String k1, Object v1) {
        FactImpl fact = new FactImpl(rule);
        fact.put(k1, v1);
        return fact;
    }


    /**
     * 创建Fact实例
     *
     * @param rule rule
     * @param k1   k1
     * @param v1   v1
     * @param k2   k2
     * @param v2   v2
     * @return fact instance
     */
    static Fact from(Rule rule, String k1, Object v1, String k2, Object v2) {
        FactImpl fact = new FactImpl(rule);
        fact.put(k1, v1);
        fact.put(k2, v2);
        return fact;
    }

    /**
     * 创建Fact实例
     *
     * @param rule rule
     * @param k1   k1
     * @param v1   v1
     * @param k2   k2
     * @param v2   v2
     * @param k3   k3
     * @param v3   v3
     * @return fact instance
     */
    static Fact from(Rule rule, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        FactImpl fact = new FactImpl(rule);
        fact.put(k1, v1);
        fact.put(k2, v2);
        fact.put(k3, v3);
        return fact;
    }

    /**
     * 创建Fact实例
     *
     * @param rule rule
     * @param t2   t2
     * @return fact instance
     */
    static Fact from(Rule rule, Tuple2<String, Object> t2) {
        return from(rule, t2.getT1(), t2.getT2());
    }

    /**
     * 创建Fact实例
     *
     * @param rule rule
     * @param t4   t4
     * @return fact instance
     */
    static Fact from(Rule rule, Tuple4<String, Object, String, Object> t4) {
        return from(rule, t4.getT1(), t4.getT2(), t4.getT3(), t4.getT4());
    }

    /**
     * 创建Fact实例
     *
     * @param rule rule
     * @param t6   t6
     * @return fact instance
     */
    static Fact from(Rule rule, Tuple6<String, Object, String, Object, String, Object> t6) {
        return from(rule, t6.getT1(), t6.getT2(), t6.getT3(), t6.getT4(), t6.getT5(), t6.getT6());
    }
}
