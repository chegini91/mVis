package com.github.TKnudsen.DMandML.model.unsupervised.outliers.impl;

import com.github.TKnudsen.DMandML.model.unsupervised.outliers.ElkiBasedOutlierAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.outlier.distance.LocalIsolationCoefficient;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;

/**
 * <p>
 * Title: LocalIsolationCoefficientOutlierAnalysis
 * </p>
 * 
 * <p>
 * Description:
 * 
 * The Local Isolation Coefficient is the sum of the kNN distance and the
 * average distance to its k nearest neighbors.
 *
 * The algorithm originally used a normalized Manhattan distance on numerical
 * attributes, and Hamming distance on categorial attributes.
 * 
 * B. Yu, and M. Song, and L. Wang<br />
 * Local Isolation Coefficient-Based Outlier Mining Algorithm<br />
 * Int. Conf. on Information Technology and Computer Science (ITCS) 2009
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class LocalIsolationCoefficientOutlierAnalysis extends ElkiBasedOutlierAlgorithm {

	/**
	 * Holds the number of nearest neighbors to query (including query point!)
	 */
	private int kNN;

	public LocalIsolationCoefficientOutlierAnalysis(int kNN) {
		this.kNN = kNN;
	}

	@Override
	protected void initializeOutlierAlgorithm() {
		this.outlierAlgorithm = new LocalIsolationCoefficient<>(EuclideanDistanceFunction.STATIC, kNN);
	}

	@Override
	public String getDescription() {
		return "Sum of the kNN distance and the average distance to its k nearest neighbors";
	}

	public int getkNN() {
		return kNN;
	}

	public void setkNN(int kNN) {
		this.kNN = kNN;

		initializeOutlierAlgorithm();
	}

}
