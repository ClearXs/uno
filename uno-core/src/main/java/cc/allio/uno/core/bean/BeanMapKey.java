package cc.allio.uno.core.bean;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * bean map key，提高性能
 *
 * @author L.cm
 */
@EqualsAndHashCode
@AllArgsConstructor
public class BeanMapKey {
	private final Class type;
	private final int require;
}
