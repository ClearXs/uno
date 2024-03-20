package cc.allio.uno.data.orm.annotation;

import java.lang.annotation.*;

/**
 * 逻辑删除
 * <pre>
 *     Example:
 *
 *     &#064;Entity
 *     &#064;Table(name="CUST")
 *     public class Customer {
 *
 *         &#064;LogicDelete
 *         private Integer isDeleted
 *      }
 *
 * </pre>
 *
 * @author j.x
 * @date 2024/2/19 16:16
 * @since 1.1.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LogicDelete {

    /**
     * 字段标识
     */
    String column() default "is_deleted";

    /**
     * 未删除值
     */
    int undeleted() default 0;

    /**
     * 已删除的值
     */
    int deleted() default 1;
}
