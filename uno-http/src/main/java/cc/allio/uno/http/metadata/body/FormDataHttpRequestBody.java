package cc.allio.uno.http.metadata.body;

import org.springframework.http.MediaType;

/**
 * Media-Type -> multipart/form-data
 *
 * @author j.x
 * @since 1.1.0
 */
public class FormDataHttpRequestBody extends FormUrlencodedHttpRequestBody {

    @Override
    public MediaType getMediaType() {
        return MediaType.MULTIPART_FORM_DATA;
    }
}
