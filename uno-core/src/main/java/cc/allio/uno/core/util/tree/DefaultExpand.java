package cc.allio.uno.core.util.tree;

import java.io.Serializable;

public record DefaultExpand(Serializable id, Serializable parentId) implements Expand {
    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Serializable getParentId() {
        return parentId;
    }
}
