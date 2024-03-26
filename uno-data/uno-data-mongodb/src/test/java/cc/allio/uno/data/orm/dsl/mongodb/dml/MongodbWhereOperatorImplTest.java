package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.test.BaseTestCase;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.junit.jupiter.api.Test;


public class MongodbWhereOperatorImplTest extends BaseTestCase {

    @Test
    void testEq() {
        var filterAnd = Filters.and(Filters.eq("a", "1"));
        var filterOr = Filters.or(Filters.eq("b", "2"));


        new BsonDocument();



        String json = filterAnd.toBsonDocument().toJson();

        System.out.println(json);
    }
}
