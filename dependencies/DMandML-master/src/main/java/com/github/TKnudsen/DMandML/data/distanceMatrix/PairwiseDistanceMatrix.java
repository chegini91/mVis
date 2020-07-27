package com.github.TKnudsen.DMandML.data.distanceMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.IDistanceMatrix;

/**
 * <p>
 * Title: PairwiseDistanceMatrix
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * *
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Christian Ritter, Juergen Bernard
 * @version 1.03
 */
public class PairwiseDistanceMatrix<T> implements IDistanceMatrix<T> {

	private double[][] distanceMatrix;
	private Map<T, Integer> objectMapping;
	private List<T> elements;

	private double minDistance;
	private double maxDistance;
	private List<T> closestElements = new ArrayList<>(2);
	private List<T> farestElements = new ArrayList<>(2);

	public PairwiseDistanceMatrix(List<T> objects, double[][] pairwiseDistances) {
		this.distanceMatrix = pairwiseDistances;
		this.elements = objects;
		initializeObjectMapping();
		calculateMinMaxDistance();
	}

	private void calculateMinMaxDistance() {
		minDistance = Double.MAX_VALUE;
		maxDistance = Double.MIN_VALUE;
		for (int i = 0; i < distanceMatrix.length; i++) {
			for (int j = i; j < distanceMatrix.length; j++) {
				if (minDistance > distanceMatrix[i][j]) {
					updateMinDistance(distanceMatrix[i][j], elements.get(i), elements.get(j));
				}

				if (maxDistance < distanceMatrix[i][j]) {
					updateMaxDistance(distanceMatrix[i][j], elements.get(i), elements.get(j));
				}

				// minDistance = Math.min(minDistance, distanceMatrix[i][j]);
				// maxDistance = Math.max(maxDistance, distanceMatrix[i][j]);
			}
		}
	}

	private void initializeObjectMapping() {
		objectMapping = new HashMap<>();
		int i = 0;
		for (T t : getElements()) {
			objectMapping.put(t, i++);
		}
	}

	@Override
	public double getDistance(T o1, T o2) {
		Integer i = objectMapping.get(o1);
		Integer j = objectMapping.get(o2);
		if (i == null || j == null) {
			return Double.NaN;
		}
		return getDistanceMatrix()[i][j];
	}

	@Override
	public double applyAsDouble(T t, T u) {
		return getDistance(t, u);
	}

	/**
	 * sets the minimum distance between two elements. these elements are also
	 * queried.
	 * 
	 * @param min
	 * @param t1
	 * @param t2
	 */
	protected final void updateMinDistance(double minDistance, T t1, T t2) {
		this.minDistance = minDistance;

		closestElements.clear();
		closestElements.add(t1);
		closestElements.add(t2);
	}

	protected final void updateMaxDistance(double maxDistance, T t1, T t2) {
		this.maxDistance = maxDistance;

		farestElements.clear();
		farestElements.add(t1);
		farestElements.add(t2);
	}

	@Override
	public String getName() {
		return "PairwiseDistanceMatrix";
	}

	@Override
	public String getDescription() {
		return "IDistanceMatrix Wrapper for pairwise distances represented as 2D double array";
	}

	@Override
	public double[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	@Override
	public List<T> getElements() {
		return elements;
	}

	@Override
	public double getMinDistance() {
		return minDistance;
	}

	@Override
	public double getMaxDistance() {
		return maxDistance;
	}

	@Override
	public List<T> getClosestElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getFarestElements() {
		// TODO Auto-generated method stub
		return null;
	}

}
