package cc.allio.uno.data.mybatis.query.mapper;

import cc.allio.uno.data.mybatis.support.TestMapperScan;
import cc.allio.uno.data.mybatis.support.mapper.RiHisMapper;
import cc.allio.uno.data.mybatis.support.param.GenericQuery;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.ResourceSqlExecutor;
import cc.allio.uno.data.mybatis.UnoDataMybatisAutoConfiguration;
import cc.allio.uno.data.model.RiHis;
import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunMapperTest;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.MybatisPlusTestEnvironment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;

import java.util.Collection;

@RunMapperTest(mapperScan = TestMapperScan.class)
@RunTest(active = "mybatis", envs = @RunTest.Environment(env = MybatisPlusTestEnvironment.class), components = UnoDataMybatisAutoConfiguration.class)
class QueryMapperTest extends BaseCoreTest {

    @Inject
    private RiHisMapper mapper;

    @Inject
    private SqlSessionFactory factory;

    @Test
    void testCollector() throws Throwable {
        init();
        QueryFilter queryFilter = new GenericQuery().setStcd("4173630015").setDataColumns(new String[]{"z"}).setTimeColumn("mot").build();

        Collection<RiHis> riHis = mapper.queryList(queryFilter);

    }

    void init() throws Throwable {
        ResourceSqlExecutor.executeSql(ResourceSqlExecutor.H2, "ri.sql", factory);
    }
}
