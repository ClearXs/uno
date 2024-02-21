package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.test.model.Operators;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cc.allio.uno.data.test.model.DataSets.*;

public class SQLAlterTableOperatorTest extends BaseTestCase {

    Operators.OperatorFeature<AlterTableOperator> feature;

    @BeforeEach
    void init() {
        AlterTableOperator alterTableOperator = OperatorGroup.getOperator(AlterTableOperator.class, OperatorKey.SQL);
        feature = Operators.feature(alterTableOperator);
    }

    @Test
    void testRename() {
        // test mysql

        // test pg
        feature.dbType(DBType.POSTGRESQL)
                .thenReset()
                .trigger(alterTableOperator -> {
                    String dsl = alterTableOperator.from(DUAL).rename("dual1").getDSL();
                    assertEquals("ALTER TABLE PUBLIC.dual\n" +
                            "\tRENAME TO dual1", dsl);
                });
    }

    @Test
    void testAddColumns() {
        feature.dbType(DBType.POSTGRESQL)
                .thenReset()
                .trigger(alterTableOperator -> {
                    String dsl = alterTableOperator.from(DUAL).addColumns(ID, CREATE_BY).getDSL();
                    assertEquals("ALTER TABLE PUBLIC.dual\n" +
                            "\tADD COLUMN id int8 PRIMARY KEY NOT NULL,\n" +
                            "\tADD COLUMN create_by int8", dsl);
                });

    }

    @Test
    void testDropColumns() {
        feature.dbType(DBType.POSTGRESQL)
                .thenReset()
                .trigger(alterTableOperator -> {
                    String dsl = alterTableOperator.from(DUAL).deleteColumns(ID.getDslName(), CREATE_BY.getDslName()).getDSL();
                    assertEquals("ALTER TABLE PUBLIC.dual\n" +
                            "\tDROP COLUMN id, create_by", dsl);
                });

    }

    @Test
    void testAlterColumns() {
        feature.dbType(DBType.POSTGRESQL)
                .thenReset()
                .trigger(alterTableOperator -> {
                    String dsl = alterTableOperator.from(DUAL).alertColumns(ID, CREATE_BY).getDSL();
                    assertEquals("ALTER TABLE PUBLIC.dual\n" +
                            "\tALTER COLUMN id int8 PRIMARY KEY NOT NULL,\n" +
                            "\tALTER COLUMN create_by int8", dsl);
                });
    }
}
