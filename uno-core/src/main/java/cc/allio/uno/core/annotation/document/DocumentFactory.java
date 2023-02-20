package cc.allio.uno.core.annotation.document;

/**
 * Implementing types can create {@link Document documents} from an annotated object.
 * <p>
 * The factory is interchangeable to enable mocking in tests and customizations.
 */
public interface DocumentFactory {

    /**
     * Creates a  {@link Document}
     *
     * @param object the object to introspect
     * @return one of {@link Document}
     * @throws DocumentFactoryException for missing annotations, wrong types, invalid combination of annotations and such
     */
    Document from(Object object) throws DocumentFactoryException;
}
