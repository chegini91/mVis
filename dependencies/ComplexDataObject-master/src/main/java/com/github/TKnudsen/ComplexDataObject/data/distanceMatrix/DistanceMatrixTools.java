package com.github.TKnudsen.ComplexDataObject.data.distanceMatrix;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;

/**
 * <p>
 * Title: DistanceMatrixTools
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class DistanceMatrixTools {

	/**
	 * 
	 * @param distanceMatrix
	 * @param excludeMainDiagonal
	 * @param skipTransponedValues
	 *            for every pair of instances only use one value (assumes a
	 *            symmetric distance measure)
	 * @return
	 */
	public static StatisticsSupport getPairwiseDistances(IDistanceMatrix<?> distanceMatrix, boolean excludeMainDiagonal, boolean skipTransponedValues) {
		List<Double> distances = new ArrayList<>(distanceMatrix.getElements().size() * distanceMatrix.getElements().size());

		double[][] dm = distanceMatrix.getDistanceMatrix();
		for (int i = 0; i < distanceMatrix.getElements().size(); i++)
			for (int j = 0; j < distanceMatrix.getElements().size(); j++)
				if (excludeMainDiagonal && i == j)
					continue;
				else if (skipTransponedValues && j < i)
					continue;
				else
					distances.add(dm[i][j]);

		return new StatisticsSupport(distances);
	}
}
