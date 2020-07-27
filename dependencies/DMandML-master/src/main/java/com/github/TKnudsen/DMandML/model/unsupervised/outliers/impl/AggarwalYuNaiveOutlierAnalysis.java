package com.github.TKnudsen.DMandML.model.unsupervised.outliers.impl;

import com.github.TKnudsen.DMandML.model.unsupervised.outliers.ElkiBasedOutlierAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.outlier.subspace.AggarwalYuNaive;

/**
 * <p>
 * Title: AggarwalYuNaiveOutlierAnalysis
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class AggarwalYuNaiveOutlierAnalysis extends ElkiBasedOutlierAlgorithm {

	/**
	 * The number of partitions for each dimension.
	 */
	private int phi;

	/**
	 * The target dimensionality.
	 */
	private int k;

	public AggarwalYuNaiveOutlierAnalysis(int phi, int k) {
		this.phi = phi;
		this.k = k;
	}

	@Override
	protected void initializeOutlierAlgorithm() {
		this.outlierAlgorithm = new AggarwalYuNaive<>(k, phi);
	}

	@Override
	public String getDescription() {
		return "Examines all possible sets of k dimensional projections";
	}

}
