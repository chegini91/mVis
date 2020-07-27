package com.github.TKnudsen.DMandML.model.distanceMeasure.featureVector;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;

/**
 * <p>
 * Title: DynamicTimeWarping
 * </p>
 * 
 * <p>
 * Description: Euclidean's Distance Measure for NumericalFeatureVectors
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class DynamicTimeWarping implements IDistanceMeasure<NumericalFeatureVector> {

	private com.github.TKnudsen.DMandML.model.distanceMeasure.Double.DynamicTimeWarping dtw;

	private int radius;

	public DynamicTimeWarping(int radius) {
		this.setRadius(radius);
	}

	private void initializeDTW(int radius) {
		this.dtw = new com.github.TKnudsen.DMandML.model.distanceMeasure.Double.DynamicTimeWarping(radius);
	}

	@Override
	public double getDistance(NumericalFeatureVector o1, NumericalFeatureVector o2) {
		if (o1 == null || o2 == null)
			throw new NullPointerException(getName() + ": given Feature Vector was null");

		return dtw.getDistance(o1.getVector(), o2.getVector());
	}

	@Override
	public double applyAsDouble(NumericalFeatureVector t, NumericalFeatureVector u) {
		return getDistance(t, u);
	}

	@Override
	public String getName() {
		return "DynamicTimeWarping";
	}

	@Override
	public String getDescription() {
		return "Dynamic Time Warping distance measures for NumericalFeatureVectors";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof DynamicTimeWarping))
			return false;
		return true;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;

		initializeDTW(radius);
	}

}
