package cc.allio.uno.data.sql.query;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class SelectContext {

    /**
     * 数据
     */
    private Map<String, Object> data = Maps.newHashMap();

    /**
     * 添加数据值
     *
     * @param key   数据key
     * @param value 数据value
     */
    public void addData(String key, Object value) {
        data.put(key, value);
    }
}
