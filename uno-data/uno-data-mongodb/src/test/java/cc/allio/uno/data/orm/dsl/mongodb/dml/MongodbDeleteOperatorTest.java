package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class MongodbDeleteOperatorTest extends BaseTestCase {

    @Test
    void testEqDelete() {
        MongodbDeleteOperator deleteOperator = Operators.getDeleteOperator(MongodbDeleteOperator.class, OperatorKey.MONGODB);

        assertNotNull(deleteOperator);

        String dsl = deleteOperator.eq("a", "a").getDSL();

        assertEquals("{\"$and\": [{\"a\": \"a\"}]}", dsl);
    }

    @Test
    void testEqAndLtAndGtDelete() {
        MongodbDeleteOperator deleteOperator = Operators.getDeleteOperator(MongodbDeleteOperator.class, OperatorKey.MONGODB);

        assertNotNull(deleteOperator);
        String dsl = deleteOperator.eq("a", "a").lt("b", "b").gt("c", "c").getDSL();
        assertEquals("{\"$and\": [{\"a\": \"a\"}, {\"b\": {\"$lt\": \"b\"}}, {\"c\": {\"$gt\": \"c\"}}]}", dsl);
    }

    @Test
    void testLikeDelete() {
        MongodbDeleteOperator deleteOperator = Operators.getDeleteOperator(MongodbDeleteOperator.class, OperatorKey.MONGODB);
        assertNotNull(deleteOperator);

        String dsl = deleteOperator.like("a", "a").$like("b", "b").getDSL();
        assertEquals("{\"$and\": [{\"a\": {\"$regularExpression\": {\"pattern\": \"a\", \"options\": \"im\"}}}, {\"b\": {\"$regularExpression\": {\"pattern\": \"^b\", \"options\": \"im\"}}}]}", dsl);
    }

    @Test
    void testAlternateLogicalDelete() {
        MongodbDeleteOperator deleteOperator = Operators.getDeleteOperator(MongodbDeleteOperator.class, OperatorKey.MONGODB);
        assertNotNull(deleteOperator);

        String dsl = deleteOperator.eq("a", "a").or().eq("b", "b").nor().eq("c", "c").getDSL();
        assertEquals("{\"$nor\": [{\"$or\": [{\"$and\": [{\"a\": \"a\"}]}, {\"b\": \"b\"}]}, {\"c\": \"c\"}]}", dsl);
    }
}
