package cc.allio.uno.component.sequential.washer;

import cc.allio.uno.component.sequential.Sequential;
import com.google.auto.service.AutoService;
import org.springframework.util.ObjectUtils;

import java.util.function.Predicate;

/**
 * 默认数据清洗操作
 *
 * @author jiangwei
 * @date 2022/5/19 13:59
 * @since 1.0
 */
@AutoService(Washer.class)
public class DefaultWasher implements Washer {

    private static final String DEFAULT_TYPE = "DEFAULT-TYPE";

    @Override
    public Predicate<Sequential> cleaning() {
        return sequential -> !ObjectUtils.isEmpty(sequential.getCode());
    }

    @Override
    public int order() {
        return MAX_PRIORITY;
    }

    @Override
    public String getType() {
        return DEFAULT_TYPE;
    }

    @Override
    public boolean contains(String type) {
        return DEFAULT_TYPE.equals(type);
    }
}
