package cc.allio.uno.component.netty.transport.body;

/**
 * screw
 * @author jiangw
 * @date 2020/12/22 17:28
 * @since 1.0
 */
public abstract class AbstractBody implements Body {

    private Object attached;

    public void attachment(Object attached) {
        this.attached = attached;
    }

    public Object getAttached() {
        return attached;
    }
}
