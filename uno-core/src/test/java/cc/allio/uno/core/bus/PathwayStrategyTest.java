package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

public class PathwayStrategyTest extends BaseTestCase {

    @Test
    void testDeduplicate() {
        String[] d1 = PathwayStrategy.SLASH.deduplicate(new String[]{"/", "/"});
        assertEquals(1, d1.length);

        String[] d2 = PathwayStrategy.SLASH.deduplicate(new String[]{"/", "/", "cc", "/", "/"});

        assertEquals(3, d2.length);

        String[] d3 = PathwayStrategy.SLASH.deduplicate(new String[]{"/", "/", "cc", "/", "/", "allio", "/", "/"});
        assertEquals(5, d3.length);

        String[] d4 = PathwayStrategy.SLASH.deduplicate(new String[]{"cc", "/", "/", "allio", "/"});
        assertEquals(4, d4.length);


        String[] d5 = PathwayStrategy.SLASH.deduplicate(new String[]{});
        assertEquals(0, d5.length);
    }

    @Test
    void testClipFirst() {
        var slashClip = PathwayStrategy.SLASH.clipFirst(new String[]{"cc", "allio"});

        assertEquals("", slashClip[0]);

        String[] dotClip = PathwayStrategy.DOT.clipFirst(new String[]{".", "cc", "allio"});
        assertEquals("cc", dotClip[0]);


        String[] underscoreClip = PathwayStrategy.UNDERSCORE.clipFirst(new String[]{"_", "cc", "allio"});
        assertEquals("cc", underscoreClip[0]);
    }

    @Test
    void testClipLast() {
        var slashClip = PathwayStrategy.SLASH.clipLast(new String[]{"cc", "allio", "/"});
        assertEquals(slashClip[slashClip.length - 1], "allio");
    }

    @Test
    void testDotTransformToSlash() {
        String t1 =
                PathwayStrategy.DOT.transformTo(PathwayStrategy.SLASH)
                        .apply("cc.allio");

        assertEquals("/cc/allio", t1);
    }

    @Test
    void testRequire() {
        PathwayStrategy dot = PathwayStrategy.require("cc.allio");
        assertEquals(dot, PathwayStrategy.DOT);

        PathwayStrategy slash = PathwayStrategy.require("cc/allio");
        assertEquals(slash, PathwayStrategy.SLASH);

        PathwayStrategy underscore = PathwayStrategy.require("cc_allio");
        assertEquals(underscore, PathwayStrategy.UNDERSCORE);

        PathwayStrategy dash = PathwayStrategy.require("cc-allio");
        assertEquals(dash, PathwayStrategy.DASH);


        PathwayStrategy space = PathwayStrategy.require("cc allio");
        assertEquals(space, PathwayStrategy.SPACE);

        PathwayStrategy singleForEmpty = PathwayStrategy.require("cc");
        assertEquals(singleForEmpty, PathwayStrategy.EMPTY);
    }

    @Test
    void testTransformSlash() {
        String dotSlash = PathwayStrategy.SLASH.transform("cc.allio");
        assertEquals("/cc/allio", dotSlash);

        String underscoreSlash = PathwayStrategy.SLASH.transform("cc_allio");
        assertEquals("/cc/allio", underscoreSlash);
    }

    @Test
    void testTransformDot() {
        String slashDot = PathwayStrategy.DOT.transform("cc/allio");
        assertEquals("cc.allio", slashDot);

        String underscoreDot = PathwayStrategy.DOT.transform("cc_allio");
        assertEquals("cc.allio", underscoreDot);
    }

    @Test
    void testTransformUnderscore() {
        String slashUnderscore = PathwayStrategy.UNDERSCORE.transform("cc/allio");
        assertEquals("cc_allio", slashUnderscore);

        String dotUnderscore = PathwayStrategy.UNDERSCORE.transform("cc.allio");
        assertEquals("cc_allio", dotUnderscore);
    }

}
