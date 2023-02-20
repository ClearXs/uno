package cc.allio.uno.data.query.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 包含值与时间对象
 *
 * @author jiangwei
 * @date 2022/10/11 14:56
 * @since 1.1.0
 */
@Data
@AllArgsConstructor
public class ValueTime {

    /**
     * 时间
     */
    private Date time;

    /**
     * 数值
     */
    private Object value;
}
