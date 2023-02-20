package cc.allio.uno.test.mock;

/**
 * Boolean类型mock
 *
 * @author jw
 * @date 2021/12/16 13:19
 */
public class BooleanMock implements Mock<Boolean> {
    @Override
    public Boolean getData() {
        return Boolean.TRUE;
    }
}
