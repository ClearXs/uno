package cc.allio.uno.core.datastructure.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则树遍历方式
 * <ul>
 *     <li>{@link #NONE}：优先访问枝干</li>
 *     <li>{@link #DEEP}：深度优先</li>
 *     <li>{@link #BREADTH}：广度优先</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public enum Traversal {

    NONE("NONE"),
    DEEP("NONE"),
    BREADTH("NONE");

    private final String value;
}
