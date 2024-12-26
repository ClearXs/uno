package cc.allio.uno.test.mock;

/**
 * Long类型mock对象
 *
 * @author j.x
 */
public class LongMock implements Mock<Long> {
    @Override
    public Long getData() {
        return 1L;
    }
}
