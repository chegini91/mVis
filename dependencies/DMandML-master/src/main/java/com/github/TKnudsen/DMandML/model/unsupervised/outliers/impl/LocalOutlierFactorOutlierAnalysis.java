package com.github.TKnudsen.DMandML.model.unsupervised.outliers.impl;

import com.github.TKnudsen.DMandML.model.unsupervised.outliers.ElkiBasedOutlierAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.outlier.lof.LOF;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;

/**
 * <p>
 * Title: LocalOutlierFactorOutlierAnalysis
 * </p>
 * 
 * <p>
 * Description:
 * 
 * M. M. Breunig, H.-P. Kriegel, R. Ng, J. Sander:<br />
 * LOF: Identifying Density-Based Local Outliers.<br />
 * In: Proc. 2nd ACM SIGMOD Int. Conf. on Management of Data (SIGMOD'00),
 * Dallas, TX, 2000.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class LocalOutlierFactorOutlierAnalysis extends ElkiBasedOutlierAlgorithm {

	/**
	 * The number of neighbors to query (including the query point!)
	 */
	private int kNN;

	public LocalOutlierFactorOutlierAnalysis(int kNN) {
		this.kNN = kNN;
	}

	@Override
	protected void initializeOutlierAlgorithm() {
		this.outlierAlgorithm = new LOF<NumberVector>(kNN, EuclideanDistanceFunction.STATIC);
	}

	@Override
	public String getDescription() {
		return "Density-based local outlier factors in a data collection";
	}

	public int getkNN() {
		return kNN;
	}

	public void setkNN(int kNN) {
		this.kNN = kNN;

		initializeOutlierAlgorithm();
	}

}
