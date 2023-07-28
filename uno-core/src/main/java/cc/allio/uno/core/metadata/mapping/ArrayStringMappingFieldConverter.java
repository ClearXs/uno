package cc.allio.uno.core.metadata.mapping;

import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 进行转换的数据可能是否Collection、可能是[{...}]的json形式。通过处理这两种形式，使最终返回一个String数据
 *
 * @author jiangwei
 * @date 2022/12/14 13:36
 * @since 1.1.3
 */
public class ArrayStringMappingFieldConverter extends BaseMappingFieldConverter<String> {

    public ArrayStringMappingFieldConverter() {
    }

    public ArrayStringMappingFieldConverter(MappingField mappingField) {
        super(mappingField);
    }

    @Override
    public String execute(ApplicationContext context, Object value) throws Throwable {
        // 处理collection数据
        List<String> maybeList = parseCollectionValue(value);
        // 处理json数据
        if (ObjectUtils.isEmpty(maybeList)) {
            maybeList = parseJsonValue(value);
        }
        return ObjectUtils.isEmpty(maybeList) ? value.toString() : maybeList.get(0);
    }

    /**
     * 处理ValueList的数据，如果是（{@link Collection}）的话
     *
     * @param value
     * @return
     */
    private List<String> parseCollectionValue(Object value) {
        if (Collection.class.isAssignableFrom(value.getClass())) {
            return Lists.newArrayList(((Collection<?>) value).stream().map(Object::toString).collect(Collectors.toList()));
        }
        return Collections.emptyList();
    }

    /**
     * 处理Value json数据
     *
     * @param value
     * @return
     */
    private List<String> parseJsonValue(Object value) {
        List<String> maybeList = Lists.newArrayList();
        try {
            maybeList = JsonUtils.readList(value.toString(), String.class);
        } catch (Throwable ex) {
            try {
                JsonNode jsonNode = JsonUtils.readTree(value.toString());
                if (jsonNode.isArray()) {
                    ArrayNode arrayNode = (ArrayNode) jsonNode;
                    for (JsonNode node : arrayNode) {
                        maybeList.add(node.textValue());
                    }
                }
            } catch (Throwable ex2) {
                return Collections.emptyList();
            }
        }
        return maybeList;
    }


    @Override
    public String keyConverter() {
        return null;
    }
}
