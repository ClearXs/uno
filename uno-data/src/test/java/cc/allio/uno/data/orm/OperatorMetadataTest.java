package cc.allio.uno.data.orm;

import cc.allio.uno.data.orm.sql.DruidOperatorMetadata;
import cc.allio.uno.data.orm.sql.ElasticSearchOperatorMetadata;
import cc.allio.uno.data.orm.sql.OperatorMetadata;
import cc.allio.uno.data.orm.sql.SQLOperatorFactory;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class OperatorMetadataTest extends BaseTestCase {

    @Test
    void testTypeOfDataCorrectness() {
        OperatorMetadata drOperator = SQLOperatorFactory.getOperatorMetadata(OperatorMetadata.DRUID_OPERATOR_KEY);
        assertTrue(DruidOperatorMetadata.class.isAssignableFrom(drOperator.getClass()));
        OperatorMetadata esOperator = SQLOperatorFactory.getOperatorMetadata(OperatorMetadata.ELASTIC_SEARCH_KEY);
        assertTrue(ElasticSearchOperatorMetadata.class.isAssignableFrom(esOperator.getClass()));
    }
}
