package cc.allio.uno.auto.service;

import java.lang.annotation.*;

/**
 * An annotation for service providers as described in {@link java.util.ServiceLoader}. The {@link
 * AutoServiceProcessor} generates the configuration files which
 * allows service providers to be loaded with {@link java.util.ServiceLoader#load(Class)}.
 *
 * <p>Service providers assert that they conform to the service provider specification.
 * Specifically, they must:
 *
 * <ul>
 * <li>be a non-inner, non-anonymous, concrete class
 * <li>have a publicly accessible no-arg constructor
 * <li>implement the interface type returned by {@code value()}
 * </ul>
 *
 * @author google
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AutoService {
	/**
	 * Returns the interfaces implemented by this service provider.
	 *
	 * @return interface array
	 */
	Class<?>[] value();
}
