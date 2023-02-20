package cc.allio.uno.core.chain;

import com.google.common.collect.Maps;

import java.util.Map;

public class TestChainContext implements ChainContext<String> {

    private String in;

    private final Map<String, Object> attribute = Maps.newHashMap();

    public TestChainContext() {
        in = "test";
    }

    @Override
    public String getIN() {
        return in;
    }

    @Override
    public Map<String, Object> getAttribute() {
        return attribute;
    }
}
