package cc.allio.uno.component.sequential.wash;

import cc.allio.uno.component.sequential.TypeSequential;
import cc.allio.uno.component.sequential.washer.TypeWasher;
import cc.allio.uno.component.sequential.washer.WashMachine;
import cc.allio.uno.component.sequential.washer.WasherAssembler;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * 测试清洁装备器
 *
 * @author jiangwei
 * @date 2022/5/19 16:17
 * @since 1.0
 */
class WasherAssemblerTest extends BaseTestCase {

	/**
	 * Test Case: 测试没有装配清洁的数据
	 */
	@Test
	void testNoAssemblerItem() {
		assertThrows(IllegalArgumentException.class, () -> WasherAssembler.motherBoard().assembleDefault().install());
	}

	@Test
	void testAssignWasher() {
		WashMachine machine = WasherAssembler.motherBoard().pushItem(new TypeSequential()).assembleAssignWasher().install();
		boolean contains = machine.contains(TypeWasher.class);
		assertTrue(contains);
	}

	@Override
	protected void onInit() throws Throwable {

	}

	@Override
	protected void onDown() throws Throwable {

	}
}
