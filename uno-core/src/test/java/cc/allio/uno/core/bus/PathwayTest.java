package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.StringPool;
import org.junit.jupiter.api.Test;

public class PathwayTest extends BaseTestCase {

    @Test
    void testDeduplicate() {
        String[] d1 = Pathway.SLASH.deduplicate(StringPool.SLASH, new String[]{"/", "/"});
        assertEquals(1, d1.length);

        String[] d2 = Pathway.SLASH.deduplicate(StringPool.SLASH, new String[]{"/", "/", "cc", "/", "/"});

        assertEquals(3, d2.length);

        String[] d3 = Pathway.SLASH.deduplicate(StringPool.SLASH, new String[]{"/", "/", "cc", "/", "/", "allio", "/", "/"});
        assertEquals(5, d3.length);

        String[] d4 = Pathway.SLASH.deduplicate(StringPool.SLASH, new String[]{"cc", "/", "/", "allio", "/"});
        assertEquals(4, d4.length);


        String[] d5 = Pathway.SLASH.deduplicate(StringPool.SLASH, new String[]{});
        assertEquals(0, d5.length);
    }

    @Test
    void testClipFirst() {
        var slashClip = Pathway.SLASH.clipFirst(false, StringPool.EMPTY, new String[]{"cc", "allio"});

        assertEquals("", slashClip[0]);

        String[] dotClip = Pathway.DOT.clipFirst(false, StringPool.ORIGIN_DOT, new String[]{".", "cc", "allio"});
        assertEquals("cc", dotClip[0]);


        String[] underscoreClip = Pathway.UNDERSCORE.clipFirst(false, StringPool.UNDERSCORE, new String[]{"_", "cc", "allio"});
        assertEquals("cc", underscoreClip[0]);
    }

    @Test
    void testClipLast() {
        var s1 = Pathway.SLASH.clipLast(true, StringPool.SLASH, new String[]{"cc", "allio", "/"});
        assertEquals(s1[s1.length - 1], "allio");

        var s2 = Pathway.SLASH.clipLast(false, StringPool.SLASH, new String[]{"cc", "allio"});
        assertEquals(s2[s2.length - 1], "/");
    }

    @Test
    void testDotTransformToSlash() {
        String t1 =
                Pathway.DOT.transformTo(Pathway.SLASH)
                        .apply("cc.allio");

        assertEquals("/cc/allio", t1);
    }

    @Test
    void testRequire() {
        Pathway dot = Pathway.require("cc.allio");
        assertEquals(dot, Pathway.DOT);

        Pathway slash = Pathway.require("cc/allio");
        assertEquals(slash, Pathway.SLASH);

        Pathway underscore = Pathway.require("cc_allio");
        assertEquals(underscore, Pathway.UNDERSCORE);

        Pathway dash = Pathway.require("cc-allio");
        assertEquals(dash, Pathway.DASH);


        Pathway space = Pathway.require("cc allio");
        assertEquals(space, Pathway.SPACE);

        Pathway singleForEmpty = Pathway.require("cc");
        assertEquals(singleForEmpty, Pathway.EMPTY);
    }

    @Test
    void testTransformSlash() {
        String dotSlash = Pathway.SLASH.transform("cc.allio");
        assertEquals("/cc/allio", dotSlash);

        String underscoreSlash = Pathway.SLASH.transform("cc_allio");
        assertEquals("/cc/allio", underscoreSlash);
    }

    @Test
    void testTransformDot() {
        String slashDot = Pathway.DOT.transform("cc/allio");
        assertEquals("cc.allio", slashDot);

        String underscoreDot = Pathway.DOT.transform("cc_allio");
        assertEquals("cc.allio", underscoreDot);
    }

    @Test
    void testTransformUnderscore() {
        String slashUnderscore = Pathway.UNDERSCORE.transform("cc/allio");
        assertEquals("cc_allio", slashUnderscore);

        String dotUnderscore = Pathway.UNDERSCORE.transform("cc.allio");
        assertEquals("cc_allio", dotUnderscore);
    }

    @Test
    void testOriginDuplicate() {
        String origin = "s//s";
        String transform = Pathway.SLASH.transform(origin);
        assertEquals("/s/s", transform);

    }

    @Test
    void testEmpty() {
        String r1 = Pathway.EMPTY.transform("s");

        assertEquals("s", r1);
    }
}
