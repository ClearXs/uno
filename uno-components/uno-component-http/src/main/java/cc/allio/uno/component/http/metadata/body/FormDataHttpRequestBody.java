package cc.allio.uno.component.http.metadata.body;

import org.springframework.http.MediaType;

/**
 * Media-Type -> multipart/form-data
 *
 * @author jiangwei
 * @date 2022/10/19 17:47
 * @since 1.1.0
 */
public class FormDataHttpRequestBody extends FormUrlencodedHttpRequestBody {

    @Override
    public MediaType getMediaType() {
        return MediaType.MULTIPART_FORM_DATA;
    }
}
