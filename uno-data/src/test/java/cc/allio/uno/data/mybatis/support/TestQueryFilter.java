package cc.allio.uno.data.mybatis.support;

import cc.allio.uno.core.util.JsonUtil;
import cc.allio.uno.data.mybatis.support.param.GenericQuery;

public interface TestQueryFilter {

    static GenericQuery mockGenericQuery(String hour, String action) {
        return JsonUtil.parse("{\n" +
                "    \"dataColumns\":[\"z\"],\n" +
                "    \"timeColumn\": \"mot\",\n" +
                "    \"setting\": {\n" +
                "        \"dataDilute\":{\n" +
                "            \"window\": " + "\"" + hour + "\"" + "," + "\n" +
                "            \"action\":" + "\"" + action + "\"" + "\n" +
                "        }\n" +
                "    }\n" +
                "}", GenericQuery.class);
    }
}
