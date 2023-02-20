package cc.allio.uno.gis.jackson.geojson.document;

import java.util.List;

/**
 * Default implementation of a {@link FeatureCollectionDocument}.
 */
public class IntrospectionFeatureCollectionDocument implements FeatureCollectionDocument {

	private final List<Object> features;
	private final double[] bbox;

	/**
	 * Constructor
	 *
	 * @param features the features, may be empty but not <code>null</code>
	 */
	public IntrospectionFeatureCollectionDocument(List<Object> features, double[] bbox) {
		this.features = features;
		this.bbox = bbox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Object> getFeatures() {
		return features;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double[] getBbox() {
		return bbox;
	}
}
