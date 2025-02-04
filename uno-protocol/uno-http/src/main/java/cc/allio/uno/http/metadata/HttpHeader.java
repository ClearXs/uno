package cc.allio.uno.http.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Http请求头
 *
 * @author j.x
 */
@Data
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpHeader {

    /**
     * 请求头的名称
     */
    private String name;

    /**
     * 请求头的内容
     */
    private String[] values;
}
