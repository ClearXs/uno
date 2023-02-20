package cc.allio.uno.test.mock;

/**
 * Long类型mock对象
 *
 * @author jw
 * @date 2021/12/16 13:18
 */
public class LongMock implements Mock<Long> {
    @Override
    public Long getData() {
        return 1L;
    }
}
