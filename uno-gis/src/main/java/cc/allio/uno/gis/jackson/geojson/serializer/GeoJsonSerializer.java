package cc.allio.uno.gis.jackson.geojson.serializer;

import cc.allio.uno.core.annotation.document.Document;
import cc.allio.uno.core.annotation.document.DocumentFactory;
import cc.allio.uno.core.annotation.document.DocumentFactoryException;
import cc.allio.uno.gis.jackson.geojson.GeoJson;
import cc.allio.uno.gis.jackson.geojson.annotation.GeoJsonType;
import cc.allio.uno.gis.jackson.geojson.crs.Crs;
import cc.allio.uno.gis.jackson.geojson.document.FeatureCollectionDocument;
import cc.allio.uno.gis.jackson.geojson.document.FeatureDocument;
import cc.allio.uno.gis.jackson.geojson.document.GeometryCollectionDocument;
import cc.allio.uno.gis.jackson.serialization.GeometrySerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;


/**
 * A {@link JsonSerializer} implementation for classes annotated by {@link GeoJsonType}.
 * <p>The {@link GeoJsonType#type() type attribute} of the annotation defines the
 * output type of the <em>GeoJSON Object</em>.
 * <p>Please refer to {@link GeoJsonType} for a list of additional annotations per type.
 *
 * @see GeoJsonType
 * @see FeatureType
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3" target="_blank">RFC 7946 - GeoJSON Object</a>
 */
public class GeoJsonSerializer extends StdSerializer<Object> {

    /**
     * {@inheritDoc}
     */
    public GeoJsonSerializer() {
        super(Object.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Object object, JsonGenerator gen, SerializerProvider provider) throws IOException {

        // find annotation on object's class
        GeoJsonType geoJsonTypeAnnotation = object.getClass().getAnnotation(GeoJsonType.class);
        if (geoJsonTypeAnnotation == null) {
            throw new IllegalArgumentException("Annotation @GeoJson is not present.");
        }

        // getValue document factory from annotation
        Class<? extends DocumentFactory> factory = geoJsonTypeAnnotation.factory();
        DocumentFactory documentFactory;
        try {
            documentFactory = factory.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Factory instantiation failed for " + factory, e);
        }

        // call document factory to getValue a document representation of the annotations
        Document document;
        try {
            document = documentFactory.from(object);
        } catch (DocumentFactoryException e) {
            throw new JsonMappingException(gen, e.getMessage(), e);
        }

        // write document
        if (document instanceof FeatureDocument) {
            write(object, (FeatureDocument) document, gen, provider);
        } else if (document instanceof FeatureCollectionDocument) {
            write((FeatureCollectionDocument) document, gen);
        } else if (document instanceof GeometryCollectionDocument) {
            write((GeometryCollectionDocument) document, gen);
        } else {
            throw new IllegalStateException("Unsupported implementation of Document: " + document.getClass());
        }
    }

    /**
     * Writes a feature document.
     *
     * @param document the feature document
     * @param gen      the generator from {@link JsonSerializer}
     * @param provider the provider from {@link JsonSerializer}
     * @throws IOException for exceptions from the generator
     */
    private void write(Object o, FeatureDocument document, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(GeoJson.TYPE, GeoJson.FEATURE);
        Object id = document.getId();
        if (id != null) {
            gen.writeObjectField(GeoJson.ID, id);
        }
        double[] bbox = document.getBbox();
        if (bbox != null) {
            gen.writeObjectField(GeoJson.BBOX, bbox);
        }
        gen.writeFieldName(GeoJson.GEOMETRY);
        Geometry geometry = document.getGeometry();
        if (geometry != null) {
            new GeometrySerializer(o).serialize(geometry, gen, provider);
        } else {
            gen.writeNull();
        }
        Object properties = document.getProperties();
        if (null != properties) {
            gen.writeObjectField(GeoJson.PROPERTIES, properties);
        }
        Crs crs = document.getCrs();
        if (crs != null) {
            gen.writeObjectField(GeoJson.CRS, crs);
        }
        gen.writeEndObject();
    }

    /**
     * Writes a feature collection document.
     *
     * @param document the feature collection document
     * @param gen      the generator from {@link JsonSerializer}
     * @throws IOException for exceptions from the generator
     */
    private void write(FeatureCollectionDocument document, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(GeoJson.TYPE, GeoJson.FEATURE_COLLECTION);
        double[] bbox = document.getBbox();
        if (bbox != null) {
            gen.writeObjectField(GeoJson.BBOX, bbox);
        }
        gen.writeObjectField(GeoJson.FEATURES, document.getFeatures());
        gen.writeEndObject();
    }

    /**
     * Writes a geometry collection document.
     *
     * @param document the geometry collection document
     * @param gen      the generator from {@link JsonSerializer}
     * @throws IOException for exceptions from the generator
     */
    private void write(GeometryCollectionDocument document, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(GeoJson.TYPE, GeoJson.GEOMETRY_COLLECTION);
        gen.writeObjectField(GeoJson.GEOMETRIES, document.getGeometries());
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        this.serialize(value, gen, serializers);
    }
}
