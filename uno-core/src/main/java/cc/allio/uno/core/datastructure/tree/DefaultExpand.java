package cc.allio.uno.core.datastructure.tree;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class DefaultExpand implements Expand {

    private final Serializable id;
    private final Serializable parentId;

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Serializable getParentId() {
        return parentId;
    }
}
