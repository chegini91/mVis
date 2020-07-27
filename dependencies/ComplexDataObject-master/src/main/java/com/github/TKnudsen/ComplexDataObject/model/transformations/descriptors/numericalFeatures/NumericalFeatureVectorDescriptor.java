package com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.objects.DoubleParser;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.IDescriptor;

/**
 * <p>
 * Title: NumericalFeatureVectorDescriptor
 * </p>
 * 
 * <p>
 * Description: Basic descriptor to transform real-world data (represented as a
 * ComplexDataObject) into numerical feature spaces.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class NumericalFeatureVectorDescriptor implements INumericFeatureVectorDescriptor<ComplexDataObject> {

	DoubleParser DoubleParser = new DoubleParser();

	@Override
	public List<NumericalFeatureVector> transform(ComplexDataObject input) {
		if (input == null)
			return null;

		return transform(Arrays.asList(new ComplexDataObject[] { input }));
	}

	@Override
	public List<NumericalFeatureVector> transform(List<ComplexDataObject> inputObjects) {
		if (inputObjects == null)
			return null;

		List<NumericalFeatureVector> featureVectors = new ArrayList<>();

		ComplexDataContainer container = new ComplexDataContainer(inputObjects);

		Iterator<ComplexDataObject> iterator = container.iterator();
		while (iterator.hasNext()) {
			ComplexDataObject cdo = iterator.next();

			List<NumericalFeature> features = new ArrayList<>();

			for (String attribute : container.getAttributeNames()) {
				if (container.isNumeric(attribute)) {
					Double d = DoubleParser.apply(cdo.getAttribute(attribute));
					NumericalFeature feature = new NumericalFeature(attribute, d);
					features.add(feature);
				}
			}

			NumericalFeatureVector fv = new NumericalFeatureVector(features);
			fv.setMaster(cdo);
			featureVectors.add(fv);
		}

		return featureVectors;
	}

	@Override
	public String getName() {
		return "NumericalFeatureVectorDescriptor";
	}

	@Override
	public String getDescription() {
		return "Transforms numeric attributes of a complex data object into a featureVector";
	}

	@Override
	public List<IDescriptor<ComplexDataObject, NumericalFeatureVector>> getAlternativeParameterizations(int count) {
		return null;
	}

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.DESCRIPTOR;
	}

}
