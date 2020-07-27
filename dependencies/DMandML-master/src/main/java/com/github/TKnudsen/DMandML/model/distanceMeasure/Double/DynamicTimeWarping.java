package com.github.TKnudsen.DMandML.model.distanceMeasure.Double;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double.DoubleDistanceMeasure;

import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.fastdtw.FastDTW;

/**
 * <p>
 * Title: DynamicTimeWarping
 * </p>
 * 
 * <p>
 * Description: dynamic time warping distance measure for double arrays. Uses
 * FastDTW from javaml.
 * 
 * Implementation of the FastDTW algorithm as described by Salvador and Chan.
 * Salvador and Philip Chan, FastDTW: Toward Accurate Dynamic Time Warping in
 * Linear Time and Space, KDD Workshop on Mining Temporal and Sequential Data,
 * pp. 70-80, 2004. http://www.cs.fit.edu/~pkc/papers/tdm04.pdf
 * 
 * Stan Salvador and Philip Chan, Toward Accurate Dynamic Time Warping in Linear
 * Time and Space, Intelligent Data Analysis, 11(5):561-580, 2007.
 * http://www.cs.fit.edu/~pkc/papers/ida07.pdf
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class DynamicTimeWarping extends DoubleDistanceMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1794154088454134546L;

	private FastDTW dtw;

	private int radius;

	@SuppressWarnings("unused")
	private DynamicTimeWarping() {
		setRadius(0);
	}

	public DynamicTimeWarping(int radius) {
		setRadius(radius);
	}

	private void initializeDTW(int radius) {
		dtw = new net.sf.javaml.distance.fastdtw.FastDTW(radius);
	}

	@Override
	public double getDistance(double[] o1, double[] o2) {
		if (o1 == null || o2 == null)
			throw new NullPointerException(getName() + ": given vector was null");

		Instance instance = new DenseInstance(o1);
		Instance instance2 = new DenseInstance(o2);

		return dtw.measure(instance, instance2);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public String getDescription() {
		return "Dynamic Time Warping distance measure";
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;

		initializeDTW(radius);
	}

}
