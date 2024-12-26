package cc.allio.uno.netty.transport.body;

/**
 * screw
 * @author j.x
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
