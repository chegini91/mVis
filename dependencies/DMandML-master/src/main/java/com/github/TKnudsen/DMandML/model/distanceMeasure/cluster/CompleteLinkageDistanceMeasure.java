package com.github.TKnudsen.DMandML.model.distanceMeasure.cluster;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.ICluster;

/**
 * <p>
 * Title: CompleteLinkageDistanceMeasure
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
 * @version 1.01
 */
public class CompleteLinkageDistanceMeasure extends ClusterDistanceMeasure<IDObject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1623946820035406623L;

	public CompleteLinkageDistanceMeasure(IDistanceMeasure<IDObject> distanceMeasure) {
		super(distanceMeasure);
	}

	@Override
	public String getName() {
		return "Complete Linkage";
	}

	@Override
	public String getDescription() {
		return "Calculates the similarity of two clusters w.r.t. their most dissimilar elements";
	}

	@Override
	public String toString() {
		return "Complete Linkage Distance";
	}

	@Override
	/**
	 * TODO: identical with maximum distance. check if this is correct.
	 */
	public double getDistance(ICluster<IDObject> c1, ICluster<IDObject> c2) {
		double distance = Double.MAX_VALUE;
		for (IDObject idObject1 : c1.getElements()) {
			for (IDObject idObject2 : c1.getElements()) {
				distance = Math.max(distance, getDistanceMeasure().getDistance(idObject1, idObject2));
			}
		}

		return distance;
	}
}
