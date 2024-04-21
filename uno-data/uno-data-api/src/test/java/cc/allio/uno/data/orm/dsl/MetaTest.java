package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

public class MetaTest extends BaseTestCase {

    private MetaAcceptorSet metaAcceptorSet;

    @BeforeEach
    void init() {
        this.metaAcceptorSet = new MetaAcceptorSetImpl();
    }

    @Test
    void testCreateMetaByArgs() {
        ConsumerDSLNameAcceptor dslNameAcceptor = new ConsumerDSLNameAcceptor(dslname -> dslname.setName("name1"));
        metaAcceptorSet.setDSLNameAcceptor(dslNameAcceptor);
        DSLName d1 = Meta.create(DSLName.class, metaAcceptorSet, "name");
        assertEquals("name1", d1.getName());

        // clear set DSLName acceptor
        metaAcceptorSet.clear();

        d1 = Meta.create(DSLName.class, metaAcceptorSet, "name");
        assertEquals("name", d1.getName());
    }

    @Test
    void testCreateColumnError() {
        assertThrows(NullPointerException.class, () -> Meta.create(ColumnDef.class, metaAcceptorSet));
    }

    public static class ConsumerDSLNameAcceptor implements MetaAcceptor<DSLName> {

        private final Consumer<DSLName> consumer;

        public ConsumerDSLNameAcceptor(Consumer<DSLName> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void onAccept(DSLName meta) {
            consumer.accept(meta);
        }
    }
}
