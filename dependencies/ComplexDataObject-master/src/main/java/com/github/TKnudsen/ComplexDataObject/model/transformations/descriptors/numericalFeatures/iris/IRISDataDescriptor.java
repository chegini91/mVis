package com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures.iris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.IDescriptor;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures.INumericFeatureVectorDescriptor;

/**
 * <p>
 * Title: IRISDataDescriptor
 * </p>
 * 
 * <p>
 * Description: Creates feature vectors from complex data objects containing the
 * information of the famous IRIS data set.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class IRISDataDescriptor implements INumericFeatureVectorDescriptor<ComplexDataObject> {

	@Override
	public String getName() {
		return "IRISDataDescriptor";
	}

	@Override
	public String getDescription() {
		return "Transforms the IRIS dataset into a featurevector representation. adds the class attribute.";
	}

	@Override
	public List<IDescriptor<ComplexDataObject, NumericalFeatureVector>> getAlternativeParameterizations(int count) {
		return null;
	}

	@Override
	public List<NumericalFeatureVector> transform(ComplexDataObject input) {
		if (input == null)
			throw new NullPointerException();

		List<NumericalFeature> features = new ArrayList<>();
		NumericalFeatureVector featureVector = new NumericalFeatureVector(features);

		Iterator<String> iterator = input.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String next = iterator.next();
			if (i == 4) {
				featureVector.add(next, input.getAttribute(next));
				break;
			} else {
				Object feature = input.getAttribute(next);
				if (feature instanceof Number)
					features.add(new NumericalFeature(next, ((Number) input.getAttribute(next)).doubleValue()));
			}

			i++;
		}

		return Arrays.asList(new NumericalFeatureVector[] { featureVector });
	}

	@Override
	public List<NumericalFeatureVector> transform(List<ComplexDataObject> inputObjects) {
		List<NumericalFeatureVector> fvs = new ArrayList<>();

		for (ComplexDataObject cdo : inputObjects)
			fvs.addAll(transform(cdo));

		return fvs;
	}

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.DESCRIPTOR;
	}

}
