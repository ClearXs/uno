package cc.allio.uno.test.mock;

/**
 * Boolean类型mock
 *
 * @author j.x
 */
public class BooleanMock implements Mock<Boolean> {
    @Override
    public Boolean getData() {
        return Boolean.TRUE;
    }
}
