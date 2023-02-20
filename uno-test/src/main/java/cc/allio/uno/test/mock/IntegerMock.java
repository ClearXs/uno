package cc.allio.uno.test.mock;

/**
 * Integer类型mock对象
 *
 * @author jw
 * @date 2021/12/16 13:17
 */
public class IntegerMock implements Mock<Integer> {

    @Override
    public Integer getData() {
        // 默认返回1
        return 1;
    }
}
