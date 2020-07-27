package com.github.TKnudsen.ComplexDataObject.data.distanceMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;

/**
 * <p>
 * Title: DistanceMatrix
 * </p>
 * 
 * <p>
 * Description: Stores and manages distances of pairs of objects (T's)
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 */
public class DistanceMatrix<T> implements IDistanceMatrix<T> {

	// constructor properties
	private List<T> objects;
	protected IDistanceMeasure<T> distanceMeasure;

	// storage, indexing
	protected double[][] distanceMatrix = null;
	protected Map<T, Integer> objectIndex;

	// statistics
	private double min;
	private double max;

	// elements associated to statistics
	private List<T> closestElements = new ArrayList<>(2);
	private List<T> farestElements = new ArrayList<>(2);

	public DistanceMatrix(List<T> objects, IDistanceMeasure<T> distanceMeasure) {
		if (distanceMeasure == null)
			throw new IllegalArgumentException("DistanceMatrix: given distance measures was null");

		this.objects = objects;
		this.distanceMeasure = distanceMeasure;

		initializeDistanceMatrix();
	}

	protected void initializeObjectIndex() {
		// create index
		objectIndex = new HashMap<>();
		for (int i = 0; i < objects.size(); i++)
			objectIndex.put(objects.get(i), i);
	}

	protected void initializeDistanceMatrix() {
		initializeObjectIndex();

		distanceMatrix = new double[objectIndex.size()][objectIndex.size()];

		for (int x = 0; x < distanceMatrix.length; x++)
			for (int y = 0; y < distanceMatrix[x].length; y++)
				distanceMatrix[x][y] = Double.NaN;

		updateMinDistance(Double.POSITIVE_INFINITY, null, null);
		updateMaxDistance(Double.NEGATIVE_INFINITY, null, null);

		// create distance matrix - optimized for inheriting index-based access
		for (T t1 : objectIndex.keySet())
			for (T t2 : objectIndex.keySet()) {
				double distance = distanceMeasure.getDistance(t1, t2);

				distanceMatrix[getObjectIndex(t1)][getObjectIndex(t2)] = distance;

				if (getMin() > distance) {
					updateMinDistance(distance, t1, t2);
				}

				if (getMax() < distance) {
					updateMaxDistance(distance, t1, t2);
				}
			}
	}

	protected Integer getObjectIndex(T object) {
		// if (objectIndex == null)
		// initializeDistanceMatrix();

		return objectIndex.get(object);
	}

	@Override
	public double getDistance(T o1, T o2) {
		Integer index1 = getObjectIndex(o1);
		Integer index2 = getObjectIndex(o2);

		if (index1 == null || index2 == null) {
			System.out.println(
					"DistanceMatrix.getDistance: item not in the DM index, thus no lookup possible. Calculating distance..");
			return distanceMeasure.getDistance(o1, o2);
		}

		return getDistanceMatrix()[index1][index2];
	}

	@Override
	public String getName() {
		return "Distance Matrix using " + distanceMeasure.toString();
	}

	@Override
	public String getDescription() {
		return getName() + ", size: " + objects.size() + " x " + objects.size() + ".";
	}

	@Override
	public double applyAsDouble(T t, T u) {
		return getDistance(t, u);
	}

	@Override
	public double[][] getDistanceMatrix() {
		if (distanceMatrix == null)
			initializeDistanceMatrix();

		return distanceMatrix;
	}

	public int size() {
		return objects.size();
	}

	@Override
	public double getMinDistance() {
		return getMin();
	}

	@Override
	public double getMaxDistance() {
		return getMax();
	}

	@Override
	public List<T> getClosestElements() {
		return closestElements;
	}

	@Override
	public List<T> getFarestElements() {
		return farestElements;
	}

	public double getMin() {
		return min;
	}

	/**
	 * sets the minimum distance between two elements. these elements are also
	 * queried.
	 * 
	 * @param min
	 * @param t1
	 * @param t2
	 */
	public final void updateMinDistance(double min, T t1, T t2) {
		this.min = min;

		closestElements.clear();
		closestElements.add(t1);
		closestElements.add(t2);
	}

	public double getMax() {
		return max;
	}

	public final void updateMaxDistance(double max, T t1, T t2) {
		this.max = max;

		farestElements.clear();
		farestElements.add(t1);
		farestElements.add(t2);
	}

	@Override
	public List<T> getElements() {
		return objects;
	}
}
