package cc.allio.uno.gis.jackson.geojson.annotation;

import cc.allio.uno.core.annotation.BeanAnnotated;

import java.util.Collection;

import static java.util.Arrays.asList;

public class GeoJsonBeanAnnotated extends BeanAnnotated {

    /**
     * All annotations to search for on fields and methods (bean properties).
     */
    private static final Collection<Class<?>> annotationClasses = asList(
            GeoJsonId.class, GeoJsonBbox.class, GeoJsonCrs.class,
            GeoJsonGeometry.class, GeoJsonGeometries.class,
            GeoJsonProperty.class, GeoJsonProperties.class,
            GeoJsonFeature.class, GeoJsonFeatures.class, GeoJsonTransform.class);

    public GeoJsonBeanAnnotated(Collection<Class<?>> annotationClasses) {
        super(annotationClasses);
    }

    public static GeoJsonBeanAnnotated getInstance() {
        return new GeoJsonBeanAnnotated(annotationClasses);
    }
}

