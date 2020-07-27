package com.github.TKnudsen.DMandML.data.cluster;

import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;

/**
 * <p>
 * Title: ICluster
 * </p>
 * 
 * <p>
 * Description: general behavior if a cluster data structure.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 */
public interface ICluster<T> extends Iterable<T>, ISelfDescription {

	/**
	 * number of elements in it
	 * 
	 * @return
	 */
	public int size();

	/**
	 * allows the assessment of centroid distances and related stuff.
	 * 
	 * Discussion: remove distance measure from Cluster? a lot of convenient
	 * functionality and context information will be gone.
	 * 
	 * @return
	 */
	public IDistanceMeasure<T> getDistanceMeasure();

	/**
	 * in most cases clusters can be represented with a centroid
	 * 
	 * @return
	 */
	public Centroid<T> getCentroid();

	/**
	 * retrieves the distance of a given T to the centroid. to be discussed: shall
	 * an exception be thrown iff T is not contained in the cluster?!
	 * 
	 * Discussion: remove distance measure from Cluster? a lot of convenient
	 * functionality and context information will be gone.
	 * 
	 * @param element
	 * @return
	 */
	public double getCentroidDistance(T element);

	/**
	 * in some cases T's may be added to a cluster
	 * 
	 * @param element
	 */
	public boolean add(T element);

	/**
	 * whether or not a cluster contains a particular element.
	 * 
	 * @param element
	 * @return
	 */
	public boolean contains(Object element);

	/**
	 * in some cases T's may be removed from a cluster
	 * 
	 * @param element
	 */
	public boolean remove(Object element);

	/**
	 * alternative to the iterator.
	 * 
	 * @return
	 */
	public Set<T> getElements();

	/**
	 * variance information produced by the elements of the cluster.
	 * 
	 * @return
	 */
	public double getVariance();

	/**
	 * we are interested in the T's that are contained in clusters. The
	 * constellation of these guys should also determine the equals function
	 * 
	 * @param o
	 * @return
	 */
	public boolean equals(Object o);

	public int hashCode();
}
