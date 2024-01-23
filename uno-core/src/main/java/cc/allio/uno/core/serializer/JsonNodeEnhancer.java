package cc.allio.uno.core.serializer;

import cc.allio.uno.core.util.BigDecimalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.*;

/**
 * Http Json数据增强
 *
 * @author jiangwei
 * @date 2022/6/17 19:52
 * @since 1.0
 */
@Slf4j
public class JsonNodeEnhancer {

    private final JsonNode jsonNode;

    public JsonNodeEnhancer(JsonNode jsonNode) {
        Assert.notNull(jsonNode, "JsonNode must not null");
        this.jsonNode = jsonNode;
    }

    // --------------------- 基础数据类型值获取 ---------------------

    /**
     * 根据指定字段获取对应的值
     *
     * @param fieldName 字段名称
     * @return null or value
     */
    public Object asValue(String fieldName) {
        if (!has(fieldName)) {
            return null;
        }
        JsonNode fieldNode = jsonNode.get(fieldName);
        JsonNodeType nodeType = fieldNode.getNodeType();
        switch (nodeType) {
            case NUMBER:
                return asBigDecimal(fieldName);
            case STRING:
                return asString(fieldName);
            case OBJECT:
            case ARRAY:
                return fieldNode;
            case BOOLEAN:
                return asBoolean(fieldName);
            default:
                return null;
        }
    }

    /**
     * 根据指定的字段名获取json BigDecimal数据
     *
     * @param fieldName 字段名称
     * @return 如果数据为空则返回0
     * @see #asBigDecimal(String, Supplier)
     */
    public BigDecimal asBigDecimal(String fieldName) {
        return asBigDecimal(fieldName, () -> new BigDecimal("0"));

    }

    /**
     * 根据指定的字段名获取json BigDecimal数据
     *
     * @param fieldName    字段名称
     * @param defaultValue 默认值
     * @return 如果数据为空则返回0
     */
    public BigDecimal asBigDecimal(String fieldName, Supplier<BigDecimal> defaultValue) {
        String maybeBigDecimal = asString(fieldName);
        BigDecimal maybe = BigDecimalUtils.creator(maybeBigDecimal);
        if (Objects.isNull(maybe)) {
            return defaultValue.get();
        }
        return maybe;

    }

    /**
     * 根据指定的字段名获取json字符串数据
     *
     * @param fieldName 字段名称
     * @return 如果数据为空则返回空字符串
     * @see #asString(String, Supplier)
     */
    public String asString(String fieldName) {
        return asString(fieldName, () -> "");
    }

    /**
     * 根据指定的字段名获取json字符串数据
     *
     * @param fieldName    字段名称
     * @param defaultValue 默认值
     * @return 如果数据为空则取默认值数据
     * @see JsonNode#asText()
     */
    public String asString(String fieldName, Supplier<String> defaultValue) {
        if (jsonNode.has(fieldName)) {
            try {
                JsonNode fieldNode = jsonNode.get(fieldName);
                if (fieldNode.isArray()) {
                    return fieldNode.toPrettyString();
                } else {
                    return fieldNode.asText(defaultValue.get());
                }
            } catch (Throwable e) {
                log.error("get {} asString failed", fieldName, e);
                return defaultValue.get();
            }
        }
        return defaultValue.get();
    }

    /**
     * 获取json数据中指定字段Integer类型的数据
     *
     * @param fieldName 字段名称
     * @return 如果获取不到则取默认值0
     * @see #asInteger(String, IntSupplier)
     */
    public Integer asInteger(String fieldName) {
        return asInteger(fieldName, () -> 0);
    }

    /**
     * 获取json数据中指定字段Integer类型的数据
     *
     * @param fieldName    字段名称
     * @param defaultValue 默认值
     * @return 如果获取不到则取默认值0
     * @see JsonNode#asInt()
     */
    public Integer asInteger(String fieldName, IntSupplier defaultValue) {
        if (jsonNode.has(fieldName)) {
            try {
                return jsonNode.get(fieldName).asInt(defaultValue.getAsInt());
            } catch (Throwable e) {
                log.error("Get {} asInteger failed", fieldName, e);
                return defaultValue.getAsInt();
            }
        }
        return defaultValue.getAsInt();
    }

    /**
     * 获取json数据中指定字段Double类型的数据
     *
     * @param fieldName 字段名称
     * @return 如果获取不到则取默认值0
     * @see #asDouble(String, DoubleSupplier)
     */
    public Double asDouble(String fieldName) {
        return asDouble(fieldName, () -> 0);
    }

    /**
     * 获取json数据中指定字段Double类型的数据
     *
     * @param fieldName    字段名称
     * @param defaultValue 默认值
     * @return 如果获取不到则取默认值0
     * @see JsonNode#asDouble()
     */
    public Double asDouble(String fieldName, DoubleSupplier defaultValue) {
        if (jsonNode.has(fieldName)) {
            try {
                return jsonNode.get(fieldName).asDouble(defaultValue.getAsDouble());
            } catch (Throwable e) {
                log.error("Get {} asDouble failed", fieldName, e);
                return defaultValue.getAsDouble();
            }
        }
        return defaultValue.getAsDouble();
    }

    /**
     * 获取json数据中指定字段Boolean类型的数据
     *
     * @param fieldName 字段名称
     * @return 如果获取不到则取默认值0
     * @see #asBoolean(String, BooleanSupplier)
     */
    public Boolean asBoolean(String fieldName) {
        return asBoolean(fieldName, () -> false);
    }

    /**
     * 获取json数据中指定字段Boolean类型的数据
     *
     * @param fieldName    字段名称
     * @param defaultValue 默认值
     * @return 如果获取不到则取默认值false
     * @see JsonNode#asBoolean()
     */
    public Boolean asBoolean(String fieldName, BooleanSupplier defaultValue) {
        if (jsonNode.has(fieldName)) {
            try {
                return jsonNode.get(fieldName).asBoolean(defaultValue.getAsBoolean());
            } catch (Throwable e) {
                log.error("Get {} asBoolean failed", fieldName, e);
                return defaultValue.getAsBoolean();
            }
        }
        return defaultValue.getAsBoolean();
    }

    /**
     * 获取json数据中指定字段Long类型的数据
     *
     * @param fieldName 字段名称
     * @return 如果获取不到则取默认值0
     * @see #asLong(String, LongSupplier)
     */
    public Long asLong(String fieldName) {
        return asLong(fieldName, () -> 0);
    }

    /**
     * 获取json数据中指定字段Long类型的数据
     *
     * @param fieldName    字段名称
     * @param defaultValue 默认值
     * @return 如果获取不到则取默认值0
     * @see JsonNode#asLong()
     */
    public Long asLong(String fieldName, LongSupplier defaultValue) {
        if (jsonNode.has(fieldName)) {
            try {
                return jsonNode.get(fieldName).asLong(defaultValue.getAsLong());
            } catch (Throwable e) {
                log.error("Get {} asLong failed", fieldName, e);
                return defaultValue.getAsLong();
            }
        }
        return defaultValue.getAsLong();
    }

    /**
     * 装饰JsonNode
     *
     * @param index 字段索引位置
     * @return True if this node is a JSON Object node, and has a property entry with specified name (with any value, including null value)
     * @see JsonNode#has(int)
     */
    public boolean has(int index) {
        return jsonNode.has(index);
    }

    /**
     * 装饰JsonNode
     *
     * @param fieldName
     * @return True if this node is a JSON Object node, and has a property entry with specified name (with any value, including null value)
     * @see JsonNode#has(String)
     */
    public boolean has(String fieldName) {
        return jsonNode.has(fieldName);
    }

    /**
     * 放回当前字段名的子集合
     *
     * @param fieldName 字段名
     * @return
     */
    public List<JsonNode> findValues(String fieldName) {
        return jsonNode.findValues(fieldName);
    }

    // --------------------- 其他操作 ---------------------

    /**
     * 从当前Json中重新获取json树结构
     *
     * @param fieldName 字段名
     * @return 增强的Json对象
     * @throws NullPointerException 如果没有找到指定的数据时抛出
     */
    public JsonNodeEnhancer getTree(String fieldName) {
        return new JsonNodeEnhancer(jsonNode.get(fieldName));
    }

    /**
     * 从当前Json中获取json结构
     *
     * @param fieldName 字段名
     * @return 增强的Json对象
     * @throws NullPointerException 如果没有找到指定的数据时抛出
     */
    public JsonNodeEnhancer getNode(String fieldName) {
        return new JsonNodeEnhancer(jsonNode.get(fieldName));
    }

    /**
     * 从当前Json数组中获取指定索引位置的Json数据
     *
     * @param index 某个索引位置
     * @return 增强的Json对象
     * @throws NullPointerException 如果没有找到指定的数据时抛出
     */
    public JsonNodeEnhancer getNode(int index) {
        return new JsonNodeEnhancer(jsonNode.get(index));
    }


    @Override
    public String toString() {
        return jsonNode.toString();
    }

    public String toPrettyString() {
        return jsonNode.toPrettyString();
    }

    /**
     * 当前json数据
     *
     * @return 当前json数据的多少
     */
    public int size() {
        return jsonNode.size();
    }

}

