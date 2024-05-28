package cc.allio.uno.http.example;

import cc.allio.uno.http.metadata.HttpResponseMetadata;
import cc.allio.uno.http.metadata.HttpSwapper;
import org.junit.jupiter.api.Test;

public class GetRequestTest {

    @Test
    void testGet() {
        String res =
                HttpSwapper.build("http://192.168.5.42:8108/feign/user/selectByLoginName?loginName=admin")
                        .swap()
                        .flatMap(HttpResponseMetadata::expectString)
                        .block();
        System.out.println(res);
    }
}
