package com.github.TKnudsen.DMandML.model.distanceMeasure.cluster;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.ICluster;

/**
 * <p>
 * Title: ClusterDistanceMeasure
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.06
 */
public abstract class ClusterDistanceMeasure<T> implements IDistanceMeasure<ICluster<T>> {

	private IDistanceMeasure<T> distanceMeasure;

	public ClusterDistanceMeasure(IDistanceMeasure<T> distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	@Override
	public double applyAsDouble(ICluster<T> t, ICluster<T> u) {
		return getDistance(t, u);
	}

	public IDistanceMeasure<T> getDistanceMeasure() {
		return distanceMeasure;
	}

	public void setDistanceMeasure(IDistanceMeasure<T> distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}
}
