package cc.allio.uno.rule.api;

import java.io.Serializable;
import java.util.List;

/**
 * uno rule api
 *
 * @author j.x
 * @since 1.1.4
 */
public interface Rule extends Serializable {

    /**
     * 获取Rule数据标识
     *
     * @return long id
     */
    Long getId();

    /**
     * 规则名称
     *
     * @return String
     */
    String getName();

    /**
     * 获取指标属性
     *
     * @return rule attr list
     */
    List<RuleAttr> getRuleAttr();

    /**
     * 根据表达式获取属性
     *
     * @param expr expr
     * @return expr
     */
    RuleAttr getIndexByExpr(String expr);

    /**
     * 获取当前规则所包含属性项的字面表达式，形如：
     * <p>>getValue('a') < xx</p>
     *
     * @return 字面量
     */
    String getLiteralExpr();
}
