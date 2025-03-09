package cc.allio.uno.data.orm.converter;

import cc.allio.uno.core.util.JsonUtils;
import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;

/**
 * base on jackson convert entity to json string
 *
 * @author j.x
 * @since 1.2.0
 */
@AllArgsConstructor
public class JsonConverter<T> implements AttributeConverter<T, String> {

    private final Class<T> entityType;

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return JsonUtils.toJson(attribute);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return JsonUtils.parse(dbData, entityType);
    }
}