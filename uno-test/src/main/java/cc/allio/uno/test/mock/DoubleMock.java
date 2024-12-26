package cc.allio.uno.test.mock;

/**
 * Double类型mock对象
 *
 * @author j.x
 */
public class DoubleMock implements Mock<Double> {
    @Override
    public Double getData() {
        return 1D;
    }
}
