package com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.data.cluster.Centroid;

/**
 * <p>
 * Title: NumericalFeatureVectorClusterTools
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
 * @version 1.02
 */
public class NumericalFeatureVectorClusterTools {

	/**
	 * Same as method above, only other type argument.
	 * 
	 * @param cluster
	 * @return
	 */
	public static Centroid<NumericalFeatureVector> calculateCentroid(NumericalFeatureVectorCluster cluster) {

		int size = cluster.size();
		StatisticsSupport[] statistics = cluster.getDataStatisticsPerDimension();
		double[] temp = new double[statistics.length];

		// calculating the Centroid
		Iterator<NumericalFeatureVector> iterator = cluster.iterator();
		while (iterator.hasNext()) {
			NumericalFeatureVector fv = iterator.next();
			for (int d = 0; d < fv.sizeOfFeatures(); d++)
				temp[d] += fv.get(d);
		}

		for (int d = 0; d < temp.length; d++)
			temp[d] = temp[d] / (double) size;

		NumericalFeatureVector fv = new NumericalFeatureVector(new NumericalFeature[] {});
		fv.setVector(temp);
		fv.setMaster(cluster);
		fv.setName("Centroid of cluster " + cluster.getName());
		fv.setDescription("Centroid of cluster " + cluster.getName());

		Centroid<NumericalFeatureVector> centroid = new Centroid<NumericalFeatureVector>(cluster, fv);
		return centroid;
	}

	public static StatisticsSupport getStatistics(NumericalFeatureVectorCluster cluster) {
		if (cluster == null)
			return null;

		List<Double> distances = new ArrayList<>();
		for (NumericalFeatureVector fv : cluster.getElements()) {
			double d = cluster.getCentroidDistance(fv);
			distances.add(d);
		}

		return new StatisticsSupport(distances);
	}

	public static double getVariance(NumericalFeatureVectorCluster cluster) {
		if (cluster == null)
			return Double.NaN;

		StatisticsSupport statistics = getStatistics(cluster);
		if (statistics == null)
			return Double.NaN;

		return statistics.getVariance();
	}
}
