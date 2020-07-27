package com.github.TKnudsen.ComplexDataObject.data.features.numericalData;

import java.util.ArrayList;
import java.util.List;

import de.javagl.nd.tuples.d.DoubleTuple;

/**
 * <p>
 * Title: NumericalFeatureVectorFactory
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class NumericalFeatureVectorFactory {

	public static NumericalFeatureVector createNumericalFeatureVector(double[] vector) {
		return createNumericalFeatureVector(vector, "", "");
	}

	public static NumericalFeatureVector createNumericalFeatureVector(Double[] vector) {
		return createNumericalFeatureVector(vector, "", "");
	}

	public static NumericalFeatureVector createNumericalFeatureVector(double[] vector, String name, String description) {
		if (vector == null)
			return null;

		List<NumericalFeature> features = new ArrayList<>();
		for (int i = 0; i < vector.length; i++)
			features.add(new NumericalFeature("[" + i + "]", vector[i]));

		NumericalFeatureVector numericalFeatureVector = new NumericalFeatureVector(features);
		numericalFeatureVector.setName(name);
		numericalFeatureVector.setDescription(description);

		return numericalFeatureVector;
	}

	public static NumericalFeatureVector createNumericalFeatureVector(Double[] vector, String name, String description) {
		if (vector == null)
			return null;

		List<NumericalFeature> features = new ArrayList<>();
		for (int i = 0; i < vector.length; i++)
			features.add(new NumericalFeature("[" + i + "]", vector[i]));

		NumericalFeatureVector numericalFeatureVector = new NumericalFeatureVector(features);
		numericalFeatureVector.setName(name);
		numericalFeatureVector.setDescription(description);

		return numericalFeatureVector;
	}

	public static NumericalFeatureVector createNumericalFeatureVector(DoubleTuple doubleTuple, String name, String description) {
		if (doubleTuple == null)
			return null;

		double[] vector = new double[doubleTuple.getSize()];

		for (int i = 0; i < doubleTuple.getSize(); i++)
			vector[i] = doubleTuple.get(i);

		return createNumericalFeatureVector(vector, name, description);
	}
}
