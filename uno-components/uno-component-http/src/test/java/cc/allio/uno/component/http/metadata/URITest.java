package cc.allio.uno.component.http.metadata;

import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

public class URITest {

    @Test
    void testGetRequestHasDate() throws URISyntaxException, UnsupportedEncodingException {
        String uriTemplate = "/v1/start/{guid}";
        Map<String, Object> uriVars = Maps.newHashMap();
        uriVars.put("startTime", "2023-07-14 00:00:00");
        uriVars.put("endTime", "2023-07-14 00:00:00");
        uriVars.put("guid", "2023-07-14 00:00:00");
        String expandUri = StringUtils.joinUrl(uriTemplate, uriVars);

        URI uri = new URI(URLEncoder.encode(expandUri, "UTF-8"));
        System.out.println(uri);
    }
}
