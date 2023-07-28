package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class DruidSQLUpdateOperatorTest extends BaseTestCase {

    DruidSQLUpdateOperator updateOperator = new DruidSQLUpdateOperator(DBType.H2);

    @Test
    void testUpdateWherePrepareValue() {
        updateOperator.eq("eq1", "eq1");
        updateOperator.update("t11", "t11");

        // 交替
        updateOperator.eq("eq2", "eq2");
        updateOperator.update("t12", "t12");

        // 循环10次 update
        for (int i = 0; i < 10; i++) {
            updateOperator.update("t12", "t12");
        }

        // 循环5次 eq
        for (int i = 0; i < 5; i++) {
            updateOperator.eq("eq2", "eq2");
        }

        // 循环10次 update
        for (int i = 0; i < 10; i++) {
            updateOperator.update("t12", "t12");
        }

        System.out.println(updateOperator.getPrepareValues());
    }
}
