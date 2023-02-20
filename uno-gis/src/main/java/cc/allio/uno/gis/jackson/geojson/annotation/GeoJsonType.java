package cc.allio.uno.gis.jackson.geojson.annotation;

import cc.allio.uno.core.annotation.document.DocumentFactory;
import cc.allio.uno.gis.jackson.geojson.document.FeatureDocumentFactory;
import cc.allio.uno.gis.jackson.geojson.serializer.FeatureType;
import cc.allio.uno.gis.jackson.geojson.serializer.GeoJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enables the annotated type to be serialized as a <em>GeoJson Object</em> by the {@link GeoJsonSerializer}.
 * <p>Please note that the {@link GeoJsonSerializer} needs to be set with the {@link JsonSerialize} annotation.
 * <p>Depending on the type, further annotations on fields or getters are complementary.
 * <p>{@link FeatureType#FEATURE}
 * <ul>
 * <li>{@link GeoJsonId} {0,1}</li>
 * <li>{@link GeoJsonGeometry} {0,1}</li>
 * <li>{@link GeoJsonProperties} {0,1} <strong>or</strong> {@link GeoJsonProperty} {0,}</li>
 * </ul>
 * <p>{@link FeatureType#FEATURE_COLLECTION}
 * <ul>
 * <li>{@link GeoJsonFeatures} {0,1} <strong>or</strong> {@link GeoJsonFeature} {0,}</li>
 * </ul>
 * <p>{@link FeatureType#GEOMETRY_COLLECTION} is not supported yet.
 * <p>Example for type <em>Feature</em>:
 * <pre>
 *    &#064;GeoJson(type = GeoJsonType.FEATURE)
 *    &#064;JsonSerialize(using = GeoJsonSerializer.class)
 *    public class TestEntity {
 *
 *    }
 * </pre>
 * The output of the above example will look like:
 * <pre>
 *    {
 *       "type": "Feature",
 *       "geometry": null,
 *       "properties": null
 *    }
 * </pre>
 *
 * @see FeatureType
 * @see JsonSerialize
 * @see GeoJsonSerializer
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3" target="_blank">RFC 7946 - GeoJSON Object</a>
 */
@Inherited
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface GeoJsonType {

	/**
	 * Returns the {@link FeatureType} to serialize to.
	 *
	 * @return the {@link FeatureType}
	 */
	FeatureType type();

	/**
	 * Return the {@link DocumentFactory} to use.
	 *
	 * @return the {@link DocumentFactory}, or the default if not specified
	 * @see FeatureDocumentFactory
	 */
	Class<? extends DocumentFactory> factory() default FeatureDocumentFactory.class;
}
