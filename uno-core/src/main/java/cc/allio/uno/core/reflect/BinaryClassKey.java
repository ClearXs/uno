package cc.allio.uno.core.reflect;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class BinaryClassKey {

    private final Class<?> cls1;
    private final Class<?> cls2;
}
