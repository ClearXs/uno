package cc.allio.uno.rule.api.vistor;

import cc.allio.uno.rule.api.RuleAttr;

import java.util.List;

/**
 * 根据跟定的对象把其汇编成规则树结果或者根据规则树反编译成规则项
 *
 * @author j.x
 * @since 1.1.4
 */
public interface CompilationRule {

    CompilationRule DEFAULT = new DefaultCompilationRule();

    /**
     * <b>传递规则项进行树化</b>
     * <p>定义：每一个平展的规则项，其表达式以：&& a > 5、 || b < 5的形式解析，即每一个规则项以逻辑谓词作为其前缀，表达式为后缀。</p>
     * <p>对于一个规则树，其具有如下性质：</p>
     * <ul>
     *     <li>root节点一定是 AND {@link GroupElement}</li>
     *     <li>当遇到与当前不一致的逻辑谓词时，会创建新的逻辑谓词组。</li>
     * </ul>
     * <p>算法逻辑如下（p指父节点，p.e只父节点非{@link GroupElement}的元素，n指当前节点，n.e只当前节点非{@link GroupElement}）：</p>
     * <ol>
     *     <li>如果当前p是AND {@link GroupElement}，n是AND {@link GroupElement}：则把n.e放入p.e中</li>
     *     <li>如果当前p是AND {@link GroupElement}，n是OR {@link GroupElement}：则把n作为p的父级，p作n的子节点，n重新为父级</li>
     *     <li>如果当前p是OR {@link GroupElement}，n无论是OR 或者 AND {@link GroupElement}都创建新的Group：如果是AND {@link GroupElement}则把p.e 放入n.e中，如果是OR {@link GroupElement}则把p置为当前n</li>
     * </ol>
     *
     * @param ruleItems 规则项
     * @return GroupElement - tree
     */
    GroupElement<?> treeifyBin(List<RuleAttr> ruleItems);

    /**
     * 把规则树平展为规则项
     *
     * @param tree 规则树
     * @return RuleAttr - list
     */
    List<RuleAttr> expand(GroupElement<?> tree);
}
