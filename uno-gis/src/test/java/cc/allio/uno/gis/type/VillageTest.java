package cc.allio.uno.gis.type;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.gis.config.UnoGisMybatisAutoConfiguration;
import cc.allio.uno.gis.type.entity.Village;
import cc.allio.uno.gis.type.mapper.VillageMapper;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;

import java.util.List;

@RunTest(components = UnoGisMybatisAutoConfiguration.class)
@MybatisPlusEnv(basePackages = "cc.allio.uno.gis.type.mapper.**")
public class VillageTest extends CoreTest {

    @Inject
    private VillageMapper mapper;

    @Test
    void testSelect() {
        List<Village> villages = mapper.selectList(Wrappers.emptyWrapper());
        System.out.println(villages);
    }
}
