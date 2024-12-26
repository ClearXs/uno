package cc.allio.uno.test.mock;

/**
 * Integer类型mock对象
 *
 * @author j.x
 */
public class IntegerMock implements Mock<Integer> {

    @Override
    public Integer getData() {
        // 默认返回1
        return 1;
    }
}
