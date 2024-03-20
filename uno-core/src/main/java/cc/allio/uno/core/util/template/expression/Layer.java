package cc.allio.uno.core.util.template.expression;

import lombok.Data;

import java.util.List;

/**
 * 运行时的Layer
 *
 * @author j.x
 * @date 2022/12/3 19:34
 * @since 1.1.2
 */
@Data
public class Layer {

    /**
     * 层级的深度
     */
    private int depth;

    /**
     * 层级所在表达式文本
     */
    private String text;

    /**
     * 父Layer
     */
    private Layer parent;

    /**
     * 子Layer
     */
    private List<Layer> children;

    /**
     * 当前层级的change实例
     */
    private Interchange change;

    /**
     * 当前层级值
     */
    private Object value;
}
