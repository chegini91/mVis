package com.github.TKnudsen.DMandML.model.unsupervised.clustering.splitting;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.DistanceMatrix;
import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.IDistanceMatrix;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.ClusterFactory;
import com.github.TKnudsen.DMandML.data.cluster.ICluster;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.IClusterSplittingAlgorithm;

/**
 * <p>
 * Title: MaximumDistanceSplitting
 * </p>
 * 
 * <p>
 * Description: splits a cluster into *n* clusters
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 */
public class MaximumDistanceSplitting<T> implements IClusterSplittingAlgorithm<T, ICluster<T>> {

	private int splitCount = 2;

	private IDistanceMeasure<T> distanceMeasure;

	public MaximumDistanceSplitting(IDistanceMeasure<T> distanceMeasure) {
		this.splitCount = 2;
		this.distanceMeasure = distanceMeasure;
	}

	@Override
	public List<ICluster<T>> splitCluster(ICluster<T> cluster) {

		// identify the two farthest elements (slow!)
		IDistanceMatrix<T> distanceMatrix = new DistanceMatrix<>(new ArrayList<>(cluster.getElements()),
				distanceMeasure);
		distanceMatrix.getMaxDistance();
		List<T> farestElements = distanceMatrix.getFarestElements();

		if (farestElements == null || farestElements.size() < 2)
			throw new IllegalArgumentException("MaximumDistanceSplitting: farest elements not available");

		if (getDistanceMeasure().getDistance(farestElements.get(0), farestElements.get(1)) == 0)
			throw new IllegalArgumentException("MaximumDistanceSplitting: farest elements had distance 0");

		// create two clusters
		List<List<T>> newDistribution = new ArrayList<>();
		for (int i = 0; i < farestElements.size(); i++) {
			List<T> list = new ArrayList<>();
			list.add(farestElements.get(i));
			newDistribution.add(list);
		}

		// assign elements
		for (T element : cluster) {
			if (farestElements.contains(element))
				continue;

			double distance = Double.MAX_VALUE;
			int targetIndex = -1;

			for (int i = 0; i < farestElements.size(); i++) {
				double dist = distanceMatrix.getDistance(element, farestElements.get(i));
				// System.out.println("Index: " + i + ", distance: " + dist);

				if (dist < distance) {
					distance = dist;
					targetIndex = i;
				}
			}

			newDistribution.get(targetIndex).add(element);
		}

		// create clusters
		List<ICluster<T>> splitResult = new ArrayList<>();
		ClusterFactory clusterFactory = new ClusterFactory();
		int i = 0;
		for (List<T> list : newDistribution) {
			if (list == null || list.size() == 0) {
				System.err.println("MaximumDistanceSplitting: cluster with no elements. ignore");
				continue;
			}

			ICluster<T> c = clusterFactory.createCluster(list, distanceMatrix, cluster.getName() + "[" + i++ + "]",
					"SplitCluster");
			splitResult.add(c);
		}

		return splitResult;
	}

	@Override
	public String getName() {
		return "MaximumDistanceSplit";
	}

	@Override
	public String getDescription() {
		return "Splits a cluster using the farthest elements as the criterion";
	}

	public int getSplitCount() {
		return splitCount;
	}

	@Override
	public void setSplitCount(int splitCount) {
		if (splitCount != 2)
			throw new IllegalArgumentException("MaximumdistanceSplit: only 2 is supported yet.");

		this.splitCount = splitCount;
	}

	@Override
	public IDistanceMeasure<T> getDistanceMeasure() {
		return distanceMeasure;
	}

	public void setDistanceMeasure(IDistanceMeasure<T> distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

}
