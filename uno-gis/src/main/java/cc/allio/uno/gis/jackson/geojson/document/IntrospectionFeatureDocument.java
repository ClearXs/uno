package cc.allio.uno.gis.jackson.geojson.document;

import cc.allio.uno.gis.jackson.geojson.crs.Crs;
import org.locationtech.jts.geom.Geometry;

/**
 * Default implementation of a {@link FeatureDocument}.
 */
public class IntrospectionFeatureDocument implements FeatureDocument {

    private final Object id;
    private final Geometry geometry;
    private final Object properties;
    private final Crs crs;
    private final double[] bbox;

    /**
     * Constructor
     */
    public IntrospectionFeatureDocument(Object id, Geometry geometry, Object properties, Crs crs, double[] bbox) {
        this.id = id;
        this.geometry = geometry;
        this.properties = properties;
        this.crs = crs;
        this.bbox = bbox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getProperties() {
        return properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Crs getCrs() {
        return crs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getBbox() {
        return bbox;
    }

}
