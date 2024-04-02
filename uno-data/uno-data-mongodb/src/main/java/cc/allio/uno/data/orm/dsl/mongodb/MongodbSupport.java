package cc.allio.uno.data.orm.dsl.mongodb;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.Document;

import java.util.List;

/**
 * Help build mongodb application
 *
 * @author j.x
 * @date 2024/3/14 01:06
 * @since 1.1.7
 */
public final class MongodbSupport {

    /**
     * parse mongodb dsl syntax to {@link Document}
     *
     * @param dsl the dsl syntax
     * @return doc
     */
    public static List<Document> parse(String dsl) {
        List<Document> docs = Lists.newArrayList();
        JsonNode node = JsonUtils.readTree(dsl);
        if (node.isArray()) {
            for (JsonNode item : node) {
                String itemDoc = item.toString();
                Document doc = Document.parse(itemDoc);
                docs.add(doc);
            }
        } else {
            Document doc = Document.parse(dsl);
            docs.add(doc);
        }
        return docs;
    }

    /**
     * from {@link Document} to dsl syntax
     *
     * @param docs the docs
     * @return dsl
     */
    public static String toJson(List<Document> docs) {
        ArrayNode items = JsonUtils.arrayEmpty();
        for (Document doc : docs) {
            items.add(JsonUtils.readTree(doc.toJson()));
        }
        return items.toPrettyString();
    }

    /**
     * transfer v to bson value if value is complex type. such as {@link Object}, {@link java.util.Collection}, {@link java.util.Map}
     *
     * @param v the v
     * @return bson document value or raw v
     */
    public static Object toBsonValue(Object v) {
        if (v == null) {
            return null;
        }
        Class<?> valueType = v.getClass();
        if (Types.isBean(valueType) || Types.isMap(valueType)) {
            return BsonDocument.parse(JsonUtils.toJson(v));
        } else if (Types.isCollection(valueType) || Types.isArray(valueType)) {
            return BsonArray.parse(JsonUtils.toJson(v));
        }
        return v;
    }
}
