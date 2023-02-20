package cc.allio.uno.gis.jackson.geojson.introspector;

import cc.allio.uno.gis.jackson.geojson.deserializer.GeoJsonDeserializer;
import cc.allio.uno.gis.jackson.geojson.serializer.GeoJsonSerializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *
 * @author Mr.QL
 * @ClassName ignoreJsonTypeInfoIntrospector
 * @Date 2022-03-05 18:19
 * @Version 1.0
 */
public class IgnoreJsonTypeInfoIntrospector extends JacksonAnnotationIntrospector {

	/**
	 * 排除反序列化注解
	 */
	private Map<String, Class<? extends GeoJsonDeserializer>> ignoreDeserializeClazzMap = Maps.newHashMapWithExpectedSize(10);
	/**
	 * 排除序列化注解
	 */
	private Map<String, Class<? extends GeoJsonSerializer>> ignoreSerializableClazzMap = Maps.newHashMapWithExpectedSize(10);

	public IgnoreJsonTypeInfoIntrospector() {
	}

	public void addIgnoreDeserializeClazz(String className, Class<? extends GeoJsonDeserializer> ignoreDeserializeClazz) {
		this.ignoreDeserializeClazzMap.put(className, ignoreDeserializeClazz);
	}

	public void addIgnoreSerializableClazz(String className, Class<? extends GeoJsonSerializer> ignoreSerializableClazz) {
		this.ignoreSerializableClazzMap.put(className, ignoreSerializableClazz);
	}

	/**
	 * @param a 注解作用类
	 * @return 返回作用类上反序列化实现
	 */
	@Override
	public Object findDeserializer(Annotated a) {

		Class<? extends GeoJsonDeserializer> ignoreDeserializer = this.ignoreDeserializeClazzMap.get(a.getName());
		Object deserializer = super.findDeserializer(a);
		if (deserializer != null && ignoreDeserializer != null) {
			// 是否为自定义反序列化对象子类
			if (deserializer.equals(ignoreDeserializer)) {
				// 排除该反序列化
				return null;
			}
		}
		return deserializer;
	}

	/**
	 * @param a 注解作用类
	 * @return 返回作用类上序列化实现
	 */
	@Override
	public Object findSerializer(Annotated a) {
		Object serializer = super.findSerializer(a);
		if (serializer != null) {
			Class<? extends GeoJsonSerializer> ignoreSerializer = ignoreSerializableClazzMap.get(a.getName());
			// 是否为自定义序列化对象子类
			if (serializer.equals(ignoreSerializer)) {
				// 排除该序列化
				return null;
			}
		}
		return serializer;
	}

}
