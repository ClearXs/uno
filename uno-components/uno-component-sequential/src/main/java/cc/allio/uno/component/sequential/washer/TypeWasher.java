package cc.allio.uno.component.sequential.washer;

import cc.allio.uno.component.sequential.Sequential;
import cc.allio.uno.core.util.StringUtils;
import com.google.auto.service.AutoService;

import java.util.function.Predicate;

@AutoService(Washer.class)
public class TypeWasher implements Washer {
    @Override
    public Predicate<Sequential> cleaning() {
        return sequential -> StringUtils.isNotEmpty(sequential.getType());
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public String getType() {
        return "type";
    }

    @Override
    public boolean contains(String type) {
        return "type".equals(type);
    }
}
