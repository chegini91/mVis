package com.github.TKnudsen.DMandML.data.cluster.general;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;

public class GeneralCluster<T> extends Cluster<T> {

	public GeneralCluster(Collection<? extends T> elements, IDistanceMeasure<T> distanceMeasure, String name,
			String description) {
		super(elements, distanceMeasure, name, description);
	}

	@Override
	public double getVariance() {
		System.err.println("General Cluster: no variance calculation provided.");
		return 0;
	}

	@Override
	public Cluster<T> clone() {
		return new GeneralCluster<T>(new LinkedHashSet<>(getElements()), distanceMeasure, getName(), getDescription());
	}

}
