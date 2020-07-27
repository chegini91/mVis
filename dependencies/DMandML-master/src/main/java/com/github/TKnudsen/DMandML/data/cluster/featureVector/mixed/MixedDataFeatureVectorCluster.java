package com.github.TKnudsen.DMandML.data.cluster.featureVector.mixed;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.FeatureVectorCluster;

/**
 * <p>
 * Title: MixedDataFeatureVectorCluster
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
 * @version 1.03
 */
public class MixedDataFeatureVectorCluster extends FeatureVectorCluster<MixedDataFeatureVector> {

	public MixedDataFeatureVectorCluster(Collection<MixedDataFeatureVector> elements,
			IDistanceMeasure<MixedDataFeatureVector> distanceMeasure) {
		this(elements, distanceMeasure, "", "");
	}

	public MixedDataFeatureVectorCluster(Collection<MixedDataFeatureVector> elements,
			IDistanceMeasure<MixedDataFeatureVector> distanceMeasure, String name, String description) {
		super(elements, distanceMeasure, name, description);
	}

	@Override
	public Cluster<MixedDataFeatureVector> clone() {
		MixedDataFeatureVectorCluster cluster = new MixedDataFeatureVectorCluster(new LinkedHashSet<>(getElements()),
				getDistanceMeasure(), getName(), getDescription());
		return cluster;
	}
}
