package com.github.TKnudsen.DMandML.model.unsupervised.outliers.impl;

import com.github.TKnudsen.DMandML.model.unsupervised.outliers.ElkiBasedOutlierAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.outlier.DWOF;
import de.lmu.ifi.dbs.elki.algorithm.outlier.DWOF.Parameterizer;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;

/**
 * <p>
 * Title: DynamicWindowOutlierFactorOutlierAnalysis
 * </p>
 * 
 * <p>
 * Description:
 * 
 * Rana Momtaz, Nesma Mohssen and Mohammad A. Gowayyed: DWOF: A Robust
 * Density-Based OutlierDetection Approach. <br>
 * In: Pattern Recognition and Image Analysis , Proc. 6th Iberian Conference,
 * IbPRIA 2013, Funchal, Madeira, Portugal, June 5-7, 2013.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class DynamicWindowOutlierFactorOutlierAnalysis extends ElkiBasedOutlierAlgorithm {

	/**
	 * Holds the value of {@link Parameterizer#K_ID} i.e. Number of neighbors to
	 * consider during the calculation of DWOF scores.
	 */
	private int kNN;

	private double delta = 1.1;

	public DynamicWindowOutlierFactorOutlierAnalysis(int kNN) {
		this.kNN = kNN;
	}

	@Override
	protected void initializeOutlierAlgorithm() {
		this.outlierAlgorithm = new DWOF<NumberVector>(EuclideanDistanceFunction.STATIC, kNN, delta);
	}

	@Override
	public String getDescription() {
		return "Computes dynamic-window outlier factors in a dataset";
	}

	public int getkNN() {
		return kNN;
	}

	public void setkNN(int kNN) {
		this.kNN = kNN;

		initializeOutlierAlgorithm();
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;

		initializeOutlierAlgorithm();
	}

}
