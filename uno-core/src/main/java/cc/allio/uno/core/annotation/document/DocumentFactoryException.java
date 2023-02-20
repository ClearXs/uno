package cc.allio.uno.core.annotation.document;

/**
 * For exceptions thrown by a {@link DocumentFactory} implementation.
 */
public class DocumentFactoryException extends Exception {

	public DocumentFactoryException(String message) {
		super(message);
	}

	public DocumentFactoryException(String message, Throwable cause) {
		super(message, cause);
	}
}
