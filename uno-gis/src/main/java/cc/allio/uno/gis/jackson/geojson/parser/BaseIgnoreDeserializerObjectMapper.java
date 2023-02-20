package cc.allio.uno.gis.jackson.geojson.parser;

import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cc.allio.uno.gis.jackson.geojson.introspector.IgnoreJsonTypeInfoIntrospector;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *
 * @author Mr.QL
 * @ClassName BaseIgnoreDeserializerObjectMapper
 * @Date 2022-03-12 22:28
 * @Version 1.0
 */
public abstract class BaseIgnoreDeserializerObjectMapper<T> {

	/**
	 * 反序列化泛型类型
	 */
	protected JavaType valueType;

	/**
	 * 重装数据后需要忽略当前指定的放序列化实现
	 */
	private Class<? extends GeoJsonDeserializer> deserializeClazz;

	/**
	 * 忽略自定义反序列化的mapper
	 */
	protected ObjectMapper ignoreDeserializerAnnotationMapper;

	protected BaseIgnoreDeserializerObjectMapper(JavaType valueType, Class<? extends GeoJsonDeserializer> deserializeClazz) {
		this.valueType = valueType;
		this.deserializeClazz = deserializeClazz;
	}

	public ObjectMapper getIgnoreDeserializerAnnotationMapper(ObjectMapper mapper) {
		if (null == ignoreDeserializerAnnotationMapper) {
			ignoreDeserializerAnnotationMapper = mapper.copy();
			//复制一份mapper config排除自定义注解
			IgnoreJsonTypeInfoIntrospector ignoreJsonTypeInfoIntrospector = new IgnoreJsonTypeInfoIntrospector();
			Class<?> rawClass = valueType.getRawClass();
			ignoreJsonTypeInfoIntrospector.addIgnoreDeserializeClazz(rawClass.getName(), deserializeClazz);
			ignoreDeserializerAnnotationMapper.setAnnotationIntrospector(ignoreJsonTypeInfoIntrospector);
		}
		return ignoreDeserializerAnnotationMapper;
	}
}
