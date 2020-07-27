package com.github.TKnudsen.DMandML.model.unsupervised.outliers.impl;

import com.github.TKnudsen.DMandML.model.unsupervised.outliers.ElkiBasedOutlierAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.outlier.distance.KNNOutlier;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;

/**
 * <p>
 * Title: KNNOutlierAnalysis
 * </p>
 * 
 * <p>
 * Description: Outlier Detection based on the distance of an object to its k
 * nearest neighbor.
 * 
 * Reference: S. Ramaswamy, and R. Rastogi, and K. Shim<br />
 * Efficient Algorithms for Mining Outliers from Large Data Sets.<br />
 * In: Proc. Int. Conf. on Management of Data, 2000.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class KNNOutlierAnalysis extends ElkiBasedOutlierAlgorithm {

	private final int kNN;

	public KNNOutlierAnalysis(int kNN) {
		this.kNN = kNN;
	}

	@Override
	protected void initializeOutlierAlgorithm() {
		this.outlierAlgorithm = new KNNOutlier<>(EuclideanDistanceFunction.STATIC, kNN);
	}

	public int getkNN() {
		return kNN;
	}

	@Override
	public String getName() {
		return "KNN Outlier Algorithm";
	}

	@Override
	public String getDescription() {
		return getName();
	}

}
