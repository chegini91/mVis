package com.github.TKnudsen.DMandML.model.degreeOfInterest.dataBased.density;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.model.degreeOfInterest.DegreeOfInterestFunction;
import com.github.TKnudsen.DMandML.model.degreeOfInterest.dataBased.IDataBasedDegreeOfInterestFunction;

/**
 * <p>
 * Title: DataDensityBasedInterestingnessFunction
 * </p>
 * 
 * <p>
 * Description: Density-based interestingness function based on the mean
 * distance of nearest neighbors.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class DensityBasedInterestingnessFunction<FV extends IFeatureVectorObject<?, ?>>
		extends DegreeOfInterestFunction<FV> implements IDataBasedDegreeOfInterestFunction<FV> {

	private int nearestNeighborCount;

	private IDistanceMeasure<FV> distanceMeasure;

	public DensityBasedInterestingnessFunction(Supplier<List<FV>> featureVectorSupplier, int nearestNeighborCount,
			IDistanceMeasure<FV> distanceMeasure) {
		super(featureVectorSupplier);

		this.nearestNeighborCount = nearestNeighborCount;

		this.distanceMeasure = distanceMeasure;
	}

	@Override
	public String getDescription() {
		return "Calculates the interestingness of instances based on local densities in the vector space (average kNN distance).";
	}

	@Override
	public void run() {
		List<FV> featureVectorList = getFeatureVectorSupplier().get();

		interestingnessScores = new LinkedHashMap<>();
		List<Double> maxDistanceMeans = new ArrayList<>();

		for (FV fv : featureVectorList) {
			List<Double> distances = new ArrayList<>();

			for (FV fv2 : featureVectorList) {
				if (fv.equals(fv2))
					continue;

				distances.add(distanceMeasure.getDistance(fv, fv2));
			}

			Collections.sort(distances);
			distances = distances.subList(0, nearestNeighborCount);

			StatisticsSupport statistics = new StatisticsSupport(distances);

			interestingnessScores.put(fv, statistics.getMean());
			maxDistanceMeans.add(statistics.getMean());
		}

		StatisticsSupport distanceMeansStatistics = new StatisticsSupport(maxDistanceMeans);

		for (FV fv : featureVectorList)
			interestingnessScores.put(fv, (1 - MathFunctions.linearScale(distanceMeansStatistics.getMin(),
					distanceMeansStatistics.getMax(), interestingnessScores.get(fv))));
	}

	public int getNearestNeighborCount() {
		return nearestNeighborCount;
	}

	public void setNearestNeighborCount(int nearestNeighborCount) {
		this.nearestNeighborCount = nearestNeighborCount;

		resetInterestingnessScores();
	}

	public IDistanceMeasure<FV> getDistanceMeasure() {
		return distanceMeasure;
	}

	public void setDistanceMeasure(IDistanceMeasure<FV> distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

}
