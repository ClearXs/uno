package cc.allio.uno.http.metadata.body;

import cc.allio.uno.http.metadata.HttpRequestMetadata;
import com.google.common.collect.Lists;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Media-Type = application/x-www-form-urlencoded
 *
 * @author jiangwei
 * @date 2022/10/19 15:38
 * @since 1.1.0
 */
public class FormUrlencodedHttpRequestBody implements HttpRequestBody {

    @Override
    public BodyInserter<?, ? super ClientHttpRequest> getBody(HttpRequestMetadata requestMetadata) {
        Object body = requestMetadata.getBody();
        if (body instanceof Map) {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.putAll(
                    ((Map<?, ?>) body)
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> Lists.newArrayList(entry.getValue().toString())))
            );
            return BodyInserters.fromFormData(formData);
        }
        throw new IllegalArgumentException("Request Body Must 'Map'");
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.APPLICATION_FORM_URLENCODED;
    }
}
