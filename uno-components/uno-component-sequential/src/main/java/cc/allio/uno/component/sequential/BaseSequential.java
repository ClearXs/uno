package cc.allio.uno.component.sequential;

import cc.allio.uno.core.util.id.IdGenerator;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public abstract class BaseSequential implements Sequential {

    /**
     * 映射转换时不存在映射数据，记录不存在的字段
     */
    private final Map<String, Object> undefinedValues = Maps.newHashMap();

    /**
     * 时序数据唯一id标识
     */
    private final Long seqId = IdGenerator.defaultGenerator().getNextId();
}
