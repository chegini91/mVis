package com.github.TKnudsen.DMandML.model.distanceMeasure.cluster;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.ICluster;

/**
 * <p>
 * Title: SingleLinkageDistanceMeasure
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class SingleLinkageDistanceMeasure<T extends IDObject> extends ClusterDistanceMeasure<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2706781830339589800L;

	public SingleLinkageDistanceMeasure(IDistanceMeasure<T> distanceMeasure) {
		super(distanceMeasure);
	}

	@Override
	public String getName() {
		return "Single Linkage";
	}

	@Override
	public String getDescription() {
		return "Calculates similarity of two clusters w.r.t. their two most similar elements";
	}

	@Override
	public String toString() {
		return "Single Linkage";
	}

	@Override
	public double getDistance(ICluster<T> c1, ICluster<T> c2) {
		double distance = Double.MAX_VALUE;
		for (T dp1 : c1.getElements())
			for (T dp2 : c2.getElements())
				distance = Math.min(distance, getDistanceMeasure().getDistance(dp1, dp2));

		return distance;
	}
}
