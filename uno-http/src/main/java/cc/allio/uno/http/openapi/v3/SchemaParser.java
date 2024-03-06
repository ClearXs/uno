package cc.allio.uno.http.openapi.v3;

import cc.allio.uno.http.openapi.Parser;
import cc.allio.uno.http.openapi.ParserContext;
import cc.allio.uno.core.util.StringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.swagger.v3.oas.models.media.*;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * 通过jackson解析成OpenApi时，无法把所有schema对象都尽可能生成。
 * 比如说ArraySchema，ComposedSchema等对象无法解析。因此创建Schema解析目的在于怎么解析这些
 *
 * @author jw
 * @date 2021/12/5 9:52
 */
public class SchemaParser implements Parser<Schema<?>> {

    @Override
    public void init(ParserContext context) {
        context.module().addDeserializer(Schema.class, new SchemaDeserializer(context));
    }

    @Override
    public void preParse(ParserContext context) {
        // TODO
    }

    @Override
    public Schema<?> parse(String unresolved, ParserContext context) {
        return context.serializer().deserialize(unresolved.getBytes(), Schema.class);
    }

    static class SchemaDeserializer extends StdDeserializer<Schema<?>> {

        private final ParserContext parserContext;

        private final String[] composedSchemaName = {"allOf", "anyOf", "oneOf"};

        public SchemaDeserializer(ParserContext parserContext) {
            this(null, parserContext);
        }

        protected SchemaDeserializer(Class<?> vc, ParserContext parserContext) {
            super(vc);
            this.parserContext = parserContext;
        }

        @Override
        public Schema<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TreeNode node = p.getCodec().readTree(p);
            if (node == null) {
                return new StringSchema();
            }
            Class<? extends Schema> schema = findSchema(node);
            if (schema.equals(MapSchema.class)) {
                MapSchema mapSchema = new MapSchema();
                TreeNode additionalProperties = node.get("additionalProperties");
                if (additionalProperties != null) {
                    Class<? extends Schema> need = findSchema(additionalProperties);
                    Schema deserialize = parserContext.serializer().deserialize(additionalProperties.toString().getBytes(), need);
                    mapSchema.setAdditionalProperties(deserialize);
                }
                return mapSchema;
            }
            if (schema.equals(ArraySchema.class)) {
                ArraySchema arraySchema = new ArraySchema();
                TreeNode item = node.get("item");
                if (item != null) {
                    Class<? extends Schema> need = findSchema(item);
                    Schema deserialize = parserContext.serializer().deserialize(item.toString().getBytes(), need);
                    arraySchema.setItems(deserialize);
                }
                return arraySchema;
            }
            return parserContext.serializer().deserialize(node.toString().getBytes(), schema);
        }


        /**
         * 寻找json串中是否含有组合的schema，组合的schema包含oneOf、anyOf、allOf
         * 在这些组合串中只会存在一个
         *
         * @param node 当前schema节点
         * @return 如果存在返回ComposedSchema，否则返回null
         */
        private Class<ComposedSchema> findArraySchema(TreeNode node) {
            try {
                Iterator<String> fieldNames = node.fieldNames();
                while (fieldNames.hasNext()) {
                    String filedName = fieldNames.next();
                    if (Arrays.asList(composedSchemaName).contains(filedName)) {
                        return ComposedSchema.class;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private Class<? extends Schema> findSchema(TreeNode node) {
            Class<ComposedSchema> composedSchema = findArraySchema(node);
            if (composedSchema != null) {
                return composedSchema;
            }
            return SchemaProvider.find(tuple2 -> {
                String t1 = tuple2.getT1();
                String t2 = tuple2.getT2();
                return Predicate.isEqual(t1).test(simpleString(node, "type"))
                        && Predicate.isEqual(t2).test(simpleString(node, "format"));
            });
        }

        public String simpleString(TreeNode node, String fieldName) {
            TreeNode result = node.get(fieldName);
            if (result == null) {
                return "";
            }
            String simple = result.toString();
            if (StringUtils.isBlank(simple)) {
                return "";
            }
            return simple.substring(1, simple.length() - 1);
        }
    }

    private static class SchemaProvider {

        private SchemaProvider() {
        }

        private static final Map<Tuple2<String, String>, Class<? extends Schema<?>>> SCHEMA_FACTORY = new HashMap<>();

        static {
            SCHEMA_FACTORY.put(Tuples.of("array", ""), ArraySchema.class);
            SCHEMA_FACTORY.put(Tuples.of("string", "binary"), BinarySchema.class);
            SCHEMA_FACTORY.put(Tuples.of("boolean", ""), BooleanSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("string", "byte"), ByteArraySchema.class);
            SCHEMA_FACTORY.put(Tuples.of("", ""), StringSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("string", "date-time"), DateTimeSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("string", "email"), EmailSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("string", "int32"), IntegerSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("object", ""), MapSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("number", ""), NumberSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("string", ""), StringSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("string", "password"), PasswordSchema.class);
            SCHEMA_FACTORY.put(Tuples.of("string", "uuid"), UUIDSchema.class);
        }

        public static Class<? extends Schema> find(Predicate<Tuple2<String, String>> judge) {
            Optional<? extends Class<? extends Schema<?>>> optional = SCHEMA_FACTORY.keySet().stream().map(key -> {
                        if (judge.test(key)) {
                            return SCHEMA_FACTORY.get(key);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .findFirst();
            boolean present = optional.isPresent();
            if (present) {
                return optional.get();
            }
            return Schema.class;
        }
    }
}
