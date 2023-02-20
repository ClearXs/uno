package cc.allio.uno.data.orm.dialect;

/**
 * H2 dialect
 *
 * @author jiangwei
 * @date 2023/1/5 18:49
 * @since 1.1.4
 */
public class H2Dialect extends AbstractDialect {

    @Override
    public Version getVersion() {
        return null;
    }

    @Override
    protected void triggerInitFunc() {

    }

    @Override
    protected void triggerInitType() {

    }
}
