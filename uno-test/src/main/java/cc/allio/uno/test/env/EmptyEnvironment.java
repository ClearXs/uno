package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;

import java.lang.annotation.Annotation;

/**
 * 空Test Environment
 *
 * @author j.x
 * @since 1.1.4
 */
public class EmptyEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {

    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return null;
    }

}
