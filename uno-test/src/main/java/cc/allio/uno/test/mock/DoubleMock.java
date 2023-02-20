package cc.allio.uno.test.mock;

/**
 * Double类型mock对象
 *
 * @author jw
 * @date 2021/12/16 13:18
 */
public class DoubleMock implements Mock<Double> {
    @Override
    public Double getData() {
        return 1D;
    }
}
