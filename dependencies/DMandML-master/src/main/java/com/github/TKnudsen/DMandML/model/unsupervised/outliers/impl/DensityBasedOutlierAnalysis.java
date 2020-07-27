package com.github.TKnudsen.DMandML.model.unsupervised.outliers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.data.outliers.OutlierAnalysisResult;
import com.github.TKnudsen.DMandML.model.unsupervised.outliers.OutlierAnalysisAlgorithm;

/**
 * <p>
 * Title: DensityBasedOutlierAnalysis
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class DensityBasedOutlierAnalysis<FV> extends OutlierAnalysisAlgorithm<FV> {

	private int kNN;

	private IDistanceMeasure<FV> distanceMeasure;

	public DensityBasedOutlierAnalysis(int kNN, IDistanceMeasure<FV> distanceMeasure) {
		this.kNN = kNN;
		this.distanceMeasure = distanceMeasure;
	}

	public DensityBasedOutlierAnalysis(int kNN, IDistanceMeasure<FV> distanceMeasure,
			List<? extends FV> featureVectors) {
		this(kNN, distanceMeasure);

		setFeatureVectors(featureVectors);
	}

	@Override
	public void run() {
		List<FV> featureVectorList = getFeatureVectors();

		if (featureVectorList == null)
			throw new NullPointerException(getName() + ": cannot be run until feature vectors are set");

		Map<FV, Double> outlierScores = new LinkedHashMap<>();
		List<Double> maxDistanceMeans = new ArrayList<>();

		for (FV fv : featureVectorList) {
			List<Double> distances = new ArrayList<>();

			for (FV fv2 : featureVectorList) {
				if (fv.equals(fv2))
					continue;

				distances.add(distanceMeasure.getDistance(fv, fv2));
			}

			Collections.sort(distances);
			distances = distances.subList(0, kNN);

			StatisticsSupport statistics = new StatisticsSupport(distances);

			outlierScores.put(fv, statistics.getMean());
			maxDistanceMeans.add(statistics.getMean());
		}

		// StatisticsSupport distanceMeansStatistics = new
		// StatisticsSupport(maxDistanceMeans);
		//
		// for (FV fv : featureVectorList)
		// outlierScores.put(fv,
		// MathFunctions.linearScale(0, distanceMeansStatistics.getMax(),
		// outlierScores.get(fv)));

		this.outlierAnalysisResult = new OutlierAnalysisResult<FV>(outlierScores, getName());
	}

	@Override
	public void calculateOutlierAnalysisResult() {
		run();
	}

	@Override
	public String getDescription() {
		return "Calculates outlier scores based on distances to nearest neighbors.";
	}

	@Override
	public void setData(List<? extends FV> data) {
		this.setFeatureVectors(data);
	}

}
