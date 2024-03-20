package cc.allio.uno.sequnetial.washer;

import cc.allio.uno.core.type.RegexType;
import cc.allio.uno.core.type.Type;
import cc.allio.uno.sequnetial.context.SequentialContext;
import com.google.auto.service.AutoService;
import org.springframework.util.ObjectUtils;

import java.util.function.Predicate;

/**
 * 默认数据清洗操作
 *
 * @author j.x
 * @date 2022/5/19 13:59
 * @since 1.0
 */
@AutoService(Washer.class)
public class DefaultWasher implements Washer {

    @Override
    public Predicate<SequentialContext> cleaning() {
        return context -> !ObjectUtils.isEmpty(context.getRealSequential().getCode());
    }

    @Override
    public int order() {
        return MAX_PRIORITY;
    }

    @Override
    public Type getType() {
        // 默认匹配所有字符串
        return new RegexType("[\\s\\S]*");
    }

    @Override
    public String description() {
        return "compare the context -> !ObjectUtils.isEmpty(context.getRealSequential().getCode())";
    }
}
