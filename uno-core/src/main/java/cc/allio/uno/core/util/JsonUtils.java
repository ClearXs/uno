package cc.allio.uno.core.util;

import cc.allio.uno.core.exception.Exceptions;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.Collections;

/**
 * Json转换的实用工具类
 *
 * @author j.x
 * @date 2022 /1/29 17:02
 * @since 1.0
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper instance = newJsonMapper();

    private JsonUtils() {

    }

    /**
     * 返回空的ObjectNode
     *
     * @return 空json数据 object node
     */
    public static ObjectNode empty() {
        return instance.createObjectNode();
    }

    /**
     * 返回空的ArrayNode
     *
     * @return array node
     */
    public static ArrayNode arrayEmpty() {
        return instance.createArrayNode();
    }

    /**
     * 将对象序列化成json字符串
     *
     * @param <T>   the type parameter
     * @param value javaBean
     * @return jsonString json字符串
     */
    public static <T> String toJson(T value) {
        try {
            return getJsonMapper().writeValueAsString(value);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static byte[] toJsonAsBytes(Object object) {
        try {
            return getJsonMapper().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param <T>       T 泛型标记
     * @param content   content
     * @param valueType class
     * @return Bean t
     */
    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return getJsonMapper().readValue(content, valueType);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param <T>       T 泛型标记
     * @param bytes     json bytes
     * @param valueType class
     * @return Bean t
     */
    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        try {
            return getJsonMapper().readValue(bytes, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonString jsonString
     * @return jsonString json字符串
     */
    public static JsonNode readTree(String jsonString) {
        Objects.requireNonNull(jsonString, "jsonString is null");
        try {
            return getJsonMapper().readTree(jsonString);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param in InputStream
     * @return jsonString json字符串
     */
    public static JsonNode readTree(InputStream in) {
        Objects.requireNonNull(in, "InputStream in is null");
        try {
            return getJsonMapper().readTree(in);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param content content
     * @return jsonString json字符串
     */
    public static JsonNode readTree(byte[] content) {
        Objects.requireNonNull(content, "byte[] content is null");
        try {
            return getJsonMapper().readTree(content);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonParser JsonParser
     * @return jsonString json字符串
     */
    public static JsonNode readTree(JsonParser jsonParser) {
        Objects.requireNonNull(jsonParser, "jsonParser is null");
        try {
            return getJsonMapper().readTree(jsonParser);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param <T>          泛型
     * @param content      bytes
     * @param elementClass elementClass
     * @return 集合 list
     */
    public static <T> List<T> readList(@Nullable byte[] content, Class<T> elementClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyList();
        }
        try {
            return getJsonMapper().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param <T>          泛型
     * @param content      InputStream
     * @param elementClass elementClass
     * @return 集合 list
     */
    public static <T> List<T> readList(@Nullable InputStream content, Class<T> elementClass) {
        if (content == null) {
            return Collections.emptyList();
        }
        try {
            return getJsonMapper().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param <T>          泛型
     * @param content      bytes
     * @param elementClass elementClass
     * @return 集合 list
     */
    public static <T> List<T> readList(@Nullable String content, Class<T> elementClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyList();
        }
        try {
            return getJsonMapper().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取为Set数据
     *
     * @param <T>          每一个元素泛型
     * @param bytes        字节数组
     * @param elementClass 元素Class对象
     * @return Set实例 setValue
     */
    public static <T> Set<T> readSet(byte[] bytes, Class<T> elementClass) {
        if (ObjectUtils.isEmpty(bytes)) {
            return Collections.emptySet();
        }
        try {
            return getJsonMapper().readValue(bytes, getSetType(elementClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     */
    public static <K, V> Map<K, V> readMap(@Nullable byte[] content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtils.isEmpty(content)) {
            return java.util.Collections.emptyMap();
        }
        try {
            return getJsonMapper().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    InputStream
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     */
    public static <K, V> Map<K, V> readMap(@Nullable InputStream content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtils.isEmpty(content)) {
            return java.util.Collections.emptyMap();
        }
        try {
            return getJsonMapper().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     */
    public static <K, V> Map<K, V> readMap(@Nullable String content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyMap();
        }
        try {
            return getJsonMapper().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * To map map.
     *
     * @param content the content
     * @return the map
     */
    public static Map<String, Object> toMap(String content) {
        try {
            return getJsonMapper().readValue(content, Map.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Map转换为JsonNode
     *
     * @param content map数据
     * @return JsonNode json node
     */
    public static JsonNode toJsonByMap(Map<String, Object> content) {
        ObjectNode node = empty();
        for (Map.Entry<String, Object> entry : content.entrySet()) {
            node.putPOJO(entry.getKey(), entry.getValue());
        }
        return node;
    }

    /**
     * To map map.
     *
     * @param <T>          the type parameter
     * @param content      the content
     * @param valueTypeRef the value type ref
     * @return the map
     */
    public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
        try {
            Map<String, Map<String, Object>> map = getJsonMapper().readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {
            });
            Map<String, T> result = new HashMap<>(16);
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), toPojo(entry.getValue(), valueTypeRef));
            }
            return result;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * To pojo t.
     *
     * @param <T>         the type parameter
     * @param fromValue   the from value
     * @param toValueType the to value type
     * @return the t
     */
    public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
        return getJsonMapper().convertValue(fromValue, toValueType);
    }

    /**
     * 获取Jackson ObjectMapper实例对象
     *
     * @return 单实例对象 json mapper
     */
    public static ObjectMapper getJsonMapper() {
        return instance;
    }

    /**
     * 封装 Set type
     *
     * @param elementClass 集合值类型
     * @return CollectionLikeType setValue type
     */
    public static CollectionLikeType getSetType(Class<?> elementClass) {
        return getJsonMapper().getTypeFactory().constructCollectionLikeType(Set.class, elementClass);
    }

    /**
     * 封装 List type
     *
     * @param elementClass 集合值类型
     * @return CollectionLikeType list type
     */
    public static CollectionLikeType getListType(Class<?> elementClass) {
        return getJsonMapper().getTypeFactory().constructCollectionLikeType(List.class, elementClass);
    }

    /**
     * 封装 map type
     *
     * @param keyClass   key 类型
     * @param valueClass value 类型
     * @return MapType map type
     */
    public static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
        return getJsonMapper().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    /**
     * 创建一个新的ObjectMapper
     *
     * @return ObjectMapper实例 object mapper
     */
    public static ObjectMapper newJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 设置地点为中国
        mapper.setLocale(Locale.CHINA);
        // 设置为中国上海时区
        mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        //序列化时，日期的统一格式
        mapper.setDateFormat(new SimpleDateFormat(DateUtil.PATTERN_DATETIME, Locale.CHINA));
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        // 失败处理
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 反序列化时，属性不存在的兼容处理s
        mapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 单引号处理
        mapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
        // 解决时间序列化问题
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * 由外部传递{@link ObjectMapper}进行使用
     */
    public static void reset(ObjectMapper objectMapper) {
        JsonUtils.instance = objectMapper;
    }
}
