package cc.allio.uno.core.chain;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

/**
 * 默认链测试
 *
 * @author j.x
 * @since 1.0
 */
class DefaultChainTest extends BaseTestCase {

    /**
     * Test Case: 测试链中结点数据
     */
    @Test
    void testNodeCount() {
        DefaultChain<String, String> chain = new DefaultChain<>(Arrays.asList(new TestNode1(), new TestNode2()));
        assertEquals(2, chain.getNodes().size());
    }

    @Test
    void testNodeOrder() {
        TestNode1 node1 = new TestNode1();
        TestNode2 node2 = new TestNode2();
        DefaultChain<String, String> chain = new DefaultChain<>(Arrays.asList(node2, node1));
        List<? extends Node<String, String>> nodes = chain.getNodes();
        assertEquals(node1, nodes.get(0));
        assertEquals(node2, nodes.get(1));
    }

    @Test
    void testNodeContext() throws Throwable {
        TestNode1 node1 = new TestNode1();
        TestNode2 node2 = new TestNode2();
        DefaultChain<String, String> chain = new DefaultChain<>(Arrays.asList(node2, node1));
        TestChainContext context = new TestChainContext();
        chain.proceed(context)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void testErrorContinue() throws Throwable {
        ErrorNode errorNode = new ErrorNode();
        SetDefaultValueNode defaultValueNode = new SetDefaultValueNode();
        DefaultChain<String, String> chain = new DefaultChain<>(Arrays.asList(errorNode, defaultValueNode));
        TestChainContext context = new TestChainContext();
        chain.proceed(context).subscribe();
        assertEquals("default", context.getAttribute().get("test"));
    }

}
