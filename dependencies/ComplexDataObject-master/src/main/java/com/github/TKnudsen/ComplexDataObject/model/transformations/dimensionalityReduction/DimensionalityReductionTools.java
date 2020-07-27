package com.github.TKnudsen.ComplexDataObject.model.transformations.dimensionalityReduction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.tools.DataConversion;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;

/**
 * <p>
 * Title: DimensionalityReductionTools
 * </p>
 * 
 * <p>
 * Description: little helpers to better cope with DimensionalityReduction
 * results (mappings from highDim to lowDim).
 * 
 * <p>
 * Copyright: Copyright (c) 2012-2018 Juergen Bernard,
 * https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class DimensionalityReductionTools {

	public static <X> List<Double[]> mappingToCoordinatesList(Map<X, NumericalFeatureVector> mapping) {
		List<Double[]> data = new ArrayList<>();

		for (X x : mapping.keySet()) {
			data.add(DataConversion.doublePrimitivesToArray(mapping.get(x).getVector()));
		}

		return data;
	}

	public static <X> Map<X, Double[]> mappingToCoordinatesMap(Map<X, NumericalFeatureVector> mapping) {
		Map<X, Double[]> data = new LinkedHashMap<>();

		for (X x : mapping.keySet()) {
			data.put(x, DataConversion.doublePrimitivesToArray(mapping.get(x).getVector()));
		}

		return data;
	}

	public static <X> List<String> mappingToNamesList(Map<X, NumericalFeatureVector> mapping) {
		List<String> data = new ArrayList<>();

		for (X x : mapping.keySet()) {
			data.add(mapping.get(x).getName());
		}

		return data;
	}

	/**
	 * statistics information for every output dimension. Used, e.g., to define axes
	 * in a scatterplot.
	 * 
	 * @param mapping
	 * @return
	 */
	public static <X> List<StatisticsSupport> mappingToStatisticsList(Map<X, NumericalFeatureVector> mapping) {
		List<List<Double>> valuesPerDimension = new ArrayList<>();

		for (X x : mapping.keySet()) {
			double[] vector = mapping.get(x).getVector();

			if (vector == null)
				continue;

			for (int i = 0; i < vector.length; i++) {
				if (valuesPerDimension.size() <= i)
					valuesPerDimension.add(new ArrayList<>());

				valuesPerDimension.get(i).add(vector[i]);
			}
		}

		List<StatisticsSupport> statistics = new ArrayList<>();
		for (int i = 0; i < valuesPerDimension.size(); i++)
			statistics.add(new StatisticsSupport(valuesPerDimension.get(i)));

		return statistics;
	}

	/**
	 * normalizes the (low-dimensional) output of a mapping into 2D. uses a global
	 * min and max across all dimensions to preserve linearity. Modifies the given
	 * output mapping.
	 * 
	 * @param mapping
	 */
	public static void normalizeMapping(Map<NumericalFeatureVector, NumericalFeatureVector> mapping) {
		// normalize feature vectors
		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;

		for (NumericalFeatureVector fv : mapping.values()) {
			for (int d = 0; d < fv.getDimensions(); d++) {
				min = Math.min(min, fv.getFeature(d).getFeatureValue());
				max = Math.max(max, fv.getFeature(d).getFeatureValue());
			}
		}

		double delta = max - min;
		for (NumericalFeatureVector fv : mapping.values())
			for (int d = 0; d < fv.getDimensions(); d++)
				fv.getFeature(d).setFeatureValue((fv.getFeature(d).getFeatureValue() - min) / delta);
	}

}
