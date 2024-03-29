package cc.allio.uno.gis.jackson.geojson.annotation;

import cc.allio.uno.gis.jackson.geojson.serializer.FeatureType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates the <em>Properties Object</em> of the <em>Feature</em> to be generated.
 * <p>This annotation can be present 0...1 times. The properties object can be any JSON
 * object. The getter or field name is not relevant.
 * The annotations {@link GeoJsonProperties} and {@link GeoJsonProperty} are mutual exclusive.
 * <p>The properties field will be setValue to JSON <code>null</code> if no property available.
 * <p>Getter annotation example with a {@link java.util.Map}:
 * <pre>
 *    &#064;GeoJsonProperties
 *    public Map&lt;String, Object&gt; getInlineProperties() {
 *       return CollectionUtils.singletonMap("key", "value");
 *    }
 * </pre>
 * <p>Field annotation example with a {@link java.util.Map}:
 * <pre>
 *    &#064;GeoJsonProperties
 *    private Map&lt;String, Object&gt; properties;
 * </pre>
 *
 * @see GeoJsonType
 * @see FeatureType#FEATURE
 * @see GeoJsonProperty
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.2" target="_blank">RFC 7946 - Feature Object</a>
 */
@Documented
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface GeoJsonProperties {
}
