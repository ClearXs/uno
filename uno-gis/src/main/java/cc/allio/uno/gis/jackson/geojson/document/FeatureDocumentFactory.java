package cc.allio.uno.gis.jackson.geojson.document;

import cc.allio.uno.gis.jackson.geojson.crs.Crs;
import cc.allio.uno.core.annotation.*;
import cc.allio.uno.core.annotation.document.Document;
import cc.allio.uno.core.annotation.document.DocumentFactory;
import cc.allio.uno.core.annotation.document.DocumentFactoryException;
import cc.allio.uno.core.annotation.document.IntrospectionDocumentFactory;
import cc.allio.uno.gis.jackson.geojson.annotation.*;
import com.google.common.collect.ListMultimap;
import org.locationtech.jts.geom.Geometry;

import java.lang.annotation.Annotation;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Default implementation of a {@link DocumentFactory} that introspects the
 * object's class for annotations and retrieves the values of the annotated
 * fields and methods.
 */
public class FeatureDocumentFactory extends IntrospectionDocumentFactory {

    /**
     * Constructor for reflection.
     */
    public FeatureDocumentFactory() {
        // for reflection only
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document from(Object object) throws DocumentFactoryException {
        if (object == null) {
            throw new DocumentFactoryException("Object is null.");
        }
        GeoJsonType geoJsonTypeAnnotation = object.getClass().getAnnotation(GeoJsonType.class);
        if (geoJsonTypeAnnotation == null) {
            throw new DocumentFactoryException("Annotation @GeoJson is not present.");
        }
        switch (geoJsonTypeAnnotation.type()) {
            case FEATURE:
                return featureFrom(object);
            case FEATURE_COLLECTION:
                return featureCollectionFrom(object);
            case GEOMETRY_COLLECTION:
                return geometryCollectionFrom(object);
            default:
                throw new DocumentFactoryException("Unsupported GeoJsonType: " + geoJsonTypeAnnotation.type());
        }
    }

    /**
     * Returns a feature document representation of the object.
     *
     * @param object the object
     * @return the feature document
     * @throws DocumentFactoryException on any error
     */
    private FeatureDocument featureFrom(Object object) throws DocumentFactoryException {
        ListMultimap<Class<? extends Annotation>, Annotated> index = GeoJsonBeanAnnotated.getInstance().index(object.getClass());

        Object id = null;
        Geometry geometry = null;
        Object properties = null;
        Crs crs = null;
        double[] bbox = null;

        Annotated idAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonId.class);
        if (idAnnotated != null) {
            id = idAnnotated.getValue(object, Object.class);
        }

        Annotated crsAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonCrs.class);
        if (crsAnnotated != null) {
            crs = crsAnnotated.getValue(object, Crs.class);
        }

        Annotated bboxAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonBbox.class);
        if (bboxAnnotated != null) {
            bbox = bboxAnnotated.getValue(object, double[].class);
        }

        Annotated geometryAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonGeometry.class);
        if (geometryAnnotated != null) {
            geometry = geometryAnnotated.getValue(object, Geometry.class);
        }

        Annotated propertiesAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonProperties.class);
        List<Annotated> propertyAnnotated = index.get(GeoJsonProperty.class);
        if (propertiesAnnotated != null && !propertyAnnotated.isEmpty()) {
            // both, @GeoJsonProperties and @GeoJsonProperty are present
            String descriptions = propertiesAnnotated.getDescription() + ", " +
                    propertyAnnotated.stream().map(Annotated::getDescription).collect(joining(", "));
            throw new DocumentFactoryException("Annotations @GeoJsonProperties and @GeoJsonProperty are mutually exclusive: " + descriptions);
        } else if (propertiesAnnotated != null) {
            // one @GeoJsonProperties
            properties = propertiesAnnotated.getValue(object, Object.class);
        } else if (!propertyAnnotated.isEmpty()) {
            // one or more @GeoJsonProperty
            properties = toProperties(object, propertyAnnotated);
        }

        return new IntrospectionFeatureDocument(id, geometry, properties, crs, bbox);
    }

    /**
     * Returns the values of all annotated field or methods as a {@link Map}.
     * The key of the map is the name of the field or method as default. When the annotation's attribute
     * {@link GeoJsonProperty#name()} is set, this name will be used as key.
     *
     * @param object     the object to retrieve the values from
     * @param annotateds the fields or methods
     * @return the map of properties, may be empty but never <code>null</code>
     * @throws DocumentFactoryException on any error
     */
    private Map<String, Object> toProperties(Object object, List<Annotated> annotateds) throws DocumentFactoryException {
        Map<String, Object> properties = new HashMap<>(annotateds.size());
        for (Annotated annotated : annotateds) {
            GeoJsonProperty annotation = findAnnotation(annotated, GeoJsonProperty.class);
            String name;
            if (!isBlank(annotation.name())) {
                name = annotation.name();
            } else {
                name = annotated.getName();
            }
            Object value = annotated.getValue(object, Object.class);
            properties.put(name, value);
        }
        return properties;
    }

    /**
     * Returns the first occurrence of an annotation of a certain type from the annotated.
     *
     * @param annotated      the annotated field or method
     * @param annotationType the annotation type
     * @return the annotation, or <code>null</code> if not found
     */
    private <T extends Annotation> T findAnnotation(Annotated annotated, Class<T> annotationType) {
        Annotation annotation = annotated.getAnnotations().stream()
                .filter(a -> a.annotationType().equals(annotationType))
                .findFirst().orElse(null);
        return annotationType.cast(annotation);
    }

    /**
     * Returns a feature collection document representation of the object.
     *
     * @param object the object
     * @return the feature collection document
     * @throws DocumentFactoryException on any error
     */
    private FeatureCollectionDocument featureCollectionFrom(Object object) throws DocumentFactoryException {
        ListMultimap<Class<? extends Annotation>, Annotated> index = GeoJsonBeanAnnotated.getInstance().index(object.getClass());

        double[] bbox = null;
        Annotated bboxAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonBbox.class);
        if (bboxAnnotated != null) {
            bbox = bboxAnnotated.getValue(object, double[].class);
        }

        Annotated featuresAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonFeatures.class);
        List<Annotated> featureAnnotated = index.get(GeoJsonFeature.class);
        if (featuresAnnotated != null && !featureAnnotated.isEmpty()) {
            // both, @GeoJsonFeatures and @GeoJsonFeature are present
            String descriptions = featuresAnnotated.getDescription() + ", " +
                    featureAnnotated.stream().map(Annotated::getDescription).collect(joining(", "));
            throw new DocumentFactoryException("Annotations @GeoJsonFeatures and @GeoJsonFeature are mutually exclusive: " + descriptions);
        } else if (featuresAnnotated != null) {
            // one @GeoJsonFeatures
            return new IntrospectionFeatureCollectionDocument(toFeatures(object, featuresAnnotated), bbox);
        } else if (!featureAnnotated.isEmpty()) {
            // one or more @GeoJsonFeature
            return new IntrospectionFeatureCollectionDocument(toFeatures(object, featureAnnotated), bbox);
        }
        return new IntrospectionFeatureCollectionDocument(emptyList(), bbox);
    }

    private List<Object> toFeatures(Object object, Annotated annotated) throws DocumentFactoryException {
        Object value = annotated.getValue(object, Object.class);
        if (value == null) {
            return emptyList();
        } else if (value instanceof Object[]) {
            return asList(((Object[]) value));
        } else if (value instanceof Collection) {
            return new ArrayList<>((Collection<?>) value);
        } else {
            throw new DocumentFactoryException("Value of " + annotated.getDescription() + " is not an Array or Collection.");
        }
    }

    private List<Object> toFeatures(Object object, List<Annotated> annotateds) throws DocumentFactoryException {
        List<Object> features = new ArrayList<>();
        for (Annotated annotated : annotateds) {
            Object feature = annotated.getValue(object, Object.class);
            if (feature != null) {
                features.add(feature);
            }
        }
        return features;
    }

    /**
     * Returns a geometry collection document representation of the object.
     *
     * @param object the object
     * @return the geometry collection document
     * @throws DocumentFactoryException on any error
     */
    private GeometryCollectionDocument geometryCollectionFrom(Object object) throws DocumentFactoryException {
        ListMultimap<Class<? extends Annotation>, Annotated> index = GeoJsonBeanAnnotated.getInstance().index(object.getClass());

        Annotated geometriesAnnotated = GeoJsonBeanAnnotated.getInstance().oneOrNull(index, GeoJsonGeometries.class);
        List<Annotated> geometryAnnotated = index.get(GeoJsonGeometry.class);
        if (geometriesAnnotated != null && !geometryAnnotated.isEmpty()) {
            // both, @GeoJsonGeometries and @GeoJsonGeometry are present
            String descriptions = geometriesAnnotated.getDescription() + ", " +
                    geometryAnnotated.stream().map(Annotated::getDescription).collect(joining(", "));
            throw new DocumentFactoryException("Annotations @GeoJsonGeometries and @GeoJsonGeometry are mutually exclusive: " + descriptions);
        } else if (geometriesAnnotated != null) {
            // one @GeoJsonGeometries
            return new IntrospectionGeometryCollectionDocument(toGeometries(object, geometriesAnnotated));
        } else if (!geometryAnnotated.isEmpty()) {
            // one or more @GeoJsonGeometry
            return new IntrospectionGeometryCollectionDocument(toGeometries(object, geometryAnnotated));
        }
        return new IntrospectionGeometryCollectionDocument(emptyList());
    }

    private List<Geometry> toGeometries(Object object, Annotated annotated) throws DocumentFactoryException {
        Object value = annotated.getValue(object, Object.class);
        if (value == null) {
            return emptyList();
        } else if (value instanceof Geometry[]) {
            return asList(((Geometry[]) value));
        } else if (value instanceof Collection) {
            List<Geometry> geometries = new ArrayList<>();
            for (Object element : ((Collection) value)) {
                if (element instanceof Geometry) {
                    geometries.add((Geometry) element);
                } else {
                    throw new DocumentFactoryException("Value of " + annotated.getDescription() + " is not an Array or Collection of type Geometry.");
                }
            }
            return geometries;
        } else {
            throw new DocumentFactoryException("Value of " + annotated.getDescription() + " is not an Array or Collection.");
        }
    }

    private List<Geometry> toGeometries(Object object, List<Annotated> annotateds) throws DocumentFactoryException {
        List<Geometry> geometries = new ArrayList<>();
        for (Annotated annotated : annotateds) {
            Geometry geometry = annotated.getValue(object, Geometry.class);
            if (geometry != null) {
                geometries.add(geometry);
            }
        }
        return geometries;
    }
}
