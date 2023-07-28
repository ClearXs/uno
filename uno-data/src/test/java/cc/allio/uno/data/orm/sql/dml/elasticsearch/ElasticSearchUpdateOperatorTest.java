package cc.allio.uno.data.orm.sql.dml.elasticsearch;

import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.data.orm.Bank;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ElasticSearchUpdateOperatorTest extends BaseTestCase {

    private final ElasticSearchUpdateOperator updateOperator = new ElasticSearchUpdateOperator();

    @Test
    void testUpdateForEQ() {
        Bank bank = JsonUtils.parse("{\n" +
                "          \"account_number\" : 37,\n" +
                "          \"balance\" : 18612,\n" +
                "          \"firstname\" : \"Mcgee\",\n" +
                "          \"lastname\" : \"Mooney\",\n" +
                "          \"age\" : 39,\n" +
                "          \"gender\" : \"M\",\n" +
                "          \"address\" : \"826 Fillmore Place\",\n" +
                "          \"employer\" : \"Reversus\",\n" +
                "          \"email\" : \"mcgeemooney@reversus.com\",\n" +
                "          \"city\" : \"Tooleville\",\n" +
                "          \"state\" : \"OK\"\n" +
                "        }", Bank.class);
        bank.setAge(33);
        String sql = updateOperator.from("bank").eq(Bank::getFirstname, "firstname").updatePojo(bank).getSQL();
        assertEquals("{\"query\":{\"bool\":{\"must\":[{\"match\":{\"firstname\":{\"query\":\"firstname\"}}}]}},\"script\":{\"params\":{\"firstname\":\"Mcgee\",\"address\":\"826 Fillmore Place\",\"gender\":\"M\",\"balance\":18612,\"city\":\"Tooleville\",\"employer\":\"Reversus\",\"state\":\"OK\",\"accountNumber\":0,\"email\":\"mcgeemooney@reversus.com\",\"age\":33,\"lastname\":\"Mooney\"},\"source\":\"ctx._source['city'] = params['city'];ctx._source['accountNumber'] = params['accountNumber'];ctx._source['lastname'] = params['lastname'];ctx._source['state'] = params['state'];ctx._source['firstname'] = params['firstname'];ctx._source['address'] = params['address'];ctx._source['balance'] = params['balance'];ctx._source['employer'] = params['employer'];ctx._source['gender'] = params['gender'];ctx._source['age'] = params['age'];ctx._source['email'] = params['email']\"}}", sql);
    }
}
