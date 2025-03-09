package cc.allio.uno.data.orm.converter;

import cc.allio.uno.core.exception.Trys;
import cc.allio.uno.core.util.ClassUtils;
import com.google.common.collect.Maps;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * about converter utilize methods.
 * <p>
 * built-in:
 * <ul>
 *     <li>{@link JsonConverter}</li>
 * </ul>
 *
 * @author j.x
 * @see jakarta.persistence.Convert
 * @see jakarta.persistence.AttributeConverter
 * @since 1.2.0
 */
public final class Converters {

    private static final Map<Class<AttributeConverter<?, ?>>, AttributeConverter<?, ?>> VALUE_CONVERTER_MAP
            = Maps.newConcurrentMap();

    /**
     * @return if {@link Field} is null or annotate {@link Field} annotation {@link Convert} is null return null
     * @see #convertToEntity(AttributeConverter, Object)
     */
    public static <D, V> V convertToEntity(@NotNull Field field, D dbData) {
        Convert convert = field.getAnnotation(Convert.class);
        if (convert != null) {
            Class<AttributeConverter<?, ?>> converterClass = convert.converter();
            if (converterClass != null) {
                // 1. try to judgement is JsonConverter
                if (JsonConverter.class.isAssignableFrom(converterClass)) {
                    JsonConverter converter = new JsonConverter<>(field.getType());
                    return Trys.onContinue(() -> (V) convertToEntity(converter, dbData));
                }

                // 2. try to from cache get converter
                // 3. if not found, create new instance and cache it
                AttributeConverter converter =
                        VALUE_CONVERTER_MAP.computeIfAbsent(converterClass, ClassUtils::newInstance);
                return Trys.onContinue(() -> (V) convertToEntity(converter, dbData));
            }
        }

        return null;
    }

    /**
     * convert db data to entity
     *
     * @param converter converter
     * @param dbData    db data
     * @param <D>       the db data type
     * @param <V>       the entity data type
     * @return the entity data
     */
    public static <D, V> V convertToEntity(@NotNull AttributeConverter<V, D> converter, D dbData) {
        return converter.convertToEntityAttribute(dbData);
    }


    /**
     * with {@link #convertToEntity(Field, Object)} likeness.
     *
     * @see #convertToEntity(AttributeConverter, Object)
     */
    public static <D, V> D convertToDbData(@NotNull Field field, V entityData) {
        Convert convert = field.getAnnotation(Convert.class);
        if (convert != null) {
            Class<AttributeConverter<?, ?>> converterClass = convert.converter();
            if (converterClass != null) {
                // 1. try to judgement is JsonConverter
                if (JsonConverter.class.isAssignableFrom(converterClass)) {
                    JsonConverter converter = new JsonConverter<>(field.getType());
                    return Trys.onContinue(() -> (D) convertToDbData(converter, entityData));
                }

                // 2. try to from cache get converter
                // 3. if not found, create new instance and cache it
                AttributeConverter converter =
                        VALUE_CONVERTER_MAP.computeIfAbsent(converterClass, ClassUtils::newInstance);
                return Trys.onContinue(() -> (D) convertToDbData(converter, entityData));
            }
        }

        return null;
    }

    /**
     * convert entity data to db data
     *
     * @param converter  converter
     * @param entityData entity data
     * @param <D>        the db data type
     * @param <V>        the entity data type
     * @return the db data
     */
    public static <D, V> D convertToDbData(@NotNull AttributeConverter<V, D> converter, V entityData) {
        return converter.convertToDatabaseColumn(entityData);
    }
}
