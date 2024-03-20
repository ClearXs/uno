package cc.allio.uno.sequnetial;

import cc.allio.uno.core.metadata.CompositeMetadata;
import cc.allio.uno.core.metadata.mapping.MappingMetadata;
import cc.allio.uno.core.type.Type;

import java.util.Map;
import java.util.function.Predicate;

/**
 * 复合类型时序数据
 *
 * @author j.x
 * @date 2022/11/22 10:33
 * @since 1.1.1
 */
public interface CompositeSequential extends CompositeMetadata<Sequential>, Sequential {

    @Override
    default Type getType() {
        return null;
    }

    @Override
    default Long processTime() {
        return null;
    }

    @Override
    default Long eventTime() {
        return null;
    }

    @Override
    default Predicate<Sequential> selfChanged() {
        return null;
    }

    @Override
    default MappingMetadata getMapping() {
        return null;
    }

    @Override
    default Map<String, Object> getValues() {
        return null;
    }
}
