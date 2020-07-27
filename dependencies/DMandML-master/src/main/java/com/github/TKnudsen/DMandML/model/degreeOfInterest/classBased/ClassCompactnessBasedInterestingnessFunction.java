package com.github.TKnudsen.DMandML.model.degreeOfInterest.classBased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.featureVector.FeatureVectorDistanceMeasureFactory;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResultSupplier;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.FeatureVectorCluster;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity.cluster.AveragePairwiseDistanceCompactnessMeasure;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity.cluster.ClusterCompactnessMeasure;

/**
 * <p>
 * Title: ClassCompactnessBasedInterestingnessFunction
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class ClassCompactnessBasedInterestingnessFunction<FV extends IFeatureVectorObject<?, ?>>
		extends ClassificationBasedInterestingnessFunction<FV> {

	ClusterCompactnessMeasure<FV> clusterCompactnessMeasure;
	private double maxValueOfDiversity = Double.NaN;

	public ClassCompactnessBasedInterestingnessFunction(Supplier<List<FV>> featureVectorSupplier,
			IClassificationResultSupplier<FV> classificationResultSupplier) {
		this(featureVectorSupplier, classificationResultSupplier, new AveragePairwiseDistanceCompactnessMeasure<>());
	}

	public ClassCompactnessBasedInterestingnessFunction(Supplier<List<FV>> featureVectorSupplier,
			IClassificationResultSupplier<FV> classificationResultSupplier,
			ClusterCompactnessMeasure<FV> clusterCompactnessMeasure) {
		super(featureVectorSupplier, classificationResultSupplier);

		this.clusterCompactnessMeasure = clusterCompactnessMeasure;
	}

	@Override
	public void run() {

		interestingnessScores = new LinkedHashMap<>();

		Map<FV, Double> scores = new HashMap<>();
		List<Double> compactnessScores = new ArrayList<>();

		IClassificationResult<FV> classificationResult = this.getClassificationResultSupplier().get();
		Map<String, List<FV>> classDistributions = classificationResult.getClassDistributions();
		IDistanceMeasure<FV> distanceMeasure = null;

		for (String label : classDistributions.keySet()) {
			List<FV> fvs = classDistributions.get(label);

			if (distanceMeasure == null)
				distanceMeasure = FeatureVectorDistanceMeasureFactory.createDistanceMeasure(fvs);

			double classCompactness = clusterCompactnessMeasure
					.getMeasure(new FeatureVectorCluster<>(fvs, distanceMeasure));
			compactnessScores.add(classCompactness);
			for (FV fv : fvs)
				scores.put(fv, classCompactness);
		}

		StatisticsSupport compactnessScoresStatistics = new StatisticsSupport(compactnessScores);
		maxValueOfDiversity = compactnessScoresStatistics.getMax();

		List<FV> featureVectorList = getFeatureVectorSupplier().get();

		for (FV fv : featureVectorList)
			if (scores.containsKey(fv))
				interestingnessScores.put(fv,
						(1 - MathFunctions.linearScale(0, compactnessScoresStatistics.getMax(), scores.get(fv))));
			else {
				interestingnessScores.put(fv, 0.0);
				System.err.println("ClassCompactnessBasedInterestingnessFunction: unknown FV");
			}
	}

	@Override
	public String getDescription() {
		return "Calculates the compactness of classes of a classification result. ";
	}

	public double getMaxValueOfDiversity() {
		return maxValueOfDiversity;
	}
}
