package cc.allio.uno.data.orm.executor.interceptor;

import cc.allio.uno.core.chain.DefaultChain;
import cc.allio.uno.core.chain.Node;

import java.util.List;


public class InterceptorChainImpl extends DefaultChain<InterceptorAttributes, InterceptorAttributes> implements InterceptorChain {

    public InterceptorChainImpl(List<? extends Node<InterceptorAttributes, InterceptorAttributes>> nodes) {
        super(nodes);
    }
}
