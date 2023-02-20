package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.util.JsonUtil;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.model.RiReal;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestDataTimeStream<T> extends ArrayList<T> implements CollectionTimeStream<T> {

    @Override
    public Flux<T> read(QueryFilter queryFilter) throws Throwable {
        return Flux.fromIterable(this);
    }

    public void more() {
        List<RiReal> riReals = JsonUtil.readList("[{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 23:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.274,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 23:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.800,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 23:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.132,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 22:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.156,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 22:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.791,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 22:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.276,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 21:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.125,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 21:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.773,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 21:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.279,\"d\":null,\"wptn\":\"KEEP\"},{\"id\":null,\"createUser\":null,\"createDept\":null,\"createTime\":null,\"updateUser\":null,\"updateTime\":null,\"isDeleted\":null,\"stcd\":null,\"stId\":null,\"sttp\":null,\"mot\":\"2022-10-01 20:00:00\",\"alarmStatus\":null,\"commStatus\":null,\"z\":0.284,\"d\":null,\"wptn\":\"KEEP\"}]", RiReal.class);
        addAll((Collection<? extends T>) riReals);
    }
}
