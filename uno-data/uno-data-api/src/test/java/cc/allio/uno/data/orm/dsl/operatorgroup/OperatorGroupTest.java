package cc.allio.uno.data.orm.dsl.operatorgroup;

import cc.allio.uno.data.orm.dsl.MetaAcceptorSet;
import cc.allio.uno.data.orm.dsl.MetaAcceptorSetImpl;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OperatorGroupTest extends BaseTestCase {

    private MetaAcceptorSet metaAcceptorSet;

    @BeforeEach
    void init() {
        this.metaAcceptorSet = new MetaAcceptorSetImpl();
    }

    @Test
    void testObtainMetaAcceptor() {
        OperatorGroup operatorGroup = OperatorGroup.getOperatorGroup(OperatorKey.REDIS, metaAcceptorSet);
        InsertOperator<?> insert = operatorGroup.insert();
        assertNotNull(insert);
        MetaAcceptorSet selfMetaAcceptorSet = insert.obtainMetaAcceptorSet();
        assertNotNull(selfMetaAcceptorSet);
    }
}
