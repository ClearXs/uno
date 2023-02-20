package cc.allio.uno.data.orm.dialect.type;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * type registry
 *
 * @author jiangwei
 * @date 2023/1/13 16:00
 * @since 1.1.4
 */
public class TypeRegistry {

    private final Set<JavaType<?>> javaTypes;
    private final Map<Integer, JdbcType> jdbcTypes;
    private final Map<Integer, JavaType<?>> typeMapping;

    public TypeRegistry() {
        this.javaTypes = Sets.newHashSet();
        this.jdbcTypes = Maps.newHashMap();
        this.typeMapping = Maps.newHashMap();
    }

    /**
     * 获取jdbc types
     *
     * @return jdbcTypes
     */
    public Set<JdbcType> getJdbcTypes() {
        return Sets.newHashSet(jdbcTypes.values());
    }

    /**
     * 获取java types
     *
     * @return javaTypes
     */
    public Set<JavaType<?>> getJavaTypes() {
        return javaTypes;
    }

    /**
     * 根据jdbc code查找出jdbc type实例L
     *
     * @param jdbcCode jdbc code
     * @return JdbcType
     */
    public JdbcType findJdbcType(Integer jdbcCode) {
        return jdbcTypes.get(jdbcCode);
    }

    /**
     * 根据jdbc code查找出映射的java type
     *
     * @param jdbcCode jdbc code
     * @return JavaType
     */
    public JavaType<?> findJavaType(Integer jdbcCode) {
        return typeMapping.get(jdbcCode);
    }

    /**
     * <pre>
     *      key = jdbcType
     *      value = javaType
     * </pre>
     *
     * @param jdbcType jdbc type
     * @param javaType java type
     */
    public void register(JdbcType jdbcType, JavaType<?> javaType) {
        typeMapping.put(jdbcType.getJdbcCode(), javaType);
        jdbcTypes.put(jdbcType.getJdbcCode(), jdbcType);
        javaTypes.add(javaType);
    }

}
