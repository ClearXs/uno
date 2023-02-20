package cc.allio.uno.gis.type;

import cc.allio.uno.gis.config.UnoGisMybatisAutoConfiguration;
import cc.allio.uno.gis.type.entity.Village;
import cc.allio.uno.gis.type.mapper.VillageMapper;
import cc.allio.uno.uno.test.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;

import java.util.List;

@RunMapperTest(mapperScan = TestScan.class)
@RunTest(components = UnoGisMybatisAutoConfiguration.class)
public class VillageTest extends BaseCoreTest {

    @Inject
    private VillageMapper mapper;

    @Test
    void testSelect() {
        List<Village> villages = mapper.selectList(Wrappers.emptyWrapper());
        System.out.println(villages);
    }
}
