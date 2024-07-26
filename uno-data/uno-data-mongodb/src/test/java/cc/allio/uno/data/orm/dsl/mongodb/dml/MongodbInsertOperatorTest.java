package cc.allio.uno.data.orm.dsl.mongodb.dml;

import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.util.function.Tuples;

class MongodbInsertOperatorTest extends BaseTestCase {

    @Test
    void testSimplyInsert() {
        MongodbInsertOperator insertOperator = Operators.getInsertOperator(MongodbInsertOperator.class, OperatorKey.MONGODB);
        String dsl = insertOperator.insert("a", "a", "b", "b").getDSL();
        assertEquals("[ {\n" +
                "  \"a\" : \"a\",\n" +
                "  \"b\" : \"b\"\n" +
                "} ]", dsl);
    }

    @Test
    void testBatchInsert() {

        MongodbInsertOperator insertOperator = Operators.getInsertOperator(MongodbInsertOperator.class, OperatorKey.MONGODB);
        for (int i = 0; i < 5; i++) {
            insertOperator.insert(Tuples.of("a" + i, "a" + i));
        }

        String dsl = insertOperator.getDSL();
        assertEquals("[ {\n" +
                "  \"a0\" : \"a0\"\n" +
                "}, {\n" +
                "  \"a1\" : \"a1\"\n" +
                "}, {\n" +
                "  \"a2\" : \"a2\"\n" +
                "}, {\n" +
                "  \"a3\" : \"a3\"\n" +
                "}, {\n" +
                "  \"a4\" : \"a4\"\n" +
                "} ]", dsl);
    }

    @Test
    void testStrictFill() {
        MongodbInsertOperator insertOperator = Operators.getInsertOperator(MongodbInsertOperator.class, OperatorKey.MONGODB);
        String dsl = insertOperator.insert("a", "a").strictFill("a", "b").getDSL();

        assertEquals("[ {\n" +
                "  \"a\" : \"b\"\n" +
                "} ]", dsl);
    }
}
