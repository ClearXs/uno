package cc.allio.uno.data.orm.dsl.type;

import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.*;

import java.sql.Types;
import java.util.*;

/**
 * Type Registry is about of {@link JavaType} and {@link JdbcType} registry. it description relation of {@link JavaType} and {@link JdbcType}
 * <p>
 *     first, it will be registration abound of {@link JavaType} and {@link JdbcType} instance to current class.
 *     then build relation of {@link JavaType} and {@link JdbcType}.
 * </p>
 * <p>
 *     {@link TypeRegistry} supply abundant method about of find {@link JavaType} and {@link JdbcType}, like as {@link #findJdbcType(Integer)}, {@link #findJavaType(Integer)} etc.
 * </p>
 *
 * @author j.x
 * @since 1.1.4
 */
public final class TypeRegistry {

    private static final TypeRegistry INSTANCE = new TypeRegistry();

    // 内部维护类型的关联关系
    // 以@key作为JavaType，快速的索引JavaType实例
    private final Map<Class<? extends JavaType<?>>, JavaType<?>> javaTypes;
    // 以@key作为integer类型，快速索引jdbctype
    private final Map<Integer, JdbcType> jdbcTypes;
    // jdbc type -> java type
    private final Multimap<Integer, JavaType<?>> jdbcTypesMappings;
    // java type -> jdbc type
    private final Multimap<JavaType<?>, Integer> javaTypeMappings;

    private TypeRegistry() {
        this.javaTypes = Maps.newHashMap();
        this.jdbcTypes = Maps.newHashMap();
        this.jdbcTypesMappings = ArrayListMultimap.create();
        this.javaTypeMappings = ArrayListMultimap.create();

        // 初始化java type 与 jdbc type
        initJavaType();
        initJdbcType();

        // 初始化java type 与 jdbc type的关联关系
        initNumericTypes();
        initCharTypes();
        initDateTypes();
        initOtherTypes();
        initComplexTypes();
        initUnknownTypes();
    }

    /**
     * 初始化jdbc type
     */
    private void initJdbcType() {
        // ----------- numeric types -----------
        // boolean
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.BOOLEAN)
                        .setName("boolean")
                        .build()
        );
        // smallint
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.SMALLINT)
                        .setSignature("smallint($m)")
                        .setMaxBytes(2L)
                        .setName("smallint")
                        .build()
        );
        // int
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.INTEGER)
                        .setSignature("int($m)")
                        .setMaxBytes(4L)
                        .setName("int")
                        .build()
        );
        // bigint
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.BIGINT)
                        .setSignature("bigint($m)")
                        .setMaxBytes(8L)
                        .setName("bigint")
                        .build()
        );
        // decimal
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.DECIMAL)
                        .setSignature("decimal($p,$s)")
                        .isDecimal(true)
                        .setMaxBytes(8L)
                        .setName("decimal")
                        .build()
        );
        // numeric
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.NUMERIC)
                        .setSignature("numeric($p,$s)")
                        .isDecimal(true)
                        .setMaxBytes(8L)
                        .setName("numeric")
                        .build()
        );
        // double
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.DOUBLE)
                        .isDecimal(true)
                        .setMaxBytes(8L)
                        .setName("double")
                        .build()
        );
        // bit
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.BIT)
                        .setName("bit")
                        .build()
        );
        // tinyint
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.TINYINT)
                        .setName("tinyint")
                        .build()
        );
        // ----------- date types -----------
        // timestamp
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.TIMESTAMP)
                        .setName("timestamp")
                        .build()
        );
        // time
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.TIME)
                        .setName("time")
                        .build()
        );
        // date
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.DATE)
                        .setName("date")
                        .build()
        );
        // time_with_timezone
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.TIME_WITH_TIMEZONE)
                        .setName("time_with_timezone")
                        .build()
        );
        // timestamp_with_timezone
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.TIMESTAMP_WITH_TIMEZONE)
                        .setName("timestamp_with_timezone")
                        .build()
        );
        // ----------- char types -----------
        // nvarchar
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.NVARCHAR)
                        .setName("nvarchar")
                        .build()
        );
        // varchar
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.VARCHAR)
                        .setName("varchar")
                        .build()
        );
        // char
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.CHAR)
                        .setName("char")
                        .build()
        );
        // nchar
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.NCHAR)
                        .setName("nchar")
                        .build()
        );
        // longvarchar
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.LONGVARCHAR)
                        .setName("longvarchar")
                        .build()
        );
        // longvarbinary
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.LONGVARBINARY)
                        .setName("longvarbinary")
                        .build()
        );
        // longnvarchar
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.LONGNVARCHAR)
                        .setName("longnvarchar")
                        .build()
        );
        // varbinary
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.VARBINARY)
                        .setName("varbinary")
                        .build()
        );
        // ----------- complex types -----------
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.BLOB)
                        .setName("blob")
                        .build()
        );
        // blob
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.CLOB)
                        .setName("clob")
                        .build()
        );
        // clob
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.NCLOB)
                        .setName("nclob")
                        .build()
        );
        // array
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.ARRAY)
                        .setName("array")
                        .build()
        );
        // ----------- unknown types -----------
        // null
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.NULL)
                        .setName("null")
                        .build()
        );
        // other
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.OTHER)
                        .setName("other")
                        .build()
        );
        // java_object
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.JAVA_OBJECT)
                        .setName("java_object")
                        .build()
        );
        // distinct
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.DISTINCT)
                        .setName("distinct")
                        .build()
        );
        // struct
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.STRUCT)
                        .setName("struct")
                        .build()
        );
        // ref
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.REF)
                        .setName("ref")
                        .build()
        );
        // datalink
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.DATALINK)
                        .setName("datalink")
                        .build()
        );
        // sqlxml
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.SQLXML)
                        .setName("sqlxml")
                        .build()
        );
        // ref_cursor
        registerJdbcType(
                JdbcType.builder()
                        .setJdbcCode(Types.REF_CURSOR)
                        .setName("ref_cursor")
                        .build()
        );
    }

    /**
     * 初始化java type
     */
    private void initJavaType() {
        registerJavaType(new BooleanJavaType());
        registerJavaType(new ShortJavaType());
        registerJavaType(new IntegerJavaType());
        registerJavaType(new LongJavaType());
        registerJavaType(new BigDecimalJavaType());
        registerJavaType(new DoubleJavaType());
        registerJavaType(new ByteJavaType());
        registerJavaType(new ShortJavaType());
        registerJavaType(new DateJavaType());
        registerJavaType(new StringJavaType());
        registerJavaType(new CharArrayJavaType());
        registerJavaType(new ArrayJavaType());
        registerJavaType(new UnknownJavaType());
        registerJavaType(new ListJavaType());
        registerJavaType(new MapJavaType());
        registerJavaType(new SetJavaType());
        registerJavaType(new StackJavaType());
        registerJavaType(new QueueJavaType());
        registerJavaType(new EnumJavaType());
    }

    /**
     * 初始化NumericTypes，如 boolean、int
     */
    private void initNumericTypes() {
        // ----------- numeric types -----------
        // smallint
        registerRelation(
                getJdbcType(Types.SMALLINT),
                getJavaType(ShortJavaType.class)
        );
        // int
        registerRelation(
                getJdbcType(Types.INTEGER),
                getJavaType(IntegerJavaType.class)
        );
        // bigint
        registerRelation(
                getJdbcType(Types.BIGINT),
                getJavaType(LongJavaType.class)
        );
        // decimal
        registerRelation(
                getJdbcType(Types.DECIMAL),
                getJavaType(BigDecimalJavaType.class)
        );
        // numeric
        registerRelation(
                getJdbcType(Types.NUMERIC),
                getJavaType(BigDecimalJavaType.class)
        );
        // double
        registerRelation(
                getJdbcType(Types.DOUBLE),
                getJavaType(DoubleJavaType.class)
        );
        // bit
        registerRelation(
                getJdbcType(Types.BIT),
                getJavaType(ByteJavaType.class)
        );
        // tinyint
        registerRelation(
                getJdbcType(Types.TINYINT),
                getJavaType(ShortJavaType.class)
        );
    }

    /**
     * 初始化 date types,如date
     */
    private void initDateTypes() {
        // ----------- date types -----------
        // timestamp
        registerRelation(
                getJdbcType(Types.TIMESTAMP),
                getJavaType(DateJavaType.class)
        );
        // time
        registerRelation(
                getJdbcType(Types.TIME),
                getJavaType(DateJavaType.class)
        );
        // date
        registerRelation(
                getJdbcType(Types.DATE),
                getJavaType(DateJavaType.class)
        );
        // time_with_timezone
        registerRelation(
                getJdbcType(Types.TIME_WITH_TIMEZONE),
                getJavaType(DateJavaType.class)
        );
        // timestamp_with_timezone
        registerRelation(
                getJdbcType(Types.TIMESTAMP_WITH_TIMEZONE),
                getJavaType(DateJavaType.class)
        );

    }

    /**
     * 初始化char types，如varchar
     */
    private void initCharTypes() {
        // ----------- char types -----------
        // varchar
        registerRelation(
                getJdbcType(Types.VARCHAR),
                getJavaType(StringJavaType.class)
        );
        registerRelation(
                getJdbcType(Types.VARCHAR),
                getJavaType(EnumJavaType.class)
        );
        // nvarchar
        registerRelation(
                getJdbcType(Types.NVARCHAR),
                getJavaType(StringJavaType.class)
        );
        // char
        registerRelation(
                getJdbcType(Types.CHAR),
                getJavaType(StringJavaType.class)
        );
        // nchar
        registerRelation(
                getJdbcType(Types.NCHAR),
                getJavaType(StringJavaType.class)
        );
        // longvarchar
        registerRelation(
                getJdbcType(Types.LONGVARCHAR),
                getJavaType(StringJavaType.class)
        );
        // longvarbinary
        registerRelation(
                getJdbcType(Types.LONGVARBINARY),
                getJavaType(StringJavaType.class)
        );
        // longnvarchar
        registerRelation(
                getJdbcType(Types.LONGNVARCHAR),
                getJavaType(StringJavaType.class)
        );
        // varbinary
        registerRelation(
                getJdbcType(Types.VARBINARY),
                getJavaType(StringJavaType.class)
        );
    }

    /**
     * init other types, like boolean
     */
    private void initOtherTypes() {
        registerRelation(
                getJdbcType(Types.BOOLEAN),
                getJavaType(BooleanJavaType.class)
        );
    }

    /**
     * 初始化复杂类型，如blob、clob
     */
    private void initComplexTypes() {
        // ----------- complex types -----------
        // blob
        registerRelation(
                getJdbcType(Types.BLOB),
                getJavaType(CharArrayJavaType.class)
        );
        // clob
        registerRelation(
                getJdbcType(Types.CLOB),
                getJavaType(CharArrayJavaType.class)
        );
        // nclob
        registerRelation(
                getJdbcType(Types.NCLOB),
                getJavaType(CharArrayJavaType.class)
        );
        // array
        registerRelation(
                getJdbcType(Types.ARRAY),
                getJavaType(ArrayJavaType.class)
        );
    }

    /**
     * 初始化未知的类型
     */
    private void initUnknownTypes() {
        // null
        registerRelation(
                getJdbcType(Types.NULL),
                getJavaType(UnknownJavaType.class)
        );
        // other
        registerRelation(
                getJdbcType(Types.OTHER),
                getJavaType(UnknownJavaType.class)
        );
        // distinct
        registerRelation(
                getJdbcType(Types.DISTINCT),
                getJavaType(UnknownJavaType.class)
        );
        // struct
        registerRelation(
                getJdbcType(Types.STRUCT),
                getJavaType(UnknownJavaType.class)
        );
        // ref
        registerRelation(
                getJdbcType(Types.REF),
                getJavaType(UnknownJavaType.class)
        );
        // datalink
        registerRelation(
                getJdbcType(Types.DATALINK),
                getJavaType(UnknownJavaType.class)
        );
        // sqlxml
        registerRelation(
                getJdbcType(Types.SQLXML),
                getJavaType(UnknownJavaType.class)
        );
        // ref_cursor
        registerRelation(
                getJdbcType(Types.REF_CURSOR),
                getJavaType(UnknownJavaType.class)
        );
    }


    /**
     * 注册java type
     *
     * @param javaType javaType INSTANCE
     */
    public void registerJavaType(JavaType<?> javaType) {
        javaTypes.put((Class<? extends JavaType<?>>) javaType.getClass(), javaType);
    }

    /**
     * 注册 jdbc type
     *
     * @param jdbcType jdbcType INSTANCE
     */
    public void registerJdbcType(JdbcType jdbcType) {
        jdbcTypes.put(jdbcType.getJdbcCode(), jdbcType);
    }

    /**
     * key = jdbcType
     * value = javaType
     *
     * @param jdbcType jdbc type
     * @param javaType java type
     */
    public void registerRelation(JdbcType jdbcType, JavaType<?> javaType) {
        jdbcTypesMappings.put(jdbcType.getJdbcCode(), javaType);
        javaTypeMappings.put(javaType, jdbcType.getJdbcCode());
    }

    /**
     * 获取jdbc types
     *
     * @return jdbcTypes
     */
    public Collection<JdbcType> getJdbcTypes() {
        return Collections.unmodifiableCollection(jdbcTypes.values());
    }

    /**
     * 根据jdbc code获取JdbcType实例
     *
     * @param code code
     * @return JdbcType
     * @throws IllegalArgumentException unknown jdbc code
     */
    public JdbcType getJdbcType(Integer code) {
        JdbcType jdbcType = jdbcTypes.get(code);
        if (jdbcType == null) {
            throw new IllegalArgumentException(String.format("unknown jdbc code %s", code));
        }
        return jdbcType;
    }

    /**
     * 获取java types
     *
     * @return javaTypes
     */
    public Collection<JavaType<?>> getJavaTypes() {
        return Collections.unmodifiableCollection(javaTypes.values());
    }

    /**
     * 根据 class获取JavaType实例
     *
     * @param classType classType
     * @return JavaType
     * @throws IllegalArgumentException unknown java type
     */
    public JavaType<?> getJavaTypeByClassType(Class<?> classType) {
        Collection<JavaType<?>> values = javaTypes.values();
        for (JavaType<?> javaType : values) {
            try {
                if (javaType.getJavaType().equals(classType)) {
                    return javaType;
                }
            } catch (UnsupportedOperationException ex) {
                // ignore
            }
        }
        throw new IllegalArgumentException(String.format("unknown java type %s", classType.getName()));

    }

    /**
     * 根据java type class获取JavaType实例
     *
     * @param javaTypeClass javaTypeClass
     * @return JavaType
     * @throws IllegalArgumentException unknown java type
     */
    public JavaType<?> getJavaType(Class<? extends JavaType<?>> javaTypeClass) {
        JavaType<?> javaType = javaTypes.get(javaTypeClass);
        if (javaType == null) {
            throw new IllegalArgumentException(String.format("unknown java type %s", javaTypeClass.getName()));
        }
        return javaType;
    }

    /**
     * 根据jdbc code查找出jdbc type实例
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
        Collection<JavaType<?>> types = jdbcTypesMappings.get(jdbcCode);
        if (CollectionUtils.isNotEmpty(types)) {
            return Lists.newArrayList(types).getFirst();
        }
        return null;
    }

    /**
     * 根据JavaType获取映射的JdbcType
     *
     * @param javaType javaType
     * @return List
     */
    public Collection<JdbcType> findJdbcTypeFindJavaType(JavaType<?> javaType) {
        Collection<Integer> jdbcCodes = javaTypeMappings.get(javaType);
        return jdbcCodes.stream().map(jdbcTypes::get).toList();
    }

    /**
     * 根据给定的值，推测它的java类型
     *
     * @param value value
     * @return JavaType
     */
    public JavaType<?> guessJavaType(Object value) {
        if (value == null) {
            return getJavaType(UnknownJavaType.class);
        }
        return guessJavaType(value.getClass());
    }

    /**
     * 根据给定的值，推测它的java类型
     *
     * @param valueType valueType
     * @return JavaType
     */
    public JavaType<?> guessJavaType(Class<?> valueType) {
        if (valueType == null) {
            return getJavaType(UnknownJavaType.class);
        }
        Collection<JavaType<?>> values = javaTypes.values();

        for (JavaType<?> javaType : values) {
            try {
                if (javaType.equalsTo(valueType)) {
                    return javaType;
                }
            } catch (Throwable ex) {
                // ignore
            }
        }
        // 判断是否为Enum
        if (valueType.isEnum()) {
            return getJavaType(EnumJavaType.class);
        }
        // 判断是否为bean
        if (cc.allio.uno.core.type.Types.isBean(valueType)) {
            return new BeanJavaType<>(valueType);
        }
        return getJavaType(UnknownJavaType.class);
    }

    /**
     * 根据给定的值，猜测属于的jdbctype
     *
     * @param value value
     * @return JdbcType
     */
    public Collection<JdbcType> guessJdbcType(Object value) {
        JavaType<?> javaType = guessJavaType(value);
        return findJdbcTypeFindJavaType(javaType);
    }

    /**
     * 根据给定的值，猜测属于的jdbctype
     *
     * @param valueType valueType
     * @return JdbcType
     */
    public Collection<JdbcType> guessJdbcType(Class<?> valueType) {
        JavaType<?> javaType = guessJavaType(valueType);
        return findJdbcTypeFindJavaType(javaType);
    }

    /**
     * 获取TypeRegistry实例
     *
     * @return TypeRegistry
     */
    public static TypeRegistry getInstance() {
        return INSTANCE;
    }

    // ----------------------------- static method -----------------------------

    /**
     * @see #getJdbcTypes()
     */
    public static Collection<JdbcType> obtainJdbcTypes() {
        return getInstance().getJdbcTypes();
    }

    /**
     * @see #getJdbcType(Integer)
     */
    public static JdbcType obtainJdbcType(Integer code) {
        return getInstance().getJdbcType(code);
    }

    /**
     * @see #getJavaTypes()
     */
    public static Collection<JavaType<?>> obtainJavaTypes() {
        return getInstance().getJavaTypes();
    }

    /**
     * @see #getJavaTypeByClassType(Class)
     */
    public static JavaType<?> obtainJavaTypeByClassType(Class<?> classType) {
        return getInstance().getJavaTypeByClassType(classType);
    }

    /**
     * @see #getJavaType(Class)
     */
    public static JavaType<?> obtainJavaType(Class<? extends JavaType<?>> javaTypeClass) {
        return getInstance().getJavaType(javaTypeClass);
    }

    /**
     * @see #findJavaType(Integer)
     */
    public static JavaType<?> obtainJavaType(Integer jdbcCode) {
        return getInstance().findJavaType(jdbcCode);
    }

    /**
     * @see #findJdbcTypeFindJavaType(JavaType)
     */
    public static Collection<JdbcType> obtainJdbcTypeFindJavaType(JavaType<?> javaType) {
        return getInstance().findJdbcTypeFindJavaType(javaType);
    }
}
