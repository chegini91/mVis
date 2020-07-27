package com.github.TKnudsen.DMandML.model.unsupervised.outliers.impl;

import com.github.TKnudsen.DMandML.model.unsupervised.outliers.ElkiBasedOutlierAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.outlier.anglebased.ABOD;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.distance.similarityfunction.SimilarityFunction;
import de.lmu.ifi.dbs.elki.distance.similarityfunction.kernel.LinearKernelFunction;

/**
 * <p>
 * Title: AngleBasedOutlierDetection
 * </p>
 * 
 * <p>
 * Description:
 * 
 * * H.-P. Kriegel, M. Schubert, and A. Zimek:<br />
 * Angle-Based Outlier Detection in High-dimensional Data.<br />
 * In: Proc. 14th ACM SIGKDD Int. Conf. on Knowledge Discovery and Data Mining
 * (KDD '08), Las Vegas, NV, 2008.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class AngleBasedOutlierDetection extends ElkiBasedOutlierAlgorithm {

	private SimilarityFunction<NumberVector> similarityFunction = LinearKernelFunction.STATIC;

	@Override
	protected void initializeOutlierAlgorithm() {
		this.outlierAlgorithm = new ABOD<>(similarityFunction);
	}

	@Override
	public String getDescription() {
		return "Based on variance analysis on angles, especially for high-dimensional data sets";
	}

}
