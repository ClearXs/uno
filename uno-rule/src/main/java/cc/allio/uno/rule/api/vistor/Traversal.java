package cc.allio.uno.rule.api.vistor;

/**
 * 规则树遍历方式
 * <ul>
 *     <li>{@link #NONE}：优先访问枝干</li>
 *     <li>{@link #DEEP}：深度优先</li>
 *     <li>{@link #BREADTH}：广度优先</li>
 * </ul>
 */
public enum Traversal {
    NONE, DEEP, BREADTH;
}
